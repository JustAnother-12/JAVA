"use strict";
import * as Bridge from "./Bridge.js";
import { disableSiblingContainer, fakeOverlay, formatPrices, headerUserInfo, hiddenException, scrollView } from "./Interface.js";
import { sleep } from "./Navigate.js";

function returnHomepage(elementsObj) {
     let testURL = location.pathname;
     const webLogo = elementsObj.getWebLogo();
     let homepageBtns = elementsObj.getHomepageBtn();
     testURL = testURL.slice(testURL.lastIndexOf("/") + 1, testURL.indexOf("?") + 1);

     homepageBtns?.forEach((btn) =>
          btn.addEventListener("click", () => Bridge.navigateRootURL())
     );
     webLogo?.forEach((element) =>
          element.addEventListener("click", () => {
               if (!testURL.includes("index")) Bridge.navigateRootURL();
          })
     );
}

// header navigation button on mobile
function smNavigationMenu(elementsObj) {
     let navigateBtn = elementsObj.getMobileNavigate();
     navigateBtn.addEventListener("click", Bridge.debounce((event) => {
          let overlay = navigateBtn.querySelector(".overlay");
          overlay.classList.add("active");
          overlay.classList.remove("overflowText");
          overlay.classList.add("menu");
          if (event.target.classList.contains("overlay")) {
               setTimeout(() => overlay.classList.remove("active"), 200);
               overlay.classList.remove("menu");
               overlay.classList.add("overflowText");
          }
     }), 200, "mobileHeaderNavigate");
}

function cancelButtons(elementsObj) {
     let loginForm = Bridge.$("#login");
     let forgotForm = Bridge.$("#forgot-password");
     const cancelBtn = elementsObj.getJsCancelBtn();
     if (cancelBtn)
          cancelBtn.forEach((btn) =>
               btn.addEventListener("click", () => {
                    loginForm?.classList.add("active");
                    forgotForm?.classList.contains("active") ? forgotForm.classList.remove("active") : forgotForm;
               })
          );
}

// navigate to order-tracking
function trackingNavigate(elementsObj) {
     // navigate to index.html if not have any container
     const buttons = elementsObj.getOrderTrackingBtn();
     if (!buttons) return;
     const trackers = localStorage.getItem("donhang");
     buttons.forEach((btn) =>
          btn.addEventListener("click", Bridge.throttle(() => showTracking(trackers), 200, "statusNav")));
     if (trackers) orderInfo();
}

function showTracking(trackers) {
     const elementsObj = Bridge.default();
     const container = elementsObj.getStatusContainer();
     const blankOrder = container?.querySelector("#blank-order");
     const customerOrder = container?.querySelector("#customer-order");
     // navigate to index.html if not have any container
     if (!container) {
          sessionStorage.setItem("retryTracking", "true");
          Bridge.navigateRootURL();
     }

     hiddenException("order-content");
     disableSiblingContainer(elementsObj.getOrderContent());
     elementsObj.getStatusContainer()?.classList.remove("disable");
     if (!trackers || !JSON.parse(sessionStorage.getItem("hasLogin"))) {
          blankOrder.classList.add("active");
          customerOrder.classList.contains("active") ? customerOrder.classList.remove("active") : customerOrder;
     } else {
          customerOrder.classList.add("active");
          blankOrder.classList.contains("active") ? blankOrder.classList.remove("active") : blankOrder;
     }
}

function orderInfo() {
     if (!sessionStorage.getItem("hasLogin")) return;
     let ordersList = JSON.parse(localStorage.getItem("donhang"));
     let loginAccount = JSON.parse(sessionStorage.getItem("hasLoginAccount"));
     let customer = ordersList.find((order) => order.id_khachhang === loginAccount.userID);
     let container = Bridge.$$(".order-info .block-order-info span");
     container.forEach((block) => {
          if (!customer) return;
          if (block.classList.contains("order-code")) block.innerHTML = customer.id_donhang;
          if (block.classList.contains("order-time")) block.innerHTML = customer.date;
          if (block.classList.contains("expected-delivery-date"))
               block.innerHTML = "3 ngày sau xác nhận đơn";
          if (block.classList.contains("Consignee")) block.innerHTML = customer.ten_khach_hang;
          if (block.classList.contains("Consignee-phone"))
               block.innerHTML = customer.phonenumber;
          if (block.classList.contains("Consignee-address"))
               block.innerHTML = customer.dia_chi;
     });
}


