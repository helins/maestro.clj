;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at https://mozilla.org/MPL/2.0/.


(ns helins.maestro

  "Core namespace hosting miscellaneous utilities.
  
   See [[ctx]]."

  {:author "Adam Helinski"}

  (:refer-clojure :exclude [test])
  (:require [clojure.edn]
            [clojure.string]
            [helins.maestro.aggr  :as $.aggr]
            [helins.maestro.alias :as $.alias]))


;;;;;;;;;; Operation over a context


(defn aggr

  "Default alias aggregating function for [[walk]].

   Uses:

   - [[helins.maestro.aggr/alias]]
   - [[helins.maestro.aggr/env]]"

  [ctx alias config]

  (-> ctx
      ($.aggr/alias alias
                    config)
      ($.aggr/env alias
                  config)))



(defn walk

  "Walks the given `alias+` collection (retrieves those found under `:maestro/main+` by default).
  
   Walking means each alias and all the transitive aliases it requires using `:maestro/require` in its
   data in `deps.edn` will go through the aggregating function found under `:maestro/aggr` in `ctx`."


  ([ctx]

   (walk ctx
         (ctx :maestro/main+)))


  ([ctx alias+]

   (let [user-aggr (ctx :maestro/aggr)]
     (reduce (fn [ctx-2 alias]
               (if (contains? (ctx-2 :maestro/seen+)
                              alias)
                 ctx-2
                 (let [data ($.alias/data ctx
                                          alias)]
                   (user-aggr (walk (update ctx-2
                                            :maestro/seen+
                                            (fnil conj
                                                  #{})
                                            alias)
                                     (:maestro/require data))
                               alias
                               data))))
             (update ctx
                     :maestro/seen+
                     (fn [seen+]
                       (or seen+
                           #{})))
             alias+))))


;;;;;;;;;; I/O


(defn deps-edn

  "Reads and returns the `deps.edn` file."

  ([]

   (deps-edn "deps.edn"))


  ([path]

    (-> path
        slurp
        clojure.edn/read-string)))


;;;;;;;;;; Initialization


(defn ctx

  "A context designates the map that is omnipresent throughout all this tool.
  
   It is an augmented `deps.edn` map which adds:

   | Key | Purpose |
   |---|---|
   | `:maestro/aggr` | See [[aggr]]. |
   | `:maestro/arg+` | CLI arguments, removing the first one which is mandatory and specifies deps aliases |
   | `:maestro/main+` | Aliases specified by the user in the CLI arguments |
   | `:maestro/require | Empty vector that will mostlikely be used to compute all required aliases |"

  ([]

   (ctx (deps-edn)
        *command-line-args*))


  ([deps-edn cli-arg+]

   (let [arg-first    (or (first cli-arg+)
                          (throw (ex-info "No argument provided, requires at least one alias"
                                          {})))
         [arg-first-2
          exec-char]  (if (clojure.string/starts-with? arg-first
                                                       "-")
                        (try
                          [(.substring ^String arg-first
                                       2)
                           (nth arg-first
                                1)]
                          (catch Throwable _ex
                            (throw (ex-info "First argument starting with '-' needs a Clojure execution letter (eg. '-M')"
                                            {:maestro/arg arg-first}))))
                        [arg-first
                         nil])]
      (-> deps-edn
          (merge (if (.ready *in*)
                   (clojure.edn/read *in*)
                   {}))
          (assoc :maestro/aggr    aggr
                 :maestro/arg+    (rest cli-arg+)
                 :maestro/main+   (or (and (clojure.string/starts-with? arg-first-2
                                                                        ":")
                                           (->> (clojure.string/split arg-first-2
                                                                      #":")
                                                rest
                                                (mapv keyword)
                                                not-empty))
                                      (throw (ex-info "At least one alias must be specified"
                                                      {:maestro/arg arg-first-2})))
                 :maestro/require [])
          (cond->
            exec-char
            (assoc :maestro/exec-char exec-char))))))


;;;;;;;;;; Miscellaneous insigts


(defn cmd

  "Given `ctx`, returns a Clojure CLI command based on:
  
   | Key | Purpose |
   |---|---|
   | `:maestro/arg+ | See [[ctx]] |
   | `:maestro/exec-char` | Eg. \"M\", \"X\" |
   | `:maestro/require` | See [[ctx]] |"

  [ctx]

  (format "-%s%s %s"
          (ctx :maestro/exec-char)
          (clojure.string/join ""
                               (ctx :maestro/require))
          (clojure.string/join " "
                               (ctx :maestro/arg+))))
