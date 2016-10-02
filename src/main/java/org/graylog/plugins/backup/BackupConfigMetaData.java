package org.graylog.plugins.backup;

import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.ServerStatus;
import org.graylog2.plugin.Version;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

/**
 * Implement the PluginMetaData interface here.
 */
public class BackupConfigMetaData implements PluginMetaData {
    private static final String PLUGIN_PROPERTIES = "org.graylog.plugins.graylog-plugin-backup-configuration/graylog-plugin.properties";

    @Override
    public String getUniqueId() {
        return "org.graylog.plugins.BackupConfigPlugin";
    }

    @Override
    public String getName() {
        return "BackupConfig";
    }

    @Override
    public String getAuthor() {
        return "Filippo Balicchia <fbalicchia@gmail.com>";
    }

    @Override
    public URI getURL() {
        return URI.create("https://github.com/https://github.com/fbalicchia");
    }

    @Override
    public Version getVersion() {
        return Version.fromPluginProperties(getClass(), PLUGIN_PROPERTIES, "version", Version.from(0, 0, 0, "unknown"));
    }

    @Override
    public String getDescription() {
        return "Pluging for backup and restore graylog configuration";
    }

    @Override
    public Version getRequiredVersion() {
        return Version.fromPluginProperties(getClass(), PLUGIN_PROPERTIES, "graylog.version", Version.from(0, 0, 0, "unknown"));
    }

    @Override
    public Set<ServerStatus.Capability> getRequiredCapabilities() {
        return Collections.emptySet();
    }
}
