package academy.devdojo.springboot2.integration;

import academy.devdojo.springboot2.domain.Pirate;
import academy.devdojo.springboot2.factory.PirateCreator;
import academy.devdojo.springboot2.factory.PiratePostRequestBodyCreator;
import academy.devdojo.springboot2.repository.PirateRepository;
import academy.devdojo.springboot2.requests.PiratePostRequestBody;
import academy.devdojo.springboot2.wrapper.PageableResponse;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PirateControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private PirateRepository pirateRepository;

    @Test
    @DisplayName("List return list of pirates inside page object when successful")
    void list_ReturnsListOfPiratesInsidePageObject_WhenSuccessful(){
        Pirate savedPirate = pirateRepository.save(PirateCreator.createPirateToBeSaved());
        String expectedName = savedPirate.getName();

        PageableResponse<Pirate> piratePage = testRestTemplate.exchange("/pirate", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Pirate>>() {
                }).getBody();

        Assertions.assertThat(piratePage).isNotNull();

        Assertions.assertThat(piratePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(piratePage.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("ListAll return list of pirates when successful")
    void listAll_ReturnsListOfPirates_WhenSuccessful(){
        Pirate savedPirate = pirateRepository.save(PirateCreator.createPirateToBeSaved());
        String expectedName = savedPirate.getName();

        List<Pirate> pirates = testRestTemplate.exchange("/pirate/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Pirate>>() {
                }).getBody();

        Assertions.assertThat(pirates)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(pirates.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindById return pirate when successful")
    void findById_ReturnsPirate_WhenSuccessful(){
        Pirate savedPirate = pirateRepository.save(PirateCreator.createPirateToBeSaved());
        Long expectedPirateId = savedPirate.getId();

        Pirate requiredPirate = testRestTemplate.getForObject("/pirate/{id}", Pirate.class, expectedPirateId);

        Assertions.assertThat(requiredPirate).isNotNull();

        Assertions.assertThat(requiredPirate.getId()).isEqualTo(expectedPirateId);

    }

    @Test
    @DisplayName("FindByName return list of pirates when successful")
    void findByName_ReturnsListOfPirate_WhenSuccessful(){
        Pirate savedPirate = pirateRepository.save(PirateCreator.createPirateToBeSaved());
        String expectedPirateName = savedPirate.getName();

        String url = String.format("/pirate/find?name=%s", expectedPirateName);

        List<Pirate> requiredPirates = testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Pirate>>() {
                }).getBody();

        Assertions.assertThat(requiredPirates)
                .isNotNull()
                .isNotEmpty();

        Assertions.assertThat(requiredPirates.get(0).getName()).isEqualTo(expectedPirateName);
    }

    @Test
    @DisplayName("FindByName return empty list when pirate is not found")
    void findByName_ReturnsEmptyList_WhenPirateNotFound(){
        List<Pirate> requiredPirates = testRestTemplate.exchange("/pirate/find?name=randomName", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Pirate>>() {
                }).getBody();

        Assertions.assertThat(requiredPirates)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("CreatePirate return pirate when successful")
    void createPirate_ReturnsPirate_WhenSuccessful(){
        PiratePostRequestBody piratePostRequestBody = PiratePostRequestBodyCreator.createValidPiratePostRequestBody();

        ResponseEntity<Pirate> pirateResponseEntity = testRestTemplate.postForEntity("/pirate", piratePostRequestBody, Pirate.class);

        Assertions.assertThat(pirateResponseEntity).isNotNull();
        Assertions.assertThat(pirateResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(pirateResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(pirateResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("ReplacePirate return pirate when successful")
    void replacePirate_UpdatesPirate_WhenSuccessful(){
        Pirate savedPirate = pirateRepository.save(PirateCreator.createPirateToBeSaved());

        savedPirate.setName("Luffy");

        ResponseEntity<Void> pirateResponseEntity = testRestTemplate.exchange("/pirate",
                HttpMethod.PUT,
                new HttpEntity<>(savedPirate),
                Void.class);

        Assertions.assertThat(pirateResponseEntity).isNotNull();
        Assertions.assertThat(pirateResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("DeletePirate return pirate when successful")
    void deletePirateById_RemovesPirate_WhenSuccessful(){
        Pirate savedPirate = pirateRepository.save(PirateCreator.createPirateToBeSaved());

        ResponseEntity<Void> pirateResponseEntity = testRestTemplate.exchange("/pirate/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                savedPirate.getId());

        Assertions.assertThat(pirateResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(pirateResponseEntity).isNotNull();
    }

}
