# JsonDatabase


## About

This project is a server app and client app pairing that interacts with a simple JSON file for storing data in JSON format. 


## Getting Started

### Prerequisites

* Java JDK 11
* Apache Maven (for building with included `pom.xml`)

### Installing

Building two fat JARs (one for server, one for client) is difficult using Apache Maven. So unfortunately, creating both executable fat JARs will require some configuration in `pom.xml` and `src/META-INF/MANIFEST.MF`.

#### Server Fat JAR

To create the server fat JAR, follow these two steps:

1. Make sure the `<mainClass></mainClass>` tag points to the `ServerApp` class. `<mainClass>` should look like this:

   ``` 
   <manifest>
       <mainClass>io.github.ldnicolasmay.jsondatabase.server.ServerApp</mainClass>
   </manifest>
   ```

2. Make sure the `MainClass` line points to the `ServerApp` class. `MainClass` in `src/META-INF/MANIFEST.MF` should look like this:

   ```
   Main-Class: io.github.ldnicolasmay.jsondatabase.server.ServerApp
   ```

3. Use Maven Assembly to create a fat JAR:
   
   ```shell 
   mvn clean package assembly:single
   ```

4. Copy/move the resulting fat JAR in the `target/` directory to another directory (like `app/`):

   ```shell 
   cp target/JsonDatabase-1.0-SNAPSHOT-jar-with-dependencies.jar app/server.jar
   ```

#### Client Fat JAR

To create the client fat JAR, follow these two steps:

1. Make sure the `<mainClass></mainClass>` tag points to the `ClientApp` class. `<mainClass>` should look like this:

   ``` 
   <manifest>
       <mainClass>io.github.ldnicolasmay.jsondatabase.client.ClientApp</mainClass>
   </manifest>
   ```

2. Make sure the `MainClass` line points to the `ClientApp` class. `MainClass` in `src/META-INF/MANIFEST.MF` should look like this:

   ```
   Main-Class: io.github.ldnicolasmay.jsondatabase.client.ClientApp
   ```

3. Use Maven Assembly to create a fat JAR:

   ```shell 
   mvn clean package assembly:single
   ```

4. Copy/move the resulting fat JAR in the `target/` directory to another directory (like `app/`):

   ```shell 
   cp target/JsonDatabase-1.0-SNAPSHOT-jar-with-dependencies.jar app/client.jar
   ```


## Usage

### Server 

From the project directory, run the server:

```shell
java -jar app/server.jar
```

You should see a "Server started!" message in the console.

### Client

From another terminal, run the client app passing parameters.

#### Get Command

Get values from the JSON data for a given key or keys.

The command ...

```shell
java -jar app/client.jar --type get --key person1
```

... returns ...

```
Client started!
Sent: {"type":"get","key":"person1"}
Received: {"response":"OK","value":{"first_name":"Terence","last_name":"McKenna","dates":{"birth_date":"1946-11-16","death_date":"2000-04-03"},"thoughts":["machine elves","opposition to organized religion","archaic revival"],"theories":["psilocybin panspermia","technological singularity","stoned ape human evolution","novelty theory","timewave zero"],"bibliography":[{"title":"The Invisible Landscape: Mind, Hallucinogens, and the I Ching","year":1975},{"title":"The Archaic Revival","year":1992},{"title":"Food of the Gods","year":1992}]}}
```

You can also pass in JSON files with preformatted JSON get requests:

```shell
java -jar app/client.jar --in testGet1.json
```

```shell
java -jar app/client.jar --in testGet2.json
```

#### Set Command

Set a value for a given key or keys.

```shell
java -jar app/client.jar --set --key test_key --value "Hello World!"
```

You can also pass in JSON files with preformatted JSON set requests:

```shell
java -jar app/client.jar --in testSet.JSON
```

#### Delete Command

Delete a key-value pair JSON object for a given key or keys.

```shell
java -jar app/client.jar --type delete --key person2
```

You can also pass in JSON files with preformatted JSON set requests:

```shell
java -jar app/client.jar --in testDelete1.JSON
```

#### Exit Command

This command shuts down the server:

```shell
java -jar app/client --type exit
```

## Built Using

* [Java 11](https://openjdk.java.net/projects/jdk/11/)
* [Apache Maven](https://maven.apache.org/)
* [JCommander](https://jcommander.org/)
* [Gson](https://github.com/google/gson)

## Authors

[@LDNicolasMay](https://github.com/ldnicolasmay) - Work

## Acknowledgments

Thanks to [JetBrains Academy](https://www.jetbrains.com/academy/) for the project!
