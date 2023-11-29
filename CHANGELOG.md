# Changelog

This is the changelog for v2 releases. See v0/v1 releases in appropriate branches.

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