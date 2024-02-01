package pavlina.EShop.exception_handling.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

@Getter
@JsonIgnoreProperties({"cause", "stackTrace", "message", "suppressed", "localizedMessage"})
public class ValidationException extends RuntimeException {

    @JsonProperty("validation_errors")
    public Map<String, String> validationErrors = new HashMap<>();

    public ValidationException(BindingResult bindingResult) {
        for (var error : bindingResult.getFieldErrors()) {
            validationErrors.put(error.getField(), error.getDefaultMessage());
        }
    }
}
