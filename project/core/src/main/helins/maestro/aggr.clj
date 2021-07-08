;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at https://mozilla.org/MPL/2.0/.


(ns helins.maestro.aggr
  
  "Functions that can be used with [[helins.maestro/walk]] as aggregators when [[helins/maestro/aggr]]
   is not enough."

  {:author "Adam Helinski"}

  (:refer-clojure :exclude [alias]))


;;;;;;;;;;


(defn alias

  "Conjs the given `alias` to `:maestro/require`."

  [ctx alias _config]

  (update ctx
          :maestro/require
          conj
          alias))



(defn env

  "Merges the `:maestro/env` map from the alias's `data` with `:maestro/env` in `ctx`.
  
   Provides a way for specifying environment variables in aliases."

  [ctx _alias data]

  (update ctx
          :maestro/env
          merge
          (:maestro/env data)))
