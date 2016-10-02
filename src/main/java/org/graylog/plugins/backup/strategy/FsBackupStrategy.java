/**
 * Copyright (C) ${project.inceptionYear} ${owner} (hello@graylog.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.graylog.plugins.backup.strategy;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import org.graylog.plugins.backup.BackupException;
import org.graylog.plugins.backup.RestoreException;
import org.graylog.plugins.backup.model.BackupStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;


public class FsBackupStrategy extends AbstractMongoBackupStrategy
{

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    private static Logger LOG = LoggerFactory.getLogger(FsBackupStrategy.class);


    public FsBackupStrategy(BackupStruct backupStruct)
    {
        this.backupStruct = backupStruct;
    }

    @Override
    public void saveData() throws BackupException
    {

        List<String> commands = Lists.newArrayList( );
        commands.add(osShellPath( ));
        commands.add("-c");
        commands.add(buildCommandLine(backupStruct));
        try
        {
            processCommand(commands);
            zipFile( );
        }
        catch (Exception e)
        {
            throw new BackupException("Error during backup configuration", e);
        }
    }

    @Override
    public void restoreData() throws RestoreException
    {

        StringBuilder restoreStr = new StringBuilder(backupStruct.getMongoInstallPath( ))
            .append(File.separator)
            .append("mongorestore")
            .append(" ")
            .append(backupStruct.getSourcePath( ))
            .append(File.separator)
            .append("graylog");

        List<String> commands = Lists.newArrayList( );
        commands.add(osShellPath( ));
        commands.add("-c");
        commands.add(restoreStr.toString( ));
        try
        {
            processCommand(commands);
        }
        catch (Exception e)
        {
            throw new RestoreException("Error during restore data", e);
        }

    }

    private String buildCommandLine(BackupStruct backupStruct)
    {
        StringBuilder result = new StringBuilder(backupStruct.getMongoInstallPath( ));
        result.append(File.separator).append("mongodump").append(" -d graylog").append(" -o ").append(backupStruct.getTargetPath( ));
        return result.toString( );
    }

    @Override
    protected void zipFile() throws Exception
    {
        LocalDateTime now = LocalDateTime.now( );
        String dateFormat = now.format(formatter);
        String srcFolder = backupStruct.getTargetPath( ) + File.separator + "graylog";
        String dstFolder = backupStruct.getTargetPath( ) + File.separator + "graylog" + dateFormat + ".zip";
        ZipUtil.pack(new File(srcFolder), new File(dstFolder));
        LOG.info("Graylog config backup completed");
        FileUtils.deleteDirectory(new File(srcFolder));
    }
}
