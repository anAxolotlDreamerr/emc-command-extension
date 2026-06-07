package io.github.anaxolotldreamerr.client.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.anaxolotldreamerr.client.identifier.Identifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestUtil {
    public static String mix(Collection<? extends Identifier> identifiers) throws JsonProcessingException {
        Map<String, List<String>> p = new HashMap<>();
        String query = "query";
        p.put(query,identifiers.stream().map(Identifier::uuid).collect(Collectors.toList()));
        return new ObjectMapper().writeValueAsString(p);
    }
}
