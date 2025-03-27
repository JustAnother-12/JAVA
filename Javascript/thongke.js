
import { activeSideBar, hiddenException } from "./JSAdmin.js";

function showthongke(){
    update_thongke('mh',false);
    document.querySelector('.thongke').classList.add("open");
    document.querySelector(".openthongke").classList.add("action");
    activeSideBar("openthongke");
    hiddenException("thongke");
  }
const openthongke = document.querySelector(".openthongke");
openthongke.addEventListener("click", showthongke);
const mh = document.getElementById("mh");
const kh = document.getElementById("kh");
mh.addEventListener("click", function () {
update_thongke("mh",false);
});
kh.addEventListener("click", function () {
update_thongke("kh",false);
});

var data_kh_temp = [];
var data_dh_temp = [];
var num_page = 1;
var data_kh = [];
var data_dh = [];
var data_dh_chitiet = [];
update_array();
export function update_array(){
    // Lấy chuỗi JSON từ localStorage
    const tk_donhang_list = localStorage.getItem('donhang');
    // Chuyển chuỗi JSON thành mảng đối tượng
    const tk_donhangArray = JSON.parse(tk_donhang_list);

    // Lọc các đơn hàng có trang_thai là 2 hoặc 3
    const filteredOrders = tk_donhangArray?.filter(order => order.trang_thai === "2" || order.trang_thai === "3");

    // Tạo một mảng mới chỉ chứa các giá trị của đối tượng
    data_dh = filteredOrders?.map(({ id_khachhang, ...otherProps }) => Object.values(otherProps));


    // Lấy chuỗi JSON từ localStorage
    const tk_chitiet_donhang_list = localStorage.getItem('chitiet_donhang');
    // Chuyển chuỗi JSON thành mảng đối tượng
    const tk_chitiet_donhangArray = JSON.parse(tk_chitiet_donhang_list);
    // Lọc `chitiet_donhang` 
    data_dh_chitiet = tk_chitiet_donhangArray?.filter(detail => 
        filteredOrders.some(order => order.id_donhang === detail.id_donhang)
    ).map(({ id_sanpham, ...otherProps }) => Object.values(otherProps));

    data_kh = filteredOrders?.map(({ trang_thai, dia_chi, sdt, ...otherProps }) => Object.values(otherProps));
}

function formatPrice() {
    const pricesContainer = document.querySelectorAll(".price");
    if (pricesContainer) {
        const formatPricesHandler = new Intl.NumberFormat("vi-VN", {
            style: "currency",
            currency: "VND",
            minimumSignificantDigits: "3",
        });
        pricesContainer.forEach((element) => {
            if (!element.innerHTML.includes("₫"))
                element.innerText = formatPricesHandler.format(element.innerText);
        });
    }
}

// Cập nhật table (số trang về 1)
function update_thongke(type, f) {
    var parent = document.getElementsByClassName("table-content")[5];
    var children = parent.children;
    if (type == 'kh') {
        Array.from(children).forEach(child => {
            if (child.id != "filter") {
                parent.removeChild(child);
            }
        });
    } else {
        Array.from(children).forEach(child => {
            parent.removeChild(child);
        });
    }
    num_page = 1
    thongke(type, f);
}

// Loại thống kê (mặt hàng, khách hàng)
function thongke(type, f) {
    var btn_tkmh = document.getElementById('mh');
    var btn_tkkh = document.getElementById('kh');
    switch (type) {
        case 'mh':
            btn_tkmh.setAttribute('style', "pointer-events: none; background-color: gray");
            btn_tkkh.removeAttribute('style');
            thongke_mh();
            break;
        case 'kh':
            btn_tkkh.setAttribute('style', "pointer-events: none; background-color: gray");
            btn_tkmh.removeAttribute('style');
            thongke_kh(f);
            break;
    }
}

