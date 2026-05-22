package io.github.anaxolotldreamerr.client.commands.favorites.argument.method;

import io.github.anaxolotldreamerr.client.commands.favorites.argument.Argument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;

import java.util.Set;

public interface MethodArgument <T extends Identifier> extends Argument {
    Set<T> lookup(Identifier identifier);
    Set<T> filter(Set<Identifier> identifiers);
}
