# Public release

Release is done via a merged pull request to main/v0/v1 and then creating a release in GitHub UI.

## Steps for v0, v1 and v2

- update CHANGELOG.md
- update the version in `gradle.properties`
- merge the PR to the `v0`/`v1`/`main` branch
- Create release tag
  - target: v0/v1/main
  - press generate relase notes
  - press publish release
  - Action starts that pushes the package to MavenCentral.
  - You can check https://oss.sonatype.org/ whether release was successful or not.

❗Release the v0 and v1 first if releasing all versions. This way the v2 is the latest in the Releases changelog.

## Make a test release locally to staging

- comment out line `useInMemoryPgpKeys(signingKey, signingPassword)` in deploy-ossrh.gradle
- Update version in `$projectRoot/gradle.properties` and call `./gradlew -Prelease :hmkit-fleet:publishToSonatype`.
- Don't merge test version names to main


## Automatic version increment

It is possible to increment version on a PR, but the plugin currently works for main branch only. We want to release all versions with the same process, so we do it as described in the above paragraph. Otherwise, version incrementing works like this:

- merge a pull request to main. This creates a new tag with minor +=1.
  - optionally set a new major/minor version in `gradle.properties` before merging. Then the tag is created according to this manual version. Otherwise, patch number is incremented automatically.
