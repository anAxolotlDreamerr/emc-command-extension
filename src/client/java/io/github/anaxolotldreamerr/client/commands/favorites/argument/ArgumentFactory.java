package io.github.anaxolotldreamerr.client.commands.favorites.argument;

import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TownType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TypeArgument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArgumentFactory {
    private static Set<String> typeNames = Set.of(
            TownType.name()
    );
    private ArgumentFactory(){};
    public static <T extends Identifier> TypeArgument<T> typeArgument(String arg){
        if(TownType.name().equals(arg)){
            return (TypeArgument<T>) TownType.getInstance();
        }
        throw new IllegalArgumentException("Unknown arg:"+arg);
    }
    public static Argument stringArgument(){
        return new Argument(){};
    }

    public static Set<String> getAllTypeName(){
        return typeNames;
    }
}
