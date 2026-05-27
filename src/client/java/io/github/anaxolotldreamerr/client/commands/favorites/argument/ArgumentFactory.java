package io.github.anaxolotldreamerr.client.commands.favorites.argument;

import io.github.anaxolotldreamerr.client.commands.favorites.argument.query.IDQuery;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.query.NameQuery;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.query.QueryArgument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.NationType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TownType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TypeArgument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import java.util.Map;
import java.util.Set;


public class ArgumentFactory {
    private final static Map<String,TypeArgument> TYPES = Map.of(
            TownType.name(),TownType.getInstance()
            , NationType.name(),NationType.getInstance()
    );
    private final static Map<String, QueryArgument> QUERY = Map.of(
            NameQuery.getName(),new NameQuery()
            , IDQuery.getName(),new IDQuery()
    );
    private ArgumentFactory(){};
    public static <T extends Identifier> TypeArgument<T> typeArgument(String arg){
        if(TYPES.containsKey(arg)){
            return (TypeArgument<T>) TYPES.get(arg);
        }
        throw new IllegalArgumentException("Unknown arg:"+arg);
    }
    public static QueryArgument queryArgument(String arg){
        if(QUERY.containsKey(arg)){
            return QUERY.get(arg);
        }
        throw new IllegalArgumentException("Unknown arg:"+arg);
    }
    public static Argument stringArgument(){
        return new Argument(){};
    }

    public static Set<String> getAllTypeName(){
        return TYPES.keySet();
    }
}
