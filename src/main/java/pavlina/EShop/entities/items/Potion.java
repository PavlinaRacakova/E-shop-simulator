package pavlina.EShop.entities.items;

import lombok.Getter;

@Getter
public class Potion extends Item {

    private final int doses;
    private final boolean isDangerous;
    private final String color;

    public Potion(String name, int price, int doses, boolean isDangerous, String color) {
        super(name, price);
        this.doses = doses;
        this.isDangerous = isDangerous;
        this.color = color;
    }
}