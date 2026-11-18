package br.com.autocenterfiap.cadastro.veiculo.application.usecase;

import br.com.autocenterfiap.cadastro.veiculo.application.port.VeiculoRepositoryPort;
import br.com.autocenterfiap.cadastro.veiculo.domain.exception.VeiculoNaoEncontradoException;

public class DeletarVeiculoUseCase {

    private final VeiculoRepositoryPort veiculoRepositoryPort;

    public DeletarVeiculoUseCase(VeiculoRepositoryPort veiculoRepositoryPort) {
        this.veiculoRepositoryPort = veiculoRepositoryPort;
    }

    public void executar(Long id) {
        // Verificar se existe
        veiculoRepositoryPort.buscarPorId(id)
            .orElseThrow(() -> new VeiculoNaoEncontradoException("Veiculo com ID " + id + " não existe!"));

        veiculoRepositoryPort.deletarPorId(id);
    }
}
