package br.com.greenpayimpact.calculadora.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.greenpayimpact.calculadora.dto.CalculoResponse;
import br.com.greenpayimpact.calculadora.service.EmpresaService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CalculoController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping("/empresas/{id}/impacto")
    public ResponseEntity<?> calcularImpacto(@PathVariable Long id) {
        try {
            CalculoResponse response = empresaService.calcularImpactoPorId(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
}