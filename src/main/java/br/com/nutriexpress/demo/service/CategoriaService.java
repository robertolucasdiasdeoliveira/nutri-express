package br.com.nutriexpress.demo.service;

import java.util.List;


import org.springframework.stereotype.Service;

import br.com.nutriexpress.demo.repository.CategoriaRepository;
import br.dtos.categoria.CategoriaRequestDTO;
import jakarta.transaction.Transactional;
import br.com.nutriexpress.demo.model.Categoria;

 /**
 * o @Service Indica que esta classe é um serviço do Spring
 * é importante para que o Spring possa gerenciar a instância da classe e injetá-la em outros componentes, 
 * como controladores.
 */
@Service
public class CategoriaService {

    // O @Autowired é usado para injetar automaticamente a dependência do CategoriaRepository na classe CategoriaService.
  
    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public List<Categoria> listarCategorias() {
        return repository.findAll();
    }

    public Categoria getCategoriaById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Categoria não encontrada com o ID: " + id));
    }

    /* O @Transactional indica que este método deve ser executado em uma transação */
    @Transactional
    public Categoria criarCategoria(CategoriaRequestDTO cat) {
        
        Categoria categoria = new Categoria();
        
        categoria.setNome(cat.getNome());
        categoria.setDescricao(cat.getDescricao());

        return repository.save(categoria);
    }

    @Transactional
    public Categoria atualizarCategoria(Long id, CategoriaRequestDTO cat) {
        Categoria categoriaExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com o ID: " + id));

        categoriaExistente.setNome(cat.getNome());
        categoriaExistente.setDescricao(cat.getDescricao());

        return repository.save(categoriaExistente);
    }

    @Transactional
    public void deletarCategoria(Long id) {
        Categoria categoriaExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com o ID: " + id));

        repository.delete(categoriaExistente);
    
    }
}
