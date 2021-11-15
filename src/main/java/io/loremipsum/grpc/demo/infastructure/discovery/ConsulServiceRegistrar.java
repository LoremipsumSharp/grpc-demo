package io.loremipsum.grpc.demo.infastructure.discovery;


import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;

import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.ImmutableRegistration;

import io.loremipsum.grpc.demo.configuration.ConsulProperties;

import io.loremipsum.grpc.demo.configuration.ConsulRegistrationProperties;
import lombok.extern.slf4j.Slf4j;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;

import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;



@Slf4j
@Component
public class ConsulServiceRegistrar implements SmartApplicationListener {

    @Autowired
    ConsulProperties consulProperties;

    @Autowired
    Consul consulClient;


    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return ApplicationReadyEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        AgentClient agentClient = consulClient.agentClient();
        ConsulRegistrationProperties consulRegistration = consulProperties.getRegistration();

        final ImmutableRegCheck check = ImmutableRegCheck.builder().tcp(consulRegistration.getAddress() + ":" + Integer.valueOf(consulRegistration.getPort()).toString())
                .timeout("3s")
                .status("passing")
                .interval("10s")
                .deregisterCriticalServiceAfter("30s").build();

        final ImmutableRegistration registration = ImmutableRegistration.builder()
                .id(consulRegistration.getInstanceId())
                .name(consulRegistration.getName())
                .address(consulRegistration.getAddress())
                .port(consulRegistration.getPort())
                .check(check)
                .build();
        agentClient.register(registration);


        Runtime.getRuntime().addShutdownHook(new Thread(() -> agentClient.deregister(consulRegistration.getInstanceId())));




    }
}
