package br.com.autocenterfiap.cadastro.servico.adapter.in;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.autocenterfiap.cadastro.ContainersConfig;
import br.com.autocenterfiap.cadastro.TokensTeste;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class ServicoApiIT {

    private static final String AUTH = "Authorization";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    private long criar(String descricao) throws Exception {
        var corpo = mvc.perform(post("/v1/servicos").header(AUTH, TokensTeste.interno())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descricao\":\"" + descricao + "\",\"status\":\"ATIVO\",\"valor\":120.00}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        JsonNode no = mapper.readTree(corpo);
        return no.get("id").asLong();
    }

    @Test
    void criarEBuscarPorId() throws Exception {
        long id = criar("Diagnostico eletronico");

        mvc.perform(get("/v1/servicos/{id}", id).header(AUTH, TokensTeste.interno()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) id)))
                .andExpect(jsonPath("$.descricao", is("Diagnostico eletronico")))
                .andExpect(jsonPath("$.status", is("ATIVO")))
                .andExpect(jsonPath("$.valor", is(120.0)));
    }

    @Test
    void buscarInexistenteRetorna404() throws Exception {
        mvc.perform(get("/v1/servicos/{id}", 999_999).header(AUTH, TokensTeste.interno()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.caminho", is("/v1/servicos/999999")));
    }

    @Test
    void deletarInativaEMantemConsultavel() throws Exception {
        long id = criar("Polimento");

        mvc.perform(delete("/v1/servicos/{id}", id).header(AUTH, TokensTeste.interno()))
                .andExpect(status().isNoContent());

        mvc.perform(get("/v1/servicos/{id}", id).header(AUTH, TokensTeste.interno()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("INATIVO")));
    }

    @Test
    void seedTemServicos() throws Exception {
        mvc.perform(get("/v1/servicos/1").header(AUTH, TokensTeste.interno()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao", is("Troca de Óleo e Filtro")));
        mvc.perform(get("/v1/servicos").header(AUTH, TokensTeste.interno()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", greaterThan(0)));
    }
}
