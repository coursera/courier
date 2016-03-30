Typescript-Lite Courier Data Binding Generator
==============================================

Experimental!

Lightweight Courier bindings for Typescript.

The guiding philosophy here was to achieve type safety with as small a runtime footprint as possible -- use the fact
that JSON is native to javascript to lightly define types and interfaces that describe your courier specs.

These bindings will achieve their goal if:

 * You can just cast the result of a JSON HTTP response (or other JSON source) into the base expected
   type and stay typesafe without having to cast anything more afterwards.
 * You can hand-write JSON in your test-cases and API requests, and those test-cases
   fail to compile when the record contract breaks.
 * You can enjoy the sweet auto-complete and compile-time warnings you've come to enjoy from typescript.

These bindings require Typescript 1.8+

Features missing in action
--------------------------

* **Coercer support**. The strong runtime component of Coercers don't gel easily with the philosophy of casting records
  lightly into target typescript interfaces, so there wasn't an easy fit.
    * Maybe these get included when Typescript-lite becomes just plain ol typescript.
* **Keyed maps**. Javascript objects naturally only support string-keyed maps. Since we are avoiding runtime
  overhead as much as possible we did not want to introduce a new type, nor the means to serialize said type.
    * Integrating `Immutable.js` would make a lot of sense when we want to expand into this direction.
* **Non-JSON serialization**. Although courier supports more compact binary formats (PSON, Avro, BSON), Typescript-lite
  bindings currently only supports valid JSON objects.
* **Default values**. As with the other points, default values require a runtime.
* **Test suites**. We haven't yet written tests against the reference suite for these bindings (though we've
  verified them by eye)
* **Flat-typed definitions**. This was just an oversight. Gotta add these.

Running the generator from the command line
-------------------------------------------

Build a fat jar from source and use that. See the below section *Building a fat jar*.

How code is generated
---------------------

**Records:**

* Records are generated as an interface within the typescript `namespace` specified by your record.
  * If you don't use a namespace, the interface will be injected at the top-level

