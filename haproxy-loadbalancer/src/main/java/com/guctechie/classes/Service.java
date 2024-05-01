package com.guctechie.classes;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class Service {
    private String serviceName;
    private String serviceId;
    private String address;
    private String port;

    @Override
    public String toString() {
        return "Service{" + "serviceName='" + serviceName + '\'' + ", serviceId='" + serviceId + '\'' + ", address='" + address + '\'' + ", port='" + port + '\'' + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Service service = (Service) obj;
        return serviceName.equals(service.serviceName) && serviceId.equals(service.serviceId) && address.equals(service.address) && port.equals(service.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, serviceId, address, port);
    }

}
