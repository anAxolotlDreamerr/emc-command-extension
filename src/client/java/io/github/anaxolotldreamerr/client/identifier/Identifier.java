package io.github.anaxolotldreamerr.client.identifier;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

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
/**
 * Every ECommand implementation class must follow these conventions:
 * 1.A dedicated method for obtaining relevant Identifiers is set in the EMCApiRequest class
 */
public interface Identifier {
    String name();
    String uuid();
    default List<String> toList(){
        return List.of(name(),uuid());
    };
}
