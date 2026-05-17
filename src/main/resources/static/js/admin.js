import { salvarFator, obterFatores } from './api.js';

/**
 * Abre a modal administrativa e carrega o histórico de fatores.
 */
export function abrirModalAdmin() {
    const modal = document.getElementById('modalAdmin');
    if (modal) {
        modal.style.display = "block";
    }
    carregarHistoricoFatores();
}

/**
 * Fecha a modal administrativa.
 */
export function fecharModalAdmin() {
    const modal = document.getElementById('modalAdmin');
    if (modal) {
        modal.style.display = "none";
    }
}

/**
 * Envia o novo fator cadastrado pelo formulário de admin.
 */
export async function salvarNovoFator() {
    const tipo = document.getElementById('fatorTipo').value;
    const valorEl = document.getElementById('fatorValor');
    const fonteEl = document.getElementById('fatorFonte');

    const valor = parseFloat(valorEl.value);
    const fonte = fonteEl.value.trim();

    if (isNaN(valor) || valor <= 0 || !fonte) {
        alert("Preencha todos os campos do fator com valores válidos!");
        return;
    }

    const payload = { tipo: tipo, valor: valor, fonteMetodologia: fonte };

    try {
        await salvarFator(payload);
        alert("Novo fator de emissão registrado! O anterior foi arquivado para auditoria.");
        valorEl.value = '';
        fonteEl.value = '';
        carregarHistoricoFatores();
    } catch (err) {
        alert("Erro ao salvar: " + err.message);
    }
}

/**
 * Carrega a tabela com o histórico de fatores de emissão.
 */
export async function carregarHistoricoFatores() {
    const tbody = document.getElementById('corpoTabelaFatores');
    if (!tbody) return;

    tbody.innerHTML = '<tr><td colspan="5" style="text-align:center; color: var(--text-muted);">Carregando...</td></tr>';
    
    try {
        const fatores = await obterFatores();
        tbody.innerHTML = '';
        
        if (fatores.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" style="text-align:center; color: var(--text-muted);">Nenhum fator cadastrado.</td></tr>';
            return;
        }

        fatores.forEach(f => {
            const data = new Date(f.dataVigencia).toLocaleString('pt-BR');
            const badgeAtivo = f.ativo 
                ? '<span class="badge-active">ATIVO</span>' 
                : '<span class="badge-obsolete">OBSOLETO</span>';

            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${f.tipo}</td>
                <td style="font-weight: 700; color: var(--primary-red);">${f.valor.toFixed(5)}</td>
                <td>${f.fonteMetodologia}</td>
                <td>${data}</td>
                <td>${badgeAtivo}</td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        tbody.innerHTML = '<tr><td colspan="5" style="text-align:center; color: var(--danger);">Erro ao carregar dados.</td></tr>';
    }
}
