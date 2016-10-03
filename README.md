# BackupConfig Plugin for Graylog

[![Build Status](https://travis-ci.org/https://github.com/fbalicchia/graylog-plugin-backup-configuration.git.svg?branch=master)](https://travis-ci.org/https://github.com/fbalicchia/graylog-plugin-backup-configuration.git)

__Use this paragraph to enter a description of your plugin.__

**Required Graylog version:** 2.0 and later

Installation
------------

[Download the plugin](https://github.com/https://github.com/fbalicchia/graylog-plugin-backup-configuration.git/releases)
and place the `.jar` file in your Graylog plugin directory. The plugin directory
is the `plugins/` folder relative from your `graylog-server` directory by default
and can be configured in your `graylog.conf` file.

Restart `graylog-server` and you are done.

Development
-----------

You can improve your development experience for the web interface part of your plugin
dramatically by making use of hot reloading. To do this, do the following:

* `git clone https://github.com/Graylog2/graylog2-server.git`
* `cd graylog2-server/graylog2-web-interface`
* `ln -s $YOURPLUGIN plugin/`
* `npm install && npm start`

Usage
-----

You can configure backup plugin Under System -> Configurations. Using  `update configuration button` is possible
adapt configuration to your environment. If not specified back data is saved under graylog data directory.
Backup file follow the this naming convention graylogyyyyMMddHHmm.zip
Now is supported only *nix console and backup can be stored on file system. In the near feature will be supported s3 storage.

For restore data is necessary to put backup file under restore folder.
At the moment there is a limitation that under restore folder there is only zip
without any directory.

* Under System-configuration press `Restore data button`.


Getting started
---------------

This project is using Maven 3 and requires Java 8 or higher.

* Clone this repository.
* Run `mvn package` to build a JAR file.
* Optional: Run `mvn jdeb:jdeb` and `mvn rpm:rpm` to create a DEB and RPM package respectively.
* Copy generated JAR file in target directory to your Graylog plugin directory.
* Restart the Graylog.

Plugin Release
--------------

We are using the maven release plugin:

```
$ mvn release:prepare
[...]
$ mvn release:perform
```

This sets the version numbers, creates a tag and pushes to GitHub. Travis CI will build the release artifacts and upload to GitHub automatically.
