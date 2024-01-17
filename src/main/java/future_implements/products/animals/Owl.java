package future_implements.products.animals;

import lombok.Getter;

@Getter
public class Owl extends Animal {

    private final OwlSpecies species;

    public Owl(String name, int price, int age, OwlSpecies species) {
        super(name, price, age);
        this.species = species;
    }
}