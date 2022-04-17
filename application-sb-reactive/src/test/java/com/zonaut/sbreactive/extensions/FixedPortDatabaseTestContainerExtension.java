package com.zonaut.sbreactive.extensions;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.atomic.AtomicBoolean;

public class FixedPortDatabaseTestContainerExtension implements BeforeAllCallback {

    private static final AtomicBoolean started = new AtomicBoolean(false);

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        if (!started.get()) {

            int containerPort = 5432;
            int localPort = 5434;
            DockerImageName postgres = DockerImageName.parse("postgres:13.2");
            GenericContainer<?> postgreDBContainer = new PostgreSQLContainer<>(postgres)
                .withDatabaseName("sb-reactive")
                .withUsername("postgres")
                .withPassword("password")
                .withReuse(true)
                .withExposedPorts(containerPort)
                .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(localPort), new ExposedPort(containerPort)))
                ));

            postgreDBContainer.start();


            started.set(true);
        }

    }
}
