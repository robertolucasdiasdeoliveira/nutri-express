package br.com.nutriexpress.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.dtos.cliente.ClienteRequestDTO;
import br.dtos.endereco.EnderecoRequestDTO;
import br.dtos.prato.PratoRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RequestValidationTest {
    @Autowired
    private Validator validator;

    @Value("${local.server.port}")
    private int port;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Test
    void invalidDishPostAndPutReturnBadRequestWithFieldMessages() throws Exception {
        String requestBody = """
                {"nome":"A","descricao":"%s","preco":-1,"calorias":5001}
                """.formatted("x".repeat(501));

        HttpResponse<String> postResponse = sendRequest("POST", "/pratos", requestBody);
        HttpResponse<String> putResponse = sendRequest("PUT", "/pratos/1", requestBody);

        assertEquals(400, postResponse.statusCode());
        assertEquals(400, putResponse.statusCode());
        assertTrue(postResponse.body().contains("nome"));
        assertTrue(postResponse.body().contains("descricao"));
        assertTrue(postResponse.body().contains("preco"));
        assertTrue(postResponse.body().contains("calorias"));
        assertTrue(postResponse.body().contains("categoriaId"));
    }

    @Test
    void addressRequestRejectsInvalidCepAndState() {
        EnderecoRequestDTO request = new EnderecoRequestDTO();
        request.setCep("12345678");
        request.setRua("Rua A");
        request.setBairro("Centro");
        request.setCidade("São Paulo");
        request.setEstado("SPX");

        Set<ConstraintViolation<EnderecoRequestDTO>> violations = validator.validate(request);

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("cep")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("estado")));
    }

    @Test
    void clientRequestRejectsInvalidEmailPhoneAndBirthDate() {
        ClienteRequestDTO request = new ClienteRequestDTO();
        request.setNomeCompleto("Maria Silva");
        request.setEmail("email-invalido");
        request.setTelefone("123");
        request.setDataNascimento(java.time.LocalDate.now().plusDays(1));

        Set<ConstraintViolation<ClienteRequestDTO>> violations = validator.validate(request);

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("telefone")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dataNascimento")));
    }

    @Test
    void dishAcceptsBoundaryValues() {
        PratoRequest request = new PratoRequest();
        request.setNome("AB");
        request.setDescricao("x".repeat(500));
        request.setPreco(0.01);
        request.setCalorias(0);
        request.setCategoriaId(1L);

        assertTrue(validator.validate(request).isEmpty());
    }

    private HttpResponse<String> sendRequest(String method, String path, String body) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + path))
                .header("Content-Type", "application/json")
                .method(method, HttpRequest.BodyPublishers.ofString(body))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}