// Thống kê mặt hàng
function thongke_mh() {
    var header_thongkemh = document.createElement('div');
    header_thongkemh.innerHTML = `
        <div class="header_thongkemh">
            <h1 style="color: black;">Thống kê mặt hàng</h1>
        </div>
        `;
    document.getElementsByClassName('table-content')[5].appendChild(header_thongkemh);

    var info_thongke = document.createElement('div');
    info_thongke.className = 'info_thongke';
    info_thongke.innerHTML = `
        <div style="display: flex; width: 100%; height: 100%; background-color: rgb(220, 220, 220);">
            <div style="display: flex; justify-content: center; align-items: center; width: 60%; height: 100%; border-right: 1px solid black;">
                <div style="width: 90%; height: 100%;">
                    <div style="width: 100%; height: 50px; display: flex; justify-content: center; align-items: center;">
                        <h2 style="color: black;">Sản phẩm bán chạy</h2>
                    </div>
                    <div style="width: 100%; border: 1px solid black;">
                        <table id="thongke_mh1" style="width: 100%; border-bottom: 1px solid black;">
                            <thead>
                                <tr>
                                    <th style="width: 12%; color: black">STT</th>
                                    <th style="width: 42%; color: black">Sản phẩm</th>
                                    <th style="width: 20%; color: black">Số lượng</th>
                                    <th style="width: 30%; color: black">Thu</th>
                                    <th style="width: 10%; color: black"></th>
                                </tr>
                            </thead>
                            <tbody>
                            
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div style="display: flex; justify-content: center; align-items: center; width: 60%; height: 100%; border-left: 1px solid black;">
                <div style="width: 90%; height: 100%;">
                    <div style="width: 100%; height: 50px; display: flex; justify-content: center; align-items: center;">
                        <h2 style="color: black;">Sản phẩm bán chậm</h2>
                    </div>
                    <div style="width: 100%; border: 1px solid black;">
                        <table id="thongke_mh2" style="width: 100%; border-bottom: 1px solid black;">
                            <thead>
                                <tr>
                                    <th style="width: 12%; color: black">STT</th>
                                    <th style="width: 42%; color: black">Sản phẩm</th>
                                    <th style="width: 20%; color: black">Số lượng</th>
                                    <th style="width: 30%; color: black">Thu</th>
                                    <th style="width: 10%; color: black"></th>
                                </tr>
                            </thead>
                            <tbody>
                            
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    `;
    document.getElementsByClassName('table-content')[5].appendChild(info_thongke);
    add_thongke_mh1();
    add_thongke_mh2();
    add_tongthu();
}

// Thống kê khách hàng
function thongke_kh(f) {
    if (!f) {
        var loc_date = document.createElement('div');
        loc_date.id = "filter";
        loc_date.innerHTML = `
            <form>
                Ngày:
                <input id="tk_date_start" type="date" class="locdon">
                -
                <input id="tk_date_end" type="date" class="locdon" style="margin-right: 20px;margin-left: 10px;">
                <input type="button" value=" Thống kê " style="color: black" id="thongke_button">
                <input type="reset" value="Reset" style="width: 50px; color: black">
            </form>
        `;
        const thongKeButton = loc_date.querySelector("#thongke_button");
        thongKeButton.addEventListener("click", function () {
            const dateStart = document.getElementById("tk_date_start");
            const dateEnd = document.getElementById("tk_date_end");
            loc_thongkekh(dateStart, dateEnd);
        });
        document.getElementsByClassName('table-content')[5].appendChild(loc_date);
    }

    var header_thongkekh = document.createElement('div');
    header_thongkekh.innerHTML = `
        <div class="header_thongkekh">
            <h1 style="color: black;">Thống kê khách hàng</h1>
        </div>
        `;
    document.getElementsByClassName('table-content')[5].appendChild(header_thongkekh);

    var info_thongke = document.createElement('div');
    info_thongke.className = 'info_thongke';
    info_thongke.innerHTML = `
    <div style="display: flex; width: 100%; height: 100%; background-color: rgb(220, 220, 220);">
        <div style="display: flex; justify-content: center; align-items: center; width: 100%; height: 100%; border-right: 1px solid black;">
            <div style="width: 50%; min-height: 15em;">
                <div style="width: 100%; height: 50px; display: flex; justify-content: center; align-items: center;">
                    <h2 style="color: black;">Danh sách khách hàng phát sinh doanh thu nhiều nhất</h2>
                </div>
                <div style="width: 100%; border: 1px solid black;">
                    <table id="thongke_kh" style="width: 100%; border-bottom: 1px solid black;">
                        <thead>
                            <tr>
                                <th style="width: 12%; color: black">STT</th>
                                <th style="width: 20%; color: black">Mã khách hàng</th>
                                <th style="width: 42%; color: black">Tên khách hàng</th>
                                <th style="width: 30%; color: black">Tổng doanh thu</th>
                                <th style="width: 10%; color: black"></th>
                            </tr>
                        </thead>
                        <tbody>
                        
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    `;
    document.getElementsByClassName('table-content')[5].appendChild(info_thongke);
    if (!f) {
        add_thongke_kh(get_thongke_kh(), false);
    } else {
        add_thongke_kh(get_thongke_kh_f(), true);
    }
}

