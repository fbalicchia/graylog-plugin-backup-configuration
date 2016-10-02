package org.graylog.plugins.backup;

import org.apache.commons.lang3.StringUtils;
import org.graylog2.plugin.InstantMillisProvider;
import org.graylog2.plugin.Tools;
import org.graylog2.plugin.cluster.ClusterConfigService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;


public class BackConfigPeriodicalTest
{
    @Mock
    private BackupConfiguration backupConfigurationMock;
    @Mock
    private ClusterConfigService clusterConfigServiceMock;

    @Before
    public void setUp()
    {
        backupConfigurationMock = mock(BackupConfiguration.class);
        clusterConfigServiceMock = mock(ClusterConfigService.class);

    }

    @Test
    public void canStartBackup()
    {
        final DateTime initialTime = new DateTime(2016, 9, 26, 23, 0, 35, 0, DateTimeZone.UTC);

        final InstantMillisProvider clock = new InstantMillisProvider(initialTime);
        DateTimeUtils.setCurrentMillisProvider(clock);

        BackupConfiguration testinfo = BackupConfiguration.builder( ).enabled(false).scheduledPeriod(Period.days(1))
            .mongoDumpPath(StringUtils.EMPTY)
            .restorePath(StringUtils.EMPTY)
            .backupPath(StringUtils.EMPTY)
            .lastbackupTime(Tools.nowUTC( )).build( );

        BackupConfigPeriodical configuration = new BackupConfigPeriodical(null, null);
        assertThat(configuration.canStartBackup(testinfo)).isFalse( );

        DateTime lastBackup = new DateTime(2016, 9, 26, 22, 56, 0, 0);
        testinfo = BackupConfiguration.builder( ).enabled(true).scheduledPeriod(Period.minutes(1)).mongoDumpPath("").restorePath("").backupPath("").lastbackupTime(lastBackup).build( );
        assertThat(configuration.canStartBackup(testinfo)).isTrue( );

        lastBackup = new DateTime(2016, 9, 26, 23, 2, 0, 0, DateTimeZone.UTC);
        testinfo = BackupConfiguration.builder( ).enabled(true).scheduledPeriod(Period.minutes(1)).mongoDumpPath("").restorePath("").backupPath("").lastbackupTime(lastBackup).build( );
        assertThat(configuration.canStartBackup(testinfo)).isFalse( );

    }

}
