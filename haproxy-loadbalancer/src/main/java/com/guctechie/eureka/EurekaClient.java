package com.guctechie.eureka;


import com.guctechie.classes.ProjectProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EurekaClient {

    public String getAllInstances() throws IOException {
        String eurekaUrl = ProjectProperties.getProperty("eureka.server.host");
        URL url = new URL(eurekaUrl + "/apps");
        HttpURLConnection connection  = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        if(connection.getResponseCode() != 200){
            throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
        }
        BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String output;
        StringBuilder response = new StringBuilder();
        while ((output = bf.readLine()) != null) {
            response.append(output);
        }
        connection.disconnect();

        return response.toString();
    }

    public static void main(String[] args) {
        EurekaClient eurekaClient = new EurekaClient();
        try {
            System.out.println(eurekaClient.getAllInstances());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
