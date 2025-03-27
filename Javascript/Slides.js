'use strict'
import * as Bridge from "./Bridge.js";
import * as Interface from "./Interface.js";

// create Dots
function createDots(parent, totalDots) {
     let dotCount = 0, breakpoint = 46.1875 * 16;
     let container = parent.querySelector(".nav-tab-container");
     let childInners = Array.from(container?.children);
     let dotBar = parent.querySelector(".dots-bar");
   
     if (window.innerWidth <= breakpoint) 
          dotCount = childInners.length;
     else 
          dotCount = Math.ceil(childInners.length / totalDots);
     if (!dotBar) return;

     dotBar.innerHTML = "";
     if (dotCount === 1) return;
     for (let i = 0; i < dotCount; i++) {
          let dot = document.createElement("div");

          if (i == 0) dot.classList.add("active");
          dot.classList.add("dot");
          dotBar.appendChild(dot);
     }
}

// slide handler with name that need to use slide's behavior
function slidesHandler(...names) {
     let parent, nameSlide;
     const elementsObj = Bridge.default();

     // params is name of html need to use add behavior
     // default flag dots is "true" / add "else if" & change flag to "false" if not need dot 
     names.forEach((name) => {
          if (name.includes("news")) {
               parent = elementsObj.getNewsBlogs();
               nameSlide = (parent?.querySelector(".news-blogs-items"));
               if (nameSlide)
                    behaviorSlides(parent, nameSlide, 3);
          }

     });
}

// get buttons and add behavior for slide
function behaviorSlides(parent, nameSlide, showCount) {
     let slidesIndex = 1;
     showCount = showCount ? showCount : 1; //check if showCount is falsy or not
     let haveDots = parent.querySelector(".dots-bar");
     let prevButtons = parent.querySelector(".prev-btn");
     let nextButtons = parent.querySelector(".next-btn");
     let container = nameSlide.children;
     // check if container is empty or not 
     if (container.length === 0) return;
     container = Array.from(container);

     // first call init for create dot
     let dots = (haveDots) ? createDots(parent, showCount) : null;
     dots = parent.querySelectorAll(".dot");

     if (nextButtons) {
          nextButtons.addEventListener("click", Bridge.throttle(() => {
               if (++slidesIndex > container.length)
                    slidesIndex = 1;
               showSlides(container, dots, slidesIndex, showCount);
          }, 200, "nextBtn"));
     }

     if (prevButtons) {
          prevButtons.addEventListener("click", Bridge.throttle(() => {
               if (--slidesIndex < 1)
                    slidesIndex = container.length;
               showSlides(container, dots, slidesIndex, showCount);
          }, 200, "nextBtn"));
     }

     // resize handler for set active 
     window.addEventListener("resize", Bridge.debounce(() => {
          createDots(parent, showCount);
          dots = (haveDots) ? parent.querySelectorAll(".dot") : null;
          showSlides(container, dots, slidesIndex, showCount);
     }, 150, "activeItems"));
     showSlides(container, dots, slidesIndex, showCount);
}

// slides active / hidden
function showSlides(slidesContainer, dots, slidesIndex, showCount) {
     let i, breakpoint = 46.1875 * 16;
     // check when screen is mobile or larger mobile for active elements nav
     if (window.innerWidth <= breakpoint) showCount = 1;

     // reset display slides
     for (i = 0; i < slidesContainer.length; i++)
          if (slidesContainer[i].classList.contains("active"))
               slidesContainer[i].classList.remove("active");

     if (dots && dots[0]) {
          // remove active dot 
          for (i = 0; i < dots.length; i++)
               if (dots[i].classList.contains("active"))
                    dots[i].classList.remove("active");

          // dots behavior
          i = Math.floor((slidesIndex - 1) / 3); //calc i for now index (case screen larger than mobile)
          if (window.innerWidth <= breakpoint)  //case for mobile screen
               i = slidesIndex - 1;
          dots[i].classList.add("active");

          // add event listener when click dots
          dots.forEach((dot, index) => {
               dot.addEventListener("click", Bridge.throttle(() => {
                    if (!dot.classList.contains("active")) {
                         // after run code add active and codes below it showCount will decrease and equal 0
                         slidesIndex = 1  // set default slidesIndex = init value of slidesIndex
                         slidesIndex += showCount * index;
                         showSlides(slidesContainer, dots, slidesIndex, showCount);
                    }
               }, 200, "dotBehavior"));
          });
     }

     // add class active to show now items
     let tempCount = showCount;
     while (tempCount > 0) {
          if (slidesIndex > slidesContainer.length) slidesIndex = 1;
          slidesContainer[slidesIndex - 1].classList.add("active");
          slidesIndex++, tempCount--;
     }
}
 
export { createDots, slidesHandler };