package br.com.autocenterfiap.cadastro.veiculo.infrastructure.config;

import br.com.autocenterfiap.cadastro.veiculo.application.port.VeiculoRepositoryPort;
import br.com.autocenterfiap.cadastro.veiculo.application.usecase.*;
import br.com.autocenterfiap.cadastro.veiculo.infrastructure.persistence.jpa.adapter.VeiculoRepositoryJpaAdapter;
import br.com.autocenterfiap.cadastro.veiculo.infrastructure.persistence.jpa.repository.VeiculoJpaRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VeiculoConfiguration {

    @Bean
    public VeiculoRepositoryPort veiculoRepositoryPort(
        VeiculoJpaRepository veiculoJpaRepository
    ) {
        return new VeiculoRepositoryJpaAdapter(veiculoJpaRepository);
    }

    @Bean
    public CriarVeiculoUseCase criarVeiculoUseCase(VeiculoRepositoryPort veiculoRepositoryPort) {
        return new CriarVeiculoUseCase(veiculoRepositoryPort);
    }

    @Bean
    public BuscarVeiculoPorIdUseCase buscarVeiculoPorIdUseCase(VeiculoRepositoryPort veiculoRepositoryPort) {
        return new BuscarVeiculoPorIdUseCase(veiculoRepositoryPort);
    }

    @Bean
    public BuscarVeiculoPorPlacaUseCase buscarVeiculoPorPlacaUseCase(VeiculoRepositoryPort veiculoRepositoryPort) {
        return new BuscarVeiculoPorPlacaUseCase(veiculoRepositoryPort);
    }

    @Bean
    public ListarVeiculosUseCase listarVeiculosUseCase(VeiculoRepositoryPort veiculoRepositoryPort) {
        return new ListarVeiculosUseCase(veiculoRepositoryPort);
    }

    @Bean
    public AtualizarVeiculoUseCase atualizarVeiculoUseCase(VeiculoRepositoryPort veiculoRepositoryPort) {
        return new AtualizarVeiculoUseCase(veiculoRepositoryPort);
    }

    @Bean
    public DeletarVeiculoUseCase deletarVeiculoUseCase(VeiculoRepositoryPort veiculoRepositoryPort) {
        return new DeletarVeiculoUseCase(veiculoRepositoryPort);
    }
}
