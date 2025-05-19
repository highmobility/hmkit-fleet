⚠️ This library is deprecate, use Fleet API directly https://docs.high-mobility.com/guides/cloud/service-account-api-fleet-clearance-tutorial/

---

This is the HMKitFleet v2 branch. See the [v1 branch](https://github.com/highmobility/hmkit-fleet/tree/v1) for the previous version.

# HMKit Fleet

HMKit Fleet is a Kotlin/Java library that combines different API-s: OAuth, Service Account API and
Telematics API to help car companies manage their fleet.

# Table of contents

* [Requirements](#requirements)
* [Getting Started](#getting-started)
* [Architecture](#architecture)
* [API Reference](/docs/hmkit-fleet/com.highmobility.hmkitfleet/-h-m-kit-fleet/index.md)
* [License](#License)
* [Contributing](#contributing)

### Requirements

- Target is Java 8
- Compiled with Kotlin and Java 17

### Getting Started

Get started with HMKit Fleet 📘[browse the documentation](https://docs.high-mobility.com/guides/getting-started/fleet/)

```
dependencies {
  implementation('com.high-mobility:hmkit-fleet:{version}')
}
```

### Architecture

**General**: HMKit Fleet is a Kotlin library that combines 3 different API-s into a single fleet
owner package.

* hmkit-fleet: Uses [OkHttp](https://github.com/square/okhttp)

### Setup

* `git submodule update --init --recursive`
* import the Gradle project
* run the tests `./gradlew test`


### Run the sample app 
* Check out the hmkit-fleet-consumer/README.md about how to setup the sample app
* Run the sample app with `./gradlew :hmkit-fleet-consumer:run`

### Release/Deployment

check `./gradle/deploy-process.md`

### License

This repository is using the MIT license. See more in 📘[LICENSE](LICENSE)

### Contributing

Before starting, please read our contribution rules 📘[Contributing](CONTRIBUTING.md)
