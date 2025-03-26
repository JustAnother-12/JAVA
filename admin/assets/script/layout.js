function closeSidebar() {
    let element = document.getElementById("sidebar");
    if (element.style.display === "none") {
        element.style.display = "flex";
        document.getElementById("header-left").style.backgroundColor = "#1a2536";
        document.getElementById("MT3H").style.float = "left";
        document.getElementById("MT3H").style.color = "rgb(254, 225, 225)";
        document.getElementById("closeSidebar").style.float = "right";
        document.getElementById("bars").classList.add("dark-img");
        document.getElementById("content").style.width = "80%";


    } else {
        element.style.display = "none";
        document.getElementById("header-left").style.backgroundColor = "white";
        document.getElementById("MT3H").style.float = "right";
        document.getElementById("closeSidebar").style.float = "left";
        document.getElementById("bars").classList.remove("dark-img");
        document.getElementById("content").style.width = "100%";
        document.getElementById("MT3H").style.color = "#080e18";

    }
}