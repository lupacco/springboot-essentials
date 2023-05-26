package academy.devdojo.springboot2.mapper;

import academy.devdojo.springboot2.domain.Pirate;
import academy.devdojo.springboot2.requests.PiratePostRequestBody;
import academy.devdojo.springboot2.requests.PiratePutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class PirateMapper {
    public static final PirateMapper INSTANCE = Mappers.getMapper(PirateMapper.class);
    @Autowired
    public abstract Pirate toPirate(PiratePostRequestBody piratePostRequestBody);
    public abstract Pirate toPirate(PiratePutRequestBody piratePostRequestBody);
}
