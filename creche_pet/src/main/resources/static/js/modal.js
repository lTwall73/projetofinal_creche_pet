// Seleciona o botão que abre o modal pelo ID
const openModalBtn = document.getElementById('open-modal');
// Seleciona o modal de login pelo ID
const modal = document.getElementById('login-modal');
// Seleciona o botão de fechar dentro do modal
const closeBtn = document.querySelector('.close-btn');

// Adiciona um evento de 'click' ao botão 'Acesso do Usuário'
openModalBtn.addEventListener('click', function (event) {
    // Impede o comportamento padrão do link de navegar para o topo da página
    event.preventDefault();
    // Altera o estilo do modal para 'flex', tornando-o visível
    modal.style.display = 'flex';
});

// Adiciona um evento de 'click' ao botão de fechar ('x')
closeBtn.addEventListener('click', function () {
    // Altera o estilo do modal para 'none', ocultando-o
    modal.style.display = 'none';
});

// Adiciona um ouvinte de evento para fechar o modal se o usuário clicar fora dele
window.addEventListener('click', function (event) {
    // Verifica se o elemento clicado é o próprio modal (a área escura)
    if (event.target === modal) {
        // Oculta o modal
        modal.style.display = 'none';
    }
});