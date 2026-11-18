package br.com.autocenterfiap.cadastro.produto.application.usecase;

import br.com.autocenterfiap.cadastro.produto.application.dto.ProdutoOutput;
import br.com.autocenterfiap.cadastro.produto.application.mapper.ProdutoApplicationMapper;
import br.com.autocenterfiap.cadastro.produto.application.port.ProdutoRepositoryPort;
import br.com.autocenterfiap.cadastro.produto.domain.entity.Produto;
import br.com.autocenterfiap.cadastro.produto.domain.exception.ProdutoNaoEncontradoException;

public class BuscarProdutoPorIdUseCase {

    private final ProdutoRepositoryPort repositoryPort;

    public BuscarProdutoPorIdUseCase(ProdutoRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public ProdutoOutput executar(Long id) {
        Produto produto = repositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
        return ProdutoApplicationMapper.toOutput(produto);
    }
}
