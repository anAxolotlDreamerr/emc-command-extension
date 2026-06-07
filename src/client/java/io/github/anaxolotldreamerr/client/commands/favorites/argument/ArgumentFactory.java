package io.github.anaxolotldreamerr.client.commands.favorites.argument;

import io.github.anaxolotldreamerr.client.commands.favorites.argument.query.IDQuery;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.query.NameQuery;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.query.QueryArgument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.search.NationSearch;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.search.PlayerSearch;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.search.SearchArgument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.search.TownSearch;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.NationType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.PlayerType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TownType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TypeArgument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import java.util.Map;
import java.util.Set;


public class ArgumentFactory {
    private final static Map<String,TypeArgument> TYPES = Map.of(
            TownType.name(),TownType.getInstance()
            , NationType.name(),NationType.getInstance()
            , PlayerType.name(),PlayerType.getInstance()
    );
    private final static Map<String, QueryArgument> QUERY = Map.of(
            NameQuery.getName(),new NameQuery()
            , IDQuery.getName(),new IDQuery()
    );
    //For each Search, it must be mapped to the keys of Type.name and Search.getName
    private final static Map<String, SearchArgument> SEARCH = Map.of(
            TownSearch.getName(),new TownSearch(),
            NationSearch.getName(),new NationSearch(),
            PlayerSearch.getName(),new PlayerSearch()
    );
    private final static Map<String , SearchArgument> DEFAULT_SEARCH = Map.of(
            TownType.name(),new TownSearch(),
            NationType.name(),new NationSearch(),
            PlayerType.name(),new PlayerSearch()
    );
    private ArgumentFactory(){};
    public static <T extends Identifier> TypeArgument<T> typeArgument(String arg){
        if(TYPES.containsKey(arg)){
            return (TypeArgument<T>) TYPES.get(arg);
        }
        throw new IllegalArgumentException("Unknown type arg:"+arg);
    }
    public static QueryArgument queryArgument(String arg){
        if(QUERY.containsKey(arg)){
            return QUERY.get(arg);
        }
        return QUERY.get(NameQuery.getName());
    }
    public static <T extends Identifier> SearchArgument<T> searchArgument(String arg){
        if(SEARCH.containsKey(arg)){
            return (SearchArgument<T>) SEARCH.get(arg);
        }
        throw new NullPointerException("Unknown search:"+arg);
    }
    public static <T extends Identifier> SearchArgument<T> defaultSearchArgument(String type){
        if(DEFAULT_SEARCH.containsKey(type)){
            return (SearchArgument<T>) DEFAULT_SEARCH.get(type);
        }
        throw new NullPointerException("The type:"+type+" dose not have default search");
    }
    public static Argument stringArgument(){
        return new Argument(){};
    }

    public static Set<String> getAllTypeName(){
        return TYPES.keySet();
    }
    public static Set<String> getAllQueryName(){return QUERY.keySet();}
    public static Set<String> getAllSearchName(){return SEARCH.keySet();}
}
