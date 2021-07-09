# `:project/kaocha`

Providing utilities for testing with [Kaocha](https://github.com/lambdaisland/kaocha).

In this [repository's own ./deps.edn file](../../deps.edn), see aliases:

- `:ext/kaocha`, for importing Kaocha
- `:task/test`, needed for tests.

See [Babashka tasks](../../bb.edn) `test` and `test:narrow`.

Those tasks generate configuration for a unique test in `./private/maestro_kaocha.edn`, bundling all needed main and test paths.

See the [test configuration file of this repository](../../kaocha.edn) for a working example of how to include that test in the
general configuration. Including is describe in [Kaocha documentation](https://cljdoc.org/d/lambdaisland/kaocha/1.0.861/doc/3-configuration).
