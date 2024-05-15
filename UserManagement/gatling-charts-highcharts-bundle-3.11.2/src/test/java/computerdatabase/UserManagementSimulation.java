package computerdatabase;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

/**
 * This sample is based on our official tutorials:
 * <ul>
 *   <li><a href="https://gatling.io/docs/gatling/tutorials/quickstart">Gatling quickstart tutorial</a>
 *   <li><a href="https://gatling.io/docs/gatling/tutorials/advanced">Gatling advanced tutorial</a>
 * </ul>
 */
public class UserManagementSimulation extends Simulation {

    private static final String otpTestEmail = "2837685131843#@$5";
    private static Connection connection;

    public static void setUpDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/user_management", "postgres", "postgres");
        System.out.println("Connected to the PostgreSQL server successfully.");
    }

    public static void closeDatabase() throws SQLException {
        connection.close();
        System.out.println("Connection closed.");
    }


    public static void clearDatabase() throws SQLException {
        /*delete from one_time_passwords;
        delete from user_roles;
        delete from login_history;
        delete from users;*/
        connection.createStatement().execute("delete from one_time_passwords");
        connection.createStatement().execute("delete from user_roles");
        connection.createStatement().execute("delete from login_history");
        connection.createStatement().execute("delete from users");
        System.out.println("Database cleared.");
    }

    public static void dropUser(String username) throws SQLException {
        connection.createStatement().execute("delete from one_time_passwords where user_id = (select user_id from users where username = '" + username + "')");
        connection.createStatement().execute("delete from user_roles where user_id = (select user_id from users where username = '" + username + "')");
        connection.createStatement().execute("delete from login_history where user_id = (select user_id from users where username = '" + username + "')");
        connection.createStatement().execute("delete from users where username = '" + username + "'");
        System.out.println("User " + username + " deleted.");
    }

    private static final ChainBuilder registerUser =
                 exec(session -> {
                     int userId = (int)(Math.random() * 10000);
                        Session newSession = session.set("username", "user" + userId)
                                .set("email", "user" + userId + "@guctechie.com")
                                .set("password", "password")
                                .set("firstName", "User")
                                .set("lastName", "User")
                                .set("dateOfBirth", "1990-01-01")
                                .set("phoneNumber", "01000000000"+userId);
                        return newSession;
                 })
                    .exec(http("Register User  #{username} #{email}")
                            .post("/auth/register")
                            .body(StringBody(
                                    "{\"username\": \"#{username}\", \"email\": \"#{email}\", \"password\": \"#{password}\", \"firstName\": \"#{firstName}\", \"lastName\": \"#{lastName}\", \"dateOfBirth\": \"#{dateOfBirth}\", \"phoneNumber\": \"#{phoneNumber}\"}"
                            ))
                            .check(status().is(200))
                    );

    private static final ChainBuilder verifyUser =
            exec(http("Verify User Email #{email}")
                    .post("/auth/verify-email")
                    .body(
                            StringBody(
                                    "{\"email\": \"#{email}\", \"otp\":\"" + otpTestEmail + "\"}"
                            )
                    ).check(status().is(200))
            );

    // login test chain the user should be registered first
    private static final ChainBuilder loginUser =
            exec(http("Login User #{username} #{password}")
                    .post("/auth/login")
                    .header("User-Agent", "Gatling")
                    .body(StringBody(
                            "{\"username\": \"#{username}\", \"password\": \"#{password}\"}"
                    )).check(status().is(200))
                    .check(jsonPath("$.accessToken").saveAs("accessToken"))
                    .check(jsonPath("$.refreshToken").saveAs("refreshToken"))
            );

    private static final ChainBuilder changePassword =
                    exec(http("Change Password #{username} #{password} #{accessToken}")
                            .post("/auth/change-password")
                            .header("Authorization", "Bearer #{accessToken}")
                            .body(StringBody(
                                    "{\"oldPassword\": \"#{password}\", \"newPassword\": \"newPassword\"}"
                            ))
                    );

    private static final ChainBuilder loginAfterChangePassword =
                    exec(http("Login User After Change Password #{username} new password")
                            .post("/auth/login")
                            .header("User-Agent", "Gatling")
                            .body(StringBody(
                                    "{\"username\": \"#{username}\", \"password\": \"newPassword\"}"
                            )).check(status().is(200))
                            .check(jmesPath("accessToken").saveAs("accessToken"))
                            .check(jmesPath("refreshToken").saveAs("refreshToken"))
                    );

    private static final ChainBuilder refreshTokens =
                    exec(http("Refresh Tokens #{username} #{password}")
                            .post("/auth/refresh-token")
                            .body(StringBody(
                                    "{\"refreshToken\": \"#{refreshToken}\"}"
                            ))
                            .check(status().is(200))
                    );

    private static final ChainBuilder forgotPassword =
                    exec(http("Forgot Password #{email}")
                            .post("/auth/forgot-password")
                            .body(StringBody(
                                    "{\"email\": \"#{email}\"}"
                            ))
                            .check(status().is(200))
                    );

    private static final ScenarioBuilder scn = scenario("User Management Service Authentication Scenario")
            .exec(registerUser)
            .pause(2)
            .exec(verifyUser)
            .pause(2)
            .exec(loginUser)
            .pause(2)
            .exec(changePassword)
            .pause(2)
            .exec(loginAfterChangePassword)
            .pause(2)
            .exec(refreshTokens)
            .pause(2)
            .exec(forgotPassword);

    HttpProtocolBuilder httpProtocol =
            http.baseUrl("http://localhost:8082/api/v1")
                    .acceptHeader("application/json")
                    .contentTypeHeader("application/json");

    {
        try {
            setUpDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setUp(
                scn.injectOpen(rampUsers(1).during(1))
        ).protocols(httpProtocol);

    }

    @Override
    public void after() {
        try {
            clearDatabase();
            closeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
