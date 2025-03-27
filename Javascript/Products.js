"use strict";
import { setQuantityBox } from "./Actions.js";
import * as Bridge from "./Bridge.js";
import { fakeOverlay, formatPrices, hiddenException, isEmpty, resizeImages, scrollView } from "./Interface.js";
import * as Navigate from "./Navigate.js";
import { attachAddToCartEvents, attachAddToCartInDetails } from "./Cart.js";
// get / set products
function getProductBooks() {
  return Array.from(JSON.parse(localStorage.getItem("products")));
}

function setProductBooks(product) {
  localStorage.setItem("products", JSON.stringify(product));
}

function getValueQuery(request) {
  let newURL = new URLSearchParams(window.location.search);
  let query = newURL.get(request);
  return query === "undefined" ? undefined : query;
}

async function dynamicDetail(product) {
  // !book detail
  if (!product) return;
  let container = Bridge.default().getMainContainer();
  const elementsObj = Bridge.default();
  let currentTitle = Bridge.$("title");
  let quantity = product.quantity;
  let release = product.release, packaging = product.packagingSize
  let productSale = product.sale, productPrice = product.price;
  let srcImage = product.img, productDescription = product.description;
  let format = product.format, type = product.type, genre = product.genre;
  let productAuth = product.author, productName = product.name, id = product.productID;
  let productCategories = product.category;

  // !Container for detail
  let saleLabel = container.querySelector(".detail-block .sale-label");
  let imageContainer = container.querySelector(".detail-block .product-image img");
  let bookTitle = container.querySelector(".product-title h1");
  let bookID = container.querySelector(".product-title .product-id");
  let bookPrice = container.querySelector(".block-product-price");
  let tableInfo = container.querySelector(".product-info");
  let bookDesc = container.querySelector(".short-desc div:last-child");
  let bookTags = container.querySelector(".product-tags div:first-child p");
  let bookCategory = container.querySelector(".product-tags div:last-child p");
  let quantityBox = container.querySelector(".quantity-box");
  let buttons = container.querySelectorAll(".button");
  // for selections
  let listOptions = container.querySelector("#product-selector-options");
  let selectOptions = Array.from(listOptions?.children);

  // fakeOverlay(container);
  // !set data (CONTINUE) GET FIELDS ON OBJ -> ADD DATA TO DOM 
  currentTitle.innerText = productName;
  // img
  imageContainer.setAttribute("src", srcImage);
  imageContainer.setAttribute("alt", productName);
  imageContainer.style.width = 80 + "%";
  // other details
  saleLabel.innerText = (productSale * 100) + "%";
  bookTitle.innerText = productName;
  bookID.innerText = id;

  await Navigate.sleep(50);
  fakeOverlay(container, 150);

  // price
  (Array.from(bookPrice.children)).forEach(() => {
    let oldPrice = bookPrice.querySelector(".old-price");
    let newPrice = bookPrice.querySelector(".new-price");
    if (oldPrice)
      oldPrice.innerText = productPrice;
    if (newPrice)
      newPrice.innerText = Math.round(productPrice * (1 - productSale));
  });
  // table info
  (Array.from(tableInfo.children)).forEach((child) => {
    let bAuthor = child.querySelector(".b-author");
    let bRelease = child.querySelector(".b-release");
    let bFormat = child.querySelector(".b-format");
    let bSize = child.querySelector(".b-size");

    if (bAuthor)
      bAuthor.innerText = productAuth;
    else if (bRelease)
      bRelease.innerText = release;
    else if (bFormat)
      bFormat.innerText = format;
    else if (bSize)
      bSize.innerText = packaging;
  });

  // remove not exist selections
  if (!productCategories)
    selectOptions.remove();
  if (!productCategories.includes("normal"))
    (selectOptions.find((child) => child.value === "normal"))?.remove();
  if (!productCategories.includes("special"))
    (selectOptions.find((child) => child.value === "special"))?.remove();
  if (productCategories.includes("collectible"))
    listOptions.innerHTML = "<option value=\"collectible\">bản sưu tập</option>"

  // execute buttons when quantity > 0 or not 
  if (quantity <= 0) {
    buttons.forEach((button) => button.classList.add("disable"));
    quantityBox.classList.add("disable")
  }

  bookDesc.innerText = productDescription;
  bookTags.innerText = genre;
  bookCategory.innerText = type;
  setQuantityBox(Bridge.default());

  // execute other container
  let sameAuthor = elementsObj.getSameAuthorContainer();
  let productLike = elementsObj.getProductLikeContainer();
  let list = getProductBooks();

  // call other functions
  productContainers(list, sameAuthor);
  productContainers(list, productLike);
  callFuncsAgain(elementsObj);
}

