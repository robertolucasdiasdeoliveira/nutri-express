package br.com.nutriexpress.demo.service;

import br.com.nutriexpress.demo.exception.ResourceNotFoundException;
import br.com.nutriexpress.demo.model.Categoria;
import br.com.nutriexpress.demo.model.Prato;
import br.com.nutriexpress.demo.repository.CategoriaRepository;
import br.com.nutriexpress.demo.repository.PratoRepository;
import br.dtos.prato.PratoRequest;
import br.dtos.prato.PratoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PratoService {

    private final PratoRepository pratoRepository;
    private final CategoriaRepository categoriaRepository;

    public PratoService(PratoRepository pratoRepository, CategoriaRepository categoriaRepository) {
        this.pratoRepository = pratoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public PratoResponse criarPrato(PratoRequest request) {
        if (pratoRepository.existsByNome(request.getNome())) {
            throw new RuntimeException("Prato já existe");
        }
        return toResponse(pratoRepository.save(toEntity(request)));
    }

    public PratoResponse buscarPratoPorId(Long id) {
        return toResponse(findPrato(id));
    }

    public List<PratoResponse> listarTodosPratos() {
        return pratoRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<PratoResponse> filtrarPratosPorCategoria(String categoriaNome) {
        List<Categoria> categorias = categoriaRepository.findAll().stream()
                .filter(categoria -> categoria.getNome() != null && categoria.getNome().equalsIgnoreCase(categoriaNome.trim()))
                .toList();

        if (categorias.isEmpty()) {
            return List.of();
        }

        List<Long> categoriaIds = categorias.stream().map(Categoria::getId).toList();

        return pratoRepository.findAll().stream()
                .filter(prato -> prato.getCategoriaId() != null && categoriaIds.contains(prato.getCategoriaId()))
                .map(this::toResponse)
                .toList();
    }

    public List<PratoResponse> filtrarPratosPorCalorias(Integer caloriasMin, Integer caloriasMax) {
        return pratoRepository.findAll().stream()
                .filter(prato -> caloriasMin == null || prato.getCalorias() >= caloriasMin)
                .filter(prato -> caloriasMax == null || prato.getCalorias() <= caloriasMax)
                .map(this::toResponse)
                .toList();
    }

    public PratoResponse atualizarPrato(Long id, PratoRequest request) {
        Prato prato = findPrato(id);
        prato.setNome(request.getNome());
        prato.setDescricao(request.getDescricao());
        prato.setPreco(request.getPreco());
        prato.setCalorias(request.getCalorias());
        prato.setDisponivel(request.getDisponivel());
        prato.setCategoriaId(request.getCategoriaId());
        return toResponse(pratoRepository.save(prato));
    }

    public PratoResponse atualizarValorPrato(Long id, Double preco) {
        Prato prato = findPrato(id);
        prato.setPreco(preco);
        return toResponse(pratoRepository.save(prato));
    }

    public void deletarPrato(Long id) {
        if (!pratoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Prato não encontrado");
        }
        pratoRepository.deleteById(id);
    }

    private Prato findPrato(Long id) {
        return pratoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prato não encontrado"));
    }

    private Prato toEntity(PratoRequest request) {
        Prato prato = new Prato();
        prato.setNome(request.getNome());
        prato.setDescricao(request.getDescricao());
        prato.setPreco(request.getPreco());
        prato.setCalorias(request.getCalorias());
        prato.setDisponivel(request.getDisponivel());
        prato.setCategoriaId(request.getCategoriaId());
        return prato;
    }

    private PratoResponse toResponse(Prato prato) {
        PratoResponse response = new PratoResponse();
        response.setId(prato.getId());
        response.setNome(prato.getNome());
        response.setDescricao(prato.getDescricao());
        response.setPreco(prato.getPreco());
        response.setCalorias(prato.getCalorias());
        response.setDisponivel(prato.getDisponivel());
        response.setCategoriaId(prato.getCategoriaId());
        return response;
    }
}
