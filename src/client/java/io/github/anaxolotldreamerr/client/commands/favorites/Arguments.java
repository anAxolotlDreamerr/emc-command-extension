package io.github.anaxolotldreamerr.client.commands.favorites;

import java.util.*;
import java.util.stream.Collectors;

public enum Arguments {
    ID("-i"),TOWN("-t"),NATION("-n"),PLAYER("-p"),RESIDENT("-r");
    private static final Map<String,Arguments> LOOKUP = new HashMap<>();
    private final String ARG;
    Arguments(String arg){
        this.ARG = arg;
        put(arg,this);
    }
    private static void put(String sArg,Arguments arg){
        if(LOOKUP.containsKey(sArg))throw new IllegalArgumentException("Duplicate enum field name:"+arg+"-"+sArg);
        LOOKUP.put(sArg,arg);
    }
    public static Set<Arguments> match(String... args){
        return Arrays.stream(args).filter(arg -> {
                    for (String sArg : args) {
                        if (LOOKUP.containsKey(arg)) return true;
                    }
                    return false;
                }
                ).map(LOOKUP::get).collect(Collectors.toSet());
    }
}
