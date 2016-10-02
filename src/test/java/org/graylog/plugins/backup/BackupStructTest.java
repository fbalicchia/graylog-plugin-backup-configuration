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
package org.graylog.plugins.backup;


import org.graylog.plugins.backup.model.BackupStruct;
import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

/**
 * Created by fbalicchia on 08/09/16.
 */
public class BackupStructTest
{

    @Test
    public void builderStrunctTest()
    {
        BackupStruct backupStruct = new BackupStruct.BackupStructBuilder()
            .setMongoInstallPath("aaaa")
            .setHostName("bbb")
            .setTargetPath("cccc")
            .setPort("1234")
            .setSourcePath("zzzss").build();

        assertThat("aaaa").isEqualTo(backupStruct.getMongoInstallPath());
        assertThat("bbb").isEqualTo(backupStruct.getHostName());
        assertThat("cccc").isEqualTo(backupStruct.getTargetPath());
        assertThat("1234").isEqualTo(backupStruct.getPort());
        assertThat("zzzss").isEqualTo(backupStruct.getSourcePath());
    }




    @Test
    public void builderStructTestTrim()
    {
        BackupStruct backupStruct = new BackupStruct.BackupStructBuilder()
            .setMongoInstallPath("   aaaa")
            .setHostName("bbb       ")
            .setTargetPath(" cccc   ")
            .setPort(" 1234   ")
            .setSourcePath("  zzzss  ").build();

        assertThat("aaaa").isEqualTo(backupStruct.getMongoInstallPath());
        assertThat("bbb").isEqualTo(backupStruct.getHostName());
        assertThat("cccc").isEqualTo(backupStruct.getTargetPath());
        assertThat("1234").isEqualTo(backupStruct.getPort());
        assertThat("zzzss").isEqualTo(backupStruct.getSourcePath());
    }


}