e.g. the result of [Fortune.courier](https://github.com/coursera/courier/blob/master/reference-suite/src/main/courier/org/example/Fortune.courier):

```typescript
// ./my-tslite-bindings/org.example.Fortune.ts
import { FortuneTelling } from "./org.example.FortuneTelling";
import { DateTime } from "./org.example.common.DateTime";

/**
 * A fortune.
 */
export interface Fortune {

  /**
   * The fortune telling.
   */
  telling : FortuneTelling;

  createdAt : DateTime;
}
```

**Enums:**

* Enums are represented as [string literal types](https://basarat.gitbooks.io/typescript/content/docs/types/stringLiteralType.html).
* Convenience constants matching the string literals are provided despite having a runtime cost
  * TODO(erem) We should consider changing this. Not sure if the benefit outweighs the wire cost.
* Unlike other bindings, Typescript-lite does not include an `UNKNOWN$` option. In case of wire inconsistency
  you will have to just fall through to `undefined`.

e.g. the result of [MagicEightBallAnswer.courier](https://github.com/coursera/courier/blob/master/reference-suite/src/main/courier/org/example/MagicEightBallAnswer.courier):

```typescript
// ./my-tslite-bindings/org.example.MagicEightBallAnswer.ts
/**
 * Magic eight ball answers.
 */
export type MagicEightBallAnswer =  "IT_IS_CERTAIN"  | "ASK_AGAIN_LATER"  | "OUTLOOK_NOT_SO_GOOD" ;
export module MagicEightBallAnswer {

  export const IT_IS_CERTAIN: MagicEightBallAnswer = "IT_IS_CERTAIN";

  export const ASK_AGAIN_LATER: MagicEightBallAnswer = "ASK_AGAIN_LATER";

  export const OUTLOOK_NOT_SO_GOOD: MagicEightBallAnswer = "OUTLOOK_NOT_SO_GOOD";
}
```

**Arrays:**

* Arrays are represented as typescript arrays.

**Maps:**

* Maps are represented as javascript objects, as interfaced by `courier.Map<ValueT>`
* Only string-keyed maps are currently supported.

**Unions:**

* Unions are represented as an intersection type between all the members of the union.
* A Run-time `unpack` accessor is provided to test each aspect of the union
* Unlike other serializers, no `UNKNOWN$` member is generated for the union. If all provided accessors end up yielding
  undefined, then conclude that it was an unknown union member (see second example)

e.g. The result of [FortuneTelling.pdsc](https://github.com/coursera/courier/blob/master/reference-suite/src/main/pegasus/org/example/FortuneTelling.pdsc):
```typescript
// file: my-tslite-bindings/org.example.FortuneTelling.ts
import { MagicEightBall } from "./org.example.MagicEightBall";
import { FortuneCookie } from "./org.example.FortuneCookie";

export type FortuneTelling =  FortuneTelling.FortuneCookieMember | FortuneTelling.MagicEightBallMember | FortuneTelling.StringMember;
export module FortuneTelling {
  export interface FortuneTellingMember {
    [key: string]:  FortuneCookie | MagicEightBall | string;
  }

  export interface FortuneCookieMember extends FortuneTellingMember {
    "org.example.FortuneCookie": FortuneCookie;
  }

  export interface MagicEightBallMember extends FortuneTellingMember {
    "org.example.MagicEightBall": MagicEightBall;
  }

  export interface StringMember extends FortuneTellingMember {
    "string": string;
  }

  export function unpack(union: FortuneTelling) {
    return {
      fortuneCookie: union["org.example.FortuneCookie"] as FortuneCookie,
      magicEightBall: union["org.example.MagicEightBall"] as MagicEightBall,
      string_: union["string"] as string
    };
  }
}
```

Here's how you would use one:
```typescript
import { FortuneTelling } from "./my-tslite-bindings/org.example.FortuneTelling";
import { MacigEightBall } from "./my-tslite-bindings/org.example.MagicEightBall";
import { FortuneCookie } from "./my-tslite-bindings/org.example.FortuneCookie";

const telling: FortuneTelling = /* Get the union from somewhere...probably the wire */;

const { fortuneCookie, magicEightBall, string_ } = FortuneTelling.unpack(telling);

if (fortuneCookie) {
  // do something with fortuneCookie
} else if (magicEightBall) {
  // do something with magicEightBall
} else if (string_) {
  // do something with str
} else {
  throw 'a fit because no one will tell your fortune';
}
```

Projections and Optionality
---------------------------

It is common to send and receive partial data through REST.  This
is very commonly used when a subset of fields of a resources are "projected".

Since even fields that are marked as required in a Pegasus schema may be absent when data is
projected, Courier's [Optionality](https://github.com/coursera/courier/blob/master/typescript/generator/src/main/java/org.coursera.courier.tslite.TSProperties.java#L31)
settings defaults to REQUIRED_FIELDS_MAY_BE_ABSENT.  This allows a single
generated typescript struct to be used for bindings to unprojected and projected data.

If this behaviour is not desired, one may set `Optionality` to `STRICT`.

See the [Optionality property docs](https://github.com/coursera/courier/blob/master/typescript/generator/src/main/java/org.coursera.courier.tslite.TSProperties.java#L8)
for details on how to set the `Optionality` property.

Custom Types
------------

Custom Types allow any Typescript type to be bound to any pegasus primitive type.

For example, say a schema has been defined to represent a "date time" as a unix timestamp long:

```
namespace org.example

typeref DateTime = long
```

This results in a typescript file:
```typescript
// ./my-tslite-bindings/org.coursera.customtypes.DateTime.ts
export type DateTime = number;
```

JSON Serialization / Deserialization
------------------------------------

JSON serialization is trivial in typescript. Just take use `JSON.stringify` on any courier type that compiles.

```typescript
import { Message } from "./my-tslite-bindings/org.coursera.records.Message";

const message: Message = {"body": "Hello Pegasus!"};
const messageJson = JSON.stringify(message);
```

And of course you can read results as well.

```typescript
import { Message } from "./my-tslite-bindings/org.coursera.records.Message";

const messageStr: string = /* Get the message string somehow */
const message = JSON.parse(messageStr) as Message;
```

Runtime library
---------------

All generated Typescript-lite bindings depend on a `CourierRuntime.ts` class. This class provides the very minimal
functionality and type definitions necessary for generated bindings to work.

Building from source
--------------------

See the main CONTRIBUTING document for details.

Building a Fat Jar
------------------

```sh
$ sbt
> project typescript-lite-generator
> assembly
```

This will build a standalone "fat jar". This is particularly convenient for use as a standalone
commandline application.

Testing
-------

1. No testing has been done yet except verifying that the generated files from reference-suite pass `tsc`.
   That's next on the list =)

TODO
----

* [ ] Write some tests
* [ ] Add support for flat type definitions
* [ ] Clean up the gnarly implementation, which is just a fuddled around version of the Swift impl.
* [ ] Figure out the best way to distribute the 'fat jar'.
* [ ] Consider getting rid of the string literal constants in generated enums. They may not give much for the wire cost.
* [ ] Automate distribution of the Fat Jar
* [ ] Publish Fat Jar to remote repos? Typically fat jars should not be published to maven/ivy
      repos, but maybe it should be hosted for easy download elsewhere?