// Tổng thu
function add_tongthu() {
    var tongthu = document.createElement('div');
    var sum = 0;
    for (let i = 0; i < data_dh_chitiet.length; i++) {
        sum = sum + data_dh_chitiet[i][4];
    }
    tongthu.classList.add('tongthu');
    tongthu.innerHTML = `<h1 style="color: black">Tổng thu: <span class="price">${sum}</span></h1>`;
    document.getElementsByClassName('table-content')[5].appendChild(tongthu);
    formatPrice();
}

// Lâp danh sách thống kê
function get_thongke_mh() {
    var rank = [];
    for (let i = 0; i < data_dh_chitiet.length; i++) {
        var f = 0;
        for (let j = 0; j < rank.length; j++) {
            if (data_dh_chitiet[i][1] == rank[j][0]) {
                rank[j][1] = rank[j][1] + data_dh_chitiet[i][3];
                rank[j][2] = rank[j][2] + data_dh_chitiet[i][4];
                f = 1;
                break;
            }
        }
        if (f == 0) {
            rank.push([data_dh_chitiet[i][1], parseInt(data_dh_chitiet[i][3]), data_dh_chitiet[i][4]]);
        }
    }
    return rank;
}

function get_thongke_kh() {
    var rank = [];
    for (let i = 0; i < data_kh.length; i++) {
        var f = 0;
        for (let j = 0; j < rank.length; j++) {
            if (data_kh[i][1] == rank[j][0]) {
                rank[j][2] = (parseInt(rank[j][2].replace(/,/g, '')) + parseInt(data_kh[i][3].replace(/,/g, ''))).toLocaleString('en-US');
                f = 1;
                break;
            }
        }
        if (f == 0) {
            rank.push([data_kh[i][1], data_kh[i][2], data_kh[i][3]]);
        }
    }
    return rank;
}

function get_thongke_kh_f() {
    var rank = [];
    for (let i = 0; i < data_kh_temp.length; i++) {
        var f = 0;
        for (let j = 0; j < rank.length; j++) {
            if (data_kh_temp[i][1] == rank[j][0]) {
                rank[j][2] = (parseInt(rank[j][2].replace(/,/g, '')) + parseInt(data_kh_temp[i][3].replace(/,/g, ''))).toLocaleString('en-US');
                f = 1;
                break;
            }
        }
        if (f == 0) {
            rank.push([data_kh_temp[i][1], data_kh_temp[i][2], data_kh_temp[i][3]]);
        }
    }
    return rank;
}

// Thống kê bán chạy
function add_thongke_mh1() {
    var body = document.querySelector('#thongke_mh1 tbody');
    var thongke = get_thongke_mh();
    var top = 0;
    thongke.sort((a, b) => b[1] - a[1]);
    for (let i = 0; i < thongke.length && top < 5; i++) {
        var row = document.createElement('tr');
        for (let j = 0; j < thongke[i].length + 2; j++) {
            var cell = document.createElement('td');
            cell.style.color = 'black';
            if (j == 0) {
                cell.textContent = i + 1;
                row.appendChild(cell);
                continue;
            }
            if (j == thongke[i].length + 1) {
                const link = document.createElement("a");
                link.style.textDecoration = "underline";
                link.style.color = "blue";
                link.style.cursor = "pointer";
                link.textContent = "Xem";
                link.addEventListener("click", function () {
                    xemhoadon_mh(thongke[i][0]);
                });
                cell.appendChild(link);
                row.appendChild(cell);
                break;
            }
            cell.textContent = thongke[i][j - 1];
            row.appendChild(cell);
        }
        body.appendChild(row);
        top++;
    }
}

