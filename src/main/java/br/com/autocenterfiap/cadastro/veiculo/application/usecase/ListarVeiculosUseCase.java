package br.com.autocenterfiap.cadastro.veiculo.application.usecase;

import br.com.autocenterfiap.cadastro.veiculo.application.dto.PageResult;
import br.com.autocenterfiap.cadastro.veiculo.application.dto.PaginationRequest;
import br.com.autocenterfiap.cadastro.veiculo.application.dto.VeiculoOutput;
import br.com.autocenterfiap.cadastro.veiculo.application.mapper.VeiculoApplicationMapper;
import br.com.autocenterfiap.cadastro.veiculo.application.port.VeiculoRepositoryPort;

public class ListarVeiculosUseCase {

    private final VeiculoRepositoryPort veiculoRepositoryPort;

    public ListarVeiculosUseCase(VeiculoRepositoryPort veiculoRepositoryPort) {
        this.veiculoRepositoryPort = veiculoRepositoryPort;
    }

    public PageResult<VeiculoOutput> executar(PaginationRequest pagination) {
        return veiculoRepositoryPort.listarTodos(pagination)
            .map(VeiculoApplicationMapper::toOutput);
    }
}
