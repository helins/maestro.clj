;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at https://mozilla.org/MPL/2.0/.


(ns helins.maestro.cp

  "Working with the classpath."

  {:author "Adam Helinski"}

  (:refer-clojure :exclude [print])
  (:require [babashka.fs         :as bb.fs]
            [clojure.string]))


;;;;;;;;;; Helpers


(defn split

  "Splits the classpath string into a vector of individual path."

  [cp-string]

  (map clojure.string/trim-newline
       (clojure.string/split cp-string
                             (re-pattern ":"))))


;;;;;;;;;; Tasks


(defn print-stdin

  "Uses [[split]] after reading classpath provided to `*in*` and prints sorted paths."

  []

  (run! println
        (-> (slurp *in*)
            split
            sort)))



(defn rm

  "Deletes the computed classpath."

  []

  (bb.fs/delete-tree ".cpcache"))
