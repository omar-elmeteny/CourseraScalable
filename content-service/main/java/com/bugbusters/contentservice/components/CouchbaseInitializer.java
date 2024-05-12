package com.bugbusters.contentservice.components;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.manager.bucket.BucketManager;
import com.couchbase.client.java.manager.bucket.BucketSettings;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Slf4j
public class CouchbaseInitializer implements ApplicationRunner {

    @Autowired
    private CouchbaseTemplate couchbaseTemplate;

    @Value("${couchbase.cluster.connectionString}")
    private String clusterConnectionString;

    @Value("${couchbase.cluster.username}")
    private String clusterUsername;

    @Value("${couchbase.cluster.password}")
    private String clusterPassword;

    @Value("${couchbase.bucket.name}")
    private String bucketName;

    @Value("${couchbase.bucket.username}")
    private String bucketUsername;

    @Value("${couchbase.bucket.password}")
    private String bucketPassword;

    private Cluster cluster;

    public void setup() {
        try {
            cluster = Cluster.connect(clusterConnectionString, clusterUsername, clusterPassword);
            BucketManager bucketManager = cluster.buckets();
            BucketSettings bucketSettings = bucketManager.getAllBuckets().get(bucketName);
            if (bucketSettings == null) {
                bucketSettings = BucketSettings.create(bucketName)
                        .bucketType(com.couchbase.client.java.manager.bucket.BucketType.COUCHBASE)
                        .flushEnabled(true);
                bucketManager.createBucket(bucketSettings);
            }
        } catch (Exception e) {
            log.error("Error setting up Couchbase", e);
        }
    }

    private void createPrimaryIndexIfNotExists() {
        try {
            couchbaseTemplate.getCouchbaseClientFactory().getCluster().query("SELECT 1 FROM `" + bucketName + "` LIMIT 1");
        } catch (Exception e) {
            couchbaseTemplate.getCouchbaseClientFactory().getCluster().query("CREATE PRIMARY INDEX ON `" + bucketName + "`" + " USING GSI");
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            setup();
            createPrimaryIndexIfNotExists();
        } finally {
            if (cluster != null) {
                cluster.disconnect();
            }
        }
    }

}


