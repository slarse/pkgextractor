# pkgextractor
A minimal CLI application that extracts the package declaration from a `.class` file or a `.java` file.

### Usage
Get the jar-file from [the latest release](https://github.com/slarse/pkgextractor/releases/tag/v1.0.0) and download
it. Then run it like so.

```
$ java -jar pkgextractor-1.0.0-jar-with-dependencies.jar <CLASSFILE_OR_JAVAFILE>
```

It takes no options, just point it to a `.class` file or a `.java` file and it will figure out what it is.

### License
This project is licensed under the [MIT](LICENSE) license.
