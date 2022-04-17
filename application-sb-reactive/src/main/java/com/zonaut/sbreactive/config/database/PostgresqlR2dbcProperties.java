package com.zonaut.sbreactive.config.database;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "postgres")
public class PostgresqlR2dbcProperties {

    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

}
