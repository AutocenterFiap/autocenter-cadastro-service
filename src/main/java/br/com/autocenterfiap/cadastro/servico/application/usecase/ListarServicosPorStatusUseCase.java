package br.com.autocenterfiap.cadastro.servico.application.usecase;

import br.com.autocenterfiap.cadastro.comum.paginacao.PageResult;
import br.com.autocenterfiap.cadastro.comum.paginacao.PaginationRequest;
import br.com.autocenterfiap.cadastro.servico.application.dto.ServicoOutput;
import br.com.autocenterfiap.cadastro.servico.application.mapper.ServicoApplicationMapper;
import br.com.autocenterfiap.cadastro.servico.application.port.ServicoRepositoryPort;
import br.com.autocenterfiap.cadastro.servico.domain.enums.StatusServico;

public class ListarServicosPorStatusUseCase {

    private final ServicoRepositoryPort repositoryPort;

    public ListarServicosPorStatusUseCase(ServicoRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public PageResult<ServicoOutput> executar(StatusServico status, PaginationRequest pagination) {
        return repositoryPort.listarPorStatus(status, pagination)
                .map(ServicoApplicationMapper::toOutput);
    }
}
