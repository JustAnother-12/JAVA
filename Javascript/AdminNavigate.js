import { activeSideBar, hiddenException } from "./JSAdmin.js";
import { convertBase64, getItemFromLocalStorage, setItemFromLocalStorage } from "./utils.js";
import { isEmail, isEmpty, isExistedProductID, isExistedUserEmail, isExistedUserID, isNumber, isValidProductID, isValidUserID } from "./validation.js";

const loginComponent = document.querySelector(".login-component");
const body = document.querySelector(".body");
const adminAccount = { username: "admin", password: "123" };

// Check login
(() => {
  const isAdminLoggedIn = JSON.parse(localStorage.getItem("isAdminLoggedIn"));
  if (!isAdminLoggedIn) {
    body.classList.add("disable");
    loginComponent.classList.remove("disable");
  }
  else {
    loginComponent.classList.add("disable");
    body.classList.remove("disable");
  }
})();

// Seed products
(async () => {
  if (!getItemFromLocalStorage("products")) await storeProducts();
})();

// Login
const login = () => {
  const handleValidation = () => {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const usernameError = document.querySelector(".username-error");
    const passwordError = document.querySelector(".password-error");

    if (username !== adminAccount.username) {
      usernameError.textContent = "Username not found.";
      usernameError.style.color = "red";
      return false;
    }
    usernameError.textContent = "";
    usernameError.style.color = "";

    if (password !== adminAccount.password) {
      passwordError.textContent = "Password is incorrect.";
      passwordError.style.color = "red";
      return false;
    }
    passwordError.textContent = "";
    passwordError.style.color = "";

    return true;
  };

  if (handleValidation()) {
    localStorage.setItem("isAdminLoggedIn", true);
    window.location.reload();
  }
};
document.querySelector(".login-btn").addEventListener("click", login);

// Logout
const logout = () => {
  localStorage.setItem("isAdminLoggedIn", false);
  window.location = "index.html";
};
document.querySelector(".logout-btn").addEventListener("click", logout);

// Store products
async function storeProducts() {
  try {
    const storage = await fetch("../Javascript/Storage.js");
    const jsonArray = await storage.json();
    const productsList = Array.from(jsonArray);
    localStorage.setItem("products", JSON.stringify(productsList));
  } catch (error) {
    console.error("Error fetching products:", error);
  }
}


// Handle validations
const handleValidationProduct = (fields) => {
  const fieldsArray = Object.entries(fields);
  const errorFields = document.querySelectorAll(".error-message");
  let errors = [];
  fieldsArray.forEach((field, index) => {
    const key = field[0];
    const value = field[1];
    if ((key.toLowerCase() === "img" && value.size === 0) || isEmpty(value)) {
      errorFields[index].innerText = `${key} is required`;
      errors.push(index);
    } else if (key.toLowerCase() === "productid" && !isValidProductID(value)) {
      errorFields[index].innerText = "Product ID is invalid";
      errors.push(index);
    } else if (
      key.toLowerCase() === "productid" &&
      isExistedProductID(getItemFromLocalStorage("products"), value)
    ) {
      errorFields[index].innerText = "Product ID is existed";
      errors.push(index);
    } else if (
      (key.toLowerCase() === "price" ||
        key.toLowerCase() === "quantity" ||
        key.toLowerCase() === "sale" 
        ) &&
      !isNumber(value)
    ) {
      errorFields[index].innerText = `${key} is invalid`;
      errors.push(index);
    } else {
      errorFields[index].innerText = "";
      errors = errors.filter((item) => item !== index);
    }
  });

  return errors.length === 0;
};

// ==================================== PRODUCTS MANAGEMENT ====================================

const addBookForm = document.querySelector(".add-book-form");
const overclass = document.querySelector(".overclass");


