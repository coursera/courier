---
layout: page
---

Data Protocols
==============

JSON
----

All of the pegasus complex types may be serialized using:

~~~ scala
val fortune = Fortune(message = "Today is your lucky day!")
val json = DataTemplates.writeDataMap(fortune.data()) // or writeDataList
~~~

And deserialized using:

~~~ scala
val dataMap = DataTemplates.readDataMap(json) // or readDataList
val fortune = Fortune(dataMap, DataConversion.SetReadOnly) // or DataConversion.DeepCopy
~~~

As with all data protocols, JSON is available via a "codec":

* JacksonDataCodec - The primary JSON encoding used by Pegasus.

Binary Protocols
----------------

* [PsonDataCodec](https://github.com/linkedin/rest.li/blob/master/data/src/main/java/com/linkedin/data/codec/PsonDataCodec.java#L41) - Non-standard "performance" optimized JSON-like codec.
* BsonDataCodec - The [bson](http://bsonspec.org/) binary encoding for JSON-like data.

Avro Translators
----------------

Avro compatibility is provided using "translators" of Avro data:

* AvroGenericToDataTranslator
* DataMapToGenericRecordTranslator

and Avro schemas:

* SchemaTranslator - Translates Avro `Schema` to and from Pegasus `DataSchema`.

Inline String Protocol
----------------------

 [InlineStringCodec](https://github.com/coursera/courier/blob/master/runtime/src/main/scala/org/coursera/courier/codecs/InlineStringCodec.scala#L38) is a URL "Friendly" string encoding of data that can be used to
 encode complex types (sucha as records) as URL query params or path parts.  It is also used by Courier to  encode complex types as JSON object map keys.

Example codec use:

~~~ scala
val codec = new InlineStringCodec()

// deserialize
val dataMap = codec.bytesToMap("(key~value)".getBytes(Charset.forName("UTF-8"))

// serialize
val string = new String(codec.mapToBytes(dataMap, Charset.forName("UTF-8")))
~~~

All codecs also support input and output streams, e.g.:

~~~ scala
val codec = PsonDataCodec()

// deserialize
val dataMap = codec.readMap(inputStream)

// serialize
codec.writeMap(dataMap, outputStream)
~~~

Custom Protocols
----------------

To add a protocol of your own to Courier, simply implement the `DataCodec`.
