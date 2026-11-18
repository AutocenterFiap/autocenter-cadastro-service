package br.com.autocenterfiap.cadastro.servico.infrastructure.persistence.jpa.adapter;

import br.com.autocenterfiap.cadastro.comum.paginacao.PageResult;
import br.com.autocenterfiap.cadastro.comum.paginacao.PaginationRequest;
import br.com.autocenterfiap.cadastro.servico.application.port.ServicoRepositoryPort;
import br.com.autocenterfiap.cadastro.servico.domain.entity.Servico;
import br.com.autocenterfiap.cadastro.servico.domain.enums.StatusServico;
import br.com.autocenterfiap.cadastro.servico.infrastructure.persistence.jpa.entity.ServicoJpaEntity;
import br.com.autocenterfiap.cadastro.servico.infrastructure.persistence.jpa.mapper.ServicoJpaMapper;
import br.com.autocenterfiap.cadastro.servico.infrastructure.persistence.jpa.repository.ServicoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.Optional;

@RequiredArgsConstructor
public class ServicoRepositoryJpaAdapter implements ServicoRepositoryPort {

    private final ServicoJpaRepository servicoJpaRepository;

    @Override
    public Optional<Servico> buscarPorId(Long id) {
        return servicoJpaRepository.findById(id)
                .map(ServicoJpaMapper::toDomain);
    }

    @Override
    public PageResult<Servico> listarTodos(PaginationRequest pagination) {
        Sort.Direction direction = "ASC".equalsIgnoreCase(pagination.getSortDirection())
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        PageRequest pageRequest = PageRequest.of(
                pagination.getPageNumber(),
                pagination.getPageSize(),
                Sort.by(direction, pagination.getSortBy())
        );

        Page<ServicoJpaEntity> pageJpa = servicoJpaRepository.findAll(pageRequest);

        return new PageResult<>(
                pageJpa.getContent().stream()
                        .map(ServicoJpaMapper::toDomain)
                        .toList(),
                pageJpa.getNumber(),
                pageJpa.getSize(),
                pageJpa.getTotalElements()
        );
    }

    @Override
    public PageResult<Servico> listarPorStatus(StatusServico status, PaginationRequest pagination) {
        Sort.Direction direction = "ASC".equalsIgnoreCase(pagination.getSortDirection())
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        PageRequest pageRequest = PageRequest.of(
                pagination.getPageNumber(),
                pagination.getPageSize(),
                Sort.by(direction, pagination.getSortBy())
        );

        Page<ServicoJpaEntity> pageJpa = servicoJpaRepository.findAllByStatus(status, pageRequest);

        return new PageResult<>(
                pageJpa.getContent().stream()
                        .map(ServicoJpaMapper::toDomain)
                        .toList(),
                pageJpa.getNumber(),
                pageJpa.getSize(),
                pageJpa.getTotalElements()
        );
    }

    @Override
    public Servico salvar(Servico servico) {
        ServicoJpaEntity jpaEntity = ServicoJpaMapper.toJpa(servico);
        ServicoJpaEntity savedEntity = servicoJpaRepository.save(jpaEntity);
        return ServicoJpaMapper.toDomain(savedEntity);
    }

}
