package br.com.autocenterfiap.cadastro.servico.controller;

import br.com.autocenterfiap.cadastro.servico.adapter.in.dto.ServicoRequestDTO;
import br.com.autocenterfiap.cadastro.servico.domain.enums.StatusServico;
import br.com.autocenterfiap.cadastro.servico.infrastructure.persistence.jpa.entity.ServicoJpaEntity;
import br.com.autocenterfiap.cadastro.servico.infrastructure.persistence.jpa.repository.ServicoJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.context.annotation.Import;
import br.com.autocenterfiap.cadastro.ContainersConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "sistema.seguranca.chave.secreta=chave-de-teste")
@AutoConfigureMockMvc
@Import(ContainersConfig.class)
@Transactional
@DisplayName("ServicoController - Testes de Integração")
class ServicoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ServicoJpaRepository servicoRepository;

    private ServicoJpaEntity servico;

    private ServicoRequestDTO servicoDto;

    @BeforeEach
    void setUp() {
        servicoRepository.deleteAll();
        servicoRepository.flush();
        
        servico = ServicoJpaEntity.builder()
                .descricao("Troca de óleo")
                .status(StatusServico.ATIVO)
                .valor(BigDecimal.valueOf(100))
                .build();

        servicoDto = ServicoRequestDTO.builder()
                .descricao("Troca de óleo")
                .status(StatusServico.ATIVO)
                .valor(BigDecimal.valueOf(100))
                .build();
    }

    @Test
    @WithMockUser(authorities = "ROLE_INTERNO")
    void deveListarTodosOsServicos() throws Exception {
        servicoRepository.save(servico);
        mockMvc.perform(get("/v1/servicos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].descricao", is("Troca de óleo")))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }

    @Test
    @WithMockUser(authorities = "ROLE_INTERNO")
    void deveRetornarListaVaziaQuandoNaoHouverServicos() throws Exception {
        mockMvc.perform(get("/v1/servicos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements", is(0)));
    }

    @Test
    @WithMockUser(authorities = "ROLE_INTERNO")
    void deveBuscarServicoPorIdComSucesso() throws Exception {
        ServicoJpaEntity servicoSalvo = servicoRepository.save(servico);
        mockMvc.perform(get("/v1/servicos/{id}", servicoSalvo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(servicoSalvo.getId().intValue())))
                .andExpect(jsonPath("$.descricao", is("Troca de óleo")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_INTERNO")
    void deveRetornar404AoBuscarServicoInexistentePorId() throws Exception {
        mockMvc.perform(get("/v1/servicos/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ROLE_INTERNO")
    void deveListarServicosPorStatusQuandoExistirem() throws Exception {
        ServicoJpaEntity outro = ServicoJpaEntity.builder()
                .descricao("Alinhamento")
                .status(StatusServico.INATIVO)
                .valor(BigDecimal.valueOf(120))
                .build();

        servicoRepository.save(servico);
        servicoRepository.save(outro);

        mockMvc.perform(get("/v1/servicos/status/{status}", StatusServico.ATIVO)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].descricao", is("Troca de óleo")))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }

    @Test
    @WithMockUser(authorities = "ROLE_INTERNO")
    void deveRetornarListaVaziaAoListarPorStatusQuandoNaoHouver() throws Exception {
        servicoRepository.save(servico);

        mockMvc.perform(get("/v1/servicos/status/{status}", StatusServico.INATIVO)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements", is(0)));
    }

    @Test
    @WithMockUser(authorities = "ROLE_INTERNO")
    void deveCriarServicoComSucesso() throws Exception {
        mockMvc.perform(post("/v1/servicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(servicoDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.descricao", is("Troca de óleo")))
                .andExpect(jsonPath("$.status", is("ATIVO")))
                .andExpect(jsonPath("$.valor", is(100)));
    }

    @Test
    @WithMockUser(authorities = "ROLE_INTERNO")
    void deveRetornar400AoCriarServicoComDadosInvalidos() throws Exception {
        ServicoRequestDTO servicoInvalido = ServicoRequestDTO.builder()
                .descricao("") // descrição vazia
                .status(StatusServico.ATIVO)
                .valor(BigDecimal.valueOf(100))
                .build();
        mockMvc.perform(post("/v1/servicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(servicoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "ROLE_INTERNO")
    void deveAtualizarServicoComSucesso() throws Exception {
        ServicoJpaEntity servicoSalvo = servicoRepository.save(servico);
        
        ServicoRequestDTO updateDto = ServicoRequestDTO.builder()
                .descricao("Troca de filtro")
                .status(StatusServico.ATIVO)
                .valor(BigDecimal.valueOf(100))
                .build();
                
        mockMvc.perform(put("/v1/servicos/{id}", servicoSalvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao", is("Troca de filtro")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_INTERNO")
    void deveRetornar404AoAtualizarServicoInexistente() throws Exception {
        mockMvc.perform(put("/v1/servicos/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(servicoDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ROLE_INTERNO")
    void deveInativarServicoAoDeletar() throws Exception {
        ServicoJpaEntity servicoSalvo = servicoRepository.save(servico);
        mockMvc.perform(delete("/v1/servicos/{id}", servicoSalvo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/v1/servicos/{id}", servicoSalvo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("INATIVO")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_INTERNO")
    void deveRetornar404AoDeletarServicoInexistente() throws Exception {
        mockMvc.perform(delete("/v1/servicos/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
