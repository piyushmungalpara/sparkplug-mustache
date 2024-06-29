package com.webapi.factory;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.logging.Logger;

public class DependencyManager {
    private static Injector injector = Guice.createInjector();
    private static java.util.logging.Logger LOG = Logger.getLogger("");

    private DependencyManager() {
    }

    public static Injector getInjector() {
        if (injector == null) {
            LOG.info("Application hasn't been configured yet.");
            throw new NullPointerException("Injector is Null ; Application hasn't been configured yet.");
        }
        return injector;
    }
}