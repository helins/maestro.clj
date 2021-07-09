;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at https://mozilla.org/MPL/2.0/.


(ns helins.test.maestro.depstar

  "Testing Depstar commands."

  {:author "Adam Helinski"}

  (:require [clojure.test             :as T]
            [helins.maestro           :as $]
            [helins.maestro.depstar   :as $.depstar]
            [helins.test.maestro.util :as $.test.util]))


;;;;;;;;;; Preparation


(def ctx
     (binding [*command-line-args* [":project/foo"]]
       ($/ctx)))


;;;;;;;;;;


(T/deftest jar

  (T/is (= {:maestro/arg+      [":jar build/jar/project/foo.jar :aliases '[:ext/kaocha :ext/bb.fs :project/core :project/kaocha :project/foo]' :pom-file '\"project/foo/pom.xml\"'"]
            :maestro/env       nil
            :maestro/exec-char "X"
            :maestro/main+     [:project/foo]
            :maestro/require   [:ext/kaocha
                                :ext/bb.fs
                                :project/core
                                :project/kaocha
                                :project/foo
                                :ext/depstar
                                :task/jar]}
           ($.test.util/simplify-ctx ($.depstar/jar ctx)))))



(T/deftest uberjar

  (T/is (= {:maestro/arg+      [":main-class helins.maestro.foo"
                                ":jar build/uberjar/project/foo.jar :aliases '[:ext/kaocha :ext/bb.fs :project/core :project/kaocha :project/foo]' :pom-file '\"project/foo/pom.xml\"'"]
            :maestro/env       nil
            :maestro/exec-char "X"
            :maestro/main+     [:project/foo]
            :maestro/require   [:ext/kaocha
                                :ext/bb.fs
                                :project/core
                                :project/kaocha
                                :project/foo
                                :ext/depstar
                                :task/uberjar]}
           ($.test.util/simplify-ctx ($.depstar/uberjar ctx)))))
