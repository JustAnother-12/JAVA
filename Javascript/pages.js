import { getProductBooks, renderProducts } from './Products.js';
import { resizeImages } from './Interface.js';
import { formatPrices } from "./Interface.js";
import { $, $$ } from './Bridge.js';

function handleCategoryNavigation() {
    const categoryButtons = $$('.category-btn');
    const navCategories = $$(".nav-categories .nav-item");

    // show more buttons
    categoryButtons.forEach(button => {
        button.addEventListener('click', (event) => {
            event.preventDefault();
            const parentSection = button.closest('section');
            if (parentSection && parentSection.id) {
                const categoryId = parentSection.id;
                window.location.href = `index.html?category=${categoryId}`;
            }
        });
    });

    // sub header navigations
    navCategories.forEach(item =>
        item.addEventListener("click", ((event) => {
            event.preventDefault();
            const categoryType = item.querySelector("a.flex.full-width");
            let type = categoryType ? categoryType.getAttribute("title") : "";
            window.location.href = `index.html?category=${type}`;
        })
        ));
}

// page.js: Xử lý hiển thị phần nội dung dựa trên URL
const ITEMS_PER_PAGE = 10; // Số sản phẩm trên mỗi trang
/**
 * Hàm khởi tạo trang
 */

function initializePage() {
    // Lấy tham số từ URL
    const categoryId = getCategoryFromURL();

    if (categoryId) {
        // Ẩn tất cả các section
        hideAllSections();

        // Hiển thị section mục tiêu
        showTargetSection(categoryId);
        // Lấy danh sách sản phẩm từ localStorage
        const allProducts = getProductBooks();
        // console.log("Dữ liệu sản phẩm từ localStorage:", allProducts);
        // Lọc sản phẩm theo danh mục
        const filteredProducts = filterProductsByCategory(allProducts, categoryId);
        // console.log("Filtered Products:", filteredProducts);

        // Tích hợp phân trang
        setupPagination(filteredProducts, categoryId);
    }
}
/**
 * Lọc sản phẩm theo danh mục
 * @param {Array} products Danh sách sản phẩm
 * @param {string} categoryId ID của danh mục
 * @returns {Array} Danh sách sản phẩm đã lọc
 */
function filterProductsByCategory(products, categoryId) {
    if (!products || !categoryId) return [];
    switch (categoryId) {
        case 'fs-container':
            return products.sort((a, b) => b.sale - a.sale);
        case 'new-books-container':
            return products.slice(-5);
        case 'best-selling-container':
            return products.sort((a, b) => b.quantity - a.quantity);
        case 'light-novel-container':
        case "light-novel":
            return products.filter((product) => product.type === 'light novel');
        case 'manga-container':
        case "manga":
            return products.filter((product) => product.type === 'manga');
        case 'other-books-container':
            return products.sort((a, b) => a.releaseDate - b.releaseDate);
        case "education":
            return products.filter((product) => product.type === 'education');
        case "literary":
            return products.filter((product) => product.type === 'literary');
        default:
            return products;
    }
}

/**
 * Tích hợp phân trang
 * @param {Array} products Danh sách sản phẩm đã lọc
 * @param {string} categoryId ID của danh mục
 */
async function setupPagination(products, categoryId) {
    if (!products || products.length === 0) {
        console.error("Không có sản phẩm để phân trang.");
        return;
    }
    let container;
    let totalPages;
    let targetSection = document.getElementById(categoryId);
    if (!targetSection) {
        const checkTarget = setInterval(() => {
            targetSection = document.getElementById(`${categoryId}-container`);
            if (targetSection) {
                container = targetSection.querySelector('.product-container');
                totalPages = Math.ceil(products.length / ITEMS_PER_PAGE);
                displayPage(products, container, 1);
                createPaginationControls(targetSection, products, container, totalPages);
                clearInterval(checkTarget);            
            }
        })
    }
    else {
        container = targetSection.querySelector('.product-container');
        if (!container) {
            console.error("Không tìm thấy container sản phẩm.");
            return;
        }
        totalPages = Math.ceil(products.length / ITEMS_PER_PAGE);
        // console.log(`Tổng số trang: ${totalPages}, Tổng sản phẩm: ${products.length}`);
        displayPage(products, container, 1);
        createPaginationControls(targetSection, products, container, totalPages);
    }
}


