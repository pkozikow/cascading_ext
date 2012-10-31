Project cascading_ext
========

cascading_ext is a collection of tools built on top of the [Cascading](https://github.com/cwensel/cascading) platform which make it easy to build, debug, and run simple and high-performance data workflows. 

Project Features
====

Some of the most interesting public classes in the project (so far).

### SubAssemblies ###

<b>BloomJoin</b>

[BloomJoin](https://github.com/LiveRamp/cascading_ext/blob/master/src/java/com/liveramp/cascading_ext/assembly/BloomJoin.java) is designed to be a drop-in replacement for CoGroup, with significant performance improvements on some datasets by filtering the LHS pipe against a bloom filter built from the keys on the RHS.  

When joining a very large LHS store against a relatively small RHS store, using a BloomJoin can massively reduce the performance cost of the job (internally, we have cut the reduce time of jobs by up to 90% by only reducing over the tiny subset of the data that makes it past the bloom filter.)  Jobs which are good candidates for HashJoin, but whose RHS tuples don't fit in memory, should benefit from a BloomJoin vs a CoGroup.

see example usage: BloomJoinExample, BloomJoinExampleWithoutCascadingUtil

*BloomFilter*

[BloomFilter](https://github.com/LiveRamp/cascading_ext/blob/master/src/java/com/liveramp/cascading_ext/assembly/BloomFilter.java) is similar to BloomJoin, but can be used when no fields from the RHS are needed in the output.  This allows for simpler field algebra (duplicate field names are not a problem.)

Another feature of BloomFilter is the ability to perform an inexact filter, and entirely avoid reducing over the LHS.  When performing an inexact join, the LHS is passed over the bloom filter from the RHS, but the final exact CoGroup is skipped, leaving both true and false positives in the output. 

*MultiGroupBy* 

[MultiGroupBy](https://github.com/LiveRamp/cascading_ext/blob/master/src/java/com/liveramp/cascading_ext/assembly/MultiGroupBy.java) allows the user to easily GroupBy two or more pipes on a common field without performing a full Inner/OuterJoin first (which can lead to an explosion in the number of tuples, if keys are not distinct.)  The MultiBuffer interface gives a user-defined function access to all tuples sharing a common key, across all input pipes.  

see TestMultiGroupBy for example usage

### Tools ###

CascadingUtil 

CascadingUtil is a utility class which makes it easy to add default properties and strategies to all jobs which are run in a codebase, and which adds some useful logging and debugging information.  For a simple example of how to use this class, see com.liveramp.cascading_ext.example.SimpleFlowExample:

CascadingUtil.get().getFlowConnector().connect("Example flow", sources, sink, pipe).complete();

By default CascadingUtil will
  - print and format counters for each step
  - retrieve and log map or reduce task errors if the job fails
  - extend Cascading's job naming scheme with improved naming for some taps which use UUID identifiers.

see com.liveramp.cascading_ext.example.FlowWithCustomCascadingUtil to see examples of how CascadingUtil can be extended to include custom default properties.  Subclasses can easily: 

  - set default properties
  - add serialization classes (addSerialization)
  - add serialization tokens (addSerializationToken)
  - add flow step strategies (addDefaultFlowStepStrategy)


FunctionStats, FilterStats, BufferStats, AggregatorStats, 

Wrapper classes which make it easier to debug a complex flow with many Filters / Functions / etc.  These wrapper classes add counters logging the number of tuples a given operation inputs or outputs.  See Test*Stats to see usage examples.

Download
====

Building
====

To build cascading_ext.jar from source:

```bash
> ant dist
```

To run the test suite locally, HADOOP_HOME and HADOOP_CONF_DIR must point to your local hadoop install.

Usage
====

See usage instructions here for running Cascading with Apache Hadoop.  Everything should work fine if cascading_ext and all third-party jars in lib/ are in your jobjar.

https://github.com/cwensel/cascading

To try out any of the code in the com.liveramp.cascading_ext.example package in production, a jobjar task for cascading_ext itself is available:

```bash
> ant jobjar
```

Bugs, features, pull requests
====

Bug reports or feature requests are welcome: https://github.com/liveramp/cascading_ext/issues

Changes you'd like us to merge in?  Pull requests are welcome. 

License
====
Copyright 2012 Liveramp

Licensed under the Apache License, Version 2.0

http://www.apache.org/licenses/LICENSE-2.0

