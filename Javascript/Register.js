import { showLogin } from "./Actions.js";
import * as Bridge from "./Bridge.js";

function validateRegister() {
  const registerForm = document.querySelector("#register-layout form");
  if (!registerForm) return;
  let nodeFirstName = Bridge.$("#customer-first-name");
  let nodeLastName = Bridge.$("#customer-last-name");
  let nodeEmail = Bridge.$("#customer-email-register");
  let nodePassword = Bridge.$("#customer-password-register");
  let confirmPassword = Bridge.$("#customer-confirm-password-register");
  let users = JSON.parse(localStorage.getItem("users")) || [];
  const emailExists = users.some((user) => user.email === nodeEmail.value.trim());
  const errorMessages = registerForm.querySelectorAll(".error-message");

  registerForm.addEventListener("submit", (event) => {
    event.preventDefault();
    if (emailExists) {
      errorMessages[0].innerHTML = "Email này đã tồn tại!";
      email.addEventListner("focus", () => (errorMessages[0].innerHTML = ""));
      return;
    }

    const passwordRegex = /^[a-zA-Z0-9 !@#$&*%]{8,20}$/;
    if (!passwordRegex.test(nodePassword.value)) {
      errorMessages[1].innerHTML = "Mật khẩu phải từ 8 ký tự trở lên";
      nodePassword.addEventListner("focus", () => (errorMessages[1].innerHTML = ""));
      return;
    }

    if (nodePassword.value !== confirmPassword.value) {
      errorMessages[2].innerHTML = "Mật khẩu không khớp!";
      errorMessages[3].innerHTML = "Mật khẩu không khớp!";
      nodePassword.addEventListner("focus", () => (errorMessages[2].innerHTML = ""));
      confirmPassword.addEventListner("focus", () => (errorMessages[3].innerHTML = ""));
      return;
    }

    const userID = generateId(users);
    let firstName = nodeFirstName.value;
    let lastName = nodeLastName.value;
    let email = nodeEmail.value;
    let password = nodePassword.value;
    const phone = "";
    const address = "";

    const newUser = { userID, firstName, lastName, email, password, phone, address };
    console.log(newUser);
    users.push(newUser);
    localStorage.setItem("users", JSON.stringify(users));

    alert("Đăng ký thành công!");
    registerForm.reset();

    let loginForm = Bridge.$("#login");
    let registForm = Bridge.$("#register");
    let forgotForm = Bridge.$("#forgot-password");
    showLogin(loginForm, registForm, forgotForm);
  });
}

function generateId(users) {
  const prefix = "KH";
  const existingIds = users.map((user) => parseInt(user.userID.slice(2))); // Lấy tất cả số ID hiện có
  const maxId = existingIds.length > 0 ? Math.max(...existingIds) : 0; // Lấy ID lớn nhất hoặc 0 nếu chưa có
  const newIdNumber = maxId + 1; // Tăng ID lên 1
  return prefix + newIdNumber.toString().padStart(3, "0"); // Format ID mới
}

export { validateRegister };