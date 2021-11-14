package io.loremipsum.grpc.demo.infastructure;

import java.net.URI;
import java.util.Optional;

import com.google.common.net.HostAndPort;


import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;

public class ConsulNameResolverProvider extends NameResolverProvider {
    public static final String SCHEME = "consul";
    private final Optional<HostAndPort> maybeConsulHostAndPort;
    /**
     * Creates a new instance.
     */
    public ConsulNameResolverProvider() {
        this.maybeConsulHostAndPort = Optional.empty();
    }

    @Override
    public NameResolver newNameResolver(URI uri, NameResolver.Args args) {
        return new ConsulNameResolver(uri, this.maybeConsulHostAndPort);
    }

    /**
     * Creates a new instance.
     *
     * @param consulHostAndPort Consul host and port combo, if the defaults shall not be used
     */
    public ConsulNameResolverProvider(HostAndPort consulHostAndPort) {
        this.maybeConsulHostAndPort = Optional.of(consulHostAndPort);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isAvailable() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int priority() {
        return 5;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultScheme() {
        return SCHEME;
    }
}
