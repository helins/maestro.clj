;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at https://mozilla.org/MPL/2.0/.


(ns helins.maestro.alias

  "Retrieving information about aliases."

  {:author "Adam Helinski"}

  (:refer-clojure :exclude [test]))


(declare related)


;;;;;;;;;;


(defn data

  "Retrieves the data map associated with the given `alias`."

  [ctx alias]

  (get-in ctx
          [:aliases
           alias]))



(defn dev

  "Retrieves dev aliases related to the given `alias`."

  [ctx alias]

  (related ctx
           :maestro/dev+
           "dev"
           alias))



(defn dev+

  "Like [[dev]] but also retrieves transitive dev aliases."


  ([ctx]

   (dev+ ctx
         (ctx :maestro/require)))


  ([ctx alias+]

   (mapcat (partial dev
                    ctx)
           alias+)))



(defn main-class

  "Retrieves the main class associated with `alias`."


  ([ctx]

   (main-class ctx
               (last (ctx :maestro/main+))))


  ([ctx alias]

   (:maestro/main-class (data ctx
                              alias))))



(defn path+

  "Retrieves all extra paths specified by the given collection of aliases."

  [ctx alias+]

  (into []
        (comp (map (partial get
                            (ctx :aliases)))
              (mapcat :extra-paths))
        alias+))



(defn related

  "Retrieves aliases related to `alias`.

   `kw` is the keyword query in the alias data for finding that list of aliases. If nothing if found, a default alias is created
   by qualified `alias` under `default-ns` and that default alias is returned in a vector if it exists."

  [ctx kw default-ns alias]

  (let [deps-alias (ctx :aliases)]
    (or (get-in deps-alias
                [alias
                 kw])
        (let [alias-default (keyword default-ns
                                     (name alias))]
          (when (get deps-alias
                     alias-default)
            [alias-default])))))



(defn root

  "Retrieves the root path of the given `alias`."

  [ctx alias]

  (:maestro/root (data ctx
                       alias)))



(defn test

  "Retrieves test aliases related to the given `alias`."

  [ctx alias]

  (related ctx
           :maestro/test+
           "test"
           alias))



(defn test+

  "Like [[test]] but also retrieves transitive test aliases."


  ([ctx]

   (test+ ctx
          (ctx :maestro/require)))


  ([ctx alias+]

   (mapcat (partial test
                    ctx)
           alias+)))
