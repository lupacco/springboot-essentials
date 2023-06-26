package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Pirate;
import academy.devdojo.springboot2.requests.PiratePostRequestBody;
import academy.devdojo.springboot2.requests.PiratePutRequestBody;
import academy.devdojo.springboot2.service.PirateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("pirate") //nome da rota
@Log4j2 //biblioteca que permite uso de recurso equivalente ao console.log
@RequiredArgsConstructor
public class PirateController {
    private final PirateService pirateService;

    @GetMapping
    public ResponseEntity<Page<Pirate>> list(Pageable pageable) {
        return new ResponseEntity<>(pirateService.listAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path="/all")
    public ResponseEntity<List<Pirate>> listAll() {
        return new ResponseEntity<>(pirateService.listAllNonPageable(), HttpStatus.OK);
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<Pirate> findById(@PathVariable long id) {
        return new ResponseEntity<>(pirateService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path="/find")
    public ResponseEntity<List<Pirate>> findByName(@RequestParam(required = false) String name) {
        return new ResponseEntity<>(pirateService.findByName(name), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Pirate> createPirate(@RequestBody @Valid PiratePostRequestBody piratePostRequestBody){
        return new ResponseEntity<>(pirateService.createPirate(piratePostRequestBody), HttpStatus.CREATED);
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<Void> deletePirateById(@PathVariable long id){
        pirateService.deletePirateById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> replacePirate(@RequestBody PiratePutRequestBody piratePutRequestBody){
        pirateService.replacePirate(piratePutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}