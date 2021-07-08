;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at https://mozilla.org/MPL/2.0/.


(ns helins.test.maestro.alias

  "Testing alias utilities."

  {:author "Adam Helinski"}

  (:refer-clojure :exclude [test])
  (:require [clojure.test         :as T]
            [helins.maestro.alias :as $.alias]))


;;;;;;;;;; Helpers


(defn- -related

  [f kw default-ns]

  (T/is (nil? (f {:aliases {:project/foo nil}}
                            :project/foo))
        "Nothing")

  (let [default (keyword default-ns
                         "foo")]
    (T/is (= [default]
             (f {:aliases {default      {}
                            :project/foo nil}}
                 :project/foo))
          "Default alias"))

  (T/is (= [:a
            :b]
           (f {:aliases {:a
                         :b
                         :project/foo {kw [:a
                                           :b]}}}
               :project/foo))
        "Given aliases"))


;;;;;;;;;; Tests


(T/deftest data

  (T/is (= :data
           ($.alias/data {:aliases {:project/foo :data}}
                         :project/foo))))



(T/deftest dev

  (-related $.alias/dev
            :maestro/dev+
            "dev"))



(T/deftest main-class

  (T/is (= :main
           ($.alias/main-class {:aliases {:project/foo {:maestro/main-class :main}}}
                               :project/foo))))



(T/deftest path+

  (T/is (= ["a"
            "b"
            "c"]
           ($.alias/path+ {:aliases {:project/bar {:extra-paths ["a"]}
                                     :project/foo {:extra-paths ["b"
                                                                 "c"]}}}
                          [:project/bar
                           :project/foo]))))



(T/deftest root

  (T/is (= "root"
           ($.alias/root {:aliases {:project/foo {:maestro/root "root"}}}
                         :project/foo))))



(T/deftest test

  (-related $.alias/test
            :maestro/test+
            "test"))
