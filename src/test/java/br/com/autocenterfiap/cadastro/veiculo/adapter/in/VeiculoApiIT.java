package br.com.autocenterfiap.cadastro.veiculo.adapter.in;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.autocenterfiap.cadastro.ContainersConfig;
import br.com.autocenterfiap.cadastro.TokensTeste;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = "sistema.seguranca.chave.secreta=" + TokensTeste.SEGREDO)
@AutoConfigureMockMvc
@Import(ContainersConfig.class)
class VeiculoApiIT {

    private static final String AUTH = "Authorization";

    @Autowired
    private MockMvc mvc;

    @Test
    void buscarPorIdDoSeed() throws Exception {
        mvc.perform(get("/v1/veiculos/1").header(AUTH, TokensTeste.interno()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.placa", is("ABC1D23")));
    }

    @Test
    void buscarPorPlaca() throws Exception {
        mvc.perform(get("/v1/veiculos/placa/ABC1D23").header(AUTH, TokensTeste.interno()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void placaDuplicadaRetornaConflito() throws Exception {
        mvc.perform(post("/v1/veiculos").header(AUTH, TokensTeste.interno()).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"placa":"ABC1D23","marca":"Fiat","modelo":"Uno",
                                 "tipoCombustivel":"FLEX","categoriaVeiculo":"CARRO"}"""))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.caminho", is("/v1/veiculos")));
    }

    @Test
    void inexistenteRetorna404() throws Exception {
        mvc.perform(get("/v1/veiculos/999999").header(AUTH, TokensTeste.interno()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").isNotEmpty());
    }
}
