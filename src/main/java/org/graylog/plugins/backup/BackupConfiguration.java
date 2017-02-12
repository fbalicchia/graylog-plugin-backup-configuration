package org.graylog.plugins.backup;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

import org.joda.time.DateTime;
import org.joda.time.Period;

import javax.annotation.Nullable;


@AutoValue
@JsonDeserialize(builder = AutoValue_BackupConfiguration.Builder.class)
@JsonAutoDetect
public abstract class BackupConfiguration {
    public static Builder builder()
    {
        return new AutoValue_BackupConfiguration.Builder( );
    }

    @JsonProperty("cfg_backup_path")
    public abstract String backupPath();

    @JsonProperty("cfg_restore_path")
    public abstract String restorePath();

    @JsonProperty("mongodump_path")
    public abstract String mongoDumpPath();

    @JsonProperty("scheduled_period")
    public abstract Period scheduledPeriod();

    @JsonProperty("enabled")
    public abstract boolean enabled();

    @JsonProperty("lastbackup_time")
    @Nullable
    public abstract DateTime lastbackupTime();

    @JsonProperty("number_of_day_before")
    @Nullable
    public abstract Integer numberOfDaysBefore();



    public static BackupConfiguration defaultConfig()
    {
        return builder( ).enabled(false).backupPath("data/backup").restorePath("data/restore").mongoDumpPath("/usr/bin").scheduledPeriod(Period.days(15)).build( );
    }

    @AutoValue.Builder
    public static abstract class Builder
    {

        abstract BackupConfiguration build();

        @JsonProperty("cfg_backup_path")
        public abstract Builder backupPath(String backupPath);

        @JsonProperty("cfg_restore_path")
        public abstract Builder restorePath(@Nullable String restorePath);

        @JsonProperty("mongodump_path")
        public abstract Builder mongoDumpPath(String mongodumpPath);

        @JsonProperty("scheduled_period")
        public abstract Builder scheduledPeriod(Period scheduledPeriod);

        @JsonProperty("enabled")
        public abstract Builder enabled(boolean enabled);

        @JsonProperty("lastbackup_time")
        public abstract Builder lastbackupTime(DateTime lastBackupTime);

        @JsonProperty("number_of_day_before")
        public abstract Builder numberOfDaysBefore(Integer numberOfDaysBefore);

    }
}
