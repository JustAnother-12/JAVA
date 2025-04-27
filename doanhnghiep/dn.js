const menuToggle = document.getElementById('menuToggle');
const sideMenu = document.getElementById('sideMenu');

menuToggle.addEventListener('mouseenter', () => {
    sideMenu.style.display = 'block';
});

menuToggle.addEventListener('mouseleave', () => {
    setTimeout(() => {
        if (!sideMenu.matches(':hover')) {
            sideMenu.style.display = 'none';
        }
    }, 200);
});

sideMenu.addEventListener('mouseleave', () => {
    sideMenu.style.display = 'none';
});

sideMenu.addEventListener('mouseenter', () => {
    sideMenu.style.display = 'block';
});