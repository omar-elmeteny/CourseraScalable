package com.guctechie.eureka;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.guctechie.classes.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class EurekaParser {

    public ArrayList<Service> parse(String response){
        ArrayList<Service> services = new ArrayList<>();
        Gson gson = new Gson();
        HashMap<String, Object> map = gson.fromJson(response, HashMap.class);
        LinkedTreeMap<String, Object> applications = (LinkedTreeMap<String, Object>) map.get("applications");
        ArrayList<LinkedTreeMap<String, Object>> application = (ArrayList<LinkedTreeMap<String, Object>>) applications.get("application");
        for (LinkedTreeMap<String, Object> app : application) {
            String appName = (String) app.get("name");
            ArrayList<LinkedTreeMap<String, Object>> instances = (ArrayList<LinkedTreeMap<String, Object>>) app.get("instance");
            for (LinkedTreeMap<String, Object> instance : instances) {
                String instanceId = (String) instance.get("instanceId");
                String ip = (String) instance.get("ipAddr");
                LinkedTreeMap<String, Object> portMap = (LinkedTreeMap<String, Object>) instance.get("port");
                Double portD = (Double) portMap.get("$");
                String port = String.valueOf(portD.intValue());
                Service service = new Service(appName, instanceId, ip, port);
                services.add(service);
            }
        }
        return services;
    }

//    public static void main(String[] args) {
//        EurekaParser eurekaParser = new EurekaParser();
//        try {
//            System.out.println(eurekaParser.parse(new EurekaClient().getAllInstances()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