async function callFuncsAgain(elementsObj) {
  resizeImages(elementsObj);
  formatPrices(elementsObj);
}

// for show detail products
async function renderProductDetails(list, wrapper) {
  try {
    // first param on fields requestRest when renderDOM is now object item
    if (!list || !wrapper) return;
    let arrayChild = Array.from(wrapper.children);
    arrayChild?.forEach((child, index) => {
      child.querySelector(".block-product")?.addEventListener("click", () => {
        let bookName = (list[index].name).replaceAll("&", "").replaceAll("!", "").replaceAll(" ", "-");
        // change path with path request
        let newURL = `${location.href.slice(0, location.href.lastIndexOf("/") + 1)}index.html?name=${bookName}`;
        window.history.pushState({}, "", newURL);
        hiddenException("detail-content");
        dynamicDetail(list[index]);
      });
    });
  } catch (error) {
    console.error(error);
  }
}

// render products
function renderProducts(list, wrapper) {
  if (!list) return;
  let html = "";
  for (let product of list) {
    html += `
      <div class="product-item grid-col col-l-2-4 col-m-3 col-s-6">
              <div class="block-product product-resize">
                    <span class="product-image js-item">
                        <img src="${product.img}" alt="${product.name}">
                    </span>
                    <div class="sale-label">${product.sale * 100}%</div>
                    <div class="sale-off font-bold capitalize ${product.quantity > 0 ? "" : "active"}">hết hàng</div>
                    <div class="info-inner flex justify-center align-center line-height-1-6">
                        <h4 class="font-light capitalize" title="${product.name}">${product.name}</h4>
                        <div class="margin-y-4">
                              <span class="price font-bold">${Math.round(product.price * (1 - product.sale))}</span>
                              <del class="price old-price padding-left-8 font-size-14">${product.price}</del>
                        </div>
                    </div>
              </div>
              <div class="action ${product.quantity > 0 ? "" : "disable"}">
                    <div class="buy-btn">
                        <div title="mua ngay" class="button">
                              <i class="fa-solid fa-bag-shopping fa-lg" style="color: var(--primary-white);"></i>
                        </div>
                    </div>

                  <div class="add-to-cart">
                    <div title="thêm vào giỏ hàng" class="button">
                      <i class="fa-solid fa-basket-shopping fa-lg" style="color: var(--primary-white);"></i>
                    </div>
                  </div>
              </div>
        </div>
    `;
  }
  if (wrapper) {
    wrapper.innerHTML = html;
    renderProductDetails(list, wrapper, "detail_product.html");
    attachAddToCartEvents();
    attachAddToCartInDetails();
  } else
    return html;
}

// get container for product and call render products
function productContainers(productsList, container) {
  if (!productsList) return;
  let listLength = productsList.length;

  if (!container) {
    let containers = Bridge.$$(".container");
    // return if not have any container
    if (!containers) return;
    containers.forEach((container) => {
      let list;
      let containerID = container.getAttribute("id");
      let wrapper = container.querySelector(".product-container");

      //gene script html
      if (!isEmpty(wrapper)) return;
      if (wrapper && containerID === "fs-container")
        list = productsList.sort((a, b) => b.sale - a.sale).toSpliced(5);

      else if (wrapper && containerID === "new-books-container")
        list = productsList.toSpliced(0, listLength - 5);

      else if (wrapper && containerID === "best-selling-container")
        list = productsList.sort((a, b) => b.quantity - a.quantity).toSpliced(5);

      else if (wrapper && containerID === "light-novel-container")
        list = productsList.filter((product) => product.type === "light novel").toSpliced(5);

      else if (wrapper && containerID === "manga-container")
        list = productsList.filter((product) => product.type === "manga").toSpliced(5);

      else if (wrapper && containerID === "other-books-container")
        list = productsList.sort((a, b) => a.releaseDate - b.releaseDate).toSpliced(5);
      else
        list = productsList.sort((a, b) => a.author - b.author).toSpliced(5);
      // render script and add it to DOM
      renderProducts(list, wrapper);
    });


    return;
  }
  else {
    let list;
    let wrapper = container.querySelector(".product-container");
    let containerID = container.getAttribute("id");
    if (wrapper && containerID === "same-author-container")
      list = productsList.filter((product) => product.author === Bridge.$(".b-author")?.innerHTML).toSpliced(5);
    else if (wrapper && containerID === "product-like-container")
      list = productsList.filter((product) => (product.genre)?.includes(Bridge.$(".product-tags div:first-child p")?.innerHTML)).toSpliced(5);
    renderProducts(list, wrapper);
  }
  if (isEmpty(container)) return;
}

export { getProductBooks, setProductBooks, productContainers, getValueQuery, renderProductDetails, renderProducts, dynamicDetail, callFuncsAgain };
