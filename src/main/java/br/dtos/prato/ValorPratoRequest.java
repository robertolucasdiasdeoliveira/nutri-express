package br.dtos.prato;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValorPratoRequest {

    // Impede que o campo seja omitido ou enviado explicitamente como null.
    @NotNull(message = "O valor do prato é obrigatório")
    // Impede valores zero ou negativos.
    @Positive(message = "O valor do prato deve ser maior que zero")
    private Double valor;
}