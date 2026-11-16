package br.com.autocenterfiap.cadastro.produto.application.usecase;

import br.com.autocenterfiap.cadastro.produto.application.dto.MovimentacaoEstoqueInput;
import br.com.autocenterfiap.cadastro.produto.application.dto.ProdutoOutput;
import br.com.autocenterfiap.cadastro.produto.application.mapper.ProdutoApplicationMapper;
import br.com.autocenterfiap.cadastro.produto.application.port.ProdutoRepositoryPort;
import br.com.autocenterfiap.cadastro.produto.domain.entity.Produto;
import br.com.autocenterfiap.cadastro.produto.domain.exception.ProdutoNaoEncontradoException;

public class AdicionarEstoqueUseCase {

    private final ProdutoRepositoryPort repositoryPort;

    public AdicionarEstoqueUseCase(ProdutoRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public ProdutoOutput executar(Long id, MovimentacaoEstoqueInput input) {
        Produto produto = repositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        produto.incrementarEstoque(input.getQuantidade());
        Produto produtoSalvo = repositoryPort.salvar(produto);
        return ProdutoApplicationMapper.toOutput(produtoSalvo);
    }
}