// navigate to history tracking
function historyNavigate(elementsObj) {
     let historyBtn = elementsObj.getHistoryBtn();
     historyBtn.forEach((btn) => btn.addEventListener("click", showOrderContent));
}

function showOrderContent() {
     let elementsObj = Bridge.default();
     let historyContainer = elementsObj.getHistoryOrder();
     let lists = localStorage.getItem("donhang");
     let orderContainer = elementsObj.getOrderContent();
     hiddenException("order-content");
     disableSiblingContainer(orderContainer);
     if (!lists || !JSON.parse(sessionStorage.getItem("hasLogin"))) {
          blankOrder(elementsObj);
          return;
     }
     // navigate to index.html if not have any container
     if (!orderContainer) {
          sessionStorage.setItem("retryShowOrder", "true");
          Bridge.navigateRootURL();
     }

     elementsObj.getHistoryContainer()?.classList.remove("disable");
     if (historyContainer.classList.contains("active")) return;
     historyContainer.classList.add("active");
     renderOrder(elementsObj);
}

function scriptOrder(customer, detailOrder) {
     let status;
     let productsList = JSON.parse(localStorage.getItem("products"));
     let product = productsList.find((product) => product.productID === detailOrder.id_sanpham);
     let deliveried = addDaysToDate(customer.date, 3).toLocaleDateString("vi-VN", {
          day: "2-digit",
          month: "2-digit",
          year: "numeric",
          hour: "2-digit",
          minute: "2-digit",
          hour12: false,
          timeZone: "Asia/Ho_Chi_Minh",
     });

     // get status of this order
     if (status == 1) status = "chờ xử lý";
     else if (status == 2) status = "chờ lấy hàng";
     else if (status == 3) status = "chờ giao hàng";
     else if (status == 4) status = "đã giao hàng";
     // get script html and append it 
     let script = `
          <div class="block-product">
              <div class="cart-content">
                  <div class="completed-order-info margin-bottom-8">
                        <img src="${product?.img}">
                        <div class="full-width padding-left-12">
                            <p class="capitalize padding-bottom-8">${product.name}</p>
                            <div class="block-product-price text-end">
                                  <div class="quantity-cart">x${detailOrder.sl}</div>
                                  <div class="new-price price">${detailOrder.don_gia * (1 - product.sale)}</div>
                            </div>
                        </div>
                  </div>
                  <div
                        class="flex justify-space-between padding-bottom-8 padding-top-8">
                        <div class="total-item opacity-0-6">${detailOrder.sl} item</div>
                        <div class="price total-price font-bold text-end">${Math.round(detailOrder.don_gia * (1 - product.sale) * detailOrder.sl)}</div>
                  </div>
                  <div class="order-status flex justify-space-between padding-top-8 padding-bottom-8">
                        <span class="opacity-0-8 font-size-13 ${status === "đã giao hàng" ? "success-color" : "waiting-color"}">${status ? status : "chờ xử lý"}</span>
                        <div><i class="fa-solid fa-chevron-right fa-xs" style="color: var(--main-color);"></i></div></div>
                  <div class="flex align-center justify-space-between padding-top-8">
                        <span class="delivered-day flex opacity-0-8">
                            <div>${deliveried}</div>
                        </span>

                        <div class="flex">
                          <span class="remove-btn button ${customer.status != 4 ? "" : "disable"}">
                                <div class="capitalize"> Hủy Đơn</div>
                          </span>
                        </div>
                  </div>
              </div>
        </div>
  `
     return new DOMParser().parseFromString(script, "text/html").body.firstChild;
}

