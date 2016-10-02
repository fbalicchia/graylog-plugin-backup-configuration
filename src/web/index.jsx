// eslint-disable-next-line no-unused-vars
import packageJson from "../../package.json";
import {PluginManifest, PluginStore} from "graylog-web-plugin/plugin";
import BackupConfiguration from "components/BackupConfiguration";

PluginStore.register(new PluginManifest(packageJson, {
    systemConfigurations: [
        {
            component: BackupConfiguration,
            configType: 'org.graylog.plugins.backup.BackupConfiguration',
        },
    ],
}));
