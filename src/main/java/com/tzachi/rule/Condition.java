package com.tzachi.rule;


import java.util.Map;

@FunctionalInterface
public interface Condition<T> {

    boolean evaluate(Map<String, T> vars);
}
