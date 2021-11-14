package io.loremipsum.grpc.demo.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "consul")
public class ConsulProperties {
    private String host = "localhost";
    private int port = 8500;
    private ConsulRegistrationProperties registration;



    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public ConsulRegistrationProperties getRegistration() {
        return registration;
    }

    public void setRegistration(ConsulRegistrationProperties registration) {
        this.registration = registration;
    }
}
