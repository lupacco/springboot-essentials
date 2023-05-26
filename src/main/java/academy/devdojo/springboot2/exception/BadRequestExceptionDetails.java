package academy.devdojo.springboot2.exception;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter //gera somente getters
@SuperBuilder
public class BadRequestExceptionDetails extends ExceptionDetails{

}
