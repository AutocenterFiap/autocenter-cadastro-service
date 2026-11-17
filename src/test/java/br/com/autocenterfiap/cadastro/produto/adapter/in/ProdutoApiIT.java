package br.com.autocenterfiap.cadastro.produto.adapter.in;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.autocenterfiap.cadastro.ContainersConfig;
import br.com.autocenterfiap.cadastro.TokensTeste;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
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
class ProdutoApiIT {

    private static final String AUTH = "Authorization";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    private long criar(int estoque, int minimo) throws Exception {
        var corpo = """
                {"nome":"Oleo 5W30","codigo":"%s","descricao":"Oleo sintetico","unidadeMedida":"LITER",
                 "precoUnitario":45.50,"quantidadeEstoque":%d,"estoqueMinimo":%d,"categoria":"Lubrificantes",
                 "tipo":"INSUMOS"}""".formatted(UUID.randomUUID().toString().substring(0, 12), estoque, minimo);
        var resposta = mvc.perform(post("/v1/produtos").header(AUTH, TokensTeste.interno())
                        .contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return mapper.readTree(resposta).get("id").asLong();
    }

    @Test
    void criarEBuscarPorId() throws Exception {
        long id = criar(10, 2);

        mvc.perform(get("/v1/produtos/{id}", id).header(AUTH, TokensTeste.interno()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Oleo 5W30")))
                .andExpect(jsonPath("$.precoUnitario", is(45.5)))
                .andExpect(jsonPath("$.ativo", is(true)));
    }

    @Test
    void buscarInexistenteRetorna404() throws Exception {
        mvc.perform(get("/v1/produtos/{id}", 999_999).header(AUTH, TokensTeste.interno()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    void deletarDesativa() throws Exception {
        long id = criar(10, 2);

        mvc.perform(delete("/v1/produtos/{id}", id).header(AUTH, TokensTeste.interno()))
                .andExpect(status().isNoContent());

        mvc.perform(get("/v1/produtos/{id}", id).header(AUTH, TokensTeste.interno()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativo", is(false)));
    }

    @Test
    void removerEstoqueAcimaDoSaldoRetornaErro() throws Exception {
        long id = criar(1, 0);

        mvc.perform(patch("/v1/produtos/{id}/estoque/remover", id).header(AUTH, TokensTeste.interno())
                        .contentType(MediaType.APPLICATION_JSON).content("{\"quantidade\":5,\"motivo\":\"uso\"}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void alertasListaEstoqueBaixo() throws Exception {
        long id = criar(1, 5);

        mvc.perform(get("/v1/produtos/estoque/alertas").header(AUTH, TokensTeste.interno()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", hasItem((int) id)));
    }
}
