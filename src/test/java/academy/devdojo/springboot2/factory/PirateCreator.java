package academy.devdojo.springboot2.factory;

import academy.devdojo.springboot2.domain.Pirate;

public class PirateCreator {
    public static Pirate createPirateToBeSaved() {
        return Pirate.builder()
                .name("Joyboy")
                .build();
    }

    public static Pirate createValidPirate() {
        return Pirate.builder()
                .name("Joyboy")
                .id(1L)
                .build();
    }

    public static Pirate createValidUpdatedPirate() {
        return Pirate.builder()
                .name("Nika")
                .id(1L)
                .build();
    }
}
