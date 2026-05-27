package io.github.anaxolotldreamerr.client.commands.favorites.argument.query;

import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.Argument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorite;

import java.util.Map;
/**
 * Obligations must be fulfilled for each new implementation class:
 * 1.Manually add the corresponding name field and its instance to query in ArgumentFactory
 */
public interface QueryArgument extends Argument {
    <T extends Identifier> Map<String, Favorite<T>> map(Cache<T> cache);
}
