IntelliJ IDEA Plugin
--------------------

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

Hacker's Setup
--------------

   1. Import the `idea-plugin` directory using IntelliJ IDEA. Key ideas:
      * You need need the IntelliJ IDEA SDK (not a Java SDK), and
      * You need to **use the current idea-plugin.iml**
   2. Go to each .bnf file using the project explorer, right-click, and "Generate Parser Code"
      * src/org/coursera/courier/Courier.bnf
      * src/org/coursera/courier/schemadoc/schemadoc.bnf
   3. Go to the main .flex using the project explorer, right-click, and "Run JFlex Generator"
      * src/org/coursera/courier/psi/Courier.flex
   4. Build the project.
   5. Test it by doing "Run > Run 'Courier Plugin' ".

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

