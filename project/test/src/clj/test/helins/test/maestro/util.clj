(ns helins.test.maestro.util

  "Utilities for tests"

  {:author "Adam Helinski"})


;;;;;;;;;;


(defn simplify-ctx

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
