package academy.devdojo.springboot2.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
public class PiratePostRequestBody {
    @NotEmpty(message = "Pirate must have a name")
    private String name;
    @URL(message = "URL Inválida")
    private String url;
}
