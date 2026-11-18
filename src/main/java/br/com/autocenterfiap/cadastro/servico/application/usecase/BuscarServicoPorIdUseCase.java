package br.com.autocenterfiap.cadastro.servico.application.usecase;

import br.com.autocenterfiap.cadastro.servico.application.dto.ServicoOutput;
import br.com.autocenterfiap.cadastro.servico.application.mapper.ServicoApplicationMapper;
import br.com.autocenterfiap.cadastro.servico.application.port.ServicoRepositoryPort;
import br.com.autocenterfiap.cadastro.servico.domain.entity.Servico;
import br.com.autocenterfiap.cadastro.servico.domain.exception.ServicoNaoEncontradoException;

public class BuscarServicoPorIdUseCase {

    private final ServicoRepositoryPort repositoryPort;

    public BuscarServicoPorIdUseCase(ServicoRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public ServicoOutput executar(Long id) {
        Servico servico = repositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ServicoNaoEncontradoException("Servico não encontrado de ID " + id));
        return ServicoApplicationMapper.toOutput(servico);
    }
}
