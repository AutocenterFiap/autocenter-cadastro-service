package br.com.autocenterfiap.cadastro.cliente.adapter.in;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.autocenterfiap.cadastro.ContainersConfig;
import br.com.autocenterfiap.cadastro.TokensTeste;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest(properties = "sistema.seguranca.chave.secreta=" + TokensTeste.SEGREDO)
@AutoConfigureMockMvc
@Import(ContainersConfig.class)
class ClienteApiIT {

    private static final String AUTH = "Authorization";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    private ResultActions criar(String documento, String email) throws Exception {
        var corpo = """
                {"nome":"Ana Teste","tipoCliente":"PESSOA_FISICA","documento":"%s","email":"%s",
                 "telefone":"11987654321","endereco":{"cep":"01310100","logradouro":"Av Paulista","numero":"1000",
                 "cidade":"Sao Paulo","estado":"SP"}}""".formatted(documento, email);
        return mvc.perform(post("/v1/clientes").header(AUTH, TokensTeste.interno())
                .contentType(MediaType.APPLICATION_JSON).content(corpo));
    }

    @Test
    void buscarPorIdRetornaStatus() throws Exception {
        mvc.perform(get("/v1/clientes/1").header(AUTH, TokensTeste.interno()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("ATIVO")))
                .andExpect(jsonPath("$.email").isNotEmpty())
                .andExpect(jsonPath("$.telefone").isNotEmpty());
    }

    @Test
    void clienteInativoDoSeed() throws Exception {
        mvc.perform(get("/v1/clientes/documento/33366699957").header(AUTH, TokensTeste.interno()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("INATIVO")));
    }

    @Test
    void documentoInvalidoRetorna400() throws Exception {
        criar("11111111112", "invalido@teste.com").andExpect(status().isBadRequest());
    }

    @Test
    void emailDuplicadoRetornaConflito() throws Exception {
        criar("52998224725", "duplicado@teste.com").andExpect(status().isCreated());
        criar("16899535009", "duplicado@teste.com").andExpect(status().isConflict());
    }

    @Test
    void deletarRetorna204() throws Exception {
        var resposta = criar("39053344705", "apagar@teste.com").andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("ATIVO")))
                .andReturn().getResponse().getContentAsString();
        long id = mapper.readTree(resposta).get("id").asLong();

        mvc.perform(delete("/v1/clientes/{id}", id).header(AUTH, TokensTeste.interno()))
                .andExpect(status().isNoContent());
        mvc.perform(get("/v1/clientes/{id}", id).header(AUTH, TokensTeste.interno()))
                .andExpect(status().isNotFound());
    }
}
