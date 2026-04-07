package br.com.greenpayimpact.calculadora.service;

import br.com.greenpayimpact.calculadora.dto.CalculoRequest;
import br.com.greenpayimpact.calculadora.model.Empresa;
import br.com.greenpayimpact.calculadora.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public void salvarEmpresa(CalculoRequest request) {
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial(request.getRazaoSocial());
        empresa.setCnpj(request.getCnpj());
        empresa.setEmail(request.getEmail());
        empresa.setQtdTransacoesAnuais(request.getTransacoes());
        
        empresaRepository.save(empresa);
    }
}