// Thống kê bán chậm
function add_thongke_mh2() {
    var body = document.querySelector('#thongke_mh2 tbody');
    var thongke = get_thongke_mh();
    var top = 0;
    thongke.sort((a, b) => a[1] - b[1]);
    for (let i = 0; i < thongke.length && top < 5; i++) {
        var row = document.createElement('tr');
        for (let j = 0; j < thongke[i].length + 2; j++) {
            var cell = document.createElement('td');
            cell.style.color = 'black';
            if (j == 0) {
                cell.textContent = i + 1;
                row.appendChild(cell);
                continue;
            }
            if (j == thongke[i].length + 1) {
                const link = document.createElement("a");
                link.style.textDecoration = "underline";
                link.style.color = "blue";
                link.style.cursor = "pointer";
                link.textContent = "Xem";
                link.addEventListener("click", function () {
                    xemhoadon_mh(thongke[i][0]);
                });
                cell.appendChild(link);
                row.appendChild(cell);
                break;
            }
            cell.textContent = thongke[i][j - 1];
            row.appendChild(cell);
        }
        body.appendChild(row);
        top++;
    }
}

// Thống kê khách hàng
function add_thongke_kh(thongke, f) {
    var body = document.querySelector('#thongke_kh tbody');
    var top = 0;
    thongke.sort((a, b) => {
        const valueA = parseFloat(a[2].replace(/,/g, "")); 
        const valueB = parseFloat(b[2].replace(/,/g, "")); 
    
        return valueB - valueA; 
    });
    for (let i = 0; i < thongke.length && top < 5; i++) {
        var row = document.createElement('tr');
        for (let j = 0; j < thongke[i].length + 2; j++) {
            var cell = document.createElement('td');
            cell.style.color = 'black';
            if (j == 0) {
                cell.textContent = i + 1;
                row.appendChild(cell);
                continue;
            }
            if (j == thongke[i].length + 1) {
                const link = document.createElement("a");
                link.style.textDecoration = "underline";
                link.style.color = "blue";
                link.style.cursor = "pointer";
                link.textContent = "Xem";
                link.addEventListener("click", function () {
                    xemhoadon_kh(thongke[i][0], f);
                });
                cell.appendChild(link);
                row.appendChild(cell);
                break;
            }
            cell.textContent = thongke[i][j - 1];
            row.appendChild(cell);
        }
        body.appendChild(row);
        top++;
    }
}

// Làm mới bảng xem hóa đơn
function refresh_xemhd() {
    var parent = document.getElementById("frame_xemhd");
    if (parent != null) {
        var children = parent.children;
        Array.from(children).forEach(child => {
            parent.removeChild(child);
        });
    }
}

// Xem danh sách hóa đơn
function xemhoadon_mh(obj) {
    // Lấy danh sách hóa đơn theo sản phẩm
    var list_hd = [];
    for (let i = 0; i < data_dh_chitiet.length; i++) {
        if (obj == data_dh_chitiet[i][1]) {
            list_hd.push(data_dh_chitiet[i][0]);
        }
    }
    refresh_xemhd();
    var frame_xemhd = document.createElement('div');
    frame_xemhd.style.width = "100%";
    frame_xemhd.id = "frame_xemhd";
    document.getElementsByClassName('table-content')[5].appendChild(frame_xemhd);

    add_header_hd();
    add_list_hd(list_hd);
    add_page_hd(list_hd);
}

function xemhoadon_kh(obj, f) {
    // Lấy danh sách hóa đơn theo khách hàng
    var list_hd = [];
    for (let i = 0; i < data_kh.length; i++) {
        if (obj == data_kh[i][1]) {
            list_hd.push(data_kh[i][0]);
        }
    }
    refresh_xemhd();
    var frame_xemhd = document.createElement('div');
    frame_xemhd.style.width = "100%";
    frame_xemhd.id = "frame_xemhd";
    document.getElementsByClassName('table-content')[5].appendChild(frame_xemhd);

    add_header_hd();
    add_list_hd(list_hd, f);
    add_page_hd(list_hd);
}

