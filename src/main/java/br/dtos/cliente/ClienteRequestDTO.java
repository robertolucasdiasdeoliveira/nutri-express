package br.dtos.cliente;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteRequestDTO {
    @NotBlank(message = "O nome completo é obrigatório")
    private String nomeCompleto;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "O e-mail deve ter um formato válido")
    private String email;

    @NotBlank(message = "O telefone é obrigatório")
    @Pattern(
            regexp = "^(?:\\+55\\s?)?\\(?[1-9][0-9]\\)?\\s?9?[0-9]{4}-?[0-9]{4}$",
            message = "O telefone deve estar em um formato brasileiro válido")
    private String telefone;

    @NotNull(message = "A data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve estar no passado")
    private LocalDate dataNascimento;
}