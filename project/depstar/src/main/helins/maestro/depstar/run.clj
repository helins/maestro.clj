;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at https://mozilla.org/MPL/2.0/.


(ns helins.maestro.depstar.run

  "Tasks for jarring and uberjarring using [Depstar](https://github.com/seancorfield/depstar).
  
   Shortcuts for running Clojure CLI after doing preparatory work using functions from the
   [[helins.maestro.depstar]] namespace."

  {:author "Adam Helinski"}

  (:require [helins.maestro         :as $]
            [helins.maestro.depstar :as $.depstar]
            [helins.maestro.run     :as $.run]))


;;;;;;;;;;


(defn jar

  "See [[helins.maestro.depstar/jar]]."


  ([]

   (jar ($/ctx)))


  ([ctx]

   (-> ctx
       $.depstar/jar
       $.run/clojure)))



(defn uberjar

  "See [[helins.maestro.depstar/uberjar]]."


  ([]

   (uberjar ($/ctx)))


  ([ctx]

   (-> ctx
       $.depstar/uberjar
       $.run/clojure)))
