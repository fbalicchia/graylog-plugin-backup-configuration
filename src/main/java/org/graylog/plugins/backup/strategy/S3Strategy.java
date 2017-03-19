package org.graylog.plugins.backup.strategy;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.apache.commons.io.FileUtils;
import org.graylog.plugins.backup.BackupException;
import org.graylog.plugins.backup.RestoreException;
import org.graylog.plugins.backup.model.BackupStruct;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;
import java.time.LocalDateTime;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import com.amazonaws.services.s3.model.PutObjectRequest;

import com.amazonaws.auth.BasicAWSCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.zip.ZipUtil;


/**
 * Created by fbalicchia on 19/03/17.
 */
public class S3Strategy extends AbstractMongoBackupStrategy {


    private AmazonS3 s3Client;

    private Logger LOG = LoggerFactory.getLogger(S3Strategy.class.getName());


    public S3Strategy(BackupStruct backupStruct) {
        this.backupStruct = backupStruct;
    }


    private void initClient() {
        AmazonS3 s3Client = new AmazonS3Client(new BasicAWSCredentials("accessKey", "Secrectkey"));
        Region euWest2 = Region.getRegion(Regions.EU_WEST_1);
        s3Client.setRegion(euWest2);

    }


    @Override
    public void saveData() throws BackupException {
        s3Client.putObject(new PutObjectRequest("numeeor12 ", "key", new File("")));
    }

    @Override
    public void restoreData() throws RestoreException {

        ObjectListing objectListing = s3Client.listObjects(new ListObjectsRequest()
                .withBucketName("bucketname")
                .withPrefix("My"));

        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            LOG.info(" - " + objectSummary.getKey() + "  " +
                    "(size = " + objectSummary.getSize() + ")");
        }


    }

    @Override
    @Deprecated
    protected void pack() throws Exception {

        LocalDateTime now = LocalDateTime.now( );
        //String dateFormat = now.format(formatter);
       // String srcFolder = backupStruct.getTargetPath( ) + File.separator + GRAYLOG_SCHEMA_NAME;
        // String dstFolder = backupStruct.getTargetPath( ) + File.separator + GRAYLOG_SCHEMA_NAME + dateFormat + ".zip";
        //ZipUtil.pack(new File(srcFolder), new File(dstFolder));
        LOG.info("Graylog config backup completed");
        //FileUtils.deleteDirectory(new File(srcFolder));

    }
}
