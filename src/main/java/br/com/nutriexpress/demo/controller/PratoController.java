package br.com.nutriexpress.demo.controller;

import br.com.nutriexpress.demo.service.PratoService;
import br.dtos.prato.PratoRequest;
import br.dtos.prato.PratoResponse;
import br.dtos.prato.ValorPratoRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pratos")
public class PratoController {

    private final PratoService pratoService;

    public PratoController(PratoService pratoService) {
        this.pratoService = pratoService;
    }

    @PostMapping
    public ResponseEntity<PratoResponse> criarPrato(@Valid @RequestBody PratoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pratoService.criarPrato(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PratoResponse> buscarPratoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pratoService.buscarPratoPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<PratoResponse>> listarPratos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Integer max) {

        if (categoria != null && !categoria.isBlank()) {
            return ResponseEntity.ok(pratoService.filtrarPratosPorCategoria(categoria));
        }

        if (max != null) {
            return ResponseEntity.ok(pratoService.filtrarPratosPorCalorias(null, max));
        }

        return ResponseEntity.ok(pratoService.listarTodosPratos());
    }

    @GetMapping("/calorias")
    public ResponseEntity<List<PratoResponse>> filtrarPratosPorCalorias(
            @RequestParam(required = false) Integer min,
            @RequestParam(required = false) Integer max) {
        return ResponseEntity.ok(pratoService.filtrarPratosPorCalorias(min, max));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PratoResponse> atualizarPrato(
            @PathVariable Long id, @Valid @RequestBody PratoRequest request) {
        return ResponseEntity.ok(pratoService.atualizarPrato(id, request));
    }

    @PatchMapping("/{id}/valor")
    public ResponseEntity<PratoResponse> atualizarValorPrato(
            @PathVariable Long id, @Valid @RequestBody ValorPratoRequest request) {
        return ResponseEntity.ok(pratoService.atualizarValorPrato(id, request.getValor()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPrato(@PathVariable Long id) {
        pratoService.deletarPrato(id);
        return ResponseEntity.noContent().build();
    }
}
