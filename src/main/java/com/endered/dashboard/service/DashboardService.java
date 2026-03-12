package com.endered.dashboard.service;

import com.endered.dashboard.model.DigitalCard;
import com.endered.dashboard.model.HeatMapPoint;
import com.endered.dashboard.model.ImpactoAmbiental;
import com.endered.dashboard.repository.DigitalCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DigitalCardRepository repository;

    @Transactional
    public DigitalCard solicitarCartaoDigital(DigitalCard card) {
        if (repository.existsByEmail(card.getEmail())) {
            throw new IllegalArgumentException("Já existe uma solicitação com este e-mail.");
        }
        card.setStatus(DigitalCard.StatusSolicitacao.PENDENTE);
        DigitalCard saved = repository.save(card);

        // Simula aprovação automática após um breve delay (em produção seria assíncrono)
        saved.setStatus(DigitalCard.StatusSolicitacao.ATIVO);
        saved.setDataAprovacao(LocalDateTime.now());
        return repository.save(saved);
    }

    @Transactional(readOnly = true)
    public ImpactoAmbiental calcularImpactoAmbiental() {
        long total = repository.count();
        long ativos = repository.countByStatus(DigitalCard.StatusSolicitacao.ATIVO);
        return ImpactoAmbiental.calcular(total, ativos);
    }

    @Transactional(readOnly = true)
    public List<HeatMapPoint> gerarHeatMap() {
        List<Object[]> rawData = repository.findHeatMapData();
        if (rawData.isEmpty()) return Collections.emptyList();

        // Encontrar máximo para normalização
        long maxQty = rawData.stream()
                .mapToLong(row -> ((Number) row[4]).longValue())
                .max()
                .orElse(1L);

        return rawData.stream().map(row -> {
            long qty = ((Number) row[4]).longValue();
            return HeatMapPoint.builder()
                    .cidade((String) row[0])
                    .estado((String) row[1])
                    .latitude((Double) row[2])
                    .longitude((Double) row[3])
                    .quantidade(qty)
                    .intensidade((double) qty / maxQty)
                    .build();
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardData() {
        Map<String, Object> data = new LinkedHashMap<>();

        ImpactoAmbiental impacto = calcularImpactoAmbiental();
        data.put("impacto", impacto);
        data.put("heatmap", gerarHeatMap());
        data.put("recentesSolicitacoes", repository.findTop10ByOrderByDataSolicitacaoDesc());

        // Dados por tipo de cartão
        Map<String, Long> porTipo = new LinkedHashMap<>();
        repository.countByTipoCartao().forEach(row -> {
            porTipo.put(((DigitalCard.TipoCartao) row[0]).name(), ((Number) row[1]).longValue());
        });
        data.put("porTipoCartao", porTipo);

        // Dados mensais
        Map<String, Long> mensal = new LinkedHashMap<>();
        String[] meses = {"Jan","Fev","Mar","Abr","Mai","Jun","Jul","Ago","Set","Out","Nov","Dez"};
        repository.countByMesAno().forEach(row -> {
            int mes = ((Number) row[0]).intValue();
            mensal.put(meses[mes - 1], ((Number) row[1]).longValue());
        });
        data.put("dadosMensais", mensal);

        return data;
    }

    @Transactional(readOnly = true)
    public List<DigitalCard> listarTodos() {
        return repository.findAll();
    }
}
