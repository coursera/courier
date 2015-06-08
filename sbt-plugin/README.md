Courier SBT Plugin
------------------

This plugin is based on both the
[rest.li-sbt-plugin](https://github.com/linkedin/rest.li-sbt-plugin) and the
[Sleipnir sbt plugin](https://github.com/dmitriy-yefremov/sleipnir).

This plugin is an SBT
[auto plugin](http://www.scala-sbt.org/0.13/docs/Plugins.html#Creating+an+auto+plugin).

Usage
-----

For usage details, see the top level README.md of this project.

Tests
-----

The plugin is tested using ["scripted"](http://eed3si9n.com/testing-sbt-plugins).

Tests projects are under `sbt-plugin/src/sbt-test/courier-sbt-plugin` and are run using:

```sh
sbt scripted
```

Test projects may be added or updated as needed, but should be used only to test plugin
functionality, all other tests should be kept in `generator-test` as these scripted
test take considerably longer to run due to the project setup and tear down that is required.
