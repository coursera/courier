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

To modify a test, first publish locally, then copy the test project to a separate directory
and run it from there, e.g.:

```sh
cp -r sbt-plugin/src/sbt-test/courier-sbt-plugin/sanity ~/tmp
cd ~/tmp
sbt -Dplugin.version=<published-courier-version>
```

TODO
----

[ ] Make this an auto-plugin that automatically adds all necessary settings to projects.
[ ] Incremental code generation? An attempt at implementing this has been started. The work-in-progress
    is available in the 'incremental' branch (https://github.com/coursera/courier/tree/incremental).
    This is technically challenging to implement, largely due to how DataSchema's are represented,
    which makes it difficult to determine which types reside in which .courier files.
    We decided not to land this branch due to both these technical issues around DataSchemas and
    also due because we discovered that the main performance bottleneck for Scala generation was
    the scalariform code formatter's performance (it consumed ~96% of the generator's running time),
    and so we focused our efforts on replacing it with the "poor man's'" code formatter.

