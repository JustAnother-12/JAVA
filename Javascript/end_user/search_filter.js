document.addEventListener("DOMContentLoaded", function () {
  const searchParams = new URLSearchParams(window.location.search);
  const searchInput = document.getElementById("search-input");
  const searchButton = document.querySelector(".search-btn");
  const filterButtons = document.querySelectorAll(".filter-btn");
  const priceButtons = document.querySelectorAll(".price-filter");

  const currentKeyword = searchParams.get("query") || "";
  const currentType = searchParams.get("type") || "";
  const currentMinPrice = searchParams.get("minPrice") || "";
  const currentMaxPrice = searchParams.get("maxPrice") || "";
  const currentFeatured = searchParams.get("featured") || "";

  if (searchInput) searchInput.value = currentKeyword;

  //Hàm build URL giữ tất cả tham số đang có
  function buildURL(extraParams = {}) {
    const urlParams = new URLSearchParams(window.location.search);

    // Cập nhật hoặc thêm các param mới
    Object.entries(extraParams).forEach(([key, value]) => {
      if (value) {
        urlParams.set(key, value);
      } else {
        urlParams.delete(key);
      }
    });

    return "/gui/shop.php?" + urlParams.toString();
  }

  //Tìm kiếm khi nhấn Enter
  if (searchInput) {
    searchInput.addEventListener("keypress", function (event) {
      if (event.key === "Enter") {
        event.preventDefault();
        const keyword = searchInput.value.trim();
        const url = buildURL({ query: keyword });
        window.location.href = url;
      }
    });
  }

  //Tìm kiếm khi nhấn nút tìm
  if (searchButton) {
    searchButton.addEventListener("click", function () {
      const keyword = searchInput.value.trim();
      const url = buildURL({ query: keyword });
      window.location.href = url;
    });
  }

  //Lọc theo thể loại
  filterButtons.forEach((button) => {

    button.addEventListener("click", function (event) {
      event.preventDefault();
      const type = this.getAttribute("data-type");
      const url = buildURL({ type });
      window.location.href = url;
    });
  });

  //Lọc theo giá tiền
  priceButtons.forEach((button) => {
    const min = button.getAttribute("data-min");
    const max = button.getAttribute("data-max");

    if (min === currentMinPrice && max === currentMaxPrice) {
      button.classList.add("active");
    }

    button.addEventListener("click", function (event) {
      event.preventDefault();
      const url = buildURL({ minPrice: min, maxPrice: max });
      window.location.href = url;
    });
  });
});
