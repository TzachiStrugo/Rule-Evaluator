package com.tzachi.reader;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;

public interface RuleReader {

    List<Map<String, Object>> read(Reader reader) throws IOException;
}
