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

import org.apache.commons.lang.SystemUtils;
import org.graylog.plugins.backup.model.BackupStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public abstract class AbstractMongoBackupStrategy implements BackupStrategy
{


    private static final Logger LOG = LoggerFactory.getLogger(AbstractMongoBackupStrategy.class);

    protected BackupStruct backupStruct;

    protected abstract void pack() throws Exception;


    protected String osShellPath()
    {

        if (SystemUtils.IS_OS_UNIX)
        {
            return "/bin/sh";
        }

        throw new IllegalArgumentException("At the moment your system is not supported");
    }


    protected synchronized void processCommand(List<String> command) throws Exception
    {
        //need to find a a smart way to manage processBuilder

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process p = pb.start( );

        Timer timeOut = new Timer( );
        timeOut.schedule(new TimerTask( )
        {
            @Override
            public void run()
            {
                if(p.isAlive())
                {
                    LOG.warn("restore process take more than 15 sec I'll destroy it");
                    p.destroy( );
                }
            }

            //this parameter need to be configurable

        }, 15000);

        p.waitFor( );
        timeOut.cancel( );

    }
}



