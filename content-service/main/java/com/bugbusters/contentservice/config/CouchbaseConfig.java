package com.bugbusters.contentservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.mapping.event.ValidatingCouchbaseEventListener;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.couchbase.client.java.query.QueryScanConsistency;

@Configuration
@EnableCouchbaseRepositories(basePackages = {
        "com.baeldung.spring.data.couchbase" })
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    @Value("${couchbase.nodes}")
    private String nodeList;

    @Value("${couchbase.bucket.name}")
    private String bucketName;

    @Value("${couchbase.bucket.username}")
    private String bucketUsername;

    @Value("${couchbase.bucket.password}")
    private String bucketPassword;

    @Override
    public String getConnectionString() {
        return nodeList;
    }

    @Override
    public String getUserName() {
        return bucketUsername;
    }

    @Override
    public String getPassword() {
        return bucketPassword;
    }

    @Override
    public String getBucketName() {
        return bucketName;
    }

    @Override
    public QueryScanConsistency getDefaultConsistency() {
        return QueryScanConsistency.REQUEST_PLUS;
    }

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public ValidatingCouchbaseEventListener validatingCouchbaseEventListener() {
        return new ValidatingCouchbaseEventListener(localValidatorFactoryBean());
    }
}