// Header bảng hóa đơn
function add_header_hd() {
    var header_xemhd = document.createElement('h2');
    header_xemhd.style = "display: grid; place-items:center; margin-top: 20px";
    header_xemhd.textContent = "Xem hoá đơn";
    document.getElementById('frame_xemhd').appendChild(header_xemhd);

    var table_xemhd = document.createElement('div');
    table_xemhd.innerHTML = `
        <table class="table-header" style="margin-top: 5px">
            <tr>
                <!-- Theo độ rộng của table content -->
                <th title="Sắp xếp" style="width: 5%" >STT</th>
                <th title="Sắp xếp" style="width: 12%" >Mã đơn</th>
                <th title="Sắp xếp" style="width: 12%" >Khách hàng</th>
                <th title="Sắp xếp" style="width: 34%" >Địa chỉ</th>
                <th title="Sắp xếp" style="width: 15%" >Tổng tiền</th>
                <th title="Sắp xếp" style="width: 12%" >Ngày giờ</th>
                <th style="width: 10%">Hành động</th>
            </tr>
        </table>
    `;
    document.getElementById('frame_xemhd').appendChild(table_xemhd);
}

// Lập danh sách hóa đơn
function add_list_hd(list_hd, f) {
    var k = 0;
    if (!f) {
        for (let i = (num_page - 1) * 5; i < (num_page - 1) * 5 + 5 && i < list_hd.length; i++) {
            for (let j = 0; j < data_dh.length; j++) {
                if (list_hd[i] == data_dh[j][0]) {
                    k++;
                    var new_hoadon = document.createElement('div');
                    new_hoadon.className = "table-donhang";
                    new_hoadon.innerHTML = `
                        <div style="width: 5%">${k}</div>
                        <div style="width: 12%">${data_dh[j][0]}</div>
                        <div style="width: 12%">${data_dh[j][1]}</div>
                        <div style="width: 34%">${data_dh[j][2]}</div>
                        <div style="width: 15%">${data_dh[j][3]}</div>
                        <div style="width: 12%">${data_dh[j][4]}</div>
                        <div style="width: 10%">
                            <a class="xem-chi-tiet">Xem chi tiết</a>
                        </div>
                    `;
                    const xemChiTietLink = new_hoadon.querySelector(".xem-chi-tiet");
                    xemChiTietLink.addEventListener("click", function () {
                        const closestElement = xemChiTietLink.closest('.table-donhang').children[1];
                        xemchitiet_dh(closestElement);
                    });
                    document.getElementById('frame_xemhd').appendChild(new_hoadon);
                    break;
                }
            }
        }
    } else {
        for (let i = (num_page - 1) * 5; i < (num_page - 1) * 5 + 5 && i < list_hd.length; i++) {
            for (let j = 0; j < data_dh_temp.length; j++) {
                if (list_hd[i] == data_dh_temp[j][0]) {
                    k++;
                    var new_hoadon = document.createElement('div');
                    new_hoadon.className = "table-donhang";
                    new_hoadon.innerHTML = `
                        <div style="width: 5%">${k}</div>
                        <div style="width: 12%">${data_dh_temp[j][0]}</div>
                        <div style="width: 12%">${data_dh_temp[j][1]}</div>
                        <div style="width: 34%">${data_dh_temp[j][2]}</div>
                        <div style="width: 15%">${data_dh_temp[j][3]}</div>
                        <div style="width: 12%">${data_dh_temp[j][4]}</div>
                        <div style="width: 10%">
                            <a class="xem-chi-tiet">Xem chi tiết</a>
                        </div>
                    `;
                    const xemChiTietLink = new_hoadon.querySelector(".xem-chi-tiet");
                    xemChiTietLink.addEventListener("click", function () {
                        const closestElement = xemChiTietLink.closest('.table-donhang').children[1];
                        xemchitiet_dh(closestElement);
                    });
                    document.getElementById('frame_xemhd').appendChild(new_hoadon);
                    break;
                }
            }
        }
    }
}

