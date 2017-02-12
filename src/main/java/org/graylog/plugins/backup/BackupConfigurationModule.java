package org.graylog.plugins.backup;

import org.graylog.plugins.backup.service.BackupResource;
import org.graylog.plugins.backup.service.BackupService;
import org.graylog.plugins.backup.service.impl.BackupServiceImpl;
import org.graylog2.plugin.PluginConfigBean;
import org.graylog2.plugin.PluginModule;

import java.util.Collections;
import java.util.Set;
import org.slf4j.Logger;

/**
 * Extend the PluginModule abstract class here to add you plugin to the system.
 */
public class BackupConfigurationModule extends PluginModule {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(BackupConfigurationModule.class);

    /**
     * Returns all configuration beans required by this plugin.
     *
     * Implementing this method is optional. The default method returns an empty {@link Set}.
     */
    @Override
    public Set<? extends PluginConfigBean> getConfigBeans() {
        return Collections.emptySet();
    }

    @Override
    protected void configure() {
        bind(BackupService.class).to(BackupServiceImpl.class);
        addPeriodical(BackupConfigurationPeriodical.class);
        addRestResource(BackupResource.class);
        addConfigBeans();
        LOG.info("graylog-plugin-backup-configuration plugin started");
    }
}
