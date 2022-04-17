package com.zonaut.braindump.design_patterns.creational.factory_method.database_config;

public class DatabaseFactory {

    public DatabaseConfig getDatabaseConfig(DatabaseType type) {
        return switch (type) {
            case MONGO -> new MongoDatabaseConfig();
            case NEO4J -> throw new IllegalStateException(type + " not yet implemented!");
            case POSTGRESQL -> new PostgresqlDatabaseConfig();
        };
    }

}
