# `:project/depstar`

Providing utilities and Babashka tasks for creating jars and uberjars in conjunction with Maestro, bundling all required aliases.

In this [repository's own ./deps.edn file](../../deps.edn), see aliases:

- `:ext/depstar`, for importing Depstar
- `:task/jar`, a working example of a task for jarring
- `:task/uberjar`, a working example of a task for jarring

Of course, follow [Depstar documentation](https://github.com/seancorfield/depstar).

See [Babashka tasks](../../bb.edn) `jar` and `uberjar`.
