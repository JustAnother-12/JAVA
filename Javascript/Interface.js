"use strict";
import { userDetail } from "./Actions.js";
import * as Bridge from "./Bridge.js";
import * as FlashSale from "./FlashSales.js";
import { sleep } from "./Navigate.js";
import * as RenderProducts from "./Products.js";

function scrollView() {
  let webContent = Bridge.default().getWebContent();
  // webContent.scrollIntoView({ behavior: "auto", block: "start", inline: "nearest" });
  window.scrollTo(0, 0);
}

// change header info when user has been login
function headerUserInfo(elementsObj) {
  let container = elementsObj.getHeaderUserInfo();
  let noSignIn = elementsObj.getNoSignIn();
  let loginBtn = elementsObj.getJsLoginBtn();
  let registerBtn = elementsObj.getJsRegisterBtn();
  let logoutBtn = elementsObj.getJsSignoutBtn();
  let userName = container.querySelector(".user-name");
  const hasLogin = sessionStorage.getItem("hasLogin");
  let hasLoginAccount = sessionStorage.getItem("hasLoginAccount");
  hasLoginAccount = hasLoginAccount ? JSON.parse(hasLoginAccount) : false;
  if (hasLogin && hasLoginAccount) {
    noSignIn.classList.add("disable");
    // disable login - register btn
    loginBtn?.forEach((btn) => btn.classList.add("disable"));
    registerBtn?.forEach((btn) => btn.classList.add("disable"));
    logoutBtn?.forEach((btn) => btn.classList.remove("disable"));
    container.classList.remove("disable");
    userName.innerHTML = hasLoginAccount.lastName + " " + hasLoginAccount.firstName;
    container.addEventListener("click", () => userDetail(elementsObj));

  }
  else {
    noSignIn.classList.remove("disable");
    loginBtn?.forEach((btn) => btn.classList.remove("disable"));
    registerBtn?.forEach((btn) => btn.classList.remove("disable"));
    logoutBtn?.forEach((btn) => btn.classList.add("disable"));
    container.classList.add("disable");
    userName.innerHTML = hasLoginAccount.firstName + hasLoginAccount.lastName;
  }
}

// funcs event
function disableSiblingContainer(container) {
  if (!container) return;
  Array.of(...container.children).forEach((child) => {
    child.classList.contains("active") ? child.classList.remove("active") : child;
    child.offsetWidth > 0 ? child.classList.add("disable") : child;
  });
}

async function fakeOverlay(container, time) {
  const overlay = document.createElement('div');
  overlay.className = 'overlay';
  overlay.innerHTML = 'Loading... :3';
  overlay.style.display = 'flex';
  overlay.style.alignItems = 'center';
  overlay.style.justifyContent = 'center';
  overlay.style.fontSize = 3 + "em";
  overlay.style.color = "var(--primary-white)";
  document.body.appendChild(overlay);
  await sleep(time);
  scrollView();
  // scroll before show DOM
  await sleep(time); // fake loading
  document.body.removeChild(overlay);
  container.removeAttribute("style");
  container.classList.remove("hidden");
}

// check container is empty or not
function isEmpty(container) {
  let children = container.children;
  if (children.length === 0) return true;
  for (let child of children)
    if (child.classList.contains("empty-mess")) return true;
  return false;
}

// format prices from default to vi-VN format
function formatPrices(elementsObj) {
  const pricesContainer = elementsObj.getElementPrices();
  if (pricesContainer) {
    const formatPricesHandler = new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
      minimumSignificantDigits: "3",
    });
    pricesContainer.forEach((element) => {
      if (!element.innerHTML.includes("₫"))
        element.innerText = formatPricesHandler.format(element.innerText);
    });
  }
}

