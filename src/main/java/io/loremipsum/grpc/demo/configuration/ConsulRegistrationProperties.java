package io.loremipsum.grpc.demo.configuration;

import io.loremipsum.grpc.demo.util.NetworkUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.SocketException;
import java.net.UnknownHostException;

public class ConsulRegistrationProperties {
    private String address;
    private String name;
    private int port;
    private String instanceId;


    public String getAddress()  {
        if (StringUtils.isBlank(this.address) || StringUtils.isEmpty(this.address)) {
            try {
                this.address = NetworkUtils.getLocalIp();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return this.address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getInstanceId() {
        if (StringUtils.isBlank(this.instanceId) || StringUtils.isEmpty(this.instanceId))
            return StringUtils.join(new String[]{this.name, this.address, Integer.valueOf(this.port).toString()}, ".");
        return this.instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
