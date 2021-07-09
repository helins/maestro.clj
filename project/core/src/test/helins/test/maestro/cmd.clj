;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at https://mozilla.org/MPL/2.0/.


(ns helins.test.maestro.cmd

  "Testing command utilities."

  {:author "Adam Helinski"}

  (:require [clojure.test       :as T]
            [helins.maestro     :as $]
            [helins.maestro.cmd :as $.cmd]))


;;;;;;;;;; Preparation


(def arg

  "Simulated CLI arg."

  "foo_bar_baz")



(def ctx
     (binding [*command-line-args* [":project/foo"
                                    arg]]
       ($/ctx)))



(defn simplified

  "Extracts only a set of key-values from `ctx` for testing."

  [ctx]

  (select-keys ctx
               [:maestro/arg+
                :maestro/dev+
                :maestro/env
                :maestro/exec-char
                :maestro/main+
                :maestro/test+
                :maestro/require]))


;;;;;;;;;; Required aliases


(def ctx-simple

  "Simplified basic ctx for assertions"

  {:maestro/arg+    [arg]
   :maestro/env     nil
   :maestro/main+   [:project/foo]
   :maestro/require [:ext/kaocha
                     :ext/bb.fs
                     :project/core
                     :project/kaocha
                     :project/foo]})


;;;;;;;;;; Tests


(T/deftest dev

  (T/is (= (merge ctx-simple
                  {:maestro/dev+      [:dev/kaocha
                                       :dev/core
                                       :dev/foo]
                   :maestro/env       {"FOO" "BAR"}
                   :maestro/exec-char \M
                   :maestro/test+     [:test/kaocha
                                       :test/core
                                       :test/foo]
                   :maestro/require   [:test/kaocha
                                       :test/core
                                       :test/foo
                                       :dev/kaocha
                                       :dev/core
                                       :dev/foo
                                       :ext/kaocha
                                       :task/test
                                       :task/no-main
                                       :task/dev
                                       :ext/bb.fs
                                       :project/core
                                       :project/kaocha
                                       :project/foo]})
           (simplified ($.cmd/dev ctx)))))



(T/deftest function

  (T/is (= (merge ctx-simple
                  {:maestro/exec-char \X})
           (simplified ($.cmd/function ctx)))))



(T/deftest main

  (T/is (= (merge ctx-simple
                  {:maestro/exec-char \M})
           (simplified ($.cmd/main ctx)))))



(T/deftest main-class

  (T/is (= (merge ctx-simple
                  {:maestro/arg+      ["-m helins.maestro.foo"
                                       arg]
                   :maestro/exec-char \M})
           (simplified ($.cmd/main-class ctx)))))



(T/deftest test-broad

  (T/is (= (merge ctx-simple
                  {:maestro/test+     [:test/core
                                       :test/kaocha
                                       :test/foo]
                   :maestro/require   [:test/core
                                       :test/kaocha
                                       :test/foo
                                       :ext/kaocha
                                       :ext/bb.fs
                                       :project/core
                                       :project/kaocha
                                       :project/foo
                                       :task/test]})
           (simplified ($.cmd/test-broad ctx)))))



(T/deftest test-narrow

  (T/is (= (merge ctx-simple
                  {:maestro/test+     [:test/foo]
                   :maestro/require   [:test/foo
                                       :ext/kaocha
                                       :ext/bb.fs
                                       :project/core
                                       :project/kaocha
                                       :project/foo
                                       :task/test]})
           (simplified ($.cmd/test-narrow ctx)))))

