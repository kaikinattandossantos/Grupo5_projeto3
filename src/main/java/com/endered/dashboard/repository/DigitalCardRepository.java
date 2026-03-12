package com.endered.dashboard.repository;

import com.endered.dashboard.model.DigitalCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DigitalCardRepository extends JpaRepository<DigitalCard, Long> {

    long countByStatus(DigitalCard.StatusSolicitacao status);

    boolean existsByEmail(String email);

    @Query("SELECT d.cidade, d.estado, d.latitude, d.longitude, COUNT(d) as qty " +
           "FROM DigitalCard d GROUP BY d.cidade, d.estado, d.latitude, d.longitude " +
           "ORDER BY qty DESC")
    List<Object[]> findHeatMapData();

    @Query("SELECT d.tipoCartao, COUNT(d) FROM DigitalCard d GROUP BY d.tipoCartao")
    List<Object[]> countByTipoCartao();

    @Query("SELECT MONTH(d.dataSolicitacao), COUNT(d) FROM DigitalCard d " +
           "WHERE YEAR(d.dataSolicitacao) = YEAR(CURRENT_DATE) " +
           "GROUP BY MONTH(d.dataSolicitacao) ORDER BY MONTH(d.dataSolicitacao)")
    List<Object[]> countByMesAno();

    List<DigitalCard> findTop10ByOrderByDataSolicitacaoDesc();
}
