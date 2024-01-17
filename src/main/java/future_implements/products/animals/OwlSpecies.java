package future_implements.products.animals;

import lombok.Getter;

@Getter
public enum OwlSpecies {

    BARN_OWL("barn owl"),
    SNOWY_OWL("snowy owl"),
    LONG_EARED_OWL("long-eared owl");


    final String readableDescription;

    OwlSpecies(String readableDescription) {
        this.readableDescription = readableDescription;
    }
}