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

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.graylog.plugins.backup.BackupException;
import org.graylog.plugins.backup.RestoreException;
import org.graylog.plugins.backup.model.BackupStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class FsBackupStrategy extends AbstractMongoBackupStrategy
{

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    private static Logger LOG = LoggerFactory.getLogger(FsBackupStrategy.class);

    private final static String RESTORE_FOLDER = "graylog";


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
            pack( );
        }
        catch (Exception e)
        {
            throw new BackupException("Error during backup configuration", e);
        }
    }


    public void unpack(String path) throws RestoreException
    {
        File[] fileList = new File(path).listFiles( );

        if (fileList.length != 1)
        {
            LOG.error("There is 0 or more then 1 files in the restore folder {}",
                path);
            Throwables.propagate(new RestoreException("There is 0 or more then 1 files in the restore "));
        }

        if (fileList[0].getName( ).endsWith("zip"))
        {
            ZipUtil.unpack(new File(backupStruct.getSourcePath( ) + File.separator + fileList[0].getName( )), new File(backupStruct.getSourcePath( ) + File.separator + RESTORE_FOLDER));
        }
        else
        {
            LOG.error("There is no zip file in {} ", path);
            throw new RestoreException("Please check zip file");
        }

    }


    @Override
    public void restoreData() throws RestoreException
    {
        unpack(backupStruct.getSourcePath( ));

        StringBuilder restoreStr = new StringBuilder(backupStruct.getMongoInstallPath( ))
            .append(File.separator)
            .append("mongorestore")
            .append(" ")
            .append("-d").append(" ").append("graylog")
            .append(" ")
            .append(backupStruct.getSourcePath( ))
            .append(File.separator)
            .append(RESTORE_FOLDER);

        List<String> commands = Lists.newArrayList( );
        commands.add(osShellPath( ));
        commands.add("-c");
        commands.add(restoreStr.toString( ));
        try
        {
            processCommand(commands);
            FileUtils.deleteDirectory(new File(backupStruct.getSourcePath( ) + File.separator+ RESTORE_FOLDER));
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
    protected void pack() throws Exception
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
