import { atualizarGrafico } from './chart.js';

/**
 * Exibe uma mensagem de erro na tela para alertar o usuário.
 * @param {string} mensagem Texto amigável de erro
 */
export function exibirErro(mensagem) {
    const display = document.getElementById('msgErro');
    if (display) {
        display.innerText = "⚠️ " + mensagem;
        display.style.display = "block";
        display.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    }
}

/**
 * Oculta a caixa de exibição de erro.
 */
export function ocultarErro() {
    const display = document.getElementById('msgErro');
    if (display) {
        display.style.display = "none";
    }
}

/**
 * Renderiza os dados obtidos da simulação ambiental no DOM.
 * @param {Object} data Objeto contendo os dados de impacto ambiental da simulação
 * @param {string} nomeEmpresa Razão Social da empresa simulada
 * @param {boolean} isAnonimo Flag que indica se a simulação foi não identificada
 */
export function exibirResultados(data, nomeEmpresa, isAnonimo) {
    const tituloRelatorio = document.getElementById('nomeEmpresaRelatorio');

    if (!tituloRelatorio) return;

    // B2B clean styling via CSS class manipulation
    if (isAnonimo || !nomeEmpresa) {
        tituloRelatorio.innerText = "Simulação Expressa (Não Identificada)";
        tituloRelatorio.classList.add('titulo-anonimo');
    } else {
        tituloRelatorio.innerText = nomeEmpresa;
        tituloRelatorio.classList.remove('titulo-anonimo');
    }

    const section = document.getElementById('resultsSection');
    if (section) {
        section.classList.remove('results-hidden');
        section.classList.add('results-visible');
    }

    const co2El = document.getElementById('co2EvitadoVal');
    if (co2El) co2El.innerText = data.co2Evitado.toFixed(2);

    const arvoresEl = document.getElementById('arvoresVal');
    if (arvoresEl) {
        const arvores = data.arvoresEquivalentes;
        arvoresEl.innerText = arvores % 1 === 0 ? arvores : arvores.toFixed(1);
    }

    const kmEl = document.getElementById('kmVal');
    if (kmEl) kmEl.innerText = Math.floor(data.kmEvitados).toLocaleString('pt-BR');

    const garrafasEl = document.getElementById('garrafasVal');
    if (garrafasEl) garrafasEl.innerText = Math.floor(data.garrafasPetEvitadas).toLocaleString('pt-BR');

    atualizarGrafico(data.impactoFisico, data.impactoDigital);

    setTimeout(() => {
        section.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }, 100);
}
