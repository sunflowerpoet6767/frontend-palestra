
document.addEventListener("DOMContentLoaded", function () {

    const loading = document.getElementById("loading-screen");

    if (!loading) return;

    // Mostra il loading quando si cambia pagina
    document.querySelectorAll("a[href]").forEach(link => {

        link.addEventListener("click", function (event) {

            if (
                event.defaultPrevented ||
                event.button !== 0 ||
                event.ctrlKey ||
                event.metaKey ||
                event.shiftKey ||
                event.altKey ||
                link.target === "_blank" ||
                link.hasAttribute("download") ||
                !link.href
            ) return;

            const url = new URL(link.href);

            // Solo navigazione interna al sito
            if (url.origin !== window.location.origin) return;

            // Ignora link che non cambiano pagina
            if (
                url.pathname === window.location.pathname &&
                url.search === window.location.search
            ) return;

            loading.classList.add("show");
        });

    });

    // Nasconde il loading quando la pagina è pronta
    window.addEventListener("pageshow", function () {
        loading.classList.remove("show");
    });

});
