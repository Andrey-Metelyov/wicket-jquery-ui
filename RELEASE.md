# wicket-jquery-ui
**jQuery UI integration in Wicket 1.5.x &amp; Wicket 6.x**

## Release process
This document explains the steps to release a new version of this project.

### Prerequisites

Open Maven settings.xml (i.e. `~/.m2/settings.xml`) file and add the needed `servers` and  with the following content:

````xml
    <!--
    <servers>
    -->
      <server>
        <id>sonatype-nexus-snapshots</id>
        <username>[THE USERNAME]</username>
        <password>[THE PASSWORD]</password>
      </server>
      <server>
        <id>sonatype-nexus-staging</id>
        <username>[THE USERNAME]</username>
        <password>[THE PASSWORD]</password>
      </server>
      <!--
    </servers>
    -->
    <!--
    <profiles>
    -->
    <profile>
      <id>sonatype-oss-release</id>
      <properties>
        <gpg.passphrase>[MY GPG PASSPHRASE]</gpg.passphrase>
      </properties>
    </profile>
  <!--
  </profiles>
  -->
  
</settings>

````

Make sure to provide username and password of a user that is allowed to manage this project at [Sonatype OSS](https://oss.sonatype.org).
The GOG passphrase is used to sign the artefacts before uploading them to Sonatype Maven repository.

### Deploy a Snapshot version
````
$ mvn clean deploy
````

After finishing the deployment you can check that the new snapshot version is at [Sonatype OSS Snapshots](https://oss.sonatype.org/content/repositories/snapshots/com/googlecode/wicket-jquery-ui/)

### Deploy a release version

* Set the version in the master pom.xml to Apache Wicket's version with `-SNAPSHOT` suffix (e.g. `6.11.0-SNAPSHOT`)
* update `<version>6.x.y</version>` in **README.md** and **homepage.html** ( *wicket-jquery-ui-samples* )
* commit and push 
* mvn release:clean
* mvn release:prepare
* mvn release:perform
* Go to [Sonatype OSS](https://oss.sonatype.org) and login with the same credentials as in settings.xml's server configuration
* Navigate to `Staging Repositories`, find the deployed bundle in the grid, select it and close it (button *Close* in the toolbar)
* Wait few minutes until Nexus closes it and then select the bundle and release it (button *Release* in the toolbar)