// Phân trang
function add_page_hd(data) {
    var page = document.createElement('div');
    page.id = "phan_trang";
    page.style.display = "grid";
    page.style.placeItems = "center";
    if (data.length <= 8) {
        page.innerHTML = `
            <nav>
                <ul class="pagination">
                    <li><a href="#" id="prev" style="pointer-events: none; color: gray;">&laquo; Trang trước</a></li>
                    <li id="current_page">1</li>
                    <li><a href="#" id="next" style="pointer-events: none; color: gray;">Trang sau &raquo;</a></li>
                </ul>
            </nav>
        `;
        const prevButton = page.querySelector('#prev');
        prevButton.addEventListener('click', function (event) {
        event.preventDefault();
        change_page(-1);
        });
        const nextButton = page.querySelector('#next');
        nextButton.addEventListener('click', function (event) {
        event.preventDefault(); 
        change_page(1);
        });
    } else {
        page.innerHTML = `
            <nav>
                <ul class="pagination">
                    <li><a href="#" id="prev" style="pointer-events: none; color: gray;">&laquo; Trang trước</a></li>
                    <li id="current_page">1</li>
                    <li><a href="#" id="next">Trang sau &raquo;</a></li>
                </ul>
            </nav>
        `;
        const prevButton = page.querySelector('#prev');
        prevButton.addEventListener('click', function (event) {
            event.preventDefault();
            change_page(-1);
        });
        const nextButton = page.querySelector('#next');
        nextButton.addEventListener('click', function (event) {
            event.preventDefault(); 
            change_page(1);
        });
    }
    document.getElementById('frame_xemhd').appendChild(page);
}

function xemchitiet_dh(id_donhang) {
    // Lấy chi tiết đơn hàng
    var donhangDetails = [];
    for (let i = 0; i < data_dh_chitiet.length; i++) {
        if (data_dh_chitiet[i][0] == id_donhang.innerHTML) {
            donhangDetails.push(data_dh_chitiet[i]);
        }
    }

    // Header
    var model_ctdh = document.createElement('div');
    model_ctdh.className = 'chitietdonhang';
    model_ctdh.innerHTML = `
        <div class="modal-content">
            <span class="close">×</span>
            <h2>Chi tiết đơn hàng ${id_donhang.innerHTML}</h2>
            <table class="table-chitiet_donhang-header">
                <tr>
                    <th style="width: 50%" >Sản phẩm</th>
                    <th style="width: 20%" >Đơn giá</th>
                    <th style="width: 10%" >SL</th>
                    <th style="width: 20%" >Thành tiền</th>
                </tr>
            </table>
        </div>
    `;
    const closeButton = model_ctdh.querySelector('.close');
    closeButton.addEventListener('click', close_ctdh);
    document.getElementsByClassName('table-content')[5].appendChild(model_ctdh);

    // Chi tiết đơn hàng
    for (let i = 0; i < donhangDetails.length; i++) {
        var new_ctdh = document.createElement('div');
        new_ctdh.className = 'table-chitiet_donhang';
        new_ctdh.innerHTML = `
            <div style="width: 50%">${donhangDetails[i][1]}</div>
            <div style="width: 20%">${donhangDetails[i][2]}</div>
            <div style="width: 10%">${donhangDetails[i][3]}</div>
            <div style="width: 20%">${donhangDetails[i][4]}</div>
        `;
        document.getElementsByClassName('modal-content')[0].appendChild(new_ctdh);
    }
}
// Đóng chi tiết đơn hàng
function close_ctdh() {
    var modal = document.querySelector(".chitietdonhang");
    if (modal) {
      modal.remove();
    }
  }
function loc_thongkekh(date_start, date_end) {
    var filtered_data_kh = [...data_kh];
    data_kh_temp = [];
    var filtered_data_dh = [...data_dh];
    data_dh_temp = [];
    // Lọc ngày bắt đầu
    if (date_start.value != "") {
        filtered_data_kh = filter_date_start(filtered_data_kh, date_start.value);
        filtered_data_dh = filter_date_start(filtered_data_dh, date_start.value);
    }
    // Lọc ngày kết thúc
    if (date_end.value != "") {
        filtered_data_kh = filter_date_end(filtered_data_kh, date_end.value);
        filtered_data_dh = filter_date_end(filtered_data_kh, date_end.value);
    }
    data_kh_temp = filtered_data_kh;
    data_dh_temp = filtered_data_dh;
    update_thongke('kh', true);
}

function filter_date_start(f_data, date_start) {
    var f_date_start = new Date(date_start);
    f_date_start.setDate(f_date_start.getDate());
    return f_data.filter(row => {
        var dateStr = row[4];
        var dateObj = new Date(dateStr);
        return dateObj >= f_date_start;
    });
}

function filter_date_end(f_data, date_end) {
    var f_date_end = new Date(date_end);
    f_date_end.setDate(f_date_end.getDate() + 1);
    return f_data.filter(row => {
        var dateStr = row[4];
        var dateObj = new Date(dateStr);
        return dateObj <= f_date_end;
    });
}
