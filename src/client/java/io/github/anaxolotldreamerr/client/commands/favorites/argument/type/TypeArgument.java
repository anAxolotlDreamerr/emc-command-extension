package io.github.anaxolotldreamerr.client.commands.favorites.argument.type;

import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.Argument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
/**
 * 每新增一个实现类都要给ArgumentFactory中的typeNames手动添加对应的name字段及其实例
 */
public interface TypeArgument <T extends Identifier> extends Argument {
    Cache<T> cache();
}