function hiddenException(exception) {
  exception = !exception ? "index-content" : exception;
  let getHandler = Bridge.default();
  let container = getHandler.getMainContainer().querySelector("#main-content .grid-row")?.children;
  let newsContainer = getHandler.getNewsBlogs();
  container = Array.of(...container);

  container.forEach((element) => {
    if (element.getAttribute("id") !== exception)
      element.classList.add("disable");
    else
      element.classList.remove("disable");
  });

  if (exception === "index-content") {
    newsContainer?.classList.contains("disable") ? newsContainer.classList.remove("disable") : newsContainer;
    return;
  }
  newsContainer?.classList.add("disable");
}

//change DOM on categories if it not have any product inside
function categoryIsEmpty() {
  Bridge.default().getCategories().forEach((category) => {
    const container = category.querySelector(".product-container");
    if (isEmpty(container)) {
      container.innerHTML = '<div class="empty-mess font-size-20 font-bold">Không có sản phẩm trong phần này</div>';
      container.classList.add("flex", "full-height", "align-center", "justify-center");
      container.querySelector(".nav-btn")?.classList.add("disable");
      category.querySelector(".category-btn")?.classList.add("disable");
    }
  });
}

// fix bug interface func
// resize image
function resizeImages(elementsObj) {
  const ratio = 333.5 / 216;
  const productImages = elementsObj.getImages();

  if (!productImages) {
    alert("not found any image!");
    return false;
  }
  productImages.forEach((image) => {
    image.addEventListener("load", () => {
      let imageWidth = image.offsetWidth;
      image.style.height = ratio * imageWidth + "px";
    });

    if (image && !image.getAttribute("style")) {
      let imageWidth = image.offsetWidth;
      image.style.height = ratio * imageWidth + "px";
    }
  });
  window.addEventListener("resize", () => {
    productImages.forEach((image) => {
      let imageWidth = image.offsetWidth;
      image.style.height = ratio * imageWidth + "px";
    });
  });
}

// resize width of nav
function resizeSmNav(elementsObj) {
  const subMenuNav = elementsObj.getSubMenuNav();

  if (!subMenuNav) {
    alert("not found nav!");
    return false;
  }

  const childInner = subMenuNav.firstElementChild;
  let parentWidth = subMenuNav.offsetWidth;
  childInner.style.width = parentWidth / 16 + "em";

  window.addEventListener("resize", function () {
    parentWidth = subMenuNav.offsetWidth;
    if (!childInner.width === "unset") childInner.style.width = "unset";
    else childInner.style.width = parentWidth / 16 + "em";
  });
}

// default add header footer and initProducts
async function addDOMHeaderFooter(elementsObj) {
  try {
    const DOM = await Bridge.promiseDOMHandler("../HTML/header_footer/headerFooter.html");
    const header = DOM.getElementById("header-container");
    const subHeader = DOM.getElementById("sub-header");
    const footer = DOM.getElementById("footer-container");
    let placeInsert = elementsObj.getMainContainer();
    // add elements into DOM
    placeInsert.insertAdjacentElement("beforebegin", header);
    placeInsert.insertAdjacentElement("afterEnd", footer);
    placeInsert.insertAdjacentElement("afterbegin", subHeader);
  } catch (error) {
    console.error(error);
  }
}

async function getInitProducts(elementsObj) {
  try {
    const storage = await fetch("../Javascript/Storage.js");
    const jsonArray = await storage.json();
    const productsList = Array.from(jsonArray);
    localStorage.setItem("products", JSON.stringify(productsList));

    // render init products
    RenderProducts.productContainers(productsList);
    FlashSale.setTimeFS(elementsObj);
    RenderProducts.renderProducts(productsList);
    formatPrices(elementsObj);
    resizeImages(elementsObj);
    categoryIsEmpty();
  } catch (error) {
    console.error(error);
  }
}

export { formatPrices, isEmpty, categoryIsEmpty, getInitProducts, hiddenException, scrollView, fakeOverlay, disableSiblingContainer };
export { resizeSmNav, resizeImages, addDOMHeaderFooter, headerUserInfo };
