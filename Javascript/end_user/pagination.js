import { attachAddToCartEvents, updateCartCount } from './cart.js';
document.addEventListener("DOMContentLoaded", function () {
  let currentPage = 1;
  const resultsContainer = document.querySelector(".product-container");
  const paginationContainer = document.createElement("div");
  paginationContainer.classList.add("pagination");
  resultsContainer.after(paginationContainer);

  function fetchProducts(page) {
    const urlParams = new URLSearchParams(window.location.search);
    const query = urlParams.get("query") || "";
    const type = urlParams.get("type") || "";
    const minPrice = urlParams.get("minPrice") || "0";
    const maxPrice = urlParams.get("maxPrice") || "999999999";
    const isFeatured = urlParams.get("featured") === "true"; // Kiểm tra tham số featured

    let url = `fetch_products.php?page=${page}&query=${query}&type=${type}&minPrice=${minPrice}&maxPrice=${maxPrice}`;

    if (isFeatured) {
      url = `fetch_products.php?featured=true&query=${query}&type=${type}&minPrice=${minPrice}&maxPrice=${maxPrice}`;
    }

    fetch(url)
      .then((response) => response.json())
      .then((data) => {
        renderProducts(data.products);
        if (!isFeatured && data.totalPages) {
          renderPagination(data.totalPages, page);
        } else {
          paginationContainer.innerHTML = ""; // Xóa nút phân trang nếu là featured
        }
      })

      .catch((error) => console.error("Lỗi tải dữ liệu:", error));
  }

  function renderProducts(products) {
    resultsContainer.innerHTML = "";
    if (products.length === 0) {
      resultsContainer.innerHTML = "<p>Không tìm thấy sản phẩm nào.</p>";
      return;
    }

    products.forEach((product) => {
      const productHtml = `
        <div class="product-item grid-col col-l-2-4 col-m-3 col-s-6">
          <div class="block-product product-resize">
            <a href="/gui/game_detail.php?id=${product.ProductID}" class="product-image js-item">
  <img src="${product.ProductImg}" alt="${product.ProductName}">
</a>
            <div class="sale-off font-bold capitalize ${
              product.Quantity > 0 ? "" : "active"
            }">Hết hàng</div>
            <div class="info-inner flex justify-center align-center line-height-1-6">
              <h4 class="font-light capitalize" title="${
                product.ProductName
              }">${product.ProductName}</h4>
              <div class="margin-y-4">
                <span class="price font-bold">
                  ${
                    product.Price > 0
                      ? new Intl.NumberFormat().format(product.Price) + " đ"
                      : "Miễn phí"
                  }
                </span>
              </div>
            </div>
          </div>
          <div class="action ${product.Quantity > 0 ? "" : "disable"}">
            <div class="add-to-cart">
              <div title="Thêm vào giỏ hàng" class="button add-to-cart-btn" data-id="${product.ProductID}">
              <i class="fa-solid fa-plus" style="color: var(--primary-white);"></i>
              </div>
            </div>
          </div>
        </div>
      `;
      resultsContainer.innerHTML += productHtml;
    });
    attachAddToCartEvents();
    updateCartCount();
    
  }

  function renderPagination(totalPages, currentPage) {
    paginationContainer.innerHTML = "";

    if (totalPages <= 1) return;

    for (let i = 1; i <= totalPages; i++) {
      const pageBtn = document.createElement("button");
      pageBtn.textContent = i;
      pageBtn.classList.add("page-btn");
      if (i === currentPage) pageBtn.classList.add("active");
      pageBtn.addEventListener("click", () => {
        fetchProducts(i);
      });
      paginationContainer.appendChild(pageBtn);
    }
  }


  fetchProducts(currentPage);
});
