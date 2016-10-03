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
package org.graylog.plugins.backup.service;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

import org.graylog.plugins.backup.BackupConfiguration;
import org.graylog.plugins.backup.RestoreException;
import org.graylog2.plugin.cluster.ClusterConfigService;
import org.graylog2.plugin.rest.PluginRestResource;
import org.graylog2.shared.rest.resources.RestResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@RequiresAuthentication
@Api(value = "backupConfig", description = "Graylog backup config api")
@Path("/")
public class BackupResource extends RestResource implements PluginRestResource
{

    private Logger LOG = LoggerFactory.getLogger(BackupResource.class);

    private BackupService backupService;

    private ClusterConfigService clusterConfigService;

    @Inject
    public BackupResource(BackupService backupService, ClusterConfigService clusterConfigService)
    {
        this.backupService = backupService;
        this.clusterConfigService = clusterConfigService;
    }


    @POST
    @Path("launchrestore")
    @Timed
    @ApiOperation(value = "Restore config data")
    @ApiResponses(value = {
        @ApiResponse(code = 202, message = "Restore config done")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response launchRestore()
    {
        try
        {
            LOG.info("Graylog restore perform");
            BackupConfiguration aDefault = clusterConfigService.getOrDefault(BackupConfiguration.class, BackupConfiguration.defaultConfig( ));

            LOG.info("Restore configuration {}",aDefault.toString());

            backupService.restore(aDefault);
            return Response.accepted( ).build( );
        }
        catch (RestoreException e)
        {
            LOG.error("", e);
            return Response.serverError( ).build( );
        }
    }

}
