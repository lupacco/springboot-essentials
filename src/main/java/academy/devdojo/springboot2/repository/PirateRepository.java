package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Pirate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PirateRepository extends JpaRepository<Pirate, Long> {

    public abstract List<Pirate> findByName(String name);
}
