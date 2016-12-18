# BackupConfig Plugin for Graylog

[![Build Status](https://api.travis-ci.org/fbalicchia/graylog-plugin-backup-configuration.svg?branch=master)](https://travis-ci.org/https://github.com/fbalicchia/graylog-plugin-backup-configuration.git)

This plugin provides the possibility of saving and restore graylog environment configurations like registered user, dashboard, input etc.etc. To do this task the plugin uses directly **mongorestore** and **mongodump** command.
At the moment is supported only unix-like os and dump can be stored only on filesystem and not on other Storage Service like s3 etc.etc

**Required Graylog version:** 2.0 and later

Installation
------------

[Download the plugin](https://github.com/fbalicchia/graylog-plugin-backup-configuration/releases)
and place the `.jar` file in your Graylog plugin directory. The plugin directory
is the `plugins/` folder relative from your `graylog-server` directory by default
and can be configured in your `graylog.conf` file.
Restart `graylog-server` and you are done.

There is no yet a "ufficial" realease but is possibile to compile source like any other plugin 

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







