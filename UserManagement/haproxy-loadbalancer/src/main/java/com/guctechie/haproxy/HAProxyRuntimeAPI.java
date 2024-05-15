package com.guctechie.haproxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HAProxyRuntimeAPI {
    private final String host;
    private final int port;

    public HAProxyRuntimeAPI(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private String executeCommand(String command) throws IOException {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out.println(command);
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line).append("\n");
            }
            return response.toString();
        }
    }

    public String addServer(String backendName, String serviceId, String address, int port) throws IOException {
        String command = "add server " + backendName + "/" + serviceId + " " + " " + "enabled " + " addr " + address + ":" + port + " check maxconn 1000";
        return executeCommand(command);
    }

    public String delServer(String backendName, String serviceId) throws IOException {
        String command = "del server " + backendName + "/" + serviceId;
        return executeCommand(command);
    }

    public String disableServer(String backendName, String serviceId) throws IOException {
        String command = "disable server " + backendName + "/" + serviceId;
        return executeCommand(command);
    }

    public String enableServer(String backendName, String serviceId) throws IOException {
        String command = "enable server " + backendName + "/" + serviceId;
        return executeCommand(command);
    }

    public String showStats() throws IOException {
        return executeCommand("show stat");
    }

    public String executeCustomCommand(String command) throws IOException {
        return executeCommand(command);
    }

    public String enableHealth(String backendName, String serviceId) throws IOException {
        String command = "enable health " + backendName + "/" + serviceId;
        return executeCommand(command);
    }

//    public static void main(String[] args) throws IOException {
//        HAProxyRuntimeAPI api = new HAProxyRuntimeAPI("localhost", 9999);
////        System.out.println(api`.showStats());
////        System.out.println(api.addServer("eureka_backend", "server2", "172.21.0.1", 8082));
////        System.out.println(api.showStats());
////        System.out.println(api.delServer("eureka_backend", "server2"));
////        System.out.println(api.showStats());
////        System.out.println(api.enableHealth("eureka_backend", "server2"));
////        System.out.println(api.enableServer("eureka_backend", "server2"));
//    }
}