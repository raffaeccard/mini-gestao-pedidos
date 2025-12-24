// Configurações
const API_CONFIG = {
    BASE_URL: 'http://localhost:8080',
    headers: { 'Content-Type': 'application/json' }
};

// Seletores de Elementos
const elements = {
    form: document.getElementById('new-order-form'),
    tableBody: document.getElementById('orders-table-body'),
    modal: document.getElementById('modal'),
    modalDetails: document.getElementById('modal-details'),
    closeBtn: document.querySelector('.close')
};

/** --- SERVIÇOS (API) --- **/
const OrderService = {
    async getAll() {
        const res = await fetch(`${API_CONFIG.BASE_URL}/orders`);
        if (!res.ok) throw new Error('Erro ao buscar pedidos');
        return res.json();
    },

    async create(data) {
        const res = await fetch(`${API_CONFIG.BASE_URL}/orders`, {
            method: 'POST',
            headers: API_CONFIG.headers,
            body: JSON.stringify(data)
        });
        return res.json();
    },

    async delete(id) {
        const res = await fetch(`${API_CONFIG.BASE_URL}/orders/${id}`, { method: 'DELETE' });
        if (!res.ok) throw new Error('Erro ao excluir');
    },

    async getById(id) {
        const res = await fetch(`${API_CONFIG.BASE_URL}/orders/${id}`);
        return res.json();
    }
};

/** --- LÓGICA DE UI --- **/

// Renderizar Tabela
async function renderOrders() {
    elements.tableBody.innerHTML = '<tr><td colspan="3">Carregando pedidos...</td></tr>';
    
    try {
        const orders = await OrderService.getAll();
        
        if (!orders || orders.length === 0) {
            elements.tableBody.innerHTML = '<tr><td colspan="3">Nenhum pedido encontrado.</td></tr>';
            return;
        }

        elements.tableBody.innerHTML = orders.map(order => `
            <tr>
                <td>${order.customerName}</td>
                <td>${order.details.substring(0, 50)}${order.details.length > 50 ? '...' : ''}</td>
                <td class="actions">
                    <button class="btn-view" data-id="${order.id}">Detalhes</button>
                    <button class="btn-edit" data-id="${order.id}">Editar</button>
                    <button class="btn-delete" data-id="${order.id}">Excluir</button>
                </td>
            </tr>
        `).join('');
    } catch (err) {
        elements.tableBody.innerHTML = `<tr><td colspan="3" style="color: red;">${err.message}</td></tr>`;
    }
}

// Gerenciar Cliques na Tabela (Delegação de Eventos)
elements.tableBody.addEventListener('click', async (e) => {
    const id = e.target.dataset.id;
    if (!id) return;

    if (e.target.classList.contains('btn-delete')) {
        if (confirm('Deseja realmente excluir este pedido?')) {
            await OrderService.delete(id);
            renderOrders();
        }
    }

    if (e.target.classList.contains('btn-view')) {
        showDetails(id);
    }
    
    if (e.target.classList.contains('btn-edit')) {
        alert(`Editar pedido ${id}`);
    }
});

// Modal
async function showDetails(id) {
    elements.modalDetails.innerHTML = 'Carregando...';
    elements.modal.style.display = 'block';
    
    try {
        const order = await OrderService.getById(id);
        elements.modalDetails.innerHTML = `
            <div class="order-info">
                <p><strong>ID:</strong> #${order.id}</p>
                <p><strong>Cliente:</strong> ${order.customerName}</p>
                <hr>
                <p><strong>Descrição:</strong><br>${order.details}</p>
            </div>
        `;
    } catch (err) {
        elements.modalDetails.innerHTML = 'Erro ao carregar detalhes.';
    }
}

// Eventos de Formulário
elements.form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const btnSubmit = e.target.querySelector('button');
    
    const formData = {
        customerName: document.getElementById('customer-name').value,
        details: document.getElementById('order-details').value
    };

    try {
        btnSubmit.disabled = true;
        btnSubmit.innerText = 'Enviando...';
        
        await OrderService.create(formData);
        e.target.reset();
        await renderOrders();
    } catch (err) {
        alert('Erro ao salvar pedido');
    } finally {
        btnSubmit.disabled = false;
        btnSubmit.innerText = 'Adicionar Pedido';
    }
});

// Fechar Modal
const closeModal = () => elements.modal.style.display = 'none';
elements.closeBtn.onclick = closeModal;
window.onclick = (e) => { if(e.target === elements.modal) closeModal(); };

// Inicialização
document.addEventListener('DOMContentLoaded', renderOrders);