;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at https://mozilla.org/MPL/2.0/.


(ns helins.maestro.aggr
  
  ""

  {:author "Adam Helinski"}

  (:refer-clojure :exclude [alias]))


;;;;;;;;;;


(defn alias

  ""

  [ctx alias _config]

  (update ctx
          :maestro/require
          conj
          alias))



(defn env

  ""

  [acc _alias config]

  (update acc
          :maestro/env
          merge
          (:maestro/env config)))
