package io.github.anaxolotldreamerr.client.identifier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class PlayerIdentifier implements Identifier {
    private final String name;
    private final String uuid;
    @JsonCreator
    public PlayerIdentifier(@JsonProperty("name") String name,@JsonProperty("uuid") String uuid){
        this.name = name;
        this.uuid = uuid;
    }
    @JsonGetter("name")
    public String name(){return name;}
    @JsonGetter("uuid")
    public String uuid(){return uuid;}

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if (!(o instanceof PlayerIdentifier that)) return false;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, uuid);
    }

    @Override
    public String toString() {
        return "PlayerIdentifier{" +
                "name='" + name + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
    public static PlayerIdentifier byIdentifier(Identifier identifier){
        if(identifier instanceof PlayerIdentifier i){
            return i;
        }
        throw new IllegalArgumentException(identifier+"isn't NationIdentifier");
    }
}
