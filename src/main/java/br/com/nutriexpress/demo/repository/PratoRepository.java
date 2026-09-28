package br.com.nutriexpress.demo.repository;

import br.com.nutriexpress.demo.model.Prato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PratoRepository extends JpaRepository<Prato, Long> {
List<Prato> findByCategoriaId(Long categoriaId);

boolean existsByNome(String nome);

}
