package org.graylog.plugins.backup;


import org.graylog.plugins.backup.service.BackupResource;
import org.graylog.plugins.backup.service.BackupService;
import org.graylog.plugins.backup.service.impl.BackupServiceImpl;
import org.graylog2.plugin.PluginModule;
import org.slf4j.Logger;

/**
 * Extend the PluginModule abstract class here to add you plugin to the system.
 */
public class BackupConfigModule extends PluginModule {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(BackupConfigModule.class);

    @Override
    protected void configure() {
        bind(BackupService.class).to(BackupServiceImpl.class);
        addPeriodical(BackupConfigPeriodical.class);
        addRestResource(BackupResource.class);
        addConfigBeans();
        LOG.info("graylog-plugin-backup-configuration plagin started");

    }
}
