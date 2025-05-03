window.addEventListener('scroll', function(){
    var menu = document.querySelector("header");
    var perfil = document.getElementById("img_perfil");

    if (window.scrollY > 0) {
        menu.classList.add("scrolled");
    } else {
        menu.classList.remove("scrolled");
    }
});
