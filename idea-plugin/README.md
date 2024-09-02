IntelliJ IDEA Plugin
--------------------

<!-- Plugin description -->
Features:

* ".courier" language support
  * Syntax highlighting, incl. config
  * Code formatting && brace matching, incl. code style config
  * Follow/find references
  * Reference auto-complete, incl. automatic import statement generation
  * Syntax errors for unrecognized type references
  * Go to type
  * Find usages
  * New file templates: "New" > "Courier Data Schema"
  * Doc comment support
  * Optimize imports
<!-- Plugin description end -->

Making changes to the Schema language
-------------------------------------

Changing the lexer:

Update `src/org/coursera/courier/psi/Courier.flex`, then right-click on `Courier.flex` and
select `Run flex Generator` from the context menu.

Changing the parser:

Update `src/org/coursera/courier/Courier.bnf` then right-click on `Courier.bnf` and
select `Generate parser code` from the context menu.

See [Grammar Kit](https://github.com/JetBrains/Grammar-Kit) for details on how to make changes
to the lexer and parser.

Installation
------------

The courier plugin is available via the JetBrains plugin repository: https://plugins.jetbrains.com/plugin/8005?pr=

It can be installed directly from IntelliJ:

```
Preferences -> Plugins -> Browse Repositories... -> Search for "Courier schema language" -> Click install
```

