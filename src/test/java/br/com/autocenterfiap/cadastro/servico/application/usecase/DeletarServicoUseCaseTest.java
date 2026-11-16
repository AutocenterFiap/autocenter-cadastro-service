package br.com.autocenterfiap.cadastro.servico.application.usecase;

import br.com.autocenterfiap.cadastro.servico.application.port.ServicoRepositoryPort;
import br.com.autocenterfiap.cadastro.servico.domain.entity.Servico;
import br.com.autocenterfiap.cadastro.servico.domain.enums.StatusServico;
import br.com.autocenterfiap.cadastro.servico.domain.exception.ServicoNaoEncontradoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeletarServicoUseCase - Testes Unitários")
class DeletarServicoUseCaseTest {

    @Mock
    private ServicoRepositoryPort repositoryPort;

    @InjectMocks
    private DeletarServicoUseCase deletarServicoUseCase;

    @Test
    @DisplayName("Deve inativar o serviço ao deletar, sem apagar o registro")
    void deveInativarServicoAoDeletar() {
        Servico servico = Servico.builder()
                .id(1L)
                .descricao("Troca de óleo")
                .status(StatusServico.ATIVO)
                .valor(BigDecimal.valueOf(100.00))
                .build();
        when(repositoryPort.buscarPorId(1L)).thenReturn(Optional.of(servico));

        deletarServicoUseCase.executar(1L);

        ArgumentCaptor<Servico> salvo = ArgumentCaptor.forClass(Servico.class);
        verify(repositoryPort).salvar(salvo.capture());
        assertEquals(StatusServico.INATIVO, salvo.getValue().getStatus());
        assertEquals(1L, salvo.getValue().getId());
    }

    @Test
    @DisplayName("Deve lançar ServicoNaoEncontradoException ao deletar serviço inexistente")
    void deveLancarNaoEncontradoAoDeletarInexistente() {
        when(repositoryPort.buscarPorId(999L)).thenReturn(Optional.empty());

        assertThrows(ServicoNaoEncontradoException.class, () -> deletarServicoUseCase.executar(999L));

        verify(repositoryPort, never()).salvar(any());
    }
}
