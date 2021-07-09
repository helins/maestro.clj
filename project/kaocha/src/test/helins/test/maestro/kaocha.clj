;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at https://mozilla.org/MPL/2.0/.


(ns helins.test.maestro.kaocha

  "Testing Kaocha commands."

  {:author "Adam Helinski"}

  (:require [clojure.test             :as T]
            [helins.maestro           :as $]
            [helins.maestro.kaocha    :as $.kaocha]
            [helins.test.maestro.util :as $.test.util]))


;;;;;;;;;; Preparation


(def ctx
     (binding [*command-line-args* [":project/foo"]]
       ($/ctx)))


;;;;;;;;;; Tests


(T/deftest broad

  (T/is (= {:maestro/arg+      []
            :maestro/env       nil
            :maestro/exec-char \M
            :maestro/main+     [:project/foo]
            :maestro/test+     [:test/core
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
                                :task/test]}
           ($.test.util/simplify-ctx ($.kaocha/broad ctx)))))



(T/deftest narrow

  (T/is (= {:maestro/arg+      []
            :maestro/env       nil
            :maestro/exec-char \M
            :maestro/main+     [:project/foo]
            :maestro/test+     [:test/foo]
            :maestro/require   [:test/foo
                                :ext/kaocha
                                :ext/bb.fs
                                :project/core
                                :project/kaocha
                                :project/foo
                                :task/test]}
           ($.test.util/simplify-ctx ($.kaocha/narrow ctx)))))