// Add
const handleAddBook = async (e) => {
  e.preventDefault();
  const form = new FormData(e.target);
  const data = Object.fromEntries(form.entries());
  const products = getItemFromLocalStorage("products");
  if (handleValidationProduct(data)) {
    data.img = await convertBase64(data.img);
    products.push(data);
    setItemFromLocalStorage("products", products);
    document.querySelector(".overclass").style.display = "none";
    showProductTable();
  }
};
document
  .querySelector(".add-book-form")
  .addEventListener("submit", handleAddBook);
addBookForm.addEventListener("submit", handleAddBook);


// Update
window.showEditBookForm = function (productID) {
  try {
    // Đọc và parse dữ liệu từ localStorage
    const products = JSON.parse(localStorage.getItem("products"));
    
    // Kiểm tra nếu không có sản phẩm nào trong localStorage
    if (!products || products.length === 0) {
      console.error("Danh sách sản phẩm rỗng hoặc không tồn tại");
      return;
    }

    // Tìm sản phẩm theo productID
    const sp = products.find((product) => product.productID === productID);
    if (!sp) {
      console.error("Không tìm thấy sản phẩm với mã sản phẩm:", productID);
      return;
    }

    console.log("Sản phẩm tìm thấy:", sp);

    // Hiển thị thông tin sản phẩm trong form sửa
    
    let khung = document.getElementById("khungSuaSanPham");
    if (!khung) {
      // Tạo phần tử nếu không tồn tại
       khung = document.createElement("div");
      khung.id = "khungSuaSanPham";
      khung.classList.add("overclass");
      document.body.appendChild(khung);
    }
    if (!khung) {
      console.error("Không tìm thấy phần tử với ID 'khungSuaSanPham'");
      return;
    }

    khung.innerHTML = `
   <span class="close" onclick="this.parentElement.style.transform = 'scale(0)';">
      <i class="fa-solid fa-xmark"></i>
    </span>
    <form class="edit-book-form">
      <table class="overlayTable table-header table-content">
        <tr>
          <th colspan="2">Sửa Sách: ${sp.name}</th>
        </tr>
        <tr>
          <td>Product ID</td>
          <td>
            <input type="text" name="productID" value="${sp.productID}" readonly />
            <div class="error-message"></div>
          </td>
        </tr>
        <tr>
          <td>Name</td>
          <td>
            <input type="text" name="name" value="${sp.name}" />
            <div class="error-message"></div>
          </td>
        </tr>
        <tr>
          <td>Category</td>
          <td>
            <input type="text" name="category" value="${sp.category}" />
            <div class="error-message"></div>
          </td>
        </tr>
        <tr>
          <td>Author</td>
          <td>
            <input type="text" name="author" value="${sp.author}" />
            <div class="error-message"></div>
          </td>
        </tr>
        <tr>
          <td>Type</td>
          <td>
            <select name="type">
              <option value="">----- Select Type -----</option>
              ${["Manga", "Light Novel", "Sách Văn Học", "Sách Thiếu Nhi", "Sách Công Nghệ", "Sách Học Ngữ"].map(
                (type) => `<option value="${type}" ${sp.type === type ? "selected" : ""}>${type}</option>`
              ).join("")}
            </select>
            <div class="error-message"></div>
          </td>
        </tr>
        <tr>
          <td>Description</td>
          <td>
            <textarea name="description">${sp.description || ""}</textarea>
            <div class="error-message"></div>
          </td>
        </tr>
        <tr>
          <td>Image</td>
          <td>
            ${
              sp.img
                ? `<img class="hinhDaiDien" src="${sp.img}" alt="Sản phẩm" />
                   <a onclick="xoaAnhSanPham('${sp.productID}')">Xóa hình</a>`
                : ""
            }
            <input type="file" accept="image/*" />
            <div class="error-message"></div>
          </td>
        </tr>
        <tr>
          <td>Price (₫)</td>
          <td>
            <input type="text" name="price" value="${sp.price}" />
            <div class="error-message"></div>
          </td>
        </tr>
        <tr>
          <td>Quantity</td>
          <td>
            <input type="text" name="quantity" value="${sp.quantity}" />
            <div class="error-message"></div>
          </td>
        </tr>
        <tr>
          <td>Release</td>
          <td>
            <input type="text" name="release" value="${sp.release}" />
            <div class="error-message"></div>
          </td>
        </tr>
        <tr>
          <td>Format</td>
          <td>
            <input type="text" name="format" value="${sp.format}" />
            <div class="error-message"></div>
          </td>
        </tr>
        <tr>
          <td>Genre</td>
          <td>
            <input type="text" name="genre" value="${sp.genre}" />
            <div class="error-message"></div>
          </td>
        </tr>
        <tr>
          <td>Sale</td>
          <td>
            <input type="text" name="sale" value="${sp.sale}" />
            <div class="error-message"></div>
          </td>
        </tr>
        <tr>
          <td>Package Size</td>
          <td>
            <input type="text" name="packagingSize" value="${sp.packagingSize}" />
            <div class="error-message"></div>
          </td>
        </tr>
        <tr>
          <td colspan="2" class="table-footer">
            <button type="button" class="btn-save" onclick=suaSanPham(${sp.masp})>LƯU THAY ĐỔI</button>
          </td>
        </tr>
      </table>
    </form>`;
    khung.style.transform = "scale(1)"; // Hiển thị form sửa sản phẩm
  } catch (error) {
    console.error("Lỗi khi đọc sản phẩm từ localStorage:", error);
  }
};


