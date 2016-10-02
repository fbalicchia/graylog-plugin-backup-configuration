/**
 * Copyright (C) ${project.inceptionYear} ${owner} (hello@graylog.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.graylog.plugins.backup.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;



public class BackupStruct
{

    private String hostName;

    private String port;

    private String targetPath;

    private String sourcePath;

    private String mongoInstallPath;


    public String getHostName()
    {
        return StringUtils.trim(hostName);
    }

    public String getPort()
    {
        return StringUtils.trim(port);
    }

    public String getTargetPath()
    {
        return StringUtils.trim(targetPath);
    }

    public String getSourcePath()
    {
        return StringUtils.trim(sourcePath);
    }

    public String getMongoInstallPath()
    {
        return StringUtils.trim(mongoInstallPath);
    }

    public BackupStruct(BackupStructBuilder backupStructBuilder)
    {
        hostName = backupStructBuilder._hostName;
        port = backupStructBuilder._port;
        targetPath = backupStructBuilder._targetPath;
        sourcePath = backupStructBuilder._sourcePath;
        mongoInstallPath = backupStructBuilder._mongoInstallPath;
    }

    public static class BackupStructBuilder
    {
        private String _hostName;

        private String _port = "27017";

        private String _targetPath;

        private String _sourcePath;

        private String _mongoInstallPath;


        public BackupStructBuilder setHostName(String _hostName)
        {
            this._hostName = _hostName;
            return this;
        }

        public BackupStructBuilder setPort(String _port)
        {
            this._port = _port;
            return this;
        }

        public BackupStructBuilder setTargetPath(String _targetPath)
        {
            this._targetPath = _targetPath;
            return this;
        }

        public BackupStructBuilder setSourcePath(String _sourcePath)
        {
            this._sourcePath = _sourcePath;
            return this;
        }

        public BackupStructBuilder setMongoInstallPath(String mongoInstallPath)
        {
            this._mongoInstallPath = mongoInstallPath;
            return this;
        }

        public BackupStruct build()
        {
            return new BackupStruct(this);
        }
    }


    @Override
    public String toString()
    {
        return new ToStringBuilder(this)
            .append(hostName)
            .append(port)
            .append(targetPath)
            .append(sourcePath)
            .append(mongoInstallPath).toString();
    }
}
