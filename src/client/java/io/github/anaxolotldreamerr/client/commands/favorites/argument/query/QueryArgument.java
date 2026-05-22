package io.github.anaxolotldreamerr.client.commands.favorites.argument.query;

import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.Argument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorites;

import java.util.Map;

public interface QueryArgument extends Argument {
    <T extends Identifier> Map<String, Favorites<T>> map(Cache<T> cache);
}
