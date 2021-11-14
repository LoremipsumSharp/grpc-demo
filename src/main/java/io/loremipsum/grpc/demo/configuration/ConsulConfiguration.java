package io.loremipsum.grpc.demo.configuration;


import com.google.common.net.HostAndPort;
import com.orbitz.consul.Consul;
import io.loremipsum.grpc.demo.infastructure.ConsulNameResolverProvider;
import io.loremipsum.grpc.demo.util.AddressUtil;
import net.devh.boot.grpc.server.config.GrpcServerProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.SocketException;
import java.net.UnknownHostException;

@Configuration
@EnableConfigurationProperties(value = ConsulProperties.class)
public class ConsulConfiguration {

    @Autowired
    private ConsulProperties consulProperties;

    @Autowired
    private GrpcServerProperties grpcServerProperties;


    @PostConstruct
    public void init() {
        this.consulProperties.getRegistration().setPort(grpcServerProperties.getPort());
    }


    @Bean
    public ConsulNameResolverProvider consulNameResolverProvider() {
        return new ConsulNameResolverProvider(HostAndPort.fromParts(consulProperties.getHost(), consulProperties.getPort()));
    }

    @Bean
    public Consul consulClient() {
        Consul.Builder clientBuilder = Consul.builder();
        clientBuilder.withHostAndPort(HostAndPort.fromParts(consulProperties.getHost(), consulProperties.getPort()));
        return clientBuilder.build();
    }



}


