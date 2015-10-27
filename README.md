## Android development kit for prismic.io

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.prismic/android-kit/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.prismic/android-kit)
[![Build Status](https://api.travis-ci.org/prismicio/android-kit.png)](https://travis-ci.org/prismicio/android-kit)

### Getting started

The kit can be installed through Maven, Gradle or if you don't use a build system by dumping the jars in your lib directory. See the badge above for the latest version number.

#### [Maven](http://maven.apache.org/)

It is available on Maven Central. The kit coordinates are:

```
<dependency>
  <groupId>io.prismic</groupId>
  <artifactId>android-kit</artifactId>
  <version>1.2.0</version>
</dependency>
```

*(you may need to adapt the version number)*

#### Gradle

The package is the same as for Maven.

Kit:

```
dependencies {
    compile 'io.prismic:android-kit:1.2.0'
}
```

#### Include the kit and dependencies as an unmanaged jar

You will need the following jars:
 * android-kit-1.2.0.jar
 * java-kit-1.3.2.jar
 * jackson-databind-2.2.2.jar
 * jackson-annotations-2.2.2.jar
 * jackson-core-2.2.2.jar

You can see the [Android starter](https://github.com/prismicio/android-starter) for an example of Android application using the kit.

#### Get started with prismic.io

You can find out [how to get started with prismic.io](https://developers.prismic.io/documentation/UjBaQsuvzdIHvE4D/getting-started) on our [prismic.io developer's portal](https://developers.prismic.io/).

#### Get started using the kit

Also on our [prismic.io developer's portal](https://developers.prismic.io/), on top of our full documentation, you will:
 * get a thorough introduction of [how to use prismic.io kits](https://developers.prismic.io/documentation/UjBe8bGIJ3EKtgBZ/api-documentation#kits-and-helpers), including this one.

The easiest way to get started is to look at the [Android starter](https://github.com/prismicio/android-starter).

The steps to retrieve documents are the following:
 * Create a singleton instance of Prismic with the URL of your repository and optionally the token [(example)](https://github.com/prismicio/android-starter/blob/master/demo/src/main/java/io/prismic/android/app/DemoApplication.java) and make sure to initialize it.
 * The initialization is asynchronous, so make sure the Api is ready before using it by registering a listener [(example)](https://github.com/prismicio/android-starter/blob/master/demo/src/main/java/io/prismic/android/app/MainActivity.java#L51)

That's it, you can make your calls to your Prismic repository. See the javadoc for more details.

#### Kit's detailed documentation

You can find the documentation on [this Javadoc](http://prismicio.github.io/android-kit/javadoc/). Also take a look at the [java-kit Javadoc](http://prismicio.github.io/java-kit/) as this kit is heavily base on the Java kit.

Through Java's syntax, this kit contains some mild differences and syntastic sugar over the section of our documentation that tells you [how to use prismic.io kits](https://developers.prismic.io/documentation/UjBe8bGIJ3EKtgBZ/api-documentation#kits-and-helpers) in general (which you should read first). The differences are listed here:
 * Traditional POJO getter-and-setter syntaxes are applied; therefore, one calls a SearchForm like this: ```api.getForm("everything")```

### Changelog

Need to see what changed, or to upgrade your kit? We keep our changelog on [this repository's "Releases" tab](https://github.com/prismicio/android-kit/releases).

### Contribute to the kit

Contribution is open to all developer levels, read our "[Contribute to the official kits](https://developers.prismic.io/documentation/UszOeAEAANUlwFpp/contribute-to-the-official-kits)" documentation to learn more.

#### Install the kit locally

You will need [Gradle](http://gradle.org/) to contribute to the kit; first install it if you haven't.

Then run ```gradle compile```.

### Documentation

Please document any bugfix or new feature using the [Javadoc syntax](http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/javadoc.html)

If you find existing code that is not optimally documented and wish to make it better, we really appreciate it; but you should document it on its own branch and its own pull request.


### Licence

This software is licensed under the Apache 2 license, quoted below.

Copyright 2013 Zengularity (http://www.zengularity.com).

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
