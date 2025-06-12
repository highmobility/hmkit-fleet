# Changelog

This is the changelog for v1 releases. See v0 changelog in the [v0 branch](https://github.com/highmobility/hmkit-fleet/tree/v0).

## [1.0.8] - 2025-06-12

### Added

- Nissan and Polestar brands

## [1.0.7] - 2024-11-20

### Added

- Dacia, Volkswagen, Seat and Cupra brands

## [1.0.6] - 2024-09-18

### Added

- Audi and Škoda brand

## [1.0.5] - 2024-3-14

### Added

- Volvo, Kia, Maserati and Porsche brand
 
## [1.0.4] - 2024-1-31

- Fix `Json {} ignoreUnknownKeys` error

## [1.0.3] - 2023-11-29

### Added

- Porsche, Maserati and Kia brands

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
