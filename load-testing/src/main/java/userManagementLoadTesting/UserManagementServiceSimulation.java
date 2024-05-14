
package userManagementLoadTesting;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class UserManagementServiceSimulation extends Simulation {
    /*
       {
   "UserManagementService": {
     "Authentication": {
       "RegisterUser": {
         "Method": "POST",
         "Endpoint": "http://localhost:8082/api/v1/auth/register",
         "Body": {
           "format": "json",
           "data": {
             "username": "omar_elmeteny",
             "email": "omar.elmeteny@gmail.com",
             "password": "omar1234",
             "firstName": "Omar",
             "lastName": "El-Meteny",
             "dateOfBirth": "2001-07-22",
             "phoneNumber": "01060684379"
           }
         }
       },
       "LoginUser": {
         "Method": "POST",
         "Endpoint": "http://localhost:8082/api/v1/auth/login",
         "Body": {
           "format": "json",
           "data": {
             "username": "omar_elmeteny",
             "password": "omar1234"
           }
         }
       },
       "ChangePassword": {
         "Method": "POST",
         "Endpoint": "http://localhost:8082/api/v1/auth/change-password",
         "Authorization": "Bearer Token",
         "Body": {
           "format": "json",
           "data": {
             "oldPassword": "meteny12345",
             "newPassword": "meteny1234"
           }
         }
       },
       "RefreshTokens": {
         "Method": "POST",
         "Endpoint": "http://localhost:8082/api/v1/auth/refresh-token",
         "Body": {
           "format": "json",
           "data": {
             "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl91c2FnZSI6InJlZnJlc2hfdG9rZW4iLCJzdWIiOiJrYXJlZW1fZWxtZXRlbnkiLCJpYXQiOjE3MTUwOTU5NDQsImV4cCI6MTcxNzY4Nzk0NH0.D3cyMsP4X-ndRArQKJz1gphrrcKj853tD4mYSHrQ-R4"
           }
         }
       },
       "ForgotPassword": {
         "Method": "POST",
         "Endpoint": "http://localhost:8082/api/v1/auth/forgot-password",
         "Body": {
           "format": "json",
           "data": {
             "email": "omar.elmeteny@gmail.com"
           }
         }
       },
       "VerifyEmail": {
         "Method": "POST",
         "Endpoint": "http://localhost:8082/api/v1/auth/verify-email",
         "Body": {
           "format": "json",
           "data": {
             "email": "omar.elmeteny@gmail.com",
             "otp": "476375"
           }
         }
       }
     },
     "Admin": {
       "CreateAdmin": {
         "Method": "POST",
         "Endpoint": "http://localhost:8082/api/v1/admin/create-admin",
         "Authorization": "Bearer Token",
         "Body": {
           "format": "json",
           "data": {
             "username": "Admin123",
             "email": "admin12.admin@gmail.com",
             "password": "admin1234",
             "firstName": "Admin1",
             "lastName": "Admin1",
             "dateOfBirth": "2001-07-22",
             "phoneNumber": "01160684379"
           }
         }
       },
       "DeleteAdmin": {
         "Method": "DELETE",
         "Endpoint": "http://localhost:8082/api/v1/admin/3",
         "Authorization": "Bearer Token"
       },
       "ResetPassword": {
         "Method": "POST",
         "Endpoint": "http://localhost:8082/api/v1/admin/reset-password/3",
         "Authorization": "Bearer Token",
         "Body": {
           "format": "json",
           "data": {
             "password": "meteny1234"
           }
         }
       },
       "LockAccount": {
         "Method": "POST",
         "Endpoint": "http://localhost:8082/api/v1/admin/lock-account/4",
         "Authorization": "Bearer Token",
         "Body": {
           "format": "json",
           "data": {
             "reason": "Entered wrong password more than 3 times",
             "lockoutTime": "2024-05-07T12:30:45Z"
           }
         }
       },
       "UnlockAccount": {
         "Method": "POST",
         "Endpoint": "http://localhost:8082/api/v1/admin/unlock-account/3",
         "Authorization": "Bearer Token"
       },
       "SearchUsers": {
         "Method": "GET",
         "Endpoint": "http://localhost:8082/api/v1/admin/search-users",
         "Authorization": "Bearer Token"
       },
       "UserStatus": {
         "Method": "GET",
         "Endpoint": "http://localhost:8082/api/v1/admin/user-status/2",
         "Authorization": "Bearer Token"
       }
     },
     "UpdateDelete": {
       "UpdateUser": {
         "Method": "POST",
         "Endpoint": "http://localhost:8082/api/v1/user-profiles/update"
       },
       "DeleteUser": {
         "Method": "DELETE",
         "Endpoint": "http://localhost:8082/api/v1/user-profiles/delete"
       }
     }
   },
   "EnrollmentService": {}
 }
     */
    private static final FeederBuilder.FileBased<Object> jsonFeeder = jsonFile("gatling-charts-highcharts-bundle-3.11.2/src/test/resources/users.json").circular();

    private static final int USER_COUNT = Integer.parseInt(System.getProperty("USERS", "5"));
    private static final int RAMP_DURATION = Integer.parseInt(System.getProperty("RAMP_DURATION", "10"));

    @Override
    public void before() {
        System.out.printf("Running test with %d users%n", USER_COUNT);
        System.out.printf("Ramping users over %d seconds%n", RAMP_DURATION);
    }

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8082/api/v1")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    private static final ChainBuilder registerUser =
            feed(jsonFeeder)
                    .exec(http("Register User")
                            .post("/auth/register")
                            .body(StringBody(
                                    "{\"username\": \"${username}\", \"email\": \"${email}\", \"password\": \"${password}\", \"firstName\": \"${firstName}\", \"lastName\": \"${lastName}\", \"dateOfBirth\": \"${dateOfBirth}\", \"phoneNumber\": \"${phoneNumber}\"}"
                            ))
                    );

    // login test chain the user should be registered first
    private static final ChainBuilder loginUser =
            feed(jsonFeeder)
                    .exec(http("Login User")
                            .post("/auth/login")
                            .body(StringBody(
                                    "{\"username\": \"${username}\", \"password\": \"${password}\"}"
                            ))
                            .check(jsonPath("$.accessToken").saveAs("accessToken"))
                    );

    private static final ChainBuilder changePassword =
            feed(jsonFeeder)
                    .exec(http("Change Password")
                            .post("/auth/change-password")
                            .header("Authorization", "Bearer ${accessToken}")
                            .body(StringBody(
                                    "{\"oldPassword\": \"${password}\", \"newPassword\": \"newPassword\"}"
                            ))
                    );

    private static final ChainBuilder refreshTokens =
            feed(jsonFeeder)
                    .exec(http("Refresh Tokens")
                            .post("/auth/refresh-token")
                            .header("Authorization", "Bearer ${accessToken}")
                            .body(StringBody(
                                    "{\"refreshToken\": \"${refreshToken}\"}"
                            ))
                    );

    private static final ScenarioBuilder scn = scenario("User Management Service")
            .exec(registerUser)
            .pause(2)
            .exec(loginUser)
            .pause(2)
            .exec(changePassword)
            .pause(2)
            .exec(refreshTokens);

    {
        setUp(
                scn.injectOpen(
                        nothingFor(5),
                        rampUsers(USER_COUNT).during(RAMP_DURATION)
                )).protocols(httpProtocol);
    }
}