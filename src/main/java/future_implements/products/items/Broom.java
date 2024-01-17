package future_implements.products.items;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Broom extends Item {

    private String color;

    public Broom(String name, int price, String color) {
        super(name, price);
        this.color = color;
    }
}