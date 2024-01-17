package future_implements.products.animals;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Animal {

    protected String name;
    protected int price;
    protected String description;
    protected int age;

    public Animal(String name, int price, int age) {
        this.name = name;
        this.price = price;
        this.age = age;
    }
}
