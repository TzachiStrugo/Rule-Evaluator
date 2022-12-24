package com.tzachi.rule;

import java.util.Map;

@FunctionalInterface
public interface Action <T> {

    void apply (Map<String, T> vars);
}
