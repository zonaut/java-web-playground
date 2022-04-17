package com.zonaut.braindump.design_patterns.creational;

import com.zonaut.braindump.design_patterns.creational.factory_method.FactoryMethod;

public class CreationalDesignPatterns {

    public CreationalDesignPatterns() {

        System.out.println("# Creational design patterns");

        FactoryMethod factoryMethod = new FactoryMethod();
        factoryMethod.databaseConfig();

        System.out.println("-".repeat(50));

    }
}
