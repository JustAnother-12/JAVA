"use strict";
import "./Products.js";

const throttleList = {}; //object for throttle function
const debounceList = {};
const $ = document.querySelector.bind(document);
const $$ = document.querySelectorAll.bind(document);

// get Elements object
function getElementsHandler() {
  let getElements = {
    // containers
    getWebContent: () => $(".web-content"),
    getHeader: () => $("#header-container"),
    getSubHeader: () => $("#sub-header"),
    getMainContainer: () => $("#main-container"),
    getFooter: () => $("#footer-container"),
    getCategories: () => $$(".category-tab"),
    getProductContainer: () => $$(".product-container"),
    getSameAuthorContainer: () => $("#same-author-container"),
    getProductLikeContainer: () => $("#product-like-container"),
    // other pages container
    getIndexContainer: () => $("#index-content"),
    getDetailContent: () => $("#detai-content"),
    getSearchContent: () => $("#search-content"),
    // block for order
    getOrderContent: () => $("#order-content"),
    getBlankOrder: () => $("#blank-order"),
    getCustomerOrder: () => $("#customer-order"),
    getStatusContainer: () => $(".order-status-container"),
    getHistoryContainer: () => $(".history-tracking-container"),
    getHistoryOrderTable: () => $(".history-order-table"),
    // block for account
    getAccountContainer: () => $("#account-content"),
    getAccountForm: () => $("#login-registration-form"),
    getUserDetail: () => $("#user-detail"),
    getUserOrders: () => $(".user-orders"),
    getHistoryBill: () => $(".account-history-bill"),
    getUserCard: () => $(".user-card"),
    // news blogs
    getNewsBtn: () => $$(".news-nav"),
    getNewsBlogs: () => $("#news-blogs-container"),
    // flash sale elements
    getTimeFS: () => $(".fs-time"),
    getFSTable: () => $("#fs-container"),
    getFSCountDown: () => $(".fs-countdown"),
    getHistoryOrder: () => $(".history-tracking-container #history-order-container"),
    //cart
    getCartItems: () => $$(".block-product"), 
    getQuantityInputs: () => $$(".quantity-cart"), 
    getTotalPrice: () => $(".total-price"),  
    getshippingFee: () => $(".shipping-fee"),
    getshippingDiscount: () => $(".shipping-discount"),
    getvoucherDiscount: () => $(".voucher-discount"),
    getPrices:() =>$(".prices"),
    getRemoveButtons: () => $$(".fa-trash"),  
    getSelectAllCheckbox: () => $('#selection-item'),  
    // buttons
    getHomepageBtn: () => $$(".return-homepage"),
    getNavBtn: () => $$(".nav-btn"),
    getPrevBtn: () => $$(".prev-btn"),
    getNextBtn: () => $$(".next-btn"),
    getJsCancelBtn: () => $$(".js-cancel"),
    getServicesBtn: () => $$(".services"),
    getJsCartBtn: () => $$(".cart-btn"),
    getHistoryBtn: () => $$(".history-order-link"),
    getOrderTrackingBtn: () => $$(".order-tracking"),
    getQuantityBox: () => $(".quantity-box"),
    getMobileNavigate: () => $(".header-items.s-m-nav-btn"),
    // account buttons
    getJsAccountBtn: () => $("#user-account"),
    getJsLoginBtn: () => $$(".js-login"),
    getJsRegisterBtn: () => $$(".js-register"),
    getJsForgotBtn: () => $$(".js-forgot-password"),
    getJsSignoutBtn: () => $$(".js-signout"),
    getJsEditBtn: () => $(".js-edit-btn"),
    getJsSubmitBtn: () => $(".js-submit-btn"),
    // search
    getResultContainer: () => $("#search-results-container"),
    getCategoryFilter: () => $("#category-filter"),
    getPriceFilter: () => $("#price-filter"),
    // others
    getScrollTop: () => $("#scroll-top"),
    getDotsBar: () => $$(".dots-bar"),
    getSubMenuNav: () => $(".sub-menu-item.menu-nav"),
    getElementPrices: () => $$(".price"),
    getWebLogo: () => $$(".web-logo div"),
    getImages: () => $$(".product-image.js-item img"),
    getHeaderUserInfo: () => $(".header-user-info"),
    getNoSignIn: () => $("#no-sign-in"),
  };
  return getElements;
}

function throttle(callback, delayTime, key) {
  // create obj key for multi throttle
  if (!throttleList[key]) throttleList[key] = { shouldWait: false };

  return function (...restArgs) {
    let shouldWait = throttleList[key].shouldWait;
  
    if (shouldWait) return;
    callback(...restArgs);
    throttleList[key].shouldWait = true;
    setTimeout(() => (throttleList[key].shouldWait = false), delayTime)};
}

function debounce(callback, delayTime, key) {
  // create obj key for multi debounce
  if (!debounceList[key]) debounceList[key] = { time: 0 };

  return function (...restArgs) {
    clearTimeout(debounceList[key].time);
    debounceList[key].time = setTimeout(() => callback(...restArgs), delayTime);
  };
}

// get promise DOM func (use async await with fetch api)
async function promiseDOMHandler(fileAddress) {
  try {
    const response = await fetch(fileAddress);
    if (!response.ok)
      throw new Error(`${response.status} (${response.statusText})`);
    const text = await response.text();
    return new DOMParser().parseFromString(text, "text/html");
  } catch (error) {
    console.error(`error when fetch your address! \n ${error}`);
  }
}

function navigateRootURL() {
  window.location.replace(`${location.href.slice(0, location.href.lastIndexOf("/") + 1)}`);
}

export default getElementsHandler;
export { $, $$, promiseDOMHandler, throttle, debounce, navigateRootURL };
