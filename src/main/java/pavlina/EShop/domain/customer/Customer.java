package pavlina.EShop.domain.customer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Customer {

    @NotNull
    private String name;

    @NotNull
    private String address;

    @NotNull
    private String email;

    private String phoneNumber;
}
