package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Pirate;
import academy.devdojo.springboot2.factory.PirateCreator;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@Log4j2
@DataJpaTest
@DisplayName("Test for Pirate Repository")
class PirateRepositoryTest {
    @Autowired
    private PirateRepository pirateRepository;

    private PirateCreator pirateCreator;

    @Test
    @DisplayName("Save creates/persist pirate when Successul")
    void save_PersistPirate_WhenSuccessful() {
        Pirate pirateToBeSaved = pirateCreator.createPirateToBeSaved();
        Pirate savedPirate = this.pirateRepository.save(pirateToBeSaved);

        log.info(savedPirate.getName());

        Assertions.assertThat(savedPirate).isNotNull();

        Assertions.assertThat(savedPirate.getId()).isNotNull();

        Assertions.assertThat(savedPirate.getName()).isEqualTo(pirateToBeSaved.getName());
    }

    @Test
    @DisplayName("Save updates pirate when Successul")
    void save_UpdatesPirate_WhenSuccessful() {
        Pirate pirateToBeSaved = pirateCreator.createPirateToBeSaved();
        Pirate savedPirate = this.pirateRepository.save(pirateToBeSaved);
        log.info(savedPirate.getName());

        savedPirate.setName("Nika");
        Pirate updatedPirate = this.pirateRepository.save(savedPirate);
        log.info(savedPirate.getName());

        Assertions.assertThat(updatedPirate).isNotNull();

        Assertions.assertThat(updatedPirate.getId()).isNotNull();

        Assertions.assertThat(updatedPirate.getName()).isEqualTo(savedPirate.getName());
    }

    @Test
    @DisplayName("Delete removes pirate when Successul")
    void delete_RemovesPirate_WhenSuccessful() {
        Pirate pirateToBeSaved = pirateCreator.createPirateToBeSaved();
        Pirate savedPirate = this.pirateRepository.save(pirateToBeSaved);

        this.pirateRepository.delete(savedPirate);

        Optional<Pirate> pirateOptional = this.pirateRepository.findById(savedPirate.getId());

        Assertions.assertThat(pirateOptional).isEmpty();
    }

    @Test
    @DisplayName("Find By Name returns list of pirates when Successul")
    void findByName_ReturnsListOfPirate_WhenSuccessful() {
        Pirate pirateToBeSaved = pirateCreator.createPirateToBeSaved();
        Pirate savedPirate = this.pirateRepository.save(pirateToBeSaved);

        List<Pirate> findedPirates = this.pirateRepository.findByName(savedPirate.getName());

        Assertions.assertThat(findedPirates).isNotEmpty();
        Assertions.assertThat(findedPirates).contains(savedPirate);
    }
    @Test
    @DisplayName("Find By Name returns an empty list when pirate not exist")
    void findByName_ReturnsEmptyList_WhenPirateNotExist() {
        Pirate pirateNotSaved = pirateCreator.createPirateToBeSaved();

        List<Pirate> findedPirates = this.pirateRepository.findByName(pirateNotSaved.getName());

        Assertions.assertThat(findedPirates).isEmpty();
    }

    @Test
    @DisplayName("Save throw ContraintViolationException when name is empty")
    void save_ThrowsContraintViolationException_WhenNameIsEmpty() {
        Pirate pirateToBeSaved = new Pirate();
//        Assertions.assertThatThrownBy(() -> this.pirateRepository.save(pirateToBeSaved))
//                .isInstanceOf(ConstraintViolationException.class);
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.pirateRepository.save(pirateToBeSaved))
                .withMessageContaining("The pirate name cannot be empty");

    }


}