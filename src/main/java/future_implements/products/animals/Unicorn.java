package future_implements.products.animals;

import lombok.Getter;

@Getter
public class Unicorn extends Animal {

    private final boolean hasWings;

    public Unicorn(String name, int price, int age, boolean hasWings) {
        super(name, price, age);
        this.hasWings = hasWings;
    }
}