package br.com.autocenterfiap.cadastro.veiculo.application.usecase;

import br.com.autocenterfiap.cadastro.veiculo.application.port.VeiculoRepositoryPort;
import br.com.autocenterfiap.cadastro.veiculo.domain.entity.Veiculo;
import br.com.autocenterfiap.cadastro.veiculo.domain.enums.CategoriaVeiculo;
import br.com.autocenterfiap.cadastro.veiculo.domain.enums.TipoCombustivel;
import br.com.autocenterfiap.cadastro.veiculo.domain.exception.VeiculoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeletarVeiculoUseCase - Testes Unitários")
class DeletarVeiculoUseCaseTest {

    @Mock
    private VeiculoRepositoryPort veiculoRepositoryPort;

    @InjectMocks
    private DeletarVeiculoUseCase deletarVeiculoUseCase;

    private Veiculo veiculoExistente;

    @BeforeEach
    void setUp() {
        veiculoExistente = Veiculo.builder()
                .id(1L)
                .placa("ABC1D23")
                .marca("Ford")
                .modelo("Bronco")
                .tipoCombustivel(TipoCombustivel.DIESEL)
                .categoriaVeiculo(CategoriaVeiculo.CARRO)
                .build();
    }

    @Test
    @DisplayName("Deve deletar veículo sem checar ordem de serviço (a OS pertence a outro serviço)")
    void deveDeletarSemChecarOrdemServico() {
        when(veiculoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(veiculoExistente));

        assertDoesNotThrow(() -> deletarVeiculoUseCase.executar(1L));

        verify(veiculoRepositoryPort, times(1)).deletarPorId(1L);
    }

    @Test
    @DisplayName("Deve lançar VeiculoNaoEncontradoException quando ID do veículo não existir")
    void deveLancarVeiculoNaoEncontradoException() {
        when(veiculoRepositoryPort.buscarPorId(999L)).thenReturn(Optional.empty());

        assertThrows(VeiculoNaoEncontradoException.class, () -> deletarVeiculoUseCase.executar(999L));
        verify(veiculoRepositoryPort, never()).deletarPorId(anyLong());
    }
}
