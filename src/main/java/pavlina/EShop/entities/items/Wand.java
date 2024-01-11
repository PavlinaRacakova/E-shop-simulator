package pavlina.EShop.entities.items;

import lombok.Getter;

@Getter
public class Wand extends Item {

    private final String core;

    public Wand(String name, int price, String core) {
        super(name, price);
        this.core = core;
    }
}