function renderOrder(elementsObj) {
     let container = elementsObj.getHistoryOrderTable();
     let ordersList = JSON.parse(localStorage.getItem("donhang"));
     let detailOrders = JSON.parse(localStorage.getItem("chitiet_donhang"));
     let loginAccount = JSON.parse(sessionStorage.getItem("hasLoginAccount"));
     let customer = ordersList.filter((order) => order.id_khachhang === loginAccount.userID);

     if (customer && container) {
          let details = [];
          detailOrders.forEach((detail) => {
               customer.forEach((cus) => {
                    if (cus.id_donhang === detail.id_donhang) {
                         let temp = {};
                         temp.customer = cus;
                         temp.detail = detail;
                         details.push(temp);
                    }
               })
          });
          details.forEach((detail) => {
               let script = scriptOrder(detail.customer, detail.detail);
               let removeBtn = script.querySelector(".remove-btn");
               removeBtn.addEventListener("click", () => {
                    container.removeChild(removeBtn.offsetParent);
                    // reduce array of detail order on local
                    detailOrders = detailOrders.filter((element) => element.id_donhang !== detail.id_donhang || element.id_sanpham !== detail.id_sanpham);
                    if (detailOrders.filter((element) => element.id_donhang === customer.id_donhang).length === 0)
                         ordersList = ordersList.filter((order) => order.id_khachhang !== loginAccount.userID);
                    // update arrays
                    localStorage.setItem("donhang", JSON.stringify(ordersList));
                    localStorage.setItem("chitiet_donhang", JSON.stringify(detailOrders));

                    // change container when not have any product
                    if (container.childNodes.length === 0)
                         blankOrder(elementsObj);
               });
               container.appendChild(script);
          });
          formatPrices(elementsObj);
     }
     if (container.childNodes.length === 0)
          blankOrder(elementsObj);
}

function blankOrder(elementsObj) {
     let orderContainer = elementsObj.getOrderContent();
     hiddenException("order-content");
     disableSiblingContainer(orderContainer);
     // navigate to index.html if not have any container
     let statusContainer = orderContainer.querySelector(".order-status-container");
     disableSiblingContainer(statusContainer);
     orderContainer?.classList.remove("disable");
     statusContainer?.classList.remove("disable");
     elementsObj.getBlankOrder().classList.add("active");
     return;
}

// update date deliveried
function addDaysToDate(dateString, daysToAdd) {
     const date = new Date(dateString); // Chuyển chuỗi thành đối tượng Date
     if (isNaN(date)) {
          throw new Error("Invalid date format");
     }

     date.setDate(date.getDate() + daysToAdd); // Thêm 3 ngày vào ngày hiện tại
     return date; // Trả về đối tượng Date mới
}


// set quantity box on detail product
function setQuantityBox(elementsObj) {
     let reduceBtn = elementsObj
          .getQuantityBox()
          .querySelector("input[type=button].reduce");
     let increaseBtn = elementsObj
          .getQuantityBox()
          .querySelector("input[type=button].increase");
     let quantity = elementsObj
          .getQuantityBox()
          .querySelector("input[type=text]#quantity");
     let productID = Bridge.$(".product-id")?.innerHTML;
     let realQuantity = Array.from(
          JSON.parse(localStorage.getItem("products"))
     ).find((product) => product.productID === productID)?.quantity;

     reduceBtn.addEventListener("click", () => (quantity.value = parseInt(quantity.value) - 1 <= 0 ? 1 : parseInt(quantity.value) - 1));
     increaseBtn.addEventListener("click", () => (quantity.value = parseInt(quantity.value) + 1 <= realQuantity ? parseInt(quantity.value) + 1 : realQuantity));
     quantity.addEventListener("change", () => (quantity.value = parseInt(quantity.value) > realQuantity ? realQuantity : parseInt(quantity.value)));
}

// handle scrolls
function scrollToHandler(nameStaticPage) {
     let staticPage;
     const elementsObj = Bridge.default();

     if (nameStaticPage === "news") {
          staticPage = elementsObj.getNewsBlogs();
          hiddenException();
     }

     if (nameStaticPage === "services") staticPage = elementsObj.getFooter();
     else if (!staticPage && nameStaticPage === "services") {
          alert("not found services!");
          return false;
     }

     // check if action is scroll to top or not
     if (nameStaticPage === "top")
          window.scroll({ top: 0, left: 0, behavior: "smooth" });
     else if (staticPage)
          window.scroll({
               top: staticPage.offsetTop + 3 * 16,
               left: 0,
               behavior: "smooth",
          });

     // check if action is scroll to top or not
     if (nameStaticPage === "top")
          window.scroll({ top: 0, left: 0, behavior: "smooth" });
     else if (staticPage)
          window.scroll({
               top: staticPage.offsetTop + 3 * 16,
               left: 0,
               behavior: "smooth",
          });
}

