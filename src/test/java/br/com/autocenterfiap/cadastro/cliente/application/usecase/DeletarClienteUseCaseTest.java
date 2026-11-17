package br.com.autocenterfiap.cadastro.cliente.application.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.autocenterfiap.cadastro.cliente.application.port.ClienteRepositoryPort;
import br.com.autocenterfiap.cadastro.cliente.domain.entity.Cliente;
import br.com.autocenterfiap.cadastro.cliente.domain.exception.ClienteNaoEncontradoException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeletarClienteUseCaseTest {

    @Mock
    private ClienteRepositoryPort repositorio;

    @InjectMocks
    private DeletarClienteUseCase deletar;

    @Test
    void deveDeletarSemChecarOrdemServico() {
        when(repositorio.buscarPorId(1L)).thenReturn(Optional.of(Mockito.mock(Cliente.class)));

        deletar.executar(1L);

        verify(repositorio).deletarPorId(1L);
    }

    @Test
    void deveLancarNaoEncontrado() {
        when(repositorio.buscarPorId(9L)).thenReturn(Optional.empty());

        assertThrows(ClienteNaoEncontradoException.class, () -> deletar.executar(9L));

        verify(repositorio, never()).deletarPorId(anyLong());
    }
}
