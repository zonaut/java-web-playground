package com.zonaut.braindump.design_patterns.creational.factory_method;

import com.zonaut.braindump.design_patterns.creational.factory_method.database_config.DatabaseConfig;
import com.zonaut.braindump.design_patterns.creational.factory_method.database_config.DatabaseFactory;
import com.zonaut.braindump.design_patterns.creational.factory_method.database_config.DatabaseType;

public class FactoryMethod {

    public FactoryMethod() {
        System.out.println("- Factory method");
    }

    public void databaseConfig() {

        System.out.println("-- Database config");

        System.out.println();
        DatabaseFactory databaseFactory = new DatabaseFactory();
        DatabaseConfig databaseConfig = databaseFactory.getDatabaseConfig(DatabaseType.POSTGRESQL);
        databaseConfig.printDetails();

    }

}