/**
 * Hiển thị sản phẩm của một trang
 * @param {Array} products Danh sách sản phẩm
 * @param {HTMLElement} container Container hiển thị sản phẩm
 * @param {number} pageNumber Số trang hiện tại
 */
function displayPage(products, container, pageNumber) {
    container.innerHTML = ""; // Làm trống container

    const startIndex = (pageNumber - 1) * ITEMS_PER_PAGE;
    const endIndex = startIndex + ITEMS_PER_PAGE;
    const pageProducts = products.slice(startIndex, endIndex);

    // Hiển thị sản phẩm
    renderProducts(pageProducts, container);
    // Chỉ định dạng giá khi đã chắc chắn sản phẩm đã được render
    setTimeout(() => {
        formatPrices({ getElementPrices: () => container.querySelectorAll('.price') });
    }, 0);

    // console.log(`Hiển thị trang ${pageNumber}:`, pageProducts);
    const elementsObj = {
        getImages: () => container.querySelectorAll('img'),
    };

    // Thay đổi kích thước hình ảnh
    resizeImages(elementsObj);

}


/**
 * Tạo nút điều khiển phân trang
 * @param {HTMLElement} section Phần tử chứa phân trang
 * @param {Array} products Danh sách sản phẩm
 * @param {HTMLElement} container Container hiển thị sản phẩm
 * @param {number} totalPages Tổng số trang
 */
function createPaginationControls(section, products, container, totalPages) {
    const paginationContainer = document.createElement('div');
    paginationContainer.className = 'pagination-controls';
    paginationContainer.style.textAlign = 'center';
    paginationContainer.style.marginTop = '20px';

    for (let i = 1; i <= totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.textContent = i;
        pageButton.className = 'page-button';
        pageButton.style.margin = '0 5px';
        pageButton.style.padding = '5px 10px';
        pageButton.style.cursor = 'pointer';
        pageButton.addEventListener('click', () => displayPage(products, container, i));

        paginationContainer.appendChild(pageButton);
    }

    section.appendChild(paginationContainer);
}
/**
 * Lấy ID của category từ URL
 * @returns {string | null} ID của category hoặc null nếu không có
 */
function getCategoryFromURL() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('category');
}

/**
 * Ẩn tất cả các section trên trang
 */
function hideAllSections() {
    const allSections = $$('section.container');
    const allCategoryButtons = $$('.category-btn'); // Lấy tất cả các nút "Xem thêm"

    // Ẩn tất cả các section
    allSections.forEach(section => {
        section.style.display = 'none';
    });

    // Ẩn tất cả các nút "Xem thêm"
    allCategoryButtons.forEach(button => {
        if (!button.classList.contains("return-homepage"))
            button.style.display = 'none';
    });
}

/**
 * Hiển thị section mục tiêu theo ID
 * @param {string} categoryId ID của section cần hiển thị
 */
function showTargetSection(categoryId) {
    const targetSection = document.getElementById(categoryId);
    if (targetSection) {
        targetSection.style.display = 'flex';
        targetSection.style.justifyContent = 'center';
        return;
    } 
    else {
        renderContainer(categoryId);    
        return;
    }
}

function renderContainer(name) {
    let script = `
        <div class="category-tab">
                <div class="heading">
                    <div class="heading-label ${name}-label"></div>
                    <div class="uppercase font-bold font-size-20 padding-left-8">${name.replace("-", " ")}</div>
                </div>

                <!-- container for products -->
                <div class="product-container"></div>

                <div
                    class="flex justify-center align-center font-bold capitalize margin-bottom-16">
                    <a href="#" class="category-btn button">Xem thêm</a>
                </div>
        </div>
    `
    let element = document.createElement("section");
    element.setAttribute("id", `${name}-container`);
    element.classList.add("flex");
    element.classList.add("justify-center");
    element.classList.add("grid-col");
    element.classList.add("col-l-12");
    element.classList.add("col-m-12");
    element.classList.add("col-s-12");
    element.classList.add("no-gutter");
    element.innerHTML = script;

    $("#index-content")?.insertAdjacentElement("afterEnd", element);
}

export { initializePage, handleCategoryNavigation }
