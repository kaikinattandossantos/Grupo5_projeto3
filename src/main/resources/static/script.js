Chart.register(ChartDataLabels);
let chartInstance = null;

async function realizarAnalise() {
    const nome = document.getElementById('razaoSocialInput').value.trim();
    const cnpj = document.getElementById('cnpjInput').value.trim();
    const email = document.getElementById('emailInput').value.trim();
    const volume = document.getElementById('transacoesInput').value;

    if (cnpj.length > 0 && cnpj.length !== 14) {
        alert("Se preenchido, o CNPJ precisa ter exatamente 14 números!");
        return;
    }

    if (!volume || parseInt(volume) < 1) {
        alert("Por favor, insira um volume válido de transações.");
        return;
    }

    const payload = {
        nomeEmpresa: nome || null,
        cnpj: cnpj || null,
        email: email || null,
        transacoes: parseInt(volume)
    };

    try {
        const responseCadastro = await fetch('http://localhost:8081/api/empresas', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (responseCadastro.ok) {
            const dadosProcessados = await responseCadastro.json();
            const idResultado = dadosProcessados.id;

            const responseCalculo = await fetch(`http://localhost:8081/api/empresas/${idResultado}/impacto`);

            if (responseCalculo.ok) {
                const dataCalculo = await responseCalculo.json();
                exibirResultados(dataCalculo, nome, dadosProcessados.anonimo);
                document.getElementById('msgErro').style.display = "none";
            } else {
                exibirErro("Erro ao recuperar a matemática do cálculo.");
            }
        } else {
            const erroBackend = await responseCadastro.json();
            exibirErro(erroBackend.erro || "Erro ao processar simulação.");
        }
    } catch (err) {
        alert("Servidor Offline ou Erro de Rede!");
    }
}

function exibirErro(mensagem) {
    const display = document.getElementById('msgErro');
    display.innerText = "⚠️ " + mensagem;
    display.style.display = "block";
}

function exibirResultados(data, nomeEmpresa, isAnonimo) {
    const tituloRelatorio = document.getElementById('nomeEmpresaRelatorio');
    
    // Tratamento B2B Profissional utilizando manipulação limpa de classes CSS
    if (isAnonimo || !nomeEmpresa) {
        tituloRelatorio.innerText = "Simulação Expressa (Não Identificada)";
        tituloRelatorio.classList.add('titulo-anonimo');
    } else {
        tituloRelatorio.innerText = nomeEmpresa;
        tituloRelatorio.classList.remove('titulo-anonimo');
    }
    
    const section = document.getElementById('resultsSection');
    section.classList.replace('results-hidden', 'results-visible');
    
    document.getElementById('co2EvitadoVal').innerText = data.co2Evitado.toFixed(2);
    
    const arvores = data.arvoresEquivalentes;
    document.getElementById('arvoresVal').innerText = arvores % 1 === 0 ? arvores : arvores.toFixed(1);
    
    document.getElementById('kmVal').innerText = Math.floor(data.kmEvitados).toLocaleString('pt-BR');
    document.getElementById('garrafasVal').innerText = Math.floor(data.garrafasPetEvitadas).toLocaleString('pt-BR');

    atualizarGrafico(data.impactoFisico, data.impactoDigital);
    section.scrollIntoView({ behavior: 'smooth' });
}

function atualizarGrafico(fisico, digital) {
    const ctx = document.getElementById('impactChart').getContext('2d');
    
    if (chartInstance) {
        chartInstance.destroy();
    }

    const gradFisico = ctx.createLinearGradient(0, 0, 0, 300);
    gradFisico.addColorStop(0, 'rgba(226, 0, 26, 1)');
    gradFisico.addColorStop(1, 'rgba(226, 0, 26, 0.1)');

    const gradDigital = ctx.createLinearGradient(0, 0, 0, 300);
    gradDigital.addColorStop(0, 'rgba(32, 201, 151, 1)');
    gradDigital.addColorStop(1, 'rgba(32, 201, 151, 0.1)');

    chartInstance = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Transação Física', 'Transação Digital'],
            datasets: [{
                data: [fisico, digital],
                backgroundColor: [gradFisico, gradDigital],
                borderColor: ['#E2001A', '#20c997'],
                borderWidth: 1,
                borderRadius: { topLeft: 8, topRight: 8 },
                barPercentage: 0.5
            }]
        },
        options: {
            devicePixelRatio: 3,
            responsive: true,
            maintainAspectRatio: false,
            layout: { padding: { top: 30 } },
            plugins: {
                legend: { display: false },
                title: {
                    display: true,
                    text: 'Comparativo de Emissão (kg CO₂)',
                    color: '#ffffff',
                    font: { size: 16, weight: 'bold' },
                    padding: { bottom: 5 }
                },
                subtitle: {
                    display: true,
                    text: 'A via digital reduz o impacto ambiental em quase 96%',
                    color: '#a0a0a0',
                    font: { size: 13, style: 'italic' },
                    padding: { bottom: 25 }
                },
                datalabels: {
                    color: '#ffffff',
                    anchor: 'end',
                    align: 'top',
                    formatter: (value) => value.toFixed(4) + ' kg',
                    font: { weight: 'bold', size: 13 }
                }
            },
            scales: {
                y: { grid: { color: 'rgba(255, 255, 255, 0.05)' }, ticks: { color: '#666' } },
                x: { grid: { display: false }, ticks: { color: '#ccc' } }
            }
        }
    });
}

