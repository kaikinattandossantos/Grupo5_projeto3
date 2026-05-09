package br.com.greenpayimpact.calculadora.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.greenpayimpact.calculadora.dto.CalculoRequest;
import br.com.greenpayimpact.calculadora.model.Empresa;
import br.com.greenpayimpact.calculadora.service.EmpresaService;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @PostMapping
public ResponseEntity<?> cadastrarEmpresa(@RequestBody @Valid CalculoRequest request, BindingResult result) {
    if (result.hasErrors()) {
        String mensagemErro = result.getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(Map.of("erro", mensagemErro));
    }
    
    try {
        Empresa empresaSalva = empresaService.salvarEmpresa(request);
        return ResponseEntity.ok(empresaSalva);
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
    }
}

    @GetMapping
    public ResponseEntity<List<Empresa>> listarEmpresas() {
        return ResponseEntity.ok(empresaService.listarTodas());
    }
}