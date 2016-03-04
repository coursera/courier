Courier Data Binding Generator for JS
=====================================

Experimental!

Here we are experimenting with generating data bindings for js.

The main types of data bindings we are experimenting with are:

* [Flow types](http://flowtype.org/docs/type-aliases.html)
* [React prop types](https://facebook.github.io/react/docs/reusable-components.html#prop-validation)
  note that React prop types can also be [statically checked using flow](http://flowtype.org/docs/react.html)

Issues discovered so far:

* https://github.com/facebook/flow/issues/1507

Setup
-----

Generate prototype bindings for flow:

```
$ sbt
> project js-generator-test
> test
```

```
cd js/testsuite
npm install
```

Testing
-------

```
cd js/testsuite
npm run build
flow check
```
