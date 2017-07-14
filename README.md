# USB Drive Detector

[![Build Status][travis-image]][travis-url]  |
[![version][maven-version]][maven-url]

A Java library to get a list of all usb storage devices connected to the computer. It works on the three main operating systems (Windows, Linux and OS X).

This was forked to get to Java 1.6 compatibility, but I ended up fixing the Mac and Linux usb detectors along the way. Note that the maven info below does not apply to this fork.


### Maven dependency

To include this library in your project just use:
```xml
<dependency>
    <groupId>net.samuelcampos</groupId>
    <artifactId>usbdrivedectector</artifactId>
    <version>1.0.2</version>
</dependency>
```


[travis-url]: https://travis-ci.org/samuelcampos/usbdrivedetector
[travis-image]: https://travis-ci.org/samuelcampos/usbdrivedetector.svg?branch=master

[maven-url]: http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22usbdrivedectector%22
[maven-version]: https://img.shields.io/maven-central/v/net.samuelcampos/usbdrivedectector.svg?style=flat

