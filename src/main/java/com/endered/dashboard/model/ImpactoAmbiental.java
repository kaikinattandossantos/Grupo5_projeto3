package com.endered.dashboard.model;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImpactoAmbiental {

    // Constantes baseadas em dados reais de produção de cartões PVC
    // Cada cartão físico PVC emite ~150g de CO2 na produção
    private static final double CO2_POR_CARTAO_KG = 0.150;
    // Cada cartão usa ~5g de PVC plástico
    private static final double PLASTICO_POR_CARTAO_G = 5.0;
    // Água utilizada na fabricação: ~1.5 litros por cartão
    private static final double AGUA_POR_CARTAO_L = 1.5;
    // Energia: ~0.08 kWh por cartão
    private static final double ENERGIA_POR_CARTAO_KWH = 0.08;
    // Uma árvore absorve ~21.7kg de CO2 por ano
    private static final double CO2_POR_ARVORE_ANO_KG = 21.7;

    private long totalCartoesSolicitados;
    private long totalCartoesAtivos;

    // Impactos calculados
    private double co2NaoEmitidoKg;
    private double plasticoNaoGeradoKg;
    private double aguaEconomizadaL;
    private double energiaEconomizadaKWh;
    private double arvoresEquivalentes;
    private double kmDrivenEquivalent;

    public static ImpactoAmbiental calcular(long totalCartoes, long cartoesAtivos) {
        double co2 = totalCartoes * CO2_POR_CARTAO_KG;
        double plastico = (totalCartoes * PLASTICO_POR_CARTAO_G) / 1000.0;
        double agua = totalCartoes * AGUA_POR_CARTAO_L;
        double energia = totalCartoes * ENERGIA_POR_CARTAO_KWH;
        double arvores = co2 / CO2_POR_ARVORE_ANO_KG;
        // Carro médio emite ~120g CO2/km
        double km = (co2 * 1000) / 120.0;

        return ImpactoAmbiental.builder()
                .totalCartoesSolicitados(totalCartoes)
                .totalCartoesAtivos(cartoesAtivos)
                .co2NaoEmitidoKg(Math.round(co2 * 100.0) / 100.0)
                .plasticoNaoGeradoKg(Math.round(plastico * 1000.0) / 1000.0)
                .aguaEconomizadaL(Math.round(agua * 100.0) / 100.0)
                .energiaEconomizadaKWh(Math.round(energia * 100.0) / 100.0)
                .arvoresEquivalentes(Math.round(arvores * 100.0) / 100.0)
                .kmDrivenEquivalent(Math.round(km * 100.0) / 100.0)
                .build();
    }
}
