package com.endered.dashboard.controller;

import com.endered.dashboard.model.DigitalCard;
import com.endered.dashboard.service.DashboardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService service;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        return ResponseEntity.ok(service.getDashboardData());
    }

    @GetMapping("/impacto")
    public ResponseEntity<?> getImpactoAmbiental() {
        return ResponseEntity.ok(service.calcularImpactoAmbiental());
    }

    @GetMapping("/heatmap")
    public ResponseEntity<?> getHeatMap() {
        return ResponseEntity.ok(service.gerarHeatMap());
    }

    @GetMapping("/cartoes")
    public ResponseEntity<?> listarCartoes() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PostMapping("/cartoes")
    public ResponseEntity<?> solicitarCartao(@Valid @RequestBody DigitalCard card) {
        try {
            DigitalCard saved = service.solicitarCartaoDigital(card);
            Map<String, Object> response = new HashMap<>();
            response.put("sucesso", true);
            response.put("mensagem", "🎉 Seu cartão digital foi criado com sucesso! Bem-vindo à era verde!");
            response.put("cartao", saved);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("sucesso", false);
            error.put("mensagem", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
