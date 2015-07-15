JMH Benchmarks
--------------
This project contains [JMH](http://openjdk.java.net/projects/code-tools/jmh/) benchmarks for Courier
using [sbt-jmh](https://github.com/ktoso/sbt-jmh).

```sh
sbt
> project benchmark
> ++2.11.6
> clean
> run -bm All

For GC data:
> clean
> run -bm All -i 5 -f 1 -wf 1 -wi 1 -prof gc
```
