package academy.devdojo.springboot2.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // gera getters e setters (lombok)
@AllArgsConstructor //gera construtores com todos os valores de atributo
@NoArgsConstructor //gera construtor sem argumentos
@Entity
@Builder
public class Pirate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Pirate(String name){
        this.name = name;
    }
}
