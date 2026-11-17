package br.com.autocenterfiap.cadastro.comum;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import br.com.autocenterfiap.cadastro.cliente.adapter.mapper.ClienteAdapterMapper;
import br.com.autocenterfiap.cadastro.cliente.application.mapper.ClienteApplicationMapper;
import br.com.autocenterfiap.cadastro.produto.adapter.mapper.ProdutoAdapterMapper;
import br.com.autocenterfiap.cadastro.produto.application.mapper.ProdutoApplicationMapper;
import br.com.autocenterfiap.cadastro.servico.adapter.mapper.ServicoAdapterMapper;
import br.com.autocenterfiap.cadastro.servico.application.mapper.ServicoApplicationMapper;
import br.com.autocenterfiap.cadastro.veiculo.adapter.mapper.VeiculoAdapterMapper;
import br.com.autocenterfiap.cadastro.veiculo.application.mapper.VeiculoApplicationMapper;
import org.junit.jupiter.api.Test;

/** Mapeadores de entrada recusam null: um request ausente e erro de programacao, nao um input vazio. */
class MapeadoresEntradaNulaTest {

	@Test
	void mapeadoresDoAdapterRecusamRequestNulo() {
		assertThatNullPointerException().isThrownBy(() -> ClienteAdapterMapper.criarClienteRequestToCriarClienteInput(null));
		assertThatNullPointerException().isThrownBy(() -> ClienteAdapterMapper.atualizarClienteRequestToAtualizarClienteInput(null));
		assertThatNullPointerException().isThrownBy(() -> VeiculoAdapterMapper.criarVeiculoRequestToCriarVeiculoInput(null));
		assertThatNullPointerException().isThrownBy(() -> VeiculoAdapterMapper.atualizarVeiculoRequestToAtualizarVeiculoInput(null));
		assertThatNullPointerException().isThrownBy(() -> ServicoAdapterMapper.criarServicoRequestToCriarServicoInput(null));
		assertThatNullPointerException().isThrownBy(() -> ServicoAdapterMapper.atualizarServicoRequestToAtualizarServicoInput(null));
		assertThatNullPointerException().isThrownBy(() -> ProdutoAdapterMapper.toCriarInput(null));
		assertThatNullPointerException().isThrownBy(() -> ProdutoAdapterMapper.toAtualizarInput(null));
		assertThatNullPointerException().isThrownBy(() -> ProdutoAdapterMapper.toMovimentacaoInput(null));
	}

	@Test
	void mapeadoresDaAplicacaoRecusamInputNulo() {
		assertThatNullPointerException().isThrownBy(() -> ClienteApplicationMapper.toEntity(null));
		assertThatNullPointerException().isThrownBy(() -> VeiculoApplicationMapper.toEntity(null));
		assertThatNullPointerException().isThrownBy(() -> ServicoApplicationMapper.toEntity(null));
		assertThatNullPointerException().isThrownBy(() -> ProdutoApplicationMapper.toEntity(null));
	}
}
