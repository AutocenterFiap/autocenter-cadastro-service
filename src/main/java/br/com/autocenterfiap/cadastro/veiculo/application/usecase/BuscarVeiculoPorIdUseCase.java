package br.com.autocenterfiap.cadastro.veiculo.application.usecase;

import br.com.autocenterfiap.cadastro.veiculo.application.dto.VeiculoOutput;
import br.com.autocenterfiap.cadastro.veiculo.application.mapper.VeiculoApplicationMapper;
import br.com.autocenterfiap.cadastro.veiculo.application.port.VeiculoRepositoryPort;
import br.com.autocenterfiap.cadastro.veiculo.domain.entity.Veiculo;
import br.com.autocenterfiap.cadastro.veiculo.domain.exception.VeiculoNaoEncontradoException;

public class BuscarVeiculoPorIdUseCase {

    private final VeiculoRepositoryPort veiculoRepositoryPort;

    public BuscarVeiculoPorIdUseCase(VeiculoRepositoryPort veiculoRepositoryPort) {
        this.veiculoRepositoryPort = veiculoRepositoryPort;
    }

    public VeiculoOutput executar(Long id) {
        Veiculo veiculo = veiculoRepositoryPort.buscarPorId(id)
            .orElseThrow(() -> new VeiculoNaoEncontradoException("Veiculo com ID " + id + " não existe!"));
        return VeiculoApplicationMapper.toOutput(veiculo);
    }
}
