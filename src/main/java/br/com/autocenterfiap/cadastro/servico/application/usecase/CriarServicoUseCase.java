package br.com.autocenterfiap.cadastro.servico.application.usecase;

import br.com.autocenterfiap.cadastro.servico.application.dto.CriarServicoInput;
import br.com.autocenterfiap.cadastro.servico.application.dto.ServicoOutput;
import br.com.autocenterfiap.cadastro.servico.application.mapper.ServicoApplicationMapper;
import br.com.autocenterfiap.cadastro.servico.application.port.ServicoRepositoryPort;
import br.com.autocenterfiap.cadastro.servico.domain.entity.Servico;

import java.time.LocalDateTime;

public class CriarServicoUseCase {

    private final ServicoRepositoryPort repositoryPort;

    public CriarServicoUseCase(ServicoRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public ServicoOutput executar(CriarServicoInput input) {
        Servico servico = ServicoApplicationMapper.toEntity(input);
        servico.validarDominio();

        servico.setDataCriacao(LocalDateTime.now());
        servico.setDataUltimaAtualizacao(LocalDateTime.now());

        Servico servicoSalvo = repositoryPort.salvar(servico);
        return ServicoApplicationMapper.toOutput(servicoSalvo);
    }
}
