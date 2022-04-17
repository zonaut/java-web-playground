package com.zonaut.braindump.design_patterns.creational.factory_method.database_config;

import java.util.Set;

public enum DatabaseType {

    MONGO,
    NEO4J,
    POSTGRESQL;

    public static final Set<DatabaseType> DATABASE_TYPE_SET = Set.of(DatabaseType.values());

}
