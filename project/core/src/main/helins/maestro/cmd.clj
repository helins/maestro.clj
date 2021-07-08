;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at https://mozilla.org/MPL/2.0/.


(ns helins.maestro.cmd

  "Preparing and computing needed arguments for the Clojure CLI.
  
   No actual running happens in this namespace."

  {:author "Adam Helinski"}

  (:refer-clojure :exclude [test])
  (:require [clojure.string]
            [helins.maestro       :as $]
            [helins.maestro.alias :as $.alias]))


;;;;;;;;;; Helpers


(defn require-alias+

  "Walks the given `alias+` collection with [[helins.maestro/walk]] ensuring current aliases
   in `:maestro/require` stay at the beginning of the sequence."

  [ctx kw-track alias+]

  (-> ctx
      (assoc :maestro/require
             [])
      ($/walk alias+)
      (as->
        ctx-2
        (assoc ctx-2
               kw-track
               (ctx-2 :maestro/require)))
      (update :maestro/require
              concat
              (ctx :maestro/require))))



(defn require-dev+

  "Uses [[require-alias+]] to require all dev aliases."


  ([ctx]

   (require-dev+ ctx
                 (ctx :maestro/require)))


  ([ctx alias+]

   (require-alias+ ctx
                   :maestro/dev+
                   ($.alias/dev+ ctx
                                 alias+))))



(defn require-test+

  "Uses [[require-alias+]] to require all test aliases."


  ([ctx]

   (require-test+ ctx
                  (ctx :maestro/require)))


  ([ctx alias+]

   (require-alias+ ctx
                   :maestro/test+
                   ($.alias/test+ ctx
                                  alias+))))


;;;;;;;;;; Preparing for commands (calling a function, launching dev mode, ...)


(defn dev

  "Prepares for dev mode:
  
   - Prepends `:task/test´ and `:task/dev` to given aliases walking
   - Requires and walk all dev aliases
   - Requires and walk all tests aliases"

  [ctx]

  (-> ctx
      ($/walk (concat [:task/test
                       :task/dev]
                      (ctx :maestro/main+)))
      require-dev+
      require-test+
      (update :maestro/exec-char
              #(or %
                   \M))))



(defn function

  "Walks aliases in `:maestro/main+` and preparing for executing with '-X' by default."

  [ctx]

  (-> ($/walk ctx)
      (update :maestro/exec-char
              #(or %
                   \X))))



(defn main

  "Walks aliases in `:maestro/main+` and preparing for executing with '-M' by default."

  [ctx]

  (-> ($/walk ctx)
      (update :maestro/exec-char
              #(or %
                   \M))))



(defn main-class

  "Like [[main]] but prepends to `:maestro/arg+` (CLI arguments) the main class found under `:maestro/main-class` in the data
   of the last user given alias in `:maestro/main+`."

  [ctx]

  (-> ctx
      $/walk
      (as->
        ctx-2
        (update ctx-2
                :maestro/arg+
                (partial cons
                         (str "-m "
                              (or ($.alias/main-class ctx-2)
                                  (throw (ex-info "Alias needs `:maestro/main-class` pointing to the class containing the `-main` function"
                                                  {})))))))
      (update :maestro/exec-char
              #(or %
                   \M))))



(defn test

  "Prepares for testing:
  
   - Appends `:task/test` alias to given aliases before walking
   - Require and walk all test aliases returned by applying `f-test-alias+` on `ctx` after previous step."

  [ctx f-test-alias+]

  (-> ctx
      ($/walk (conj (ctx :maestro/main+)
                    :task/test))
      (as->
        ctx-2
        (require-test+ ctx-2
                       (f-test-alias+ ctx-2)))))



(defn test-broad

  "Like [[test]] but ensures all transitives test aliases are required."

  [ctx]

  (test ctx
        :maestro/require))



(defn test-narrow

  "Like [[test]] but ensures only the test aliases related to the user given ones are required, not all transitives ones."

  [ctx]

  (test ctx
        :maestro/main+))
