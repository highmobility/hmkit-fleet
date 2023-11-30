# Changelog

This is the changelog for v1 releases. See v0 changelog in the [v0 branch](https://github.com/highmobility/hmkit-fleet/tree/v0).

## [1.0.2] - 2023-11-16

### Added

- Tesla brand

## [1.0.1] - 2023-08-25

### Added

- Lexus brand to the `Brand` enum

## [1.0.0] - 2023-05-22

### Added

- Initialize HMKitFleet with the `new` keyword. [PR](https://github.com/highmobility/hmkit-fleet/pull/19)
```java
HMKitFleet hmkit = new HMKitFleet(
  apiConfiguration,
  HMKitFleet.Environment.SANDBOX
);
```

### Removed

- Remove the singleton variant of HMKitFleet
