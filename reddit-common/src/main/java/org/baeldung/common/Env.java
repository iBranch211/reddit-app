package org.baeldung.common;

import java.util.List;

import com.google.common.collect.Lists;

public enum Env {

    LOCAL("local"), STAGING("staging");

    private String name;

    private Env(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static List<String> envs() {
        return Lists.newArrayList(LOCAL.getName());
    }

}
