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
package org.graylog.plugins.backup.service.impl;

import org.apache.commons.lang3.StringUtils;

import org.graylog.plugins.backup.BackupConfiguration;
import org.graylog.plugins.backup.BackupException;
import org.graylog.plugins.backup.RestoreException;
import org.graylog.plugins.backup.model.BackupStruct;
import org.graylog.plugins.backup.service.BackupService;
import org.graylog.plugins.backup.strategy.BackupFactoryStrategy;
import org.graylog.plugins.backup.strategy.BackupStrategy;
import org.graylog2.configuration.MongoDbConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Singleton
public class BackupServiceImpl implements BackupService
{

    private static final Logger LOG = LoggerFactory.getLogger(BackupServiceImpl.class);

    private MongoDbConfiguration mongoDbConfiguration;


    @Inject
    public BackupServiceImpl(MongoDbConfiguration mongoDbConfiguration)
    {
        this.mongoDbConfiguration = mongoDbConfiguration;
    }


    @Override
    public void start(BackupConfiguration backupConfig) throws BackupException
    {

        List<BackupStruct> bckList = mongoDbConfiguration.getMongoClientURI( ).getHosts( ).stream( ).filter(item -> StringUtils.isNotEmpty(item)).map(item ->
        {
            String[] addressToken = item.split(":");

            String host = StringUtils.EMPTY;
            String port = StringUtils.EMPTY;
            if (addressToken.length == 2)
            {
                host = addressToken[0];
                port = addressToken[1];
            }
            return new BackupStruct.BackupStructBuilder( ).setMongoInstallPath(backupConfig.mongoDumpPath( )).setPort(port).setHostName(host).setTargetPath(backupConfig.backupPath( )).build( );
        }).collect(Collectors.toList( ));

        for (BackupStruct item : bckList)
        {
            doDump(item);
        }

    }

    @Override
    public void restore(BackupConfiguration backupConfig) throws RestoreException
    {
        BackupStruct backupStruct = new BackupStruct.BackupStructBuilder( ).setMongoInstallPath(backupConfig.mongoDumpPath( )).setTargetPath(backupConfig.backupPath( )).setSourcePath(backupConfig.restorePath( )).build( );
        doRestore(backupStruct);
    }

    private void doRestore(BackupStruct backupStruct) throws RestoreException
    {

        if (!Files.exists(Paths.get(backupStruct.getSourcePath( ))))
        {
            throw new RestoreException("Dest folder doesn't exists ");
        }

        BackupStrategy backupStrategy = BackupFactoryStrategy.create(backupStruct);
        backupStrategy.restoreData( );
    }

    private void doDump(BackupStruct backupStruct) throws BackupException
    {
        BackupStrategy backupStrategy = BackupFactoryStrategy.create(backupStruct);
        backupStrategy.saveData( );
    }


}
