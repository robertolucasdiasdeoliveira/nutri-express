package br.dtos.prato;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PratoResponse {
    private Long id;
    private String nome;
    private String descricao;
    private Double preco;
    private Integer calorias;
    private Boolean disponivel;
    private Long categoriaId;
}
