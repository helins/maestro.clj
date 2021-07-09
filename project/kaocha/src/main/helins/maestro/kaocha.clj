;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at https://mozilla.org/MPL/2.0/.


(ns helins.maestro.kaocha

  "Running tests with Kaocha while gathering all needed aliases.
 
   This namespace does only preparatory work while [[helins.maestro.depstar.run]] can be used to
   directly run Clojure CLI."

  {:author "Adam Helinski"}

  (:refer-clojure :exclude [test])
  (:require [babashka.fs          :as bb.fs]
            [helins.maestro       :as $]
            [helins.maestro.alias :as $.alias]
            [helins.maestro.cmd   :as $.cmd]))


;;;;;;;;;;


(defn test

  "Common work between [[broad]] and [[narrow]]."

  [ctx]

  (let [root (or (ctx :maestro.kaocha/root)
                 "private")]
    (when-not (bb.fs/exists? root)
      (bb.fs/create-dir root))
    (spit (format "%s/%s"
                  root
                  (or (ctx :maestro.kaocha/file)
                      "maestro_kaocha.edn"))
          (pr-str {:kaocha/source-paths ($.alias/path+ ctx
                                                       (ctx :maestro/main+))
                   :kaocha/test-paths   ($.alias/path+ ctx
                                                       (ctx :maestro/test+))})))
  (update ctx
          :maestro/exec-char
          #(or %
               \M)))


;;;;;;;;;;


(defn broad

  "Generates a file with a unique test that can be included in Kaocha test configuration.

   Optionally, `ctx` may contain:

   | Key | Purpose | Default |
   |---|---|---|
   | `:maestro.kaocha/file` | Filename | \"maestro_kaocha.edn\" |
   | `:maestro.kaocha/root` | Directory where the file will be written | \"private\" |
  
   See README for example."


  ([]

   (broad ($/ctx)))


  ([ctx]

  (-> ctx
      $.cmd/test-broad
      test)))



(defn narrow

  "Like [[broad]] but tests only the user given profiles under `:maestro/main+`, no transitive
   alias."


  ([]

   (narrow ($/ctx)))


  ([ctx]

   (-> ctx
       $.cmd/test-narrow
       test)))
