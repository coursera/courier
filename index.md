---
layout: page
---
<br><br>
<div class="row">
  <div class="col-md-12">
    <p class="lead">
      Courier is a modern data interchange system for web + mobile stacks that
      combines an expressive schema language with language idiomatic data
      binding generators.
    </p>
  </div>
</div>
<hr>
<div class="row">
  <div class="col-md-12">
    <a id="why-courier"></a>
    <h2>Why Courier?</h2>
    <p class="lead">
    Courier is the only comprehensive, schema based data system centered around
    JSON. Binary protocols, such as Protobuf and Thrift have proven that data
    schemas are an excellent way to share the structure of data messaged
    between multiple systems and programming languages. For JSON, however,
    there are few systems providing this schema driven approach and none that
    provide satisfactory type systems. Courier's solves this problem, providing
    a type safe, schema driven way of sharing data between backends, web and
    mobile.
    </p>
  </div>
</div>

<hr>

<div class="row">
  <a id="features"></a>
  <div class="col-md-12"><center><h2>Courier Features</h2></center></div>
</div>
<br>
<div class="row">
  <div class="col-md-4" style="text-align: center;">
    <h4>
      Build for JSON.. and Binary
    </h4>
    <p>
      Courier serializes to natural looking JSON but also supports multiple
      binary data protocols including Avro, PSON and BSON.
    </p>
  </div>

  <div class="col-md-4" style="text-align: center;">
    <h4>
      Modern Schema language
    </h4>
    <p>
      Courier is a concise but comprehensive schema language, based on
      Pegasus and Avro, and with language features from Scala, Swift, and Avro
      IDL.
    </p>
  </div>

  <div class="col-md-4" style="text-align: center;">
    <h4>
      Language interoperability
    </h4>
    <p>
      Full support for <strong>Scala</strong>, <strong>Java</strong>,
      <strong>Swift</strong> and  <strong>Javascript</strong>
    </p>
    <p>
      Courier schemas enable languages to easily share the structure of the
      data they read and write, and Courier's data binding code generators
      eliminate the tedious and error prone process of manually writing
      serializers.
    </p>
  </div>
</div>
<br>
<div class="row">
  <div class="col-md-4" style="text-align: center;">
    <h4>
      Full IDE Support
    </h4>
    <p>
      Courier has a full featured
      <a href="https://plugins.jetbrains.com/plugin/8005?pr=idea">IntelliJ plugin</a>
      that includes syntax highlighting,
      syntax error indication, code formatting, auto-import and auto-complete.
    </p>
  </div>

  <div class="col-md-4" style="text-align: center;">
    <h4>
      Idiomatic Data Bindings
    </h4>
    <p>
      Courier generates language idiomatic data bindings that look and feel
      natural to developers.
    </p>
    <p>
      For instance, the Scala generator produces code with case classes,
      <code>Option</code>s, default parameters, pattern matching, sealed traits
      and Scala collections.
    </p>
  </div>

  <div class="col-md-4" style="text-align: center;">
    <h4>
      Fast and stable
    </h4>

    <p>
      Courier is built on the
      <a href="https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates">Pegasus</a>
      data system, an efficient JVM data system that is in large scale
      production use at Linkedin.
    </p>
  </div>
</div>

<br><br><br>

<div class="row">
  <div class="col-md-12" style="text-align: center;">
    <a href="{{ site.github.url }}/gettingstarted/#getting-started" role="button" class="btn btn-primary btn-lg">Get Started</a>
  </div>
</div>

<br><br><br>

<div class="row">
  <div class="col-md-4" style="text-align: center;">
    <h4>
      Community
    </h4>
    <p>
      <a href="https://groups.google.com/d/forum/courier">Discussion Group</a>
    </p>
  </div>
  <div class="col-md-4" style="text-align: center;">
    <h4>
      License
    </h4>
    <p>
      Courier is <a href="https://github.com/coursera/courier/blob/master/LICENSE.txt">Apache 2.0 Licensed</a>.
    </p>
  </div>

  <div class="col-md-4" style="text-align: center;">
    <h4>
      Contributing
    </h4>
    <p>
    See
    <a href="https://github.com/coursera/courier/blob/master/CONTRIBUTING.md">CONTRIBUTING.md</a>.
    </p>
  </div>
</div>
