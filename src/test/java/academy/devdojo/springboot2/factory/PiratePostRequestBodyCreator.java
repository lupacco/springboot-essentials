package academy.devdojo.springboot2.factory;

import academy.devdojo.springboot2.requests.PiratePostRequestBody;

public class PiratePostRequestBodyCreator {

    public static PiratePostRequestBody createValidPiratePostRequestBody(){
        return PiratePostRequestBody.builder()
                .name("Joyboy")
                .build();
    }
}
