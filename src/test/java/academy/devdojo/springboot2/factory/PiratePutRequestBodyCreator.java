package academy.devdojo.springboot2.factory;

import academy.devdojo.springboot2.requests.PiratePutRequestBody;

public class PiratePutRequestBodyCreator {

    public static PiratePutRequestBody createValidPiratePutRequestBody() {
        return PiratePutRequestBody.builder()
                .id(PirateCreator.createValidUpdatedPirate().getId())
                .name(PirateCreator.createValidUpdatedPirate().getName())
                .build();
    }
}
