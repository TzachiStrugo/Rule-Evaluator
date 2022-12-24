package com.tzachi.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Value;

@Value
public class JsonRuleDefinitionReader implements RuleReader {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> read(Reader reader) throws IOException {
        Object[] rules = OBJECT_MAPPER.readValue(reader, Object[].class);
        List<Map<String, Object>> ruleMap = new ArrayList<>();
        for(Object rule: rules) {
            ruleMap.add((Map<String, Object> )rule);
        }
        return ruleMap;
    }
}
