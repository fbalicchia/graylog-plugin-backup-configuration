package org.graylog.plugins.backup;

import com.google.common.base.MoreObjects;
import org.graylog.plugins.backup.service.BackupService;
import org.graylog2.plugin.Tools;
import org.graylog2.plugin.cluster.ClusterConfigService;
import org.graylog2.plugin.periodical.Periodical;
import org.joda.time.*;
import org.slf4j.Logger;

import javax.inject.Inject;

import static org.joda.time.DateTimeFieldType.*;


/**
 * This is the plugin. Your class should implement one of the existing plugin
 * interfaces. (i.e. AlarmCallback, MessageInput, MessageOutput)
 */
public class BackupConfigurationPeriodical extends Periodical
{

    private BackupService backupService;

    private ClusterConfigService clusterConfigService;

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(BackupConfigurationPeriodical.class);

    @Inject
    public BackupConfigurationPeriodical(BackupService backupService, ClusterConfigService clusterConfigService)
    {
        this.backupService = backupService;
        this.clusterConfigService = clusterConfigService;
    }

    @Override
    public boolean runsForever()
    {
        return false;
    }

    @Override
    public boolean stopOnGracefulShutdown()
    {
        return true;
    }

    @Override
    public boolean masterOnly()
    {
        return true;
    }

    @Override
    public boolean startOnThisNode()
    {
        return true;
    }

    @Override
    public boolean isDaemon()
    {
        return true;
    }

    @Override
    public int getInitialDelaySeconds()
    {
        return 0;
    }

    @Override
    public int getPeriodSeconds()
    {
        return 60;
    }

    @Override
    protected Logger getLogger()
    {
        return LOG;
    }

    @Override
    public void doRun()
    {
        BackupConfiguration info = clusterConfigService.getOrDefault(BackupConfiguration.class, BackupConfiguration.defaultConfig( ));
        if (canStartBackup(info))
        {
            try
            {
                backupService.start(info);
                LOG.info("Backup GrayLog configuration completed ");
                BackupConfiguration newInfo = BackupConfiguration.builder( ).enabled(info.enabled( )).scheduledPeriod(info.scheduledPeriod( )).mongoDumpPath(info.mongoDumpPath( )).restorePath(info.restorePath( )).backupPath(info.backupPath( )).lastbackupTime(Tools.nowUTC( )).build( );
                clusterConfigService.write(newInfo);
            }
            catch (BackupException e)
            {
                LOG.error("", e);
            }
        }

    }


    public Boolean canStartBackup(BackupConfiguration backupConfiguration)
    {
        if (!backupConfiguration.enabled( ))
        {
            return false;
        }
        DateTime nextTime = determineRotationPeriodAnchor(backupConfiguration).plus(backupConfiguration.scheduledPeriod( ));
        if (nextTime.isAfter(Tools.nowUTC( )))
        {
            LOG.debug("Next time backup {}", nextTime.toString( ));
        }
        return backupConfiguration.lastbackupTime( ) == null || !nextTime.isAfterNow( );
    }


    /**
     * I like the approach used in TimeBasedRotationStrategy but i don't want to have refence to that class
     * This method come from org.graylog2.indexer.rotation.strategies.TimeBasedRotationStrategy
     *
     * @param backupConfiguration
     * @return
     */
    private DateTime determineRotationPeriodAnchor(BackupConfiguration backupConfiguration)
    {

        DateTime lastAnchor = backupConfiguration.lastbackupTime( );
        Period period = backupConfiguration.scheduledPeriod( );

        final Period normalized = period.normalizedStandard( );
        int years = normalized.getYears( );
        int months = normalized.getMonths( );
        int weeks = normalized.getWeeks( );
        int days = normalized.getDays( );
        int hours = normalized.getHours( );
        int minutes = normalized.getMinutes( );
        int seconds = normalized.getSeconds( );

        if (years == 0 && months == 0 && weeks == 0 && days == 0 && hours == 0 && minutes == 0 && seconds == 0)
        {
            throw new IllegalArgumentException("Invalid rotation period specified");
        }

        // find the largest non-zero stride in the period. that's our anchor type. statement order matters here!
        DateTimeFieldType largestStrideType = null;
        if (seconds > 0)
            largestStrideType = secondOfMinute( );
        if (minutes > 0)
            largestStrideType = minuteOfHour( );
        if (hours > 0)
            largestStrideType = hourOfDay( );
        if (days > 0)
            largestStrideType = dayOfMonth( );
        if (weeks > 0)
            largestStrideType = weekOfWeekyear( );
        if (months > 0)
            largestStrideType = monthOfYear( );
        if (years > 0)
            largestStrideType = year( );
        if (largestStrideType == null)
        {
            throw new IllegalArgumentException("Could not determine rotation stride length.");
        }

        final DateTime anchorTime = MoreObjects.firstNonNull(lastAnchor, Tools.nowUTC( ));

        final DateTimeField field = largestStrideType.getField(anchorTime.getChronology( ));
        // use normalized here to make sure we actually have the largestStride type available! see https://github.com/Graylog2/graylog2-server/issues/836
        int periodValue = normalized.get(largestStrideType.getDurationType( ));
        final long fieldValue = field.roundFloor(anchorTime.getMillis( ));

        final int fieldValueInUnit = field.get(fieldValue);
        if (periodValue == 0)
        {
            // https://github.com/Graylog2/graylog2-server/issues/836
            LOG.warn("Determining stride length failed because of a 0 period. Defaulting back to 1 period to avoid crashing, but this is a bug!");
            periodValue = 1;
        }
        final long difference = (fieldValueInUnit % periodValue);
        final long newValue = field.add(fieldValue, -1 * difference);
        return new DateTime(newValue, DateTimeZone.UTC);
    }
}

