package io.github.anaxolotldreamerr.client.util;

import io.github.anaxolotldreamerr.client.commands.favorites.argument.search.SearchArgument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;

import java.util.Set;

public class SearchUtil {
    public static Identifier search(Set<? extends Identifier> identifiers,String name){
        for(Identifier identifier : identifiers)
        {
            if(identifier.name().equalsIgnoreCase(name)) return identifier;
        }
        throw new NullPointerException("No such name: "+name);
    }
}
