package br.com.greenpayimpact.calculadora.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "resultados_calculo")
public class ResultadoCalculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long qtdTransacoes;

    @Column(nullable = false, precision = 15, scale = 5)
    private BigDecimal impactoFisico;

    @Column(nullable = false, precision = 15, scale = 5)
    private BigDecimal impactoDigital;

    @Column(nullable = false, precision = 15, scale = 5)
    private BigDecimal co2Evitado;

    @Column(nullable = false)
    private Double arvoresEquivalentes;

    @Column(nullable = false)
    private Double kmEvitados;

    @Column(nullable = false)
    private Integer garrafasPetEvitadas;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCalculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fator_fisico_id", nullable = false)
    private FatorEmissao fatorFisico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fator_digital_id", nullable = false)
    private FatorEmissao fatorDigital;

    /* Permite o vínculo opcional com a Empresa. 
       Se for um cálculo anónimo (apenas transações), este campo fica a null no banco.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = true)
    private Empresa empresa;

    public ResultadoCalculo() {
        this.dataCalculo = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getQtdTransacoes() { return qtdTransacoes; }
    public void setQtdTransacoes(Long qtdTransacoes) { this.qtdTransacoes = qtdTransacoes; }

    public BigDecimal getImpactoFisico() { return impactoFisico; }
    public void setImpactoFisico(BigDecimal impactoFisico) { this.impactoFisico = impactoFisico; }

    public BigDecimal getImpactoDigital() { return impactoDigital; }
    public void setImpactoDigital(BigDecimal impactoDigital) { this.impactoDigital = impactoDigital; }

    public BigDecimal getCo2Evitado() { return co2Evitado; }
    public void setCo2Evitado(BigDecimal co2Evitado) { this.co2Evitado = co2Evitado; }

    public Double getArvoresEquivalentes() { return arvoresEquivalentes; }
    public void setArvoresEquivalentes(Double arvoresEquivalentes) { this.arvoresEquivalentes = arvoresEquivalentes; }

    public Double getKmEvitados() { return kmEvitados; }
    public void setKmEvitados(Double kmEvitados) { this.kmEvitados = kmEvitados; }

    public Integer getGarrafasPetEvitadas() { return garrafasPetEvitadas; }
    public void setGarrafasPetEvitadas(Integer garrafasPetEvitadas) { this.garrafasPetEvitadas = garrafasPetEvitadas; }

    public LocalDateTime getDataCalculo() { return dataCalculo; }
    public void setDataCalculo(LocalDateTime dataCalculo) { this.dataCalculo = dataCalculo; }

    public FatorEmissao getFatorFisico() { return fatorFisico; }
    public void setFatorFisico(FatorEmissao fatorFisico) { this.fatorFisico = fatorFisico; }

    public FatorEmissao getFatorDigital() { return fatorDigital; }
    public void setFatorDigital(FatorEmissao fatorDigital) { this.fatorDigital = fatorDigital; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
}