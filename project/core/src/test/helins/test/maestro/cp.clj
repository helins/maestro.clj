;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at https://mozilla.org/MPL/2.0/.


(ns helins.test.maestro.cp

  "Testing classpath utilities."

  {:author "Adam Helinski"}

  (:require [clojure.test      :as T]
            [helins.maestro.cp :as $.cp]))


;;;;;;;;;;


(T/deftest split

  (T/is (= ["foo"
            "bar"
            "baz"
            "bouz"]
           ($.cp/split "foo:bar:baz\n:bouz"))
        "Splitting and trimming newlines"))
