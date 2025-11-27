$(document).ready(function () {
    // Seleciona todas as seções da página e os itens de navegação
    const sections = $('section');
    const navItems = $('.nav-item');

    // Adiciona um evento que é ativado toda vez que o usuário rola a página
    $(window).on('scroll', function () {
        const header = $('header');
        // Calcula a posição de rolagem ajustada pela altura do cabeçalho
        const scrollPosition = $(window).scrollTop() - header.outerHeight();

        let activeSectionIndex = 0;

        // Se a rolagem estiver no topo, remove a sombra do cabeçalho
        if (scrollPosition <= 0) {
            header.css('box-shadow', 'none');
        } else {
            // Caso contrário, adiciona uma sombra para dar um efeito de profundidade
            header.css('box-shadow', '5px 1px 5px rgba(0, 0, 0, 0.1');
        }

        // Itera sobre cada seção da página para verificar qual delas está visível
        sections.each(function (i) {
            const section = $(this);
            const sectionTop = section.offset().top - 96;
            const sectionBottom = sectionTop + section.outerHeight();

            // Se a posição de rolagem estiver dentro da seção atual, marca-a como a seção ativa
            if (scrollPosition >= sectionTop && scrollPosition < sectionBottom) {
                activeSectionIndex = i;
                return false; // Sai do loop para otimizar
            }
        });

        // Remove a classe 'active' de todos os itens de navegação
        navItems.removeClass('active');
        // Adiciona a classe 'active' ao item de navegação correspondente à seção visível
        $(navItems[activeSectionIndex]).addClass('active');
    });

    // Configurações das animações de rolagem (ScrollReveal)
    // Anima a seção #cta vindo da esquerda
    ScrollReveal().reveal('#cta', {
        origin: 'left',
        duration: 2000,
        distance: '20%'
    });

    // Anima os elementos com a classe .dish vindo da esquerda
    ScrollReveal().reveal('.dish', {
        origin: 'left',
        duration: 2000,
        distance: '20%'
    });

    // Anima o elemento #testimonial_chef vindo da esquerda
    ScrollReveal().reveal('#testimonial_chef', {
        origin: 'left',
        duration: 1000,
        distance: '20%'
    });

    // Anima os elementos com a classe .feedback vindo da direita
    ScrollReveal().reveal('.feedback', {
        origin: 'right',
        duration: 1000,
        distance: '20%'
    });
});
