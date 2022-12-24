package com.tzachi.rule;

import java.util.Map;

public interface Rule <T> {

    /**
     * Evaluate the condition of the rule
     * @return boolean is successfully evaluate condition
     */
    boolean evaluate (Map<String, T> vars);

    /**
     * Apply the Action in case all conditions true
     */
    void apply(Map<String, T> vars);

    String getName();
}
