# Changelog

## [0.8.2] - 2024-09-18

### Added

- Audi and Škoda brand

## [0.8.1] - 2024-3-14

### Added

- Volvo brand

## [0.8.0] - 2024-1-31

- Fix `Json {} ignoreUnknownKeys` error
- Add deprecated `sendCommandV05` method to support 0.5.0 API

## [0.7.5] - 2023-11-29

### Added

- Porsche, Maserati and Kia brands

## [0.7.4] - 2023-11-16

### Added

- Tesla brand

## [0.7.3] - 2023-08-25

### Added

- Lexus brand to the `Brand` enum

## [0.7.2]
- Add Toyota brand

## [0.7.0]
- Add eligibility check for fleet vehicles

## [0.6.7]
- fix request clearance error parsing

## [0.6.6]
- fix JWT Authorization error in sandbox

## [0.6.5]
- Compile for Java 8

## [0.6.4]
- Add Sandbox brand
- Fix authentication for sandbox

## [0.6.3]
- Add Renault brand

## [0.6.2]
- Fix error `NoSuchMethodError: kotlinx.coroutines.AbstractCoroutine.<init>`

## [0.6.1]

- Parse the Telematics command response more specifically.   
  It can return an errors array or a Telematics command response. Please
  see [HMKitFleet.sendCommand](hmkit-fleet/src/main/kotlin/HMKitFleet.kt)
  and  [TelematicsResponse](hmkit-fleet/src/main/kotlin/network/Response.kt)

## [0.5.2]
- add getClearanceStatus:vin method
- undeprecate ClearanceStatus.Status.ERROR

## [0.5.1]

- add ford brand for fleet vehicles

## [0.5.0]

- add cancelClearance() method
- deprecate revokeClearance() method
- update dependencies to latest versions

## [0.4.1] - 2022.08.23

### Added

- Revoking clearance status
- jeep, fiat and alfa romeo brands

### Deprecated

- Error clearance status
