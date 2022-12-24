package com.tzachi.expression;

import com.tzachi.rule.Condition;
import java.io.Serializable;
import java.util.Map;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.mvel2.MVEL;

@Value
@Slf4j
public class MvelCondition<T> implements Condition <T> {

    Serializable predicate;
    String expression;

    public MvelCondition(String expression) {
        this.expression = expression;
        predicate = MVEL.compileExpression(expression);
    }

    @Override
    public boolean evaluate(Map<String, T> vars) {

        Boolean aBoolean = (Boolean) MVEL.executeExpression(predicate, vars);
        if(log.isTraceEnabled()) {
            String logMessage = "evaluate condition: " + expression + ", fact: " + vars.get("fact") + ", result: " + aBoolean;
            log.trace(logMessage);
        }

        return aBoolean;
    }
}
