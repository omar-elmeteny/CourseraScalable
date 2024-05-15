package com.guctechie.datainsertions.runners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Order(1)
public class TableCreator implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(TableCreator.class);

    private final JdbcTemplate jdbcTemplate;

    public TableCreator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        // load schema.sql from resources
        logger.info("Creating tables");
        try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream("schema.sql")) {
            if (inputStream != null) {
                String sql = new String(inputStream.readAllBytes());
                jdbcTemplate.execute(sql);
            }
            logger.info("Tables created");
        } catch (Exception e) {
            logger.error("Error creating tables: " + e.getMessage());
            throw e;
        }
    }

}