async function updateSanPham(productID) {
  try {
    // Lấy danh sách sản phẩm từ localStorage
    const products = JSON.parse(localStorage.getItem("products"));
    if (!products || products.length === 0) {
      console.error("Danh sách sản phẩm rỗng hoặc không tồn tại");
      return;
    }

    // Tìm sản phẩm cần cập nhật
    const spIndex = products.findIndex((product) => product.productID === productID);
    if (spIndex === -1) {
      console.error("Không tìm thấy sản phẩm với mã sản phẩm:", productID);
      return;
    }

    // Lấy form chỉnh sửa sản phẩm
    const form = document.querySelector(".edit-book-form");
    if (!form) {
      console.error("Không tìm thấy form chỉnh sửa sản phẩm");
      return;
    }

    // Lấy dữ liệu từ form
    const formData = new FormData(form);
    const updatedProduct = Object.fromEntries(formData.entries());

    // Xác thực dữ liệu bằng handleValidationProduct
    if (!handleValidationProduct(updatedProduct)) {
      console.error("Dữ liệu không hợp lệ");
      alert("Vui lòng kiểm tra lại dữ liệu nhập vào!");
      return;
    }

    // Kiểm tra và chuyển ảnh sang Base64 (nếu có)
    const fileInput = form.querySelector("input[name='img']");
    if (fileInput && fileInput.files && fileInput.files[0]) {
      const convertBase64 = async (file) => {
        return new Promise((resolve, reject) => {
          const reader = new FileReader();
          reader.onload = () => resolve(reader.result);
          reader.onerror = (error) => reject(error);
          reader.readAsDataURL(file);
        });
      };
      updatedProduct.img = await convertBase64(fileInput.files[0]);
    } else {
      updatedProduct.img = products[spIndex].img; // Giữ nguyên ảnh cũ nếu không thay đổi
    }

    // Cập nhật sản phẩm trong danh sách
    products[spIndex] = { ...products[spIndex], ...updatedProduct };

    // Lưu lại vào localStorage
    localStorage.setItem("products", JSON.stringify(products));
    console.log("Sản phẩm đã được cập nhật:", updatedProduct);

    // Hiển thị thông báo thành công
    alert("Sản phẩm đã được lưu thay đổi!");

    // Ẩn khung sửa sản phẩm
    const khung = document.getElementById("khungSuaSanPham");
    if (khung) {
      khung.style.transform = "scale(0)";
    }

    // Cập nhật lại giao diện
    if (typeof showProductTable === "function") {
      showProductTable();
    }
  } catch (error) {
    console.error("Lỗi khi cập nhật sản phẩm:", error);
  }
}





