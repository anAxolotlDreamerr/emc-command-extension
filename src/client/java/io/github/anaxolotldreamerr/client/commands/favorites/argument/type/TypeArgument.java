package io.github.anaxolotldreamerr.client.commands.favorites.argument.type;

import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.Argument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
/**
 * Obligations must be fulfilled for each new implementation class:
 * 1.Manually add the corresponding name field and its instance to types in ArgumentFactory
 * 2.Detection must be manually added in the ChatUtil.showFavoriteList method
 */
public interface TypeArgument <T extends Identifier> extends Argument {
    Cache<T> cache();
}
