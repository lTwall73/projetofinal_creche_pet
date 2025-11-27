$(document).ready(function () {
    // Adiciona um evento de clique ao botão do menu hambúrguer com o ID 'btn_mobile'
    $('#btn_mobile').on('click', function () {
        // Alterna a classe 'active' no menu móvel com o ID 'mobile_menu', mostrando ou escondendo-o
        $('#mobile_menu').toggleClass('active');
        // Encontra o ícone dentro do botão e alterna entre as classes 'fa-bars' (três barras) e 'fa-xmark' (um 'x')
        $('#btn_mobile').find('i').toggleClass('fa-bars fa-xmark');
    });
});
