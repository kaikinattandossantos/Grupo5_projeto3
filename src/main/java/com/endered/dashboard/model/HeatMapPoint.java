package com.endered.dashboard.model;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeatMapPoint {
    private String cidade;
    private String estado;
    private Double latitude;
    private Double longitude;
    private Long quantidade;
    private Double intensidade; // 0.0 a 1.0 normalizado
}
