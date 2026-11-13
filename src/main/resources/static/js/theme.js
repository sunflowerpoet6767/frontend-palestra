function applyTheme(theme) {
    document.documentElement.setAttribute("data-bs-theme", theme);

    const button = document.getElementById("themeButton");

    if (button) {
        button.textContent = theme === "dark" ? "☀️ Light" : "🌙 Dark";
    }
}

function toggleTheme() {
    const currentTheme = document.documentElement.getAttribute("data-bs-theme");
    const newTheme = currentTheme === "dark" ? "light" : "dark";

    localStorage.setItem("theme", newTheme);
    applyTheme(newTheme);
}

applyTheme(localStorage.getItem("theme") || "light");

document.addEventListener("DOMContentLoaded", function () {
    applyTheme(localStorage.getItem("theme") || "light");
});