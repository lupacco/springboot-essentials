package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Pirate;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.factory.PirateCreator;
import academy.devdojo.springboot2.factory.PiratePostRequestBodyCreator;
import academy.devdojo.springboot2.factory.PiratePutRequestBodyCreator;
import academy.devdojo.springboot2.repository.PirateRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class PirateServiceTest {
    @InjectMocks //utilizar qdo a classe que vc quiser testar a classe em si
    private PirateService pirateService;
    @Mock //utilizar quando vc quiser testar as classes que estao dentro da classe que vc etá testando
    private PirateRepository pirateRepositoryMock;

    @BeforeEach
    void setup(){
        PageImpl<Pirate> piratePage = new PageImpl<>(List.of(PirateCreator.createValidPirate()));
        BDDMockito.when(pirateRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(piratePage);

        BDDMockito.when(pirateRepositoryMock.findAll())
                .thenReturn(List.of(PirateCreator.createValidPirate()));

        BDDMockito.when(pirateRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(PirateCreator.createValidPirate()));

        BDDMockito.when(pirateRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(PirateCreator.createValidPirate()));

        BDDMockito.when(pirateRepositoryMock.save(ArgumentMatchers.any(Pirate.class)))
                .thenReturn(PirateCreator.createValidPirate());

        BDDMockito.doNothing().when(pirateRepositoryMock).delete(ArgumentMatchers.any(Pirate.class));
    }
    @Test
    @DisplayName("List return list of pirates inside page object when successful")
    void listAll_ReturnsListOfPiratesInsidePageObject_WhenSuccessul(){
        String expectedName = PirateCreator.createValidPirate().getName();

        Page<Pirate> piratePage = pirateService.listAll(PageRequest.of(1,1));

        Assertions.assertThat(piratePage).isNotNull();

        Assertions.assertThat(piratePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(piratePage.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("ListAll return list of pirates when successful")
    void listAllNonPageable_ReturnsListOfPirates_WhenSuccessful(){
        List<Pirate> pirates = pirateService.listAllNonPageable();


        Assertions.assertThat(pirates)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    @DisplayName("FindById return pirate when successful")
    void findById_ReturnsPirate_WhenSuccessful(){
        Long expectedPirateId = PirateCreator.createValidPirate().getId();

        Pirate requiredPirate = pirateService.findById(expectedPirateId);

        Assertions.assertThat(requiredPirate).isNotNull();

        Assertions.assertThat(requiredPirate.getId()).isEqualTo(expectedPirateId);


    }

    @Test
    @DisplayName("FindById throws BadRequestException when pirate is not found")
    void findById_ThrowsBadRequestException_WhenPirateNotFound(){
        BDDMockito.when(pirateRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                        .isThrownBy(() -> pirateService.findById(1));

    }

    @Test
    @DisplayName("FindByName return  list of pirates when successful")
    void findByName_ReturnsListOfPirate_WhenSuccessful(){
        String expectedPirateName = PirateCreator.createValidPirate().getName();
        List<Pirate> requiredPirates = pirateService.findByName(expectedPirateName);

        Assertions.assertThat(requiredPirates)
                .isNotNull()
                .isNotEmpty();

        Assertions.assertThat(requiredPirates.get(0).getName()).isEqualTo(expectedPirateName);

    }

    @Test
    @DisplayName("FindByName return empty list when pirate is not found")
    void findByName_ReturnsEmptyList_WhenPirateNotFound(){
        BDDMockito.when(pirateRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Pirate> requiredPirates = pirateService.findByName("Nome qualquer");

        Assertions.assertThat(requiredPirates)
                .isNotNull()
                .isEmpty();

    }

    @Test
    @DisplayName("CreatePirate return pirate when successful")
    void createPirate_ReturnsEmptyList_WhenPirateNotFound(){
        Long expectedId = PirateCreator.createValidPirate().getId();
        String expectedName = PirateCreator.createValidPirate().getName();

        Pirate createdPirate = pirateService.createPirate(PiratePostRequestBodyCreator.createValidPiratePostRequestBody());

        Assertions.assertThat(createdPirate)
                .isNotNull();

        Assertions.assertThat(createdPirate.getId())
                .isEqualTo(expectedId);

        Assertions.assertThat(createdPirate.getName())
                .isEqualTo(expectedName);
    }

    @Test
    @DisplayName("ReplacePirate return pirate when successful")
    void replacePirate_UpdatesPirate_WhenSuccessful(){
        Assertions.assertThatCode(() -> pirateService.replacePirate(PiratePutRequestBodyCreator.createValidPiratePutRequestBody()))
                .doesNotThrowAnyException();

    }

    @Test
    @DisplayName("DeletePirate return pirate when successful")
    void deletePirateById_RemovesPirate_WhenSuccessful(){
        Assertions.assertThatCode(() -> pirateService.deletePirateById(1))
                .doesNotThrowAnyException();

    }
}