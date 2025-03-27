'use strict'
import * as Interface from "./Interface.js";
import * as Bridge from "./Bridge.js";
import { dynamicDetail, getValueQuery } from "./Products.js";
import { renderSearchDOM } from "./search.js";

function sleep(ms) {
     return new Promise(resolve => setTimeout(resolve, ms));
}

// funcs execute url
function execQueryHandler(request) {
     // case not have
     if (!request) {
          let href = location.href; 
          request = href.slice(href.lastIndexOf("?") + 1, href.lastIndexOf("="));
     }

     // case have request
     let query = getValueQuery(request);
     let url = location.href;
     url = url.slice(url.lastIndexOf("/") + 1);
     let productsList = JSON.parse(localStorage.getItem("products"));
     if (query && request === "name") {
          let product = productsList.find((item) => (item.name).replaceAll("&", "").replaceAll("!", "").replaceAll(" ", "-") === query);
          if (Bridge.$("#detail-content").classList.contains("disable"))
               Interface.hiddenException("detail-content");
          dynamicDetail(product);
          return;
     }
     else if (query && request === "query") {
          return query;
     }
     return "";
}

// func for popstate listener (it's will be very long)
function popStateHandler() {
     window.addEventListener("popstate", Bridge.throttle(() => {
          let url = location.href;
          let container = Bridge.default().getMainContainer();
          let path = url.slice(url.lastIndexOf("/") + 1, url.length);
          if (!path || path.includes("index")) {
               // Bridge.navigateRootURL();
               Interface.hiddenException();
          }
          if (path.includes("?query=")) {
               let bookName = execQueryHandler("query");
               renderSearchDOM(bookName);
               Interface.fakeOverlay(container, 150);
          }

          execQueryHandler("name");
     }, 200, "popstate"));
}

function forbiddenDOM() {
     let href = location.href;
     let path = href.slice(href.lastIndexOf("/HTML/") + 5, href.length);
     if (path.includes("header_footer"))
          window.location.replace(`${location.href.slice(0, location.href.lastIndexOf("/HTML/") + 6)}`);
}

export { popStateHandler, execQueryHandler, forbiddenDOM, sleep }