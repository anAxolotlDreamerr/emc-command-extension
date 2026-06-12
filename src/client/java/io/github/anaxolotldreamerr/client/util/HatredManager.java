package io.github.anaxolotldreamerr.client.util;

import io.github.anaxolotldreamerr.client.commands.favorites.argument.ArgumentFactory;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorite;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HatredManager {

    private static volatile Set<String> CACHE = Set.of();

    public static synchronized void refresh() {

        Map<String, Favorite<Identifier>> map =
                ArgumentFactory.queryArgument("-i")
                        .map(ArgumentFactory.typeArgument("-p").cache());

        if (map == null || !map.containsKey("hate")) {
            CACHE = Set.of();
            return;
        }

        Set<Identifier> set = map.get("hate").objects();

        if (set == null) {
            CACHE = Set.of();
            return;
        }

        CACHE = set.stream()
                .map(Identifier::uuid)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static boolean isHatredPlayer(String uuid) {
        return CACHE.contains(uuid);
    }
}