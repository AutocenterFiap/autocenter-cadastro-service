package br.com.autocenterfiap.cadastro.veiculo.application.usecase;

import br.com.autocenterfiap.cadastro.veiculo.application.dto.VeiculoOutput;
import br.com.autocenterfiap.cadastro.veiculo.application.mapper.VeiculoApplicationMapper;
import br.com.autocenterfiap.cadastro.veiculo.application.port.VeiculoRepositoryPort;
import br.com.autocenterfiap.cadastro.veiculo.domain.entity.Veiculo;
import br.com.autocenterfiap.cadastro.veiculo.domain.exception.VeiculoNaoEncontradoException;

public class BuscarVeiculoPorPlacaUseCase {

    private final VeiculoRepositoryPort veiculoRepositoryPort;

    public BuscarVeiculoPorPlacaUseCase(VeiculoRepositoryPort veiculoRepositoryPort) {
        this.veiculoRepositoryPort = veiculoRepositoryPort;
    }

    public VeiculoOutput executar(String placa) {
        Veiculo veiculo = veiculoRepositoryPort.buscarPorPlaca(placa)
            .orElseThrow(() -> new VeiculoNaoEncontradoException("Veiculo com Placa " + placa + " não existe!"));
        return VeiculoApplicationMapper.toOutput(veiculo);
    }
}
