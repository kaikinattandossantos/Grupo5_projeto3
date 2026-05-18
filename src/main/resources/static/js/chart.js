let chartInstance = null;

// Registering chart data labels plugin (assumes it is loaded globally)
if (window.Chart && window.ChartDataLabels) {
    Chart.register(ChartDataLabels);
}

/**
 * Desenha e atualiza o gráfico comparativo de impacto ambiental físico vs digital.
 * @param {number} fisico Impacto em kg CO2 da transação física
 * @param {number} digital Impacto em kg CO2 da transação digital
 */
export function atualizarGrafico(fisico, digital) {
    const canvas = document.getElementById('impactChart');
    if (!canvas) return;

    const ctx = canvas.getContext('2d');

    if (chartInstance) {
        chartInstance.destroy();
    }

    const gradFisico = ctx.createLinearGradient(0, 0, 0, 250);
    gradFisico.addColorStop(0, '#E2001A');
    gradFisico.addColorStop(1, 'rgba(226, 0, 26, 0.1)');

    const gradDigital = ctx.createLinearGradient(0, 0, 0, 250);
    gradDigital.addColorStop(0, '#00A184');
    gradDigital.addColorStop(1, 'rgba(0, 161, 132, 0.1)');

    chartInstance = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Transação Física', 'Transação Digital'],
            datasets: [{
                data: [fisico, digital],
                backgroundColor: [gradFisico, gradDigital],
                borderColor: ['#E2001A', '#00A184'],
                borderWidth: 1.5,
                borderRadius: { topLeft: 8, topRight: 8 },
                barPercentage: 0.45
            }]
        },
        options: {
            devicePixelRatio: 2,
            responsive: true,
            maintainAspectRatio: false,
            layout: { padding: { top: 30 } },
            plugins: {
                legend: { display: false },
                title: {
                    display: true,
                    text: 'Comparativo de Emissão (kg CO₂)',
                    color: '#111F36',
                    font: { family: 'Outfit', size: 16, weight: '700' },
                    padding: { bottom: 5 }
                },
                subtitle: {
                    display: true,
                    text: 'A via digital reduz o impacto ambiental drasticamente',
                    color: '#6D7787',
                    font: { family: 'Outfit', size: 13, style: 'italic' },
                    padding: { bottom: 20 }
                },
                datalabels: {
                    color: '#111F36',
                    anchor: 'end',
                    align: 'top',
                    formatter: (value) => value.toFixed(4) + ' kg',
                    font: { family: 'Outfit', weight: 'bold', size: 12 }
                }
            },
            scales: {
                y: {
                    grid: { color: 'rgba(225, 228, 235, 0.6)' },
                    ticks: { color: '#6D7787', font: { family: 'Outfit' } }
                },
                x: {
                    grid: { display: false },
                    ticks: { color: '#111F36', font: { family: 'Outfit', weight: '600', size: 12 } }
                }
            }
        }
    });
}
