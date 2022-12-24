package com.tzachi.rule;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class BaseRule <T> implements Rule<T> {

    protected String name;
    protected String description;
    protected boolean enabled;

    protected List<Condition<T>> conditions;
    protected List <Action<T>> actions;
}
