Courier Maven Plugin
====================

Usage
------

Add the `courier-maven-plugin`` plugin to a maven project:

```xml
<project>
  ...
  <build>
    <sourceDirectory>src/main/scala</sourceDirectory>
    <testSourceDirectory>src/test/scala</testSourceDirectory>
    <plugins>
      <plugin>
        <groupId>org.coursera.courier</groupId>
        <artifactId>courier-maven-plugin</artifactId>
        <version>${courierVersion}</version>
        <executions>
          <execution>
            <phase>generate-sources</phase>
            <goals>
              <goal>schemas</goal>
            </goals>
            <configuration>
              <sourceDirectory>${project.basedir}/src/main/courier</sourceDirectory>
              <outputDirectory>${project.basedir}/src/main/scala</outputDirectory>
            </configuration>
          </execution>
        </executions>
      </plugin>

      <plugin>
        <groupId>net.alchim31.maven</groupId>
        <artifactId>scala-maven-plugin</artifactId>
        <version>3.2.1</version>
      </plugin>

    </plugins>
  </build>

  <dependencies>
    <dependency>
      <groupId>org.scala-lang</groupId>
      <artifactId>scala-library</artifactId>
      <version>2.11.5</version>
    </dependency>
    <dependency>
      <groupId>org.coursera.courier</groupId>
      <artifactId>courier-runtime_2.11</artifactId>
      <version>${courierVersion}</version>
    </dependency>
  </dependencies>

</project>
```

Add an a `.courier` file to your project:

`src/main/courier/org/example/Fortune.courier`:

```
namespace org.example

record Fortune {
  message: string
}
```

Generate bindings:

```sh
mvn generate-sources scala:compile
```

Publishing locally
------------------

mvn install

Publishing to maven central
---------------------------

mvn clean deploy -Prelease
