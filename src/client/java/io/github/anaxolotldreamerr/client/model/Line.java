package io.github.anaxolotldreamerr.client.model;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record Line(Coordinate pos1, Coordinate pos2) {

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Line line)) return false;
        return Objects.equals(pos1, line.pos1) && Objects.equals(pos2, line.pos2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos1, pos2);
    }

    @Override
    public @NotNull String toString() {
        return "Line{" +
                "pos1=" + pos1 +
                ", pos2=" + pos2 +
                '}';
    }
}
