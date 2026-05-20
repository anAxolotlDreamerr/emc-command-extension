package io.github.anaxolotldreamerr.client.commands.favorites;


import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;
import io.github.anaxolotldreamerr.client.model.Favorites;
import io.github.anaxolotldreamerr.client.util.ChatUtil;


import java.util.function.BiConsumer;



public enum MethodArguments {
    TOWN("-t",
            ((favorites, identifier) -> {
                if(identifier instanceof TownIdentifier town){
                    favorites.add(identifier);
                }
            }),
            ((favorites, identifier) -> {
                if(identifier instanceof TownIdentifier town){
                    favorites.remove(identifier);
                }
            })

    );
    private final String name;
    private final BiConsumer<Favorites,Identifier> add;
    private final BiConsumer<Favorites,Identifier> remove;
     MethodArguments(String name,BiConsumer<Favorites,Identifier> add,BiConsumer<Favorites,Identifier> remove){
        this.name = name;
        this.add = add;
        this.remove=remove;
    }
    public void add(Favorites favorites,Identifier identifier){
        add.accept(favorites,identifier);
    }
    public void remove(Favorites favorites, Identifier identifier){
        remove.accept(favorites,identifier);
    }
    public MethodArguments defaultArug(TypeArguments type){
        switch (type){
            case TOWN -> {
                return TOWN;
            }
            default -> {
                ChatUtil.sendException(new IllegalArgumentException("You have to specified a argument for"+type.name()));
                return null;
            }
        }
    }
}
