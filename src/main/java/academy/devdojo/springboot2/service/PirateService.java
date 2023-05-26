package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Pirate;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.mapper.PirateMapper;
import academy.devdojo.springboot2.repository.PirateRepository;
import academy.devdojo.springboot2.requests.PiratePostRequestBody;
import academy.devdojo.springboot2.requests.PiratePutRequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class PirateService {
    private final PirateRepository pirateRepository;

    public Page<Pirate> listAll(Pageable pageable) {
        return pirateRepository.findAll(pageable);
    }
    public List<Pirate> listAllNonPageable() {
        return pirateRepository.findAll();
    }

    public List<Pirate> findByName(String name){
        return pirateRepository.findByName(name);
    }
    public Pirate findById(long id) {
        return pirateRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Pirate not Found"));
    }

    @Transactional //checar se o que vc ta fazendo, em caso de exception, precisa de rollback
    public Pirate createPirate(PiratePostRequestBody piratePostRequestBody) {
        Pirate pirate = PirateMapper.INSTANCE.toPirate(piratePostRequestBody);
        return pirateRepository.save(pirate);
    }

    public void deletePirateById(long id) {
        pirateRepository.delete(findById(id));
    }

    public void replacePirate(PiratePutRequestBody piratePutRequestBody) {
        Pirate findedPirate = findById(piratePutRequestBody.getId());
        Pirate pirate = PirateMapper.INSTANCE.toPirate(piratePutRequestBody);
        pirate.setId(findedPirate.getId());
        pirateRepository.save(pirate);
    }

}
