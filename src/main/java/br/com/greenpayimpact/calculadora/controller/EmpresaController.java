package br.com.greenpayimpact.calculadora.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import br.com.greenpayimpact.calculadora.dto.CalculoRequest;
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
            Map<String, Object> resposta = empresaService.processarCalculo(request);
            return ResponseEntity.ok(resposta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarEmpresas() {
        return ResponseEntity.ok(empresaService.listarHistoricoSimulacoes());
    }



    @GetMapping("/exportar-csv")
    public ResponseEntity<byte[]> exportarCsv() {
        try {
            String csvConteudo = empresaService.exportarHistoricoParaCsv();
            
            byte[] csvBytes = csvConteudo.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", "relatorio_esg_greenpay.csv");
            headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvBytes);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


}