function abrirModalHistorico() {
    document.getElementById('modalHistorico').style.display = "block";
    carregarHistorico();
}

function fecharModalHistorico() {
    document.getElementById('modalHistorico').style.display = "none";
}

window.onclick = function(event) {
    if (event.target === document.getElementById('modalHistorico')) fecharModalHistorico();
    if (event.target === document.getElementById('modalAdmin')) fecharModalAdmin();
}

async function carregarHistorico() {
    const corpo = document.getElementById('corpoHistorico');
    corpo.innerHTML = '<tr><td colspan="4" style="text-align: center; color: #888;">Carregando histórico...</td></tr>';

    try {
        const res = await fetch('http://localhost:8081/api/empresas');
        if (!res.ok) throw new Error("Falha na API");
        const simulacoes = await res.json();
        
        corpo.innerHTML = '';
        if (simulacoes.length === 0) {
            corpo.innerHTML = '<tr><td colspan="4" style="text-align: center; color: #888;">Nenhuma análise com identificação realizada ainda.</td></tr>';
            return;
        }

        simulacoes.forEach(sim => {
            const dataFormatada = new Date(sim.criadoEm).toLocaleDateString('pt-BR');
            const nomeExibicao = sim.razaoSocial || "Empresa " + sim.cnpj;
            const nomeCodificado = encodeURIComponent(nomeExibicao);

            corpo.innerHTML += `
                <tr>
                    <td>${nomeExibicao}</td>
                    <td>${sim.cnpj}</td>
                    <td>${dataFormatada}</td>
                    <td><button class="btn-view" onclick="revisualizar(${sim.id}, '${nomeCodificado}')">VER</button></td>
                </tr>
            `;
        });
    } catch (err) {
        corpo.innerHTML = '<tr><td colspan="4" style="text-align: center; color: #ff4444;">Erro ao carregar histórico.</td></tr>';
    }
}

async function revisualizar(idResultado, nomeCodificado) {
    try {
        const res = await fetch(`http://localhost:8081/api/empresas/${idResultado}/impacto`);
        if (!res.ok) throw new Error("Falha na simulação");
        
        const data = await res.json();
        fecharModalHistorico();
        exibirResultados(data, decodeURIComponent(nomeCodificado), false);
    } catch (err) {
        alert("Erro ao recuperar os dados dessa simulação congelada.");
    }
}

function gerarRelatorio() { window.print(); }

function abrirModalAdmin() {
    document.getElementById('modalAdmin').style.display = "block";
    carregarHistoricoFatores();
}
function fecharModalAdmin() { document.getElementById('modalAdmin').style.display = "none"; }

async function salvarNovoFator() {
    const tipo = document.getElementById('fatorTipo').value;
    const valor = parseFloat(document.getElementById('fatorValor').value);
    const fonte = document.getElementById('fatorFonte').value;

    if (!valor || !fonte) { alert("Preencha todos os campos do fator!"); return; }
    const payload = { tipo: tipo, valor: valor, fonteMetodologia: fonte };

    try {
        const response = await fetch('http://localhost:8081/api/fatores', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        if (response.ok) {
            alert("Novo fator de emissão registrado! O anterior foi arquivado para auditoria.");
            document.getElementById('fatorValor').value = '';
            document.getElementById('fatorFonte').value = '';
            carregarHistoricoFatores();
        } else {
            const erro = await response.json();
            alert("Erro ao salvar: " + (erro.erro || JSON.stringify(erro)));
        }
    } catch (err) { alert("Erro de conexão com o servidor."); }
}

async function carregarHistoricoFatores() {
    const tbody = document.getElementById('corpoTabelaFatores');
    tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;">Carregando...</td></tr>';
    try {
        const response = await fetch('http://localhost:8081/api/fatores');
        if (!response.ok) throw new Error("Falha ao buscar fatores");
        const fatores = await response.json();
        tbody.innerHTML = '';
        if(fatores.length === 0) { tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;">Nenhum fator cadastrado.</td></tr>'; return; }

        fatores.forEach(f => {
            const data = new Date(f.dataVigencia).toLocaleString('pt-BR');
            const badgeAtivo = f.ativo 
                ? '<span style="background: #28a745; color: white; padding: 2px 6px; border-radius: 4px; font-size: 0.7rem;">ATIVO</span>' 
                : '<span style="background: #666; color: white; padding: 2px 6px; border-radius: 4px; font-size: 0.7rem;">OBSOLETO</span>';

            tbody.innerHTML += `
                <tr>
                    <td>${f.tipo}</td>
                    <td style="font-weight: bold;">${f.valor}</td>
                    <td>${f.fonteMetodologia}</td>
                    <td>${data}</td>
                    <td>${badgeAtivo}</td>
                </tr>
            `;
        });
    } catch (error) { tbody.innerHTML = '<tr><td colspan="5" style="text-align:center; color: red;">Erro ao carregar dados.</td></tr>'; }
}