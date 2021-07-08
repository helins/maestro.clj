;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at https://mozilla.org/MPL/2.0/.


(ns helins.maestro.run

  "Running Clojure CLI.
  
   Shortcuts that call directly related functions in the [[helins.maestro.cmd]] namespace, assuming
   there is no need to modify the context before execution."

  {:author "Adam Helinski"}

  (:require [babashka.tasks     :as bb.task]
            [helins.maestro     :as $]
            [helins.maestro.cmd :as $.cmd]))


;;;;;;;;;;


(defn clojure

  "Executes Clojure CLI.
  
   Command is computed using [[helins.maestro/cmd]]."

  [ctx]

  (let [cmd-2 ($/cmd ctx)]
    (when (ctx :maestro/debug?)
      (println cmd-2))
    (bb.task/clojure {:extra-env (ctx :maestro/env)}
                     cmd-2)))



(defn dev

  "See [[helins.maestro.cmd/dev]]."


  ([]

   (dev ($/ctx)))


  ([ctx]

   (-> ctx
       $.cmd/dev
       clojure)))



(defn function

  "See [[helins.maestro.cmd/function]]."


  ([]

   (function ($/ctx)))


  ([ctx]

   (-> ctx
       $.cmd/function
       clojure)))



(defn main

  "See [[helins.maestro.cmd/main]]."

  ([]

   (main ($/ctx)))


  ([ctx]

   (-> ctx
       $.cmd/main
       clojure)))



(defn main-class

  "See [[helins.maestro.cmd/main-class]]."

  ([]

   (main-class ($/ctx)))


  ([ctx]

   (-> ctx
       $.cmd/main-class
       clojure)))
