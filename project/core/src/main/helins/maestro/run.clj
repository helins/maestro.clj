;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at https://mozilla.org/MPL/2.0/.


(ns helins.maestro.run

  ""

  {:author "Adam Helinski"}

  (:require [babashka.tasks     :as bb.task]
            [helins.maestro     :as $]
            [helins.maestro.cmd :as $.cmd]))


;;;;;;;;;;


(defn clojure

  ""

  [ctx]

  (let [cmd-2 ($/cmd ctx)]
    (when (ctx :maestro/debug?)
      (println cmd-2))
    (bb.task/clojure {:extra-env (ctx :maestro/env)}
                     cmd-2)))



(defn dev

  ""


  ([]

   (dev ($/ctx)))


  ([ctx]

   (-> ctx
       $.cmd/dev
       clojure)))



(defn function

  ""


  ([]

   (function ($/ctx)))


  ([ctx]

   (-> ctx
       $.cmd/function
       clojure)))



(defn main-class

  ""


  ([]

   (main-class ($/ctx)))


  ([ctx]

   (-> ctx
       $.cmd/main-class
       clojure)))
