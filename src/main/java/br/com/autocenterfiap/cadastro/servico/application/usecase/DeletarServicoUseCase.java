package br.com.autocenterfiap.cadastro.servico.application.usecase;

import br.com.autocenterfiap.cadastro.servico.application.port.ServicoRepositoryPort;
import br.com.autocenterfiap.cadastro.servico.domain.entity.Servico;
import br.com.autocenterfiap.cadastro.servico.domain.exception.ServicoNaoEncontradoException;

public class DeletarServicoUseCase {

    private final ServicoRepositoryPort repositoryPort;

    public DeletarServicoUseCase(ServicoRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public void executar(Long id) {
        Servico servico = repositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ServicoNaoEncontradoException("Servico não encontrado de ID " + id));

        servico.inativar();
        repositoryPort.salvar(servico);
    }
}
