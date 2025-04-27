
<?php
include('../database/connectDB.php');
include('header_footer/header.php');
$conn = connectDB::getConnection();

// Lấy ProductID từ URL
$product_id = isset($_GET['id']) ? $_GET['id'] : '';

if ($product_id === '') {
    echo "Không tìm thấy sản phẩm.";
    exit;
}
$sql = "SELECT p.*, s.Email AS SupplierEmail, tp.TypeID
        FROM product p
        JOIN supplier s ON p.SupplierID = s.SupplierID
        LEFT JOIN type_product tp ON p.ProductID = tp.ProductID
        WHERE p.ProductID = '$product_id'";

$result = mysqli_query($conn, $sql);

if ($result && mysqli_num_rows($result) > 0) {
    $product = mysqli_fetch_assoc($result);
    $author = $product['Author'];
    $sql_same_supplier = "SELECT * FROM product 
                         WHERE Author = '$author' 
                         AND ProductID != '$product_id'
                         LIMIT 5";
    $result_same_supplier = mysqli_query($conn, $sql_same_supplier);
    $sql_related = "
SELECT p.*
FROM product p
JOIN type_product tp ON p.ProductID = tp.ProductID
WHERE tp.TypeID IN (
    SELECT TypeID FROM type_product WHERE ProductID = '$product_id'
)
AND p.ProductID != '$product_id'
LIMIT 5
";
$result_related = mysqli_query($conn, $sql_related);
} else {
    echo "Sản phẩm không tồn tại.";
    exit;
}
$category = '';
$type_id = trim($product['TypeID']);  // Loại bỏ khoảng trắng dư thừa

// Lấy phần đầu của TypeID (ví dụ: AC từ AC001)
$type_id_prefix = substr($type_id, 0, 2);


switch ($type_id_prefix) {
    case 'AC':
        $category = 'Action';
        break;
    case 'F2':
        $category = 'Free To Play';
        break;
    case 'OW':
        $category = 'Open World';
        break;
    case 'RP':
        $category = 'Role-Playing';
        break;
    default:
        $category = 'Không xác định';
        break;
}

?>

