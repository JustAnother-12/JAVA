document.addEventListener("DOMContentLoaded", function () {
  // ====================== [PHẦN XỬ LÝ FORM ĐĂNG KÝ] ======================
  const form = document.querySelector("form");
  const password = document.getElementById("customer-password-register");
  const confirmPassword = document.getElementById(
    "customer-confirm-password-register"
  );
  const email = document.getElementById("customer-email-register");

  // Hàm hiển thị lỗi
  function showError(inputElement, message) {
    const errorDiv = inputElement.nextElementSibling;
    errorDiv.textContent = message;
  }

  // Hàm xoá toàn bộ lỗi
  function clearAllErrors() {
    document
      .querySelectorAll(".error-message")
      .forEach((div) => (div.textContent = ""));
  }

  // Kiểm tra mật khẩu có khớp không
  function isPasswordMatch(pass, confirmPass) {
    return pass === confirmPass;
  }

  // Kiểm tra định dạng email
  function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  // Hàm tổng kiểm tra form
  function validateRegisterForm() {
    let isValid = true;
    clearAllErrors();

    if (!isPasswordMatch(password.value, confirmPassword.value)) {
      showError(confirmPassword, "Mật khẩu không khớp.");
      isValid = false;
    }

    if (!isValidEmail(email.value)) {
      showError(email, "Email không hợp lệ.");
      isValid = false;
    }

    return isValid;
  }

  // Gắn sự kiện submit nếu có form
  if (form) {
    form.addEventListener("submit", function (e) {
      if (!validateRegisterForm()) {
        e.preventDefault(); // Ngăn gửi form nếu có lỗi
      }
    });
  }

  // ====================== [PHẦN XỬ LÝ HEADER TÀI KHOẢN] ======================
  fetch("/gui/account/user_session.php")
    .then((res) => res.json())
    .then((data) => {
      const noSignIn = document.getElementById("no-sign-in");
      const userInfo = document.querySelector(".header-user-info");
      const userName = document.querySelector(".user-name");
      const btnLogin = document.querySelector("button[title='Đăng nhập']");
      const btnRegister = document.querySelector("button[title='Đăng ký']");
      const btnLogout = document.querySelector("button[title='Đăng xuất']");

      if (data.loggedIn) {
        if (noSignIn) noSignIn.classList.add("disable");
        if (userInfo) userInfo.classList.remove("disable");
        if (userName) userName.textContent = data.username;
        if (btnLogin) btnLogin.classList.add("disable");
        if (btnRegister) btnRegister.classList.add("disable");
        if (btnLogout) btnLogout.classList.remove("disable");

        if (btnLogout) {
          btnLogout.onclick = function () {
            fetch("/gui/account/logout.php")
              .then(() => {
                window.location.href = "/index.php"; // Chuyển hướng đến trang index.php
              });
          };
        }
        
      }
    });
});