// Delete
window.handleDeleteBook = (productID) => {
  const ok = confirm("Are you sure you want to delete the product?");
  if (ok) {
    let products = getItemFromLocalStorage("products");
    products = products.filter(
      (product) => product.productID.toString() !== productID.toString()
    );
    setItemFromLocalStorage("products", products);
    showProductTable();
  }
};

// Search
// Show products table
const productNavItem = document.querySelector(".opensanpham");
const productConponent = document.querySelector(".sanpham");
const tk_comp = document.querySelector('.thongke');
const tk_nav = document.querySelector(".openthongke");
const dh_comp = document.querySelector('.donhang');
const dh_nav = document.querySelector(".opendonhang");
const showProductTable = () => {
  if (customerComponent) {
    if (productConponent) {
      customerComponent.classList.remove("open");
      customerNavItem.classList.remove("action");
    }
    if(tk_comp){
      tk_comp.classList.remove("open");
      tk_nav.classList.remove("action");
    }
    if(dh_comp){
      dh_comp.classList.remove("open");
      dh_nav.classList.remove("action");
    }
    productConponent.classList.add("open");
    activeSideBar("opensanpham");
    hiddenException("sanpham");
    showProductContent();
  }

  function showProductContent() {
    const tableContent = productConponent.querySelector("tbody");
    const products = getItemFromLocalStorage("products");
    let content = "<table>";
    content += products
      .map((product) => {
        return `
                <tr>
                    <td title="Sắp xếp">${product.productID}</td>
                    <td title="Sắp xếp">${product.name}</td>
                    <td title="Sắp xếp">${product.author}</td>
                    <td title="Sắp xếp">${product.sale}</td>
                    <td title="Sắp xếp">${product.price}</td>
                    <td title="Sắp xếp">${product.quantity}</td>
                    <td title="Sắp xếp">${product.type}</td>
                    <td title="Sắp xếp" style="text-align: center">
                        <i class="fa-solid fa-pen-to-square" style="cursor:pointer;margin: 0 4px"onclick="showEditBookForm('${product.productID}')"></i>

                        <i class="fa-regular fa-trash-can" style='cursor:pointer;margin: 0 4px' onclick="handleDeleteBook('${product.productID}')"></i>
                    </td>
                </tr>`;
      })
      .join("");
    content += "</table>";
    tableContent.innerHTML = content;
  }
};
productNavItem.addEventListener("click", showProductTable);

// Reset form
const resetForm = () => {
  addBookForm.querySelector("input[name='name']").value = "";
  addBookForm.querySelector("input[name='category']").value = "";
  addBookForm.querySelector("input[name='author']").value = "";
  addBookForm.querySelector("select[name='type']").value = "";
  addBookForm.querySelector("textarea[name='description']").value = "";
  // addBookForm.querySelector("input[name='img']").valu="";
  addBookForm.querySelector("input[name='price']").value = "";
  addBookForm.querySelector("input[name='quantity']").value = "";
  addBookForm.querySelector("input[name='release']").value = "";
  addBookForm.querySelector("input[name='format']").value = "";
  addBookForm.querySelector("input[name='genre']").value = "";
  addBookForm.querySelector("input[name='sale']").value = "";
  addBookForm.querySelector("input[name='packagingSize']").value = "";
};

// ==================================== CUSTOMERS MANAGEMENT ====================================

