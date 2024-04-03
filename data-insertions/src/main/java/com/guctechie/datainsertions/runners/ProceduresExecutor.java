package com.guctechie.datainsertions.runners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Order(3)
public class ProceduresExecutor implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(ProceduresExecutor.class);

    private final JdbcTemplate jdbcTemplate;

    public ProceduresExecutor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Executing procedures");
        try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream("procedures.sql")) {
            if (inputStream != null) {
                String sql = new String(inputStream.readAllBytes());
                jdbcTemplate.execute(sql);
            }
            logger.info("Procedures executed");
        } catch (Exception e) {
            logger.error("Error executing procedures: " + e.getMessage());
            throw e;
        }
    }
}
