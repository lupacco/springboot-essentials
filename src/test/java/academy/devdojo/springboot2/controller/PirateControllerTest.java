package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Pirate;
import academy.devdojo.springboot2.factory.PirateCreator;
import academy.devdojo.springboot2.factory.PiratePostRequestBodyCreator;
import academy.devdojo.springboot2.factory.PiratePutRequestBodyCreator;
import academy.devdojo.springboot2.requests.PiratePostRequestBody;
import academy.devdojo.springboot2.requests.PiratePutRequestBody;
import academy.devdojo.springboot2.service.PirateService;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import org.assertj.core.api.AssertionInfo;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
class PirateControllerTest {

    @InjectMocks //utilizar qdo a classe que vc quiser testar a classe em si
    private PirateController pirateController;
    @Mock //utilizar quando vc quiser testar as classes que estao dentro da classe que vc etá testando
    private PirateService pirateServiceMock;

    @BeforeEach
    void setup(){
        PageImpl<Pirate> piratePage = new PageImpl<>(List.of(PirateCreator.createValidPirate()));
        BDDMockito.when(pirateServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(piratePage);

        BDDMockito.when(pirateServiceMock.listAllNonPageable())
                .thenReturn(List.of(PirateCreator.createValidPirate()));

        BDDMockito.when(pirateServiceMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(PirateCreator.createValidPirate());

        BDDMockito.when(pirateServiceMock.findByName(ArgumentMatchers.any()))
                .thenReturn(List.of(PirateCreator.createValidPirate()));

        BDDMockito.when(pirateServiceMock.createPirate(ArgumentMatchers.any(PiratePostRequestBody.class)))
                .thenReturn(PirateCreator.createValidPirate());

        BDDMockito.doNothing().when(pirateServiceMock).replacePirate(ArgumentMatchers.any(PiratePutRequestBody.class));

        BDDMockito.doNothing().when(pirateServiceMock).deletePirateById(ArgumentMatchers.anyLong());
    }
    @Test
    @DisplayName("List return list of pirates inside page object when successful")
    void list_ReturnsListOfPiratesInsidePageObject_WhenSuccessul(){
        String expectedName = PirateCreator.createValidPirate().getName();
        Page<Pirate> piratePage = pirateController.list(null).getBody();

        Assertions.assertThat(piratePage).isNotNull();

        Assertions.assertThat(piratePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(piratePage.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("ListAll return list of pirates when successful")
    void listAll_ReturnsListOfPirates_WhenSuccessful(){
        List<Pirate> pirates = pirateController.listAll().getBody();


        Assertions.assertThat(pirates)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    @DisplayName("FindById return pirate when successful")
    void findById_ReturnsPirate_WhenSuccessful(){
        Long expectedPirateId = PirateCreator.createValidPirate().getId();
        Pirate requiredPirate = pirateController.findById(expectedPirateId).getBody();

        Assertions.assertThat(requiredPirate).isNotNull();

        Assertions.assertThat(requiredPirate.getId()).isEqualTo(expectedPirateId);

    }

    @Test
    @DisplayName("FindByName return  list of pirates when successful")
    void findByName_ReturnsListOfPirate_WhenSuccessful(){
        String expectedPirateName = PirateCreator.createValidPirate().getName();
        List<Pirate> requiredPirates = pirateController.findByName(expectedPirateName).getBody();

        Assertions.assertThat(requiredPirates)
                .isNotNull()
                .isNotEmpty();

        Assertions.assertThat(requiredPirates.get(0).getName()).isEqualTo(expectedPirateName);

    }

    @Test
    @DisplayName("FindByName return empty list when pirate is not found")
    void findByName_ReturnsEmptyList_WhenPirateNotFound(){
        BDDMockito.when(pirateServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Pirate> requiredPirates = pirateController.findByName("Nome qualquer").getBody();

        Assertions.assertThat(requiredPirates)
                .isNotNull()
                .isEmpty();

    }

    @Test
    @DisplayName("CreatePirate return pirate when successful")
    void createPirate_ReturnsEmptyList_WhenPirateNotFound(){
        Long expectedId = PirateCreator.createValidPirate().getId();
        String expectedName = PirateCreator.createValidPirate().getName();

        Pirate createdPirate = pirateController.createPirate(PiratePostRequestBodyCreator.createValidPiratePostRequestBody()).getBody();

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
        Assertions.assertThatCode(() -> pirateController.replacePirate(PiratePutRequestBodyCreator.createValidPiratePutRequestBody()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = pirateController.replacePirate(PiratePutRequestBodyCreator.createValidPiratePutRequestBody());

        Assertions.assertThat(entity)
                        .isNotNull();

        Assertions.assertThat(entity.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("DeletePirate return pirate when successful")
    void deletePirateById_RemovesPirate_WhenSuccessful(){
        Assertions.assertThatCode(() -> pirateController.deletePirateById(1))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = pirateController.deletePirateById(1);

        Assertions.assertThat(entity)
                .isNotNull();

        Assertions.assertThat(entity.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);

    }

}