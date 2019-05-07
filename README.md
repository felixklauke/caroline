# caroline

Ever tried using the java version of the reactive extensions ( https://github.com/ReactiveX/RxJava ) in a minecraft
bukkit server? Yes? Then you may know what this is and why you need it. Without any modifications its very
unlikely that your server will run very long. Using this pluginManager you can schedule rx javas internal thread architecture
on bukkit schedulers to make rxjava usable in general. Have fun using this.

# Build Status
|             	| Build Status                                                                                                                                              	| Test Code Coverage                                                                                                                                               	|
|-------------	|-----------------------------------------------------------------------------------------------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------------------------------------------------------------------	|
| Master      	| [![Build Status](https://travis-ci.org/FelixKlauke/caroline.svg?branch=master)](https://travis-ci.org/FelixKlauke/caroline) 	| [![codecov](https://codecov.io/gh/FelixKlauke/caroline/branch/master/graph/badge.svg)](https://codecov.io/gh/FelixKlauke/caroline) 	|
| Development 	| [![Build Status](https://travis-ci.org/FelixKlauke/caroline.svg?branch=dev)](https://travis-ci.org/FelixKlauke/caroline)    	| [![codecov](https://codecov.io/gh/FelixKlauke/caroline/branch/dev/graph/badge.svg)](https://codecov.io/gh/FelixKlauke/caroline)    	|

# Usage
- Install [Maven](http://maven.apache.org/download.cgi)
- Clone this repo
- Install: ```mvn clean install```

**Maven repositories**
```xml
<repositories>
    <!-- Klauke Enterprises Releases -->
    <repository>
        <id>klauke-enterprises-maven-releases</id>
        <name>Klauke Enterprises Maven Releases</name>
        <url>https://repository.klauke-enterprises.com/repository/maven-releases/</url>
    </repository>
	
    <!-- Klauke Enterprises Snapshots -->
    <repository>
        <id>klauke-enterprises-maven-snapshots</id>
        <name>Klauke Enterprises Maven Snapshots</name>
        <url>https://repository.klauke-enterprises.com/repository/maven-snapshots/</url>
    </repository>
</repositories>
```

**Maven dependencies**

_Caroline Core:_
```xml
<dependency>
    <groupId>de.felixklauke.caroline</groupId>
    <artifactId>caroline-core</artifactId>
    <version>1.2.0</version>
</dependency>
```
# Example

## Event Example

_Plain old listener:_
```java
RxCaroline.observeEvent(PlayerJoinEvent.class).subscribe(event -> {
        event.setJoinMessage("A new player joined: " + event.getPlayer().getName());          
    });
```

_Use a specific priority:_
```java
RxCaroline.observeEvent(PlayerJoinEvent.class, EventPriority.LOWEST).subscribe(event -> {
        event.setJoinMessage("A new player joined: " + event.getPlayer().getName());          
    });
```

_Ignore cancelled events:_
```java
RxCaroline.observeEvent(PlayerJoinEvent.class, true).subscribe(event -> {
        event.setJoinMessage("A new player joined: " + event.getPlayer().getName());          
    });
```

_Both at the same time:_
```java
RxCaroline.observeEvent(PlayerJoinEvent.class, EventPriority.LOWEST, true).subscribe(event -> {
        event.setJoinMessage("A new player joined: " + event.getPlayer().getName());          
    });
```

## Command Example

## Packet Adapter example

# Architecture
We use google guice ( https://github.com/google/guice )  for dependency injection. You should have a look at that
before you consider touching our architecture. The guice dependencies are defined in the
`CarolineModule`. The bukkit pluginManager will create an instance of the main
application using the guice injector. The main application will hook our schedulers into rx java. Currently
we support
- Computation Scheduler (synchronous)
- IO Scheduler (asynchronous)
- New Thread Scheduler (asynchronous)
and configure rx java to use them. The tasks executed by these scheduler will be mapped on bukkits internal
scheduler architecture.
