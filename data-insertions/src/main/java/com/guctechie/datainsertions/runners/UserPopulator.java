package com.guctechie.datainsertions.runners;

import com.guctechie.datainsertions.configs.AppConfig;
import com.guctechie.datainsertions.models.User;
import com.guctechie.datainsertions.services.UserGenerator;
import com.guctechie.datainsertions.services.UserInserter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class UserPopulator implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(UserPopulator.class);

    private final UserInserter userInserter;
    private final UserGenerator userGenerator;
    private final AppConfig appConfig;

    public UserPopulator(UserInserter userInserter, UserGenerator userGenerator, AppConfig appConfig) {
        this.userInserter = userInserter;
        this.userGenerator = userGenerator;
        this.appConfig = appConfig;
    }

    @Override
    public void run(String... args) throws Exception {

        for (int i = 0; i < appConfig.getUserCount(); i++) {
            User user = userGenerator.generateUser();

            int id = userInserter.insertUser(user);
            user.setUserId(id);
            //logger.info("User {} inserted with id {}", user.getUsername(), user.getUserId());
        }

    }
}
