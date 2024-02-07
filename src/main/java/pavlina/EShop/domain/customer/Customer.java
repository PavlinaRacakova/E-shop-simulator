package pavlina.EShop.domain.customer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Customer {

    private String name;
    private String address;
    private String email;
    private String phoneNumber;
}
