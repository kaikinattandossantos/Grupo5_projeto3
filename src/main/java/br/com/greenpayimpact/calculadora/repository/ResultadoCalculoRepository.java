package br.com.greenpayimpact.calculadora.repository;

import br.com.greenpayimpact.calculadora.model.ResultadoCalculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultadoCalculoRepository extends JpaRepository<ResultadoCalculo, Long> {
    
    // Permite listar no histórico apenas os cálculos que foram associados a empresas
    List<ResultadoCalculo> findByEmpresaIsNotNullOrderByDataCalculoDesc();
}