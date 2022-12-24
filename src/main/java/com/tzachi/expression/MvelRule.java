package com.tzachi.expression;

import com.tzachi.rule.Action;
import com.tzachi.rule.BaseRule;
import com.tzachi.rule.Condition;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class MvelRule<T> extends BaseRule<T> {

    @Builder
    public MvelRule(String name, String description, boolean enabled, List<Condition<T>> conditions,
        List<Action<T>> actions) {
        super(name, description, enabled, conditions, actions);
    }

    @Override
    public boolean evaluate(Map<String, T> vars) {
        for(Condition <T> condition: conditions) {
            if (!condition.evaluate(vars)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void apply(Map<String, T> vars) {
        for(Action<T> action : actions) {
            action.apply(vars);
        }
    }
}
