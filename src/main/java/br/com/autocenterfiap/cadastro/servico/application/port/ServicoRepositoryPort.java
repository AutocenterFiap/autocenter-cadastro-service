package br.com.autocenterfiap.cadastro.servico.application.port;

import br.com.autocenterfiap.cadastro.comum.paginacao.PageResult;
import br.com.autocenterfiap.cadastro.comum.paginacao.PaginationRequest;
import br.com.autocenterfiap.cadastro.servico.domain.entity.Servico;
import br.com.autocenterfiap.cadastro.servico.domain.enums.StatusServico;

import java.util.Optional;

public interface ServicoRepositoryPort {
    Optional<Servico> buscarPorId(Long id);
    PageResult<Servico> listarTodos(PaginationRequest pagination);
    PageResult<Servico> listarPorStatus(StatusServico status, PaginationRequest pagination);
    Servico salvar(Servico servico);
}
