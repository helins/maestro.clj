;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at https://mozilla.org/MPL/2.0/.


(ns helins.maestro.depstar

  "Provides tasks for jarring and uberjarring via [Depstar](https://github.com/seancorfield/depstar)."

  {:author "Adam Helinski"}

  (:require [helins.maestro       :as $]
            [helins.maestro.alias :as $.alias]))


;;;;;;;;;; Private


(defn- -jar

  ;; Takes care of the common work between [[jar]] and [[uberjar]].

  [ctx dir alias f]

  (let [main+      (ctx :maestro/main+)
        ctx-2      ($/walk ctx
                           main+)
        alias-main (last main+)]
    (-> ctx-2
        ($/walk [alias])
        (assoc :maestro/main
               alias-main)
        (as->
          ctx-3
          (update ctx-3
                  :maestro/arg+
                  (partial cons
                           (let [root (or ($.alias/root ctx-3
                                                        alias-main)
                                          (throw (ex-info "Alias needs `:maestro/root` pointing to project root directory"
                                                          {})))]
                             (format ":jar %s/%s/%s.jar :aliases '%s' :pom-file '\"%s/pom.xml\"'"
                                     (or (ctx :maestro.depstar/dir)
                                         "build")
                                     dir
                                     root
                                     (ctx-2 :maestro/require)
                                     root)))))
        (assoc :maestro/exec-char
               "X")
        f)))


;;;;;;;;;; Public


(defn jar

  "Prepares for creating a jar.

   Assumes Depstar is configured for jarring in the `:task/jar` alias.

   The main alias is the last alias given by the user as a CLI argument. Its data in `deps.edn` must contain
   a `:maestro/root` which points (if it exists) to the root directory of the related (sub)project.

   The root path is used to outputting the jar to `./build/jar/$ROOT.jar`."


  ([]

   (jar ($/ctx)))


  ([ctx]

   (-jar ctx
         "jar"
         :task/jar
         identity)))



(defn uberjar

  "Prepares for creating a jar.

   Assumes Depstar is configured for jarring in the `:task/uberjar` alias.

   See [[jar]] for comment about main alias and root.

   Also adds a main class argument if the main alias hask a `:maestro/main-class` key in its data that points
   to it."

  ([]

   (uberjar ($/ctx)))


  ([ctx]

   (-jar ctx
         "uberjar"
         :task/uberjar
         (fn [ctx]
           (if-some [main-class ($.alias/main-class ctx
                                                    (ctx :maestro/main))]
             (update ctx
                     :maestro/arg+
                     (partial cons
                              (str ":main-class "
                                   main-class)))
             ctx)))))
