;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at https://mozilla.org/MPL/2.0/.


(ns helins.maestro.kaocha.run

  ""

  {:author "Adam Helinski"}

  (:require [helins.maestro        :as $]
            [helins.maestro.kaocha :as $.kaocha]
            [helins.maestro.run    :as $.run]))


;;;;;;;;;;


(defn broad

  ""


  ([]

   (broad ($/ctx)))


  ([ctx]

   (-> ctx
       $.kaocha/broad
       $.run/clojure)))



(defn narrow

  ""


  ([]

   (narrow ($/ctx)))


  ([ctx]

   (-> ctx
       $.kaocha/narrow
       $.run/clojure)))
