# HMKit Fleet

HMKit Fleet is a Kotlin/Java library that combines different API-s: OAuth, Service Account API and
Telematics API to help car companies manage their fleet.

# Table of contents

* [Requirements](#requirements)
* [Getting Started](#getting-started)
* [Architecture](#architecture)
* [License](#License)
* [Contributing](#contributing)

### Requirements

Java 8+

### Getting Started

Get started with HMKit Fleet 📘[browse the documentation](https://docs.high-mobility.com/guides/getting-started/fleet/)

### Architecture

**General**: HMKit Fleet is a Kotlin library that combines 3 different API-s into a single fleet
owner package.

* hmkit-fleet: Uses [OkHttp](https://github.com/square/okhttp) to communicate with High-Mobility
  and [HMKit crypto](https://github.com/highmobility/hmkit-crypto-java/tree/telematics)
  to encrypt messages.

### Setup

* `git submodule update --init --recursive`
* import the Gradle project
* run the tests `./gradlew test`

### License

This repository is using the MIT license. See more in 📘[LICENSE](LICENSE)

### Contributing

Before starting, please read our contribution rules 📘[Contributing](CONTRIBUTING.md)
