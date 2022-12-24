package com.tzachi.expression;

import com.tzachi.rule.Action;
import java.io.Serializable;
import java.util.Map;
import lombok.Value;
import org.mvel2.MVEL;

@Value
public class MvelAction<T> implements Action <T> {

    Serializable consumerExpression;

    public MvelAction(String expression) {
        consumerExpression = MVEL.compileExpression(expression);
    }

    @Override
    public void apply(Map<String, T> vars) {
        MVEL.executeExpression(consumerExpression, vars);
    }
}
