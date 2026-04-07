package br.com.greenpayimpact.calculadora.controller;

import br.com.greenpayimpact.calculadora.dto.CalculoRequest;
import br.com.greenpayimpact.calculadora.service.CalculoService;
import br.com.greenpayimpact.calculadora.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CalculoController {

    @Autowired
    private CalculoService calculoService;

    @Autowired
    private EmpresaService empresaService;

    @PostMapping("/calcular-impacto")
    public ResponseEntity<?> executarCalculo(@RequestBody @Valid CalculoRequest request, BindingResult result) {
        
        if (result.hasErrors()) {
            String mensagemErro = result.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(Map.of("erro", mensagemErro));
        }
        empresaService.salvarEmpresa(request);
        
        Map<String, BigDecimal> calculo = calculoService.calcularImpacto(request.getTransacoes());        
        
        return ResponseEntity.ok(calculo);
    }
}