// Show customers table
const customerNavItem = document.querySelector(".openkhachhang");
const customerComponent = document.querySelector(".khachhang");
const showCustomerTable = (filteredUsers) => {
  const users = filteredUsers || getItemFromLocalStorage("users");
  if (customerComponent) {
    if (productConponent) {
      productConponent.classList.remove("open");
      productNavItem.classList.remove("action");
    }
    if(dh_comp){
      dh_comp.classList.remove("open");
      dh_nav.classList.remove("action");
    }
    if(tk_comp){
      tk_comp.classList.remove("open");
      tk_nav.classList.remove("action");
    }
    customerComponent.classList.add("open");
    activeSideBar("openkhachhang");
    hiddenException("khachhang")
    showUserContent(users);
  }

  function showUserContent(users) {
    const tableContent = customerComponent.querySelector("tbody");
    let content = "<table>";
    content += users
      .map((user) => {
        return `<tr>
                <td title="Sắp xếp">${user.userID}</td>
                <td title="Sắp xếp">${user.firstName}</td>
                <td title="Sắp xếp">${user.lastName}</td>
                <td title="Sắp xếp">${user.email}</td>
                <td title="Sắp xếp" style="text-align: center">
                    <i class="fa-solid fa-pen-to-square" style="cursor:pointer;margin: 0 4px" onclick="showEditUser('${user.userID}')"></i>
                    <i class="fa-regular fa-trash-can" style="cursor:pointer;margin: 0 4px" onclick="handleDeleteUser('${user.userID}')"></i>
                </td>
              </tr>`;
      })
      .join("");
    content += "</table>";
    tableContent.innerHTML = content;
  }
};
customerNavItem.addEventListener("click", () => showCustomerTable());

// Delete
window.handleDeleteUser = (userID) => {
  const ok = confirm("Are you sure you want to delete this user?");
  if (ok) {
    let users = getItemFromLocalStorage("users");
    users = users.filter(
      (user) => user.userID.toString() !== userID.toString()
    );
    setItemFromLocalStorage("users", users);
    showCustomerTable();
  }
};

const addUserForm = document.querySelector(".add-user-form");
const handleValidationUser = (fields) => {
  const fieldsArray = Object.entries(fields);
  const errorFields = addUserForm.querySelectorAll(".error-message");
  let errors = [];
  fieldsArray.forEach((field, index) => {
    const key = field[0];
    const value = field[1];
    if (isEmpty(value)) {
      errorFields[index].innerText = `${key} is required`;
      errors.push(index);
    } else if (key.toLowerCase() === "userid" && !isValidUserID(value)) {
      errorFields[index].innerText = "User ID is invalid";
      errors.push(index);
    } else if (
      key.toLowerCase() === "userid" &&
      isExistedUserID(getItemFromLocalStorage("users"), value)
    ) {
      errorFields[index].innerText = "User ID is existed";
      errors.push(index);
    } else if (key.toLowerCase() === "email" && !isEmail(value)) {
      errorFields[index].innerText = "Email is invalid";
      errors.push(index);
    } else if (
      key.toLowerCase() === "email" &&
      isExistedUserEmail(getItemFromLocalStorage("users"), value)
    ) {
      errorFields[index].innerText = "Email is existed";
      errors.push(index);
    } else {
      errorFields[index].innerText = "";
      errors = errors.filter((item) => item !== index);
    }
  });
  return errors.length === 0;
};

// Add
const handleAddUser = (e) => {
  e.preventDefault();
  const form = new FormData(e.target);
  const data = Object.fromEntries(form.entries());
  const users = getItemFromLocalStorage("users");
  if (handleValidationUser(data)) {
    users.push(data);
    setItemFromLocalStorage("users", users);
    addUserForm.closest(".overclass").style.display = "none";
    showCustomerTable();
  }
};
addUserForm.addEventListener("submit", handleAddUser);

// Search
const searchInput = document.querySelector(
  ".khachhang input[name='searchInput']"
);
const onSearchInputChange = (e) => {
  const filtered = (users, search) => {
    return users.filter((user) => {
      return (
        user.firstName.toLowerCase().includes(search) ||
        user.lastName.toLowerCase().includes(search) ||
        user.email.toLowerCase().includes(search)
      );
    });
  };

  const value = e.target.value.trim().toLowerCase();
  const users = getItemFromLocalStorage("users");
  const filteredUsers = filtered(users, value);
  if (value === "") showCustomerTable();
  else showCustomerTable(filteredUsers);
};
searchInput.addEventListener("change", onSearchInputChange);


