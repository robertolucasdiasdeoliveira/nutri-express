package br.dtos.prato;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;




@Getter
@Setter
public class PratoRequest {
    @NotBlank(message = "O nome do prato é obrigatório")
    @Size(min = 2, max = 100, message = "O nome do prato deve ter entre 2 e 100 caracteres")
    private String nome;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser positivo")
    private Double preco;

    @NotNull(message = "As calorias são obrigatórias")
    @Min(value = 0, message = "As calorias devem ser no mínimo 0")
    @Max(value = 5000, message = "As calorias devem ser no máximo 5000")
    private Integer calorias;

    private Boolean disponivel;

    @NotNull(message = "A categoria é obrigatória")
    private Long categoriaId;
}
