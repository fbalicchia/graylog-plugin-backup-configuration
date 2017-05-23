# BackupConfig Plugin for Graylog

[![Build Status](https://api.travis-ci.org/fbalicchia/graylog-plugin-backup-configuration.svg?branch=master)](https://travis-ci.org/fbalicchia/graylog-plugin-backup-configuration.git)
[![Github Downloads](https://img.shields.io/github/downloads/fbalicchia/graylog-plugin-backup-configuration/total.svg)](https://github.com/fbalicchia/graylog-plugin-backup-configuration/releases)


This plugin provides the possibility of saving and restore graylog environment configurations like registered user, dashboard, input etc.etc. To do this task the plugin uses directly **mongorestore** and **mongodump** command.
At the moment is supported only unix-like os and dump can be stored only on filesystem and not on other Storage Service like s3 etc.etc

Any feedback are wellcome !

**Required Graylog version:** 2.1 and later

Installation
------------

[Download the plugin](https://github.com/fbalicchia/graylog-plugin-backup-configuration/releases)
and place the `.jar` file in your Graylog plugin directory. The plugin directory
is the `plugins/` folder relative from your `graylog-server` directory by default
and can be configured in your `graylog.conf` file.
Restart `graylog-server` and you are done.

"Official" release is cut around the last Graylog build of plugins release in this case 2.1.2, nothing prevents to compile it independently using 
for example rc or beta version. In the same way binary package install jar under folder '/usr/share/graylog-server/plugin'

To adapt it to yours environment just change 'graylog.version' and 'graylog.plugin-dir' properties in pom file
the is created using a third library and not '-gzip' flag introduced with 3.2 Mongo's version


Usage
-----

You can configure backup plugin Under System -> Configurations.

[![](https://github.com/fbalicchia/graylog-plugin-backup-configuration/blob/master/images/backupconf_overview.png)]

Using  `update configuration button` is possible adapt configuration to your environment. If not specified back data is saved under graylog data directory.

[![](https://github.com/fbalicchia/graylog-plugin-backup-configuration/blob/master/images/backupconf_popup.png)]

Backup file follow the this naming convention **graylogyyyyMMddHHmm.zip**

For restore data is necessary to put backup file under restore folder.
At the moment there is a limitation that under restore folder there is only zip without any directory.

* Under System-configuration press `Restore data button`. If something fails the system show the corresponding error

[![](https://github.com/fbalicchia/graylog-plugin-backup-configuration/blob/master/images/backup_conf_restore_fail.png)]

Otherwise if process ends successfully

[![](https://github.com/fbalicchia/graylog-plugin-backup-configuration/blob/master/images/backconfig_restore.png)]


`Simple configuration example`
* threshold PT1M (every minute)
* Enable backup Process
* Select directory where you want to save data for example /tmp/data/bck and /tmp/data/restore
* find where MongoDump is installed. On ubuntu, for example, by default is under */usr/bin*

To restore your data please put the zip in this restore folder in this case /tmp/data/restore then from GUI click restore data





