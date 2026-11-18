package br.com.autocenterfiap.cadastro.bdd;

import br.com.autocenterfiap.cadastro.ContainersConfig;
import br.com.autocenterfiap.cadastro.TokensTeste;
import br.com.autocenterfiap.cadastro.cliente.infrastructure.persistence.jpa.repository.ClienteJpaRepository;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CucumberContextConfiguration
@SpringBootTest(properties = "sistema.seguranca.chave.secreta=chave-de-teste")
@AutoConfigureMockMvc
@Import(ContainersConfig.class)
public class CadastroSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteJpaRepository clienteRepository;

    private MvcResult ultimoResultado;
    private Long ultimoIdCriado;

    @Before
    public void limparBase() {
        clienteRepository.deleteAll();
        clienteRepository.flush();
    }

    @Dado("que nenhum cliente esta cadastrado")
    public void queNenhumClienteEstaCadastrado() {
        clienteRepository.deleteAll();
        clienteRepository.flush();
    }

    @Quando("eu cadastrar um cliente PF com nome {string} e CPF {string}")
    public void euCadastrarUmClientePFComNomeECPF(String nome, String cpf) throws Exception {
        String payload = String.format("""
                {
                  "nome": "%s",
                  "tipoCliente": "PESSOA_FISICA",
                  "documento": "%s",
                  "email": "cliente.bdd@autocenter.com",
                  "telefone": "11987654321",
                  "dataNascimento": "1990-05-15",
                  "endereco": {
                    "cep": "01310100",
                    "logradouro": "Avenida Paulista",
                    "numero": "1578",
                    "bairro": "Bela Vista",
                    "cidade": "Sao Paulo",
                    "estado": "SP"
                  }
                }
                """, nome, cpf);

        ultimoResultado = mockMvc.perform(post("/v1/clientes")
                        .header("Authorization", TokensTeste.interno())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andReturn();

        String responseBody = ultimoResultado.getResponse().getContentAsString();
        ultimoIdCriado = com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
                .readTree(responseBody).get("id").asLong();
    }

    @Entao("o cliente deve ser criado com status {string}")
    public void oClienteDeveSerCriadoComStatus(String status) throws Exception {
        mockMvc.perform(get("/v1/clientes/{id}", ultimoIdCriado)
                        .header("Authorization", TokensTeste.interno())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(status)));
    }

    @E("deve ser possivel consulta-lo pelo ID retornado")
    public void deveSerPossivelConsultaLoPeloIDRetornado() throws Exception {
        mockMvc.perform(get("/v1/clientes/{id}", ultimoIdCriado)
                        .header("Authorization", TokensTeste.interno())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ultimoIdCriado.intValue())));
    }

    @Quando("eu consultar um cliente com ID {string}")
    public void euConsultarUmClienteComID(String id) throws Exception {
        ultimoResultado = mockMvc.perform(get("/v1/clientes/{id}", id)
                        .header("Authorization", TokensTeste.interno())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Entao("devo receber resposta com status {int}")
    public void devoReceberRespostaComStatus(int statusEsperado) {
        org.junit.jupiter.api.Assertions.assertEquals(statusEsperado,
                ultimoResultado.getResponse().getStatus());
    }
}
