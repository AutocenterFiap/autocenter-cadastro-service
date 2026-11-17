package br.com.autocenterfiap.cadastro.servico.adapter.mapper;

import java.util.Objects;

import br.com.autocenterfiap.cadastro.servico.adapter.in.dto.ServicoRequestDTO;
import br.com.autocenterfiap.cadastro.servico.adapter.in.dto.ServicoResponseDTO;
import br.com.autocenterfiap.cadastro.servico.application.dto.AtualizarServicoInput;
import br.com.autocenterfiap.cadastro.servico.application.dto.CriarServicoInput;
import br.com.autocenterfiap.cadastro.servico.application.dto.ServicoOutput;

public class ServicoAdapterMapper {

    public static CriarServicoInput criarServicoRequestToCriarServicoInput(ServicoRequestDTO request) {
        Objects.requireNonNull(request, "request");

        return CriarServicoInput.builder()
                .descricao(request.getDescricao())
                .status(request.getStatus())
                .valor(request.getValor())
                .build();
    }

    public static AtualizarServicoInput atualizarServicoRequestToAtualizarServicoInput(ServicoRequestDTO request) {
        Objects.requireNonNull(request, "request");

        return AtualizarServicoInput.builder()
                .descricao(request.getDescricao())
                .status(request.getStatus())
                .valor(request.getValor())
                .build();
    }

    public static ServicoResponseDTO servicoOutputToServicoResponse(ServicoOutput output) {
        if (output == null) return null;

        return ServicoResponseDTO.builder()
                .id(output.getId())
                .descricao(output.getDescricao())
                .status(output.getStatus())
                .valor(output.getValor())
                .build();
    }
}
