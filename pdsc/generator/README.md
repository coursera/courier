.courier -> .pdsc generator
==============================================

Experimental! Work in Progress!

Coursera is looking into tooling to bridge the gap between Naptime / Courier (frameworks developed
in-house) and outside specifications like gRPC. One path forward would be to build Courier -> `X`
generators for each target `X` within this repository. An alternative path is to add support for
generating artifacts in a smaller set of intermediate target languages, then build / make use of
external libraries to convert these intermediate artifacts into some final target form.

This project explores the latter path by adding a `.courier` to `.pdsc` generator. While `.pdsc` is
not a widely adopted external specification, it is fairly simple and its JSON-serialized form is
easy to parse, which makes it far more portable and easier mesh with other tooling than the
human-optimized `.courier` schema format. The rough idea is that the
`org.coursera.courier.PdscGenerator` class can be packaged as a fat jar that takes a list of
`.courier` files and generates a corresponding list of `.pdsc` files. This in turn could be
script called within a generic build framework like gradle or bazel, which would feed the `.pdsc`
files into downstream generators.