<div id="detail-content" class="grid-col col-l-12 col-m-12 col-s-12 ">
                                   <section
                                        class="detail-product-container grid-col col-l-12 col-m-12 col-s-12 margin-bottom-16">
                                        <div class="detail-block">
                                             <div class="block-product grid-col col-l-5 col-m-12 col-s-12">
                                                  <div class="product-image">
                                                       <div class="flex justify-center">
                                                       <img src="<?php echo $product['ProductImg']; ?>" alt="<?php echo $product['ProductName']; ?>">
                                                       </div>
                                                       <div class="sale-off font-bold">Hết hàng</div>
                                                  </div>
                                                  <div class="sale-label"></div>
                                             </div>

                                             <div class="grid-col col-l-7 col-m-12 col-s-12">
                                                  <div>
                                                       <div class="product-title">
                                                            <h1 class="font-size-26 capitalize font-light"> <?php echo $product['ProductName']; ?></h1>
                                                            <div
                                                                 class="product-id margin-y-12 font-size-14 opacity-0-8">
                                                                 Mã sản phẩm: <?php echo $product['ProductID']; ?>
                                                            </div>
                                                       </div>
                                                       <div class="block-product-price margin-bottom-12">
                                                            <span
                                                                 class="price new-price font-bold padding-right-8 font-size-26"><?php echo $product['Price'] == 0 ? "Miễn phí" : number_format($product['Price']) . "₫"; ?></span>
                                                            
                                                       </div>
                                                       <div
                                                            class="product-info grid-col col-l-12 col-m-12 col-s-12 no-gutter flex margin-bottom-12">
                                                            <div class="grid-col col-l-6 col-m-6 col-s-12 no-gutter">
                                                                 <strong>Nhà phát hành</strong>
                                                                 <div class="b-author"><?php echo $product['Author']; ?></div>
                                                            </div>
                                                            <div class="grid-col col-l-6 col-m-6 col-s-12 no-gutter">
                                                                 <strong>Email </strong>
                                                                 <div class="b-release opacity-0-8"><?php echo $product['SupplierEmail']; ?></div>
                                                            </div>
                                                            <div class="grid-col col-l-6 col-m-6 col-s-12 no-gutter">
                                                                 <strong>Nhà phát triển</strong>
                                                                 <div class="b-publisher opacity-0-8"><?php echo $product['Publisher']; ?></div>
                                                            </div>
                                                            <div class="grid-col col-l-6 col-m-6 col-s-12 no-gutter">
                                                                <strong>Trạng thái</strong>
                                                                <div class="b-status opacity-0-8">
                                                                    <?php echo ($product['Status'] == 1) ? "Còn bán" : "Hết bán"; ?>
                                                                </div>
                                                            </div>

                                                       </div>

                                                       <div class="short-desc">
                                                            <strong class="font-size-14">nội dung:</strong>
                                                            <div class="font-size-14 opacity-0-8"><?php echo $product['Description']; ?></div>
                                                       </div>

                                                       <div class="product-selector margin-top-16">
                                                            
                                                           
                                                            
                                                            <div
                                                                 class="grid-col col-l-8 col-m-6 col-s-12 no-gutter flex justify-space-between margin-bottom-12">
                                                                 <div class="buy-btn button margin-bottom-8 buy-now-btn" data-id="<?php echo $product['ProductID']; ?>">mua ngay</div>
                                                                 <div
                                                                 class="quantity-box margin-bottom-12 grid-col col-l-2 col-m-10 col-s-10 no-gutter">
                                                                 <!-- <input type="button" value="-" class="reduce">-->
                                                                 <input type="number" name="quantity" 
                                                                      placeholder="1" value="1" class="quantity-cart">
                                                                <!-- <input type="button" value="+" class="increase">-->
                                                            </div>
                                                                 <div class="add-to-cart button margin-bottom-8 add-to-cart-btn" data-id="<?php echo $product['ProductID']; ?>" >thêm
                                                                      vào giỏ hàng</div>
                                                            </div>
                                                            
                                                      
                                                    </div>
                   
              
                                                       <div class="product-tags margin-top-16">
                                                            <div class="flex font-size-14">
                                                                 <strong class="margin-right-8">tag:</strong>
                                                                 <p><?php echo $category; ?></p>
                                                            </div>
                                                            <div class="flex font-size-14">
                                                                 <strong class="margin-right-8">danh mục:</strong>
                                                                 <p><?php echo $category; ?></p>
                                                            </div>
                                                       </div>
                                                  </div>
                                             </div>
                                        </div>
                                   </section>

                            
                                   <section id="same-author-container"
                                        class="container grid-col col-l-12 col-m-12 col-s-12">
                                        <div class="category-tab">
                                             <div class="list-title margin-bottom-12 padding-top-12 padding-bottom-12">
                                                  <strong class="font-size-20">Game cùng nhà sản xuất</strong>
                                             </div>

                                             <!-- container for products -->
                                             <div class="product-containerr flex full-height align-center justify-start">
                                             <?php 
    if ($result_same_supplier && mysqli_num_rows($result_same_supplier) > 0) {
        while ($row = mysqli_fetch_assoc($result_same_supplier)) {
            ?>
            <div class="product-item grid-col col-l-2-4 col-m-3 col-s-6">
    <div class="block-product product-resize">
        <a href="game_detail.php?id=<?php echo $row['ProductID']; ?>" class="product-image js-item">
            <img src="<?php echo $row['ProductImg']; ?>" alt="<?php echo $row['ProductName']; ?>" style="width:100%; height:auto; object-fit:cover;">
        </a>
        <div class="sale-off font-bold capitalize <?php echo $row['Quantity'] > 0 ? '' : 'active'; ?>">Hết hàng</div>
        <div class="info-inner flex justify-center align-center line-height-1-6">
            <h4 class="font-light capitalize" title="<?php echo $row['ProductName']; ?>"><?php echo $row['ProductName']; ?></h4>
            <div class="margin-y-4">
                <span class="price font-bold">
                    <?php echo $row['Price'] > 0 ? number_format($row['Price']) . "₫" : "Miễn phí"; ?>
                </span>
            </div>
        </div>
    </div>
    <div class="action <?php echo $row['Quantity'] > 0 ? '' : 'disable'; ?>">
        <div class="add-to-cart">
            <div title="Thêm vào giỏ hàng" class="button add-to-cart-btn" data-id="<?php echo $row['ProductID']; ?>">
                <i class="fa-solid fa-plus" style="color: var(--primary-white);"></i>
            </div>
        </div>
    </div>
</div>
            <?php
        }
    } else {
        echo '<div class="hieule"><p>Không có sản phẩm nào khác từ nhà sản xuất này.</p></div>';
    }
    ?>
                                             </div>
                                        </div>
                                   </section>

                               
                                   <section id="product-like-container"
                                        class="container grid-col col-l-12 col-m-12 col-s-12">
                                        <div class="category-tab">
                                             <div class="list-title margin-bottom-12 padding-top-12 padding-bottom-12">
                                                  <strong class="font-size-20">Các tựa game liên quan</strong>
                                             </div>

                                             <!-- container for products -->
                                             <div class="product-containerrr flex full-height align-center justify-start">
                                             <?php
    if ($result_related && mysqli_num_rows($result_related) > 0) {
        while ($related = mysqli_fetch_assoc($result_related)) {
            ?>
            <div class="product-item grid-col col-l-2-4 col-m-3 col-s-6">
                <div class="block-product product-resize">
                    <a href="game_detail.php?id=<?php echo $related['ProductID']; ?>" class="product-image js-item">
                        <img src="<?php echo $related['ProductImg']; ?>" alt="<?php echo $related['ProductName']; ?>" style="width:100%; height:auto; object-fit:cover;">
                    </a>
                    <div class="sale-off font-bold capitalize <?php echo $related['Quantity'] > 0 ? '' : 'active'; ?>">Hết hàng</div>
                    <div class="info-inner flex justify-center align-center line-height-1-6">
                        <h4 class="font-light capitalize" title="<?php echo $related['ProductName']; ?>"><?php echo $related['ProductName']; ?></h4>
                        <div class="margin-y-4">
                            <span class="price font-bold">
                                <?php echo $related['Price'] > 0 ? number_format($related['Price']) . "₫" : "Miễn phí"; ?>
                            </span>
                        </div>
                    </div>
                </div>
                <div class="action <?php echo $related['Quantity'] > 0 ? '' : 'disable'; ?>">
                    <div class="add-to-cart">
                        <div title="Thêm vào giỏ hàng" class="button add-to-cart-btn" data-id="<?php echo $related['ProductID']; ?>">
                            <i class="fa-solid fa-plus" style="color: var(--primary-white);"></i>
                        </div>
                    </div>
                </div>
            </div>
            <?php
        }
    } else {
        echo '<div style="hieule"><p>Không có tựa game liên quan.</p></div>';
    }
    ?>
                                             </div>
                                        </div>
                                   </section>
                              </div>
                              <?php 
connectDB::closeConnection($conn); 
include('header_footer/footer.php');
?>