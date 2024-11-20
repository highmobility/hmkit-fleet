# Changelog

This is the changelog for v2 releases. See v0/v1 releases in appropriate branches.

## [2.1.2] - 2024-11-20

### Added

- Dacia, Volkswagen, Seat and Cupra brands

## [2.1.1] - 2024-09-18

### Added
- Audi and Škoda brand

## [2.1.0] - 2024-4-23

### Added

- Vehicle static data request

## [2.0.4] - 2024-3-14

### Added

- Volvo brand

## [2.0.3] - 2024-1-31

- Fix `Json {} ignoreUnknownKeys` error

## [2.0.2] - 2023-12-7

- Update javadoc

## [2.0.1] - 2023-11-29

### Added

- Porsche, Maserati and Kia brands

## [2.0.0] - 2023-16-11

### Added
- new `HMKitFleet` constructor with OAuth/OAuth private key credentials

```java
HMKitCredentials credentials = new HMKitOAuthCredentials(
  "client_id",
  "client_secret"
);

HMKitConfiguration configuration = new HMKitConfiguration.Builder()
  .credentials(credentials)
  .environment(HMKitFleet.Environment.SANDBOX)
  .build();

HMKitFleet hmkit = new HMKitFleet(configuration);
```

- Tesla brand