package com.zonaut.braindump.design_patterns.creational.factory_method.database_config;

public class PostgresqlDatabaseConfig implements DatabaseConfig {
    @Override
    public void printDetails() {
        System.out.println("PostgresqlDatabaseConfig");
    }
}
