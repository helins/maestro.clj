;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at https://mozilla.org/MPL/2.0/.


(ns helins.maestro.depstar.run

  ""

  {:author "Adam Helinski"}

  (:require [helins.maestro         :as $]
            [helins.maestro.depstar :as $.depstar]
            [helins.maestro.run     :as $.run]))


;;;;;;;;;;


(defn jar

  ""


  ([]

   (jar ($/ctx)))


  ([ctx]

   (-> ctx
       $.depstar/jar
       $.run/clojure)))



(defn uberjar

  ""


  ([]

   (uberjar ($/ctx)))


  ([ctx]

   (-> ctx
       $.depstar/uberjar
       $.run/clojure)))
