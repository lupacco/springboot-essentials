package academy.devdojo.springboot2.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PiratePutRequestBody {
    private long id;
    private String name;
}
