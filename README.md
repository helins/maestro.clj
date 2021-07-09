# Maestro, calmly orchestrating your Clojure/script monorepo

Leveraging [Clojure Deps](https://clojure.org/guides/deps_and_cli) and [Babashka](https://github.com/babashka/babashka), managing a repository
hosting any number of projects becomes a breeze.

This tools aims to provide a reasonable and flexible solution focusing on:

- Building the exact classpath that is needed, for the required purposes, nothing more
- Pinning external dependencies, avoiding any duplication
- Promoting documentation of repository structure and overview

Not yet released, fork this repository or see build instructions below.


## Concept

In essence, the idea is to use one `deps.edn` alias for any unit of source: external libraries, internal ones, projects in the repository, etc.

This principle is demonstrated in this repository's own [./deps.edn file](./deps.edn). One can see that aliases are namespaced with recognizable names::
`:ext/kaocha` for the alias that imports [Kaocha](https://github.com/lambdaisland/kaocha), `:project/core` for core utilities, `project/kaocha` for
utilities targetting Kaocha, etc.

By being so granular and by providing a way for an alias to require other aliases, it becomes surprisingly simple to build arbitrarily complex projects
and provide a plethora of useful features.


## Writing an alias

The namespaces of this [./deps.edn file](./deps.edn]'s aliases are mostly arbitrary. One can choose whatever describes best the organization of a repository. We
recommend:

| Recommended ns | For | Example |
|---|---|---|
| `dev` | Bringing more paths and dependencies to a project when developing | `:dev/foo` |
| `ext` | External dependency | `:ext/kaocha` |
| `project` | A (sub)project ; eg. conveniently located in a `./project` directory | `:project/foo` |
| `task` | Needed for a task such as starting tests or creating a jar | `:task/test` |
| `test` | Bringing more paths and dependencies to a project for testing | `:test/foo` |

Alias data in `:aliases` of [./deps.edn](./deps.edn), besides conventional keys like `:extra-paths`, may contain:

| Key | Value |
|---|---|
| `:maestro/dev+` | Vector of aliases needed when starting this alias in dev mode ; if not present, looks for a related dev alias |
| `:maestro/doc` | Docstring documenting the purpose of that alias |
| `:maestro/main-class` | If relevant, points to the main class of this alias ; eg. needed for uberjarring |
| `:maestro/require` | Vector of extra aliases that are automatically added when this alias is used ; other projects, external deps, ... |
| `:maestro/root` | If relevant, points to the root directory where this alias is maintained |
| `:maestro/test+` | Vector of aliases needed when testing this alias ; if not present, looks for a related test alias |

During dev mode, if `:dev+` is not supplied, the namespace of the alias is replaced with **"dev"** in an attempt to find a related dev alias.
For instance: `:project/foo` -> `:dev/foo`. Similarly for testing, replacing the namespace with **"test"**.

Those are the only existing special treatments at the level of namespaces since those use cases are so common. Any other structure is at the
liberty of the user.


## Tasks

Given that kind of organization, this tool offers common tasks which are especially convenient when called as [Babashka tasks](https://book.babashka.org/#tasks).

The best examples is this [repository's own `bb.edn` file](./bb.edn) which lists all currently available tasks and where to find them.
The API has been designed to be flexible so that if defaults are not enough, users can hack around. **Namespaces of interest**: `helins.maestro`, `helins.maestro.cmd`.

Cloning/forking this repository, inspecting `deps.edn` and `bb.edn`, and trying things out should quickly provide an idea of how well the principle 
can scale.

Defaults are sufficient for most use cases. Starting a Clojure process is very similar to using Clojure CLI. For instance:

```bash
# Starting core project in dev mode + my own personal :nrepl alias
$ bb dev :project/core:nrepl

# Testing the Kaocha project and all the aliases it requires
$ bb test :project/kaocha

# Testing the Kaocha project only, not test paths for transitive projects
$ bb test:narrow :projeect/kaocha

# Printing classpath for core in dev mode, '-A' is mentioned explicitly
$ bb dev -A:project/core -Spath | bb cp
```


## In addition

Support for common tools and PRs are welcome.

Currently:

- [Depstar](./project/depstar), for jarring and uberjarring
- [Kaocha](./project/kaocha), for running tests


## Building locally

While not released, building a local jar:

```bash
$ bb jar :project/all

# See ./build/jar/project/all.jar
```


## Roadmap

More features can be imagined: printing documentation for aliases, sorting and filtering them using tags, pulling not only required aliases by X but also
aliases that rely on X for complex integration tests, etc.

Ideas and PRs welcome, and happy hacking ;)


## License

Copyright © 2021 Adam Helinski and Contributors

Licensed under the term of the Mozilla Public License 2.0, see LICENSE.
