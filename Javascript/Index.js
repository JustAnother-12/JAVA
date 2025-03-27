"use strict";
import * as Storage from "./Storage.js";
import * as Products from "./Products.js";
import * as Interface from "./Interface.js";
import * as Bridge from "./Bridge.js";
import * as Actions from "./Actions.js";
import * as FlashSales from "./FlashSales.js";
import * as Navigate from "./Navigate.js";
import * as Search from "./search.js";
import * as Login from "./Login.js";
import * as Register from "./Register.js";
import * as Slides from "./Slides.js";
import * as Cart from "./Cart.js";
import * as Pages from "./pages.js";

document.addEventListener("DOMContentLoaded", () => {
  let elementsObj = Bridge.default();
  let lastPath = location.href;
  lastPath = lastPath.slice(lastPath.lastIndexOf("/") + 1, lastPath.length);

  // DOM ON action.js
  Navigate.forbiddenDOM();
  Interface.addDOMHeaderFooter(elementsObj);
  const checkDOM = setInterval(() => {
    if (
      elementsObj.getHeader() &&
      elementsObj.getSubHeader() &&
      elementsObj.getFooter()
    ) {
      // call funcs
      Interface.resizeSmNav(elementsObj);
      Interface.headerUserInfo(elementsObj);
      Cart.increaseCartCount();
      Cart.updateCartCount(elementsObj);
      Cart.handleCartNavigation();
      Cart.handlePaymentOptionChange();
      Cart.handleDefaultAddressCheckbox();
      Actions.accountEvents(elementsObj);
      Actions.staticContents(elementsObj);
      Actions.historyNavigate(elementsObj);
      Actions.returnHomepage(elementsObj);
      Actions.trackingNavigate(elementsObj);
      Actions.smNavigationMenu(elementsObj);
      Search.searchBtn();
      Pages.handleCategoryNavigation();
      // remove Interval
      clearInterval(checkDOM);
    }
  }, 200);

  // call funcs
  if (!lastPath) Interface.hiddenException();
  Actions.cancelButtons(elementsObj);
  Interface.getInitProducts(elementsObj);

  // navigate.js
  Navigate.execQueryHandler();
  Navigate.popStateHandler();
  Navigate.forbiddenDOM();
  // login
  Login.validateAccount();
  Register.validateRegister();
  if (lastPath.includes("cart")) {
    Cart.displayCartItems(elementsObj);
    Cart.updateCartTotal(elementsObj);
    Cart.handleQuantityChange(elementsObj);
    Cart.handleCheckboxChange(elementsObj);
    Cart.handleSelectAllCheckbox(elementsObj);
    Cart.handleRemoveItem(elementsObj);
    Cart.handleOrderPlacement(elementsObj);
  }
  // other
  Slides.slidesHandler("news");
  Pages.initializePage();
});

window.addEventListener("load", () => {
  let loginForm = Bridge.$("#login");
  let registForm = Bridge.$("#register");
  let forgotForm = Bridge.$("#forgot-password");

  if (JSON.parse(sessionStorage.getItem("retryShowOrder"))) {
    sessionStorage.removeItem("retryShowOrder");
    Actions.showOrderContent();
  }

  if (JSON.parse(sessionStorage.getItem("retryTracking"))) {
    sessionStorage.removeItem("retryTracking");
    Actions.showTracking(localStorage.getItem("donhang"));
  }

  if (JSON.parse(sessionStorage.getItem("login"))) {
    sessionStorage.removeItem("login");
    Actions.showLogin(loginForm, registForm, forgotForm);
  }

  if (JSON.parse(sessionStorage.getItem("register"))) {
    sessionStorage.removeItem("register");
    Actions.showRegister(loginForm, registForm, forgotForm);
  }

  if (JSON.parse(sessionStorage.getItem("forgotPassword"))) {
    sessionStorage.removeItem("forggotPassword");
    Actions.showForgotPassword(loginForm, registForm, forgotForm);
  }

  if (JSON.parse(sessionStorage.getItem("userDetail"))) {
    sessionStorage.removeItem("userDetail");
    Actions.userDetail(Bridge.default());
  }
});
