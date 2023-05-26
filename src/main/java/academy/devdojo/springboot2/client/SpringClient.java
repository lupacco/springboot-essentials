package academy.devdojo.springboot2.client;

/*Requisições para outras APIs são feitas aqui*/

import academy.devdojo.springboot2.domain.Pirate;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Pirate> entity = new RestTemplate().getForEntity("http://localhost:8080/pirate/{id}", Pirate.class, 2);
        log.info(entity);

        Pirate object =
                new RestTemplate().getForObject("http://localhost:8080/pirate/{id}",
                        Pirate.class,
                        4);
        log.info(object);

        Pirate[] pirates =
                new RestTemplate().getForObject("http://localhost:8080/pirate/all", Pirate[].class);
        log.info(Arrays.toString(pirates));
        //@formatter:off
        ResponseEntity<List<Pirate>> exchange =
                new RestTemplate().exchange("http://localhost:8080/pirate/all",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Pirate>>() {});
        //@formatter:on
        log.info(exchange.getBody());

//        Pirate kuzan = Pirate.builder().name("Kuzan").build();
//        ResponseEntity<Pirate> kuzanSaved = new RestTemplate().exchange("http://localhost:8080/pirate",
//                HttpMethod.POST,
//                new HttpEntity<>(kuzan, createJsonHeader()),
//                Pirate.class);
        //Utilizando postForObject:
//        Pirate doffy = Pirate.builder().name("Doffy").build();
//        Pirate doffySaved = new RestTemplate().postForObject("http://localhost:8080/pirate", doffy, Pirate.class);
//        log.info("saved pirate {}", doffySaved);

        //@formatter:off
        ResponseEntity<Pirate> kuzanDB =
                new RestTemplate().getForEntity("http://localhost:8080/pirate/{id}",Pirate.class, 45);
        //@formatter:on
        Pirate aokiji = kuzanDB.getBody();
        aokiji.setName("Ao kiji Kuzan");
        ResponseEntity<Void> kuzanUpdated = new RestTemplate().exchange("http://localhost:8080/pirate",
                HttpMethod.PUT,
                new HttpEntity<>(aokiji, createJsonHeader()),
                Void.class);
        log.info(kuzanUpdated);

        ResponseEntity<Void> deletedPirate = new RestTemplate().exchange("http://localhost:8080/pirate/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                aokiji.getId());
        log.info(deletedPirate);

    }

    private static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
