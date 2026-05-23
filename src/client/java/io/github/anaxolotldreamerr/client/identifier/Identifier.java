package io.github.anaxolotldreamerr.client.identifier;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TownIdentifier.class, name = "town"),
        @JsonSubTypes.Type(value = NationIdentifier.class, name = "nation"),
        @JsonSubTypes.Type(value = PlayerIdentifier.class,name = "player")
})
public interface Identifier {
    String name();
    String uuid();
}
