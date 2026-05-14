package br.com.greenpayimpact.calculadora.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CalculoRequest {
    
    private String nomeEmpresa;

    @Size(min = 14, max = 14, message = "O CNPJ deve ter exatamente 14 dígitos")
    private String cnpj;

    @Email(message = "Formato de e-mail inválido")
    private String email;

    @NotNull(message = "O volume de transações é obrigatório")
    @Min(value = 1, message = "O volume de transações deve ser pelo menos 1")
    private Long transacoes;

    public CalculoRequest() {}

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = (nomeEmpresa == null || nomeEmpresa.trim().isEmpty()) ? null : nomeEmpresa.trim();
    }

    /**
     * Alias de compatibilidade com o frontend atual (script.js), que envia a propriedade JSON como 'razaoSocial'.
     */
    public void setRazaoSocial(String razaoSocial) {
        setNomeEmpresa(razaoSocial);
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = (cnpj == null || cnpj.trim().isEmpty()) ? null : cnpj.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = (email == null || email.trim().isEmpty()) ? null : email.trim();
    }

    public Long getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(Long transacoes) {
        this.transacoes = transacoes;
    }
}