// func for click nav btn on sub header or click to scroll top btn
function staticContents(elementsObj) {
     const newsButtons = elementsObj.getNewsBtn();
     const scrollTopButtons = elementsObj.getScrollTop();
     const servicesButtons = elementsObj.getServicesBtn();

     // add event listener
     if (newsButtons) {
          newsButtons.forEach((btn) => {
               btn.addEventListener(
                    "click",
                    Bridge.throttle(() => scrollToHandler("news"), 200, "newsBtn")
               );
          });
     }

     if (servicesButtons) {
          servicesButtons.forEach((btn) => {
               btn.addEventListener(
                    "click",
                    Bridge.throttle(() => scrollToHandler("services"), 200, "servicesBtn")
               );
          });
     }

     if (scrollTopButtons) {
          scrollTopButtons.addEventListener(
               "click",
               Bridge.throttle(() => scrollToHandler("top"), 200, "ScrollTopBtn")
          );
     }
}

// DOM navigate handler (SPAs)
// func account's events handle
function accountEvents(elementsObj) {
     const loginButtons = elementsObj.getJsLoginBtn();
     let loginForm = Bridge.$("#login");
     const registerButtons = elementsObj.getJsRegisterBtn();
     let registForm = Bridge.$("#register");
     const forgotButtons = elementsObj.getJsForgotBtn();
     let forgotForm = Bridge.$("#forgot-password");
     const signoutButtons = elementsObj.getJsSignoutBtn();

     loginButtons?.forEach((btn) =>
          btn.addEventListener("click", Bridge.throttle(() => showLogin(loginForm, registForm, forgotForm), 200, "login")));

     registerButtons?.forEach((btn) =>
          btn.addEventListener("click", Bridge.throttle(() => showRegister(loginForm, registForm, forgotForm), 200, "register")));

     forgotButtons?.forEach((btn) =>
          btn.addEventListener("click", Bridge.throttle(() => showForgotPassword(loginForm, registForm, forgotForm), 200, "forgotPassword")));

     signoutButtons?.forEach((btn) => btn.addEventListener("click", Bridge.throttle(() => singoutAccount(elementsObj), 200, "signout")));
}

// for login
function showLogin(loginForm, registForm, forgotForm) {
     hiddenException("account-content");
     if (!loginForm) {
          sessionStorage.setItem("login", true);
          Bridge.navigateRootURL();
     }
     loginForm?.classList.add("active");
     registForm?.classList.contains("active") ? registForm.classList.remove("active") : registForm;
     forgotForm?.classList.contains("active") ? forgotForm.classList.remove("active") : forgotForm;
     scrollView();
}

// for register
function showRegister(loginForm, registForm, forgotForm) {
     hiddenException("account-content");
     if (!registForm) {
          sessionStorage.setItem("register", true);
          Bridge.navigateRootURL();
     }
     registForm?.classList.add("active");
     loginForm?.classList.contains("active") ? loginForm.classList.remove("active") : loginForm;
     forgotForm?.classList.contains("active") ? forgotForm.classList.remove("active") : forgotForm;
     scrollView();
}

// for show forgot password
function showForgotPassword(loginForm, registForm, forgotForm) {
     if (!forgotForm) {
          sessionStorage.setItem("forgotPassword", true);
          Bridge.navigateRootURL();
     }
     hiddenException("account-content");
     forgotForm?.classList.add("active");
     loginForm?.classList.contains("active") ? loginForm.classList.remove("active") : loginForm;
     registForm?.classList.contains("active") ? registForm.classList.remove("active") : registForm;
     scrollView();
}

// for signout account
function singoutAccount(elementsObj) {
     sessionStorage.removeItem("hasLogin");
     sessionStorage.removeItem("hasLoginAccount");
     headerUserInfo(elementsObj);
     Bridge.navigateRootURL();
}

function userDetail(elementsObj) {
     let userDetailForm = elementsObj.getUserDetail();
     let loginRegistrationForm = elementsObj.getAccountForm();
     let historyBill = elementsObj.getHistoryBill();
     let userOrders = elementsObj.getUserOrders();
     // for checking account have order or not
     let ordersList = JSON.parse(localStorage.getItem("donhang"));
     if (!ordersList) return;

     let loginAccount = JSON.parse(sessionStorage.getItem("hasLoginAccount"));
     let customer = ordersList.find((order) => order.id_khachhang === loginAccount.userID);

     if (!userDetailForm) {
          sessionStorage.setItem("userDetail", true);
          Bridge.navigateRootURL();
     }

     // init action
     hiddenException("account-content");
     userDetailForm?.classList.remove("disable");
     loginRegistrationForm?.classList.add("disable");
     changeInfoUser(elementsObj);

     // case action
     if (!customer) {
          historyBill.classList.add("disable");
          userOrders.innerHTML = "Bạn chưa đặt mua sản phẩm.";
     }
     else {
          historyBill.classList.remove("disable");
          userOrders.innerHTML = `
      <div class="order-tracking uppercase text-center font-bold font-size-16 button padding-8" 
          style="max-width: 25%; background-color: var(--main-color);">tra cứu đơn hàng</div>
    `;
          trackingNavigate(elementsObj);
     }
}

function changeInfoUser(elementsObj) {
     let editInfoBtn = elementsObj.getJsEditBtn();
     let submitInfoBtn = elementsObj.getJsSubmitBtn();
     let userCard = elementsObj.getUserCard();
     let accountLogin = JSON.parse(sessionStorage.getItem("hasLoginAccount"));
     let inputFields = userCard.querySelectorAll("input");
     // change default value to user value
     inputFields.forEach((fields) => {
          if (fields.getAttribute("id") === "user-password") {
               fields.style.border = "none";
               fields.style.outline = "none";
          }
          fields.style.borderLeft = "1px solid var(--main-color)";
          if (fields.getAttribute("id") === "user-last-name")
               fields.value = accountLogin.lastName;
          if (fields.getAttribute("id") === "user-first-name")
               fields.value = accountLogin.firstName;
          if (fields.getAttribute("id") === "user-email")
               fields.value = accountLogin.email;
          if (fields.getAttribute("id") === "user-password")
               fields.value = accountLogin.password;
          if (fields.getAttribute("id") === "user-phone")
               fields.value = accountLogin.phone ? accountLogin.phone : "";
          if (fields.getAttribute("id") === "user-address")
               fields.value = accountLogin.address ? accountLogin.address : "";
     });

     // new info object
     let newInfo = {
          userID: accountLogin.userID,
          password: accountLogin.password,
     };
     // edit btn 
     editInfoBtn.addEventListener("click", Bridge.throttle(() => {
          inputFields.forEach((fields) => fields.removeAttribute("disabled"));
          sessionStorage.setItem("editing", true);
     }, 200, "editInfo"));
     // submit btn
     submitInfoBtn.addEventListener("click", Bridge.throttle(() => {
          if (JSON.parse(sessionStorage.getItem("editing"))) {
               // disable edit mode
               inputFields.forEach((fields) => {
                    // get new info
                    if (fields.getAttribute("id") === "user-last-name")
                         newInfo.lastName = fields.value;
                    if (fields.getAttribute("id") === "user-first-name")
                         newInfo.firstName = fields.value;
                    if (fields.getAttribute("id") === "user-email")
                         newInfo.email = fields.value;
                    if (fields.getAttribute("id") === "user-password")
                         newInfo.password = fields.value;
                    if (fields.getAttribute("id") === "user-phone")
                         newInfo.phone = fields.value;
                    if (fields.getAttribute("id") === "user-address")
                         newInfo.address = fields.value;
                    fields.setAttribute("disabled", "");
               });
               // change account handler
               updateListAccount(newInfo);
               sessionStorage.setItem("editing", false);
          }
     }, 200, "submitNewInfo"));
}

function updateListAccount(newInfo) {
     let listAccount = JSON.parse(localStorage.getItem("users"));
     sessionStorage.setItem("hasLoginAccount", JSON.stringify(newInfo));
     console.log(listAccount);
     listAccount.forEach((account) => {
          if (account.userID === newInfo.userID) {
               account.password = newInfo.password;
               account.email = newInfo.email;
               account.lastName = newInfo.lastName;
               account.firstName = newInfo.firstName;
               account.phone = newInfo.phone;
               account.address = newInfo.address;
          }
     });
     console.log(listAccount);
     localStorage.setItem("users", JSON.stringify(listAccount));
}

export { cancelButtons, accountEvents, staticContents, historyNavigate, setQuantityBox, returnHomepage, trackingNavigate, smNavigationMenu };
export { showOrderContent, showTracking, showLogin, showRegister, showForgotPassword, userDetail };
