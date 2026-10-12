
document.addEventListener("DOMContentLoaded", function () {
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
});

