package future_implements.products.items;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Item {

    protected String name;
    protected int price;
    protected String description;

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }
}