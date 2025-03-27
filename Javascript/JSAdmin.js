import * as Admin from "./admin.js";
import * as AdminNavigate from "./AdminNavigate.js";
import * as utils from "./utils.js";
import * as Validation from "./validation.js";
import * as ThongKe from "./thongke.js";
import * as DonHang from "./donhang.js";

function activeSideBar(exception) {
     exception = !exception ? "opentrangchu" : exception;
     let container = document.querySelector(".sidebar .nav")?.children;
     container = Array.of(...container);
     container.forEach((element) => {
       if (!element.classList.contains(exception))
         element.classList.remove("action");
       else
         element.classList.add("action");
     });
}

function hiddenException(exception) {
     exception = !exception ? "trangchu" : exception;
     let container = document.querySelector(".main").children;
     container = Array.of(...container);
     console.log(container);
   
     container.forEach((element) => {
          if (element.classList.contains(exception) || element.classList.contains("overclass"))
            element.classList.remove("disable");
          else
            element.classList.add("disable");
        });
}
export { activeSideBar, hiddenException }
