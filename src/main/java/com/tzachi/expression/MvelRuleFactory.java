package com.tzachi.expression;

import com.tzachi.reader.RuleReader;
import com.tzachi.rule.Action;
import com.tzachi.rule.Condition;
import com.tzachi.rule.Rule;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Value;

@Value
@SuppressWarnings("unchecked")
public class MvelRuleFactory {

    RuleReader ruleDefinitionReader;

    public MvelRuleFactory(RuleReader ruleReader) {
        this.ruleDefinitionReader = ruleReader;
    }


    public List<Rule<String>> createRules(Reader readerFile) throws IOException {
        return ruleDefinitionReader
            .read(readerFile)
            .stream()
            .map(this::createRule)
            .collect(Collectors.toList());
    }


    private Rule<String> createRule (Map<String, Object> ruleDef) {
        List<MvelCondition<String>> conditions = getConditions(ruleDef);
        List<MvelAction<String>> actions = getActions(ruleDef);

        return MvelRule.<String>builder()
            .name((String) ruleDef.get("name"))
            .enabled((boolean) ruleDef.get("enabled"))
            .description((String) ruleDef.get("description"))
            .conditions((List<Condition<String>>) (List<?>) conditions)
            .actions((List<Action<String>>) (List<?>) actions)
            .build();
    }

    private static List<MvelCondition<String>> getConditions(Map<String, Object> ruleDef) {
        List<String> conditions = (List<String>)ruleDef.get("conditions");
        if(conditions == null || conditions.isEmpty()) {
            throw new IllegalArgumentException("The rule must have at least one condition");
        }
        return conditions
            .stream()
            .map(c -> new MvelCondition<String>(c))
            .collect(Collectors.toList());
    }

    private static List<MvelAction<String>> getActions(Map<String, Object> ruleDef) {
        return Optional
            .ofNullable((List<String>) ruleDef.get("actions"))
            .map(list -> list
                .stream()
                .map(a -> new MvelAction<String>(a))
                .collect(Collectors.toList()))
            .orElse(Collections.emptyList());
    }
}
