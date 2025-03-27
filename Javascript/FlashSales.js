"use strict";
import { categoryIsEmpty } from "./Interface.js";
import * as Bridge from "./Bridge.js";

// active flash sale event
function activeFlashSale() {
  const date = new Date();
  localStorage.setItem("flashSaleTime", date.toLocaleTimeString("it-IT"));
  return localStorage.getItem("flashSaleTime");
}

// funcs for count down time flash sale events
function setTimeFS(elementsObj) {
  let countDown = elementsObj.getFSCountDown();
  let fSTime = elementsObj.getTimeFS();

  if (!localStorage.getItem("activeFlashSale"))
    activeFlashSale();

  if (countDown && fSTime) {
    let stringTime = localStorage.getItem("flashSaleTime");
    fSTime = Array.from(fSTime.children);

    // execute
    if (!stringTime) 
      stringTime = activeFlashSale();
    if (stringTime?.includes("-")) {
      localStorage.removeItem("flashSaleTime");
      return;
    }

    if (stringTime === "00:00:00") {
      localStorage.removeItem("flashSaleTime");
      let timeCount = Bridge.$(".fs-countdown");
      timeCount.innerHTML = '<p class="s-m-hidden padding-right-8 font-size-20 font-bold">Đã hết hạn</p>';
    }

    let index = 0;
    let timeArray = [];
    stringTime = stringTime?.split(":");
    fSTime.forEach((time) => {
      if (time.classList.contains("fs-number")) {
        time.innerText = stringTime[index];
        timeArray.push(time);
        index++;
      }
    });
    startCountDown(timeArray, elementsObj);
  }
}

async function startCountDown(timeArray, elementsObj) {
  try {
    window.addEventListener("beforeunload", () => {
      localStorage.setItem("flashSaleTime", `${timeArray[0].innerText}:${timeArray[1].innerText}:${timeArray[2].innerText}`
      );
    });

    if (timeArray[2].innerText !== "00")
      timeArray[2].innerText = await countDown(timeArray[2], "seconds");

    if (timeArray[2].innerText === "00" && timeArray[1].innerText !== "00") {
      timeArray[2].innerText = "60";
      timeArray[1].innerText = await countDown(timeArray[1], "minutes");
    } else if (timeArray[2].innerText === "00" && timeArray[1].innerText === "00" && timeArray[0].innerText !== "00") {
      timeArray[1].innerText = "60";
      timeArray[0].innerText = await countDown(timeArray[0], "minutes");
    }

    if (timeArray[1].innerText !== "00" || timeArray[2].innerText !== "00" || timeArray[0].innerText !== "00")
      startCountDown(timeArray);
    else {
      const container = elementsObj
        .getFSTable()
        .querySelector(".product-container");
      if (!container) throw new Error("not found flash sale product!");
      container.innerHTML = "";
      categoryIsEmpty();
      localStorage.removeItem("activeFlashSale");
    }
  } catch (error) {
    console.error(error);
  }
}

function countDown(timeHandler, typeTime) {
  return new Promise((resolve, reject) => {
    if (
      !timeHandler ||
      isNaN(parseInt(timeHandler.innerText)) ||
      parseInt(timeHandler.innerText) <= 0
    )
      reject(new Error("invalid time!"));
    const timeLength = timeHandler.innerText;

    if (typeTime === "seconds") {
      for (let i = 0; i < timeLength; i++) {
        setTimeout(() => {
          let currentTime = parseInt(timeHandler.innerText);
          timeHandler.innerText = (--currentTime).toString().padStart(2, "0");
          if (timeHandler.innerText === "00") resolve(timeHandler.innerText);
        }, 1000 * i);
      }
    } else if (typeTime === "minutes" || typeTime === "hours") {
      let currentTime = parseInt(timeHandler.innerText);
      timeHandler.innerText = (--currentTime).toString().padStart(2, "0");
      resolve(timeHandler.innerText);
    } else reject(new Error("error time!"));
  });
}

export { setTimeFS, activeFlashSale };
