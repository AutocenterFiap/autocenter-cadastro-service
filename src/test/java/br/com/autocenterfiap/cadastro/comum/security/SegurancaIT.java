package br.com.autocenterfiap.cadastro.comum.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.autocenterfiap.cadastro.ContainersConfig;
import br.com.autocenterfiap.cadastro.TokensTeste;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = "sistema.seguranca.chave.secreta=" + TokensTeste.SEGREDO)
@AutoConfigureMockMvc
@Import({ContainersConfig.class, SegurancaIT.RotaTeste.class})
class SegurancaIT {

    private static final String ROTA = "/v1/teste-seguranca";

    @Autowired
    private MockMvc mvc;

    @TestConfiguration
    @RestController
    static class RotaTeste {
        @GetMapping(ROTA)
        String ok() {
            return "ok";
        }
    }

    @Test
    void semTokenRetorna401() throws Exception {
        mvc.perform(get(ROTA)).andExpect(status().isUnauthorized()).andExpect(jsonPath("$.erro").value("nao_autenticado"));
    }

    @Test
    void tokenClienteRetorna403() throws Exception {
        mvc.perform(get(ROTA).header("Authorization", TokensTeste.cliente(1)))
                .andExpect(status().isForbidden()).andExpect(jsonPath("$.erro").value("acesso_negado"));
    }

    @Test
    void tokenInternoRetorna200() throws Exception {
        mvc.perform(get(ROTA).header("Authorization", TokensTeste.interno())).andExpect(status().isOk());
    }

    @Test
    void tokenExpiradoRetorna401() throws Exception {
        mvc.perform(get(ROTA).header("Authorization", TokensTeste.internoExpirado())).andExpect(status().isUnauthorized());
    }

    @Test
    void swaggerPublico() throws Exception {
        mvc.perform(get("/v3/api-docs")).andExpect(status().isOk());
    }

    @Test
    void healthPublico() throws Exception {
        mvc.perform(get("/actuator/health")).andExpect(status().isOk());
    }

    @Test
    void rotaForaDeV1Negada() throws Exception {
        mvc.perform(get("/qualquer").header("Authorization", TokensTeste.interno())).andExpect(status().isForbidden());
    }
}
