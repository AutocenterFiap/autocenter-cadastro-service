package br.com.autocenterfiap.cadastro.servico.infrastructure.persistence.jpa.repository;

import br.com.autocenterfiap.cadastro.servico.domain.enums.StatusServico;
import br.com.autocenterfiap.cadastro.servico.infrastructure.persistence.jpa.entity.ServicoJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicoJpaRepository extends JpaRepository<ServicoJpaEntity, Long> {
    Page<ServicoJpaEntity> findAllByStatus(StatusServico status, Pageable pageable);
}
