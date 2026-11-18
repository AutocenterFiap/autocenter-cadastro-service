package br.com.autocenterfiap.cadastro.produto.application.usecase;

import br.com.autocenterfiap.cadastro.produto.application.dto.MovimentacaoEstoqueInput;
import br.com.autocenterfiap.cadastro.produto.application.dto.ProdutoOutput;
import br.com.autocenterfiap.cadastro.produto.application.mapper.ProdutoApplicationMapper;
import br.com.autocenterfiap.cadastro.produto.application.port.ProdutoRepositoryPort;
import br.com.autocenterfiap.cadastro.produto.domain.entity.Produto;
import br.com.autocenterfiap.cadastro.produto.domain.exception.ProdutoNaoEncontradoException;

public class RemoverEstoqueUseCase {

    private final ProdutoRepositoryPort repositoryPort;

    public RemoverEstoqueUseCase(ProdutoRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public ProdutoOutput executar(Long id, MovimentacaoEstoqueInput input) {
        Produto produto = repositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        produto.decrementarEstoque(input.getQuantidade());
        Produto produtoSalvo = repositoryPort.salvar(produto);
        return ProdutoApplicationMapper.toOutput(produtoSalvo);
    }
}
