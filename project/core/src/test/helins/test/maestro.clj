;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at https://mozilla.org/MPL/2.0/.


(ns helins.test.maestro

  "Testing the core namespace.

   In reality, all useful operations are rather tested in [[helins.test.maestro.cmd]]."

  (:require [clojure.test   :as T]
            [helins.maestro :as $]))


;;;;;;;;;;


(T/deftest cmd

  (T/is (= "-X:foo:bar:baz lol mdr"
           ($/cmd {:maestro/arg+      ["lol"
                                       "mdr"]
                   :maestro/exec-char \X
                   :maestro/require   [:foo
                                       :bar
                                       :baz]}))))
