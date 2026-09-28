package br.dtos.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoRequestDTO {
    @NotBlank(message = "O CEP é obrigatório")
    @Pattern(regexp = "^[0-9]{5}-[0-9]{3}$", message = "O CEP deve estar no formato 00000-000")
    private String cep;

    @NotBlank(message = "A rua é obrigatória")
    private String rua;

    @NotBlank(message = "O bairro é obrigatório")
    private String bairro;

    @NotBlank(message = "A cidade é obrigatória")
    private String cidade;

    @NotBlank(message = "O estado é obrigatório")
    @Size(min = 2, max = 2, message = "O estado deve ter exatamente 2 caracteres")
    @Pattern(regexp = "^[A-Za-z]{2}$", message = "O estado deve ser uma UF com duas letras")
    private String estado;
}