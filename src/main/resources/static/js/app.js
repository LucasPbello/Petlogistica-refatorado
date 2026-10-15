
document.addEventListener("DOMContentLoaded", function () {

    // ========================================
    // TEMA CLARO E ESCURO
    // ========================================

    const botaoTema = document.getElementById("alternar-tema");
    function lerCookie(nome) {
        const prefixo = nome + "=";
        const cookies = document.cookie.split(";");

        for (let cookie of cookies) {
            cookie = cookie.trim();
            if (cookie.startsWith(prefixo)) {
                return decodeURIComponent(
                        cookie.substring(prefixo.length)
                        );
            }
        }

        return null;
    }

    function aplicarTema(tema) {
        document.body.classList.toggle(
                "tema-escuro",
                tema === "escuro"
                );

        if (botaoTema) {
            botaoTema.textContent =
                    tema === "escuro" ? "☀️" : "🌙";

            botaoTema.setAttribute(
                    "aria-label",
                    tema === "escuro"
                    ? "Ativar tema claro"
                    : "Ativar tema escuro"
                    );
        }
    }

    function salvarTema(tema) {
        const validade = new Date();
        validade.setFullYear(validade.getFullYear() + 1);

        document.cookie =
                "tema=" + encodeURIComponent(tema) +
                "; expires=" + validade.toUTCString() +
                "; path=/; SameSite=Lax";
    }

    const temaSalvo = lerCookie("tema");
    aplicarTema(temaSalvo === "escuro" ? "escuro" : "claro");

    if (botaoTema) {
        botaoTema.addEventListener("click", function () {
            const temaAtual = document.body.classList.contains(
                    "tema-escuro"
                    ) ? "escuro" : "claro";

            const novoTema =
                    temaAtual === "escuro" ? "claro" : "escuro";

            aplicarTema(novoTema);
            salvarTema(novoTema);
        });
    }

    // ========================================
    // MÁSCARA DE CPF
    // ========================================

    function aplicarMascaraCPF(valor) {

        let numeros = valor.replace(/\D/g, "").slice(0, 11);

        numeros = numeros.replace(
                /^(\d{3})(\d)/,
                "$1.$2"
                );

        numeros = numeros.replace(
                /^(\d{3})\.(\d{3})(\d)/,
                "$1.$2.$3"
                );

        numeros = numeros.replace(
                /\.(\d{3})(\d)/,
                ".$1-$2"
                );

        return numeros;
    }

    // ========================================
    // MÁSCARA DE TELEFONE
    // ========================================

    function aplicarMascaraTelefone(valor) {

        const numeros = valor.replace(/\D/g, "").slice(0, 11);

        if (numeros.length === 0) {
            return "";
        }

        if (numeros.length < 3) {
            return "(" + numeros;
        }

        const ddd = numeros.slice(0, 2);
        const restante = numeros.slice(2);

        if (restante.length <= 4) {
            return "(" + ddd + ") " + restante;
        }

        if (numeros.length <= 10) {
            return "(" + ddd + ") " +
                    restante.slice(0, -4) + "-" +
                    restante.slice(-4);
        }

        return "(" + ddd + ") " +
                restante.slice(0, 5) + "-" +
                restante.slice(5);
    }

    // ========================================
    // APLICAR MÁSCARAS AOS CAMPOS
    // ========================================

    const camposCPF = document.querySelectorAll(
            'input[data-mascara="cpf"]'
            );

    camposCPF.forEach(function (campo) {

        campo.value = aplicarMascaraCPF(campo.value);

        campo.addEventListener("input", function () {
            campo.value = aplicarMascaraCPF(campo.value);
        });
    });

    const camposTelefone = document.querySelectorAll(
            'input[data-mascara="telefone"]'
            );

    camposTelefone.forEach(function (campo) {

        campo.value = aplicarMascaraTelefone(campo.value);

        campo.addEventListener("input", function () {
            campo.value = aplicarMascaraTelefone(campo.value);
        });
    });

});


