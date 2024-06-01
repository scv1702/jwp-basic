package core.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Model {
    private final Map<String, Object> attributes = new HashMap<>();

    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }
}
