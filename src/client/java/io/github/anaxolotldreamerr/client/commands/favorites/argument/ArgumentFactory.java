package io.github.anaxolotldreamerr.client.commands.favorites.argument;

import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TownType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TypeArgument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;

public class ArgumentFactory {
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
}
