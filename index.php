<?php include('gui/header_footer/header.php') ?>


                    <!-- homepage -->
                    <div id="index-content" class="grid-col col-l-12 col-m-12 col-s-12">
                           <!-- banner -->
                                <section class="banner-container flex justify-center align-center">
                                     <div class="homepage grid-col col-l-12 col-m-12 col-s-12 no-gutter">
                                          <a data-panel="{&quot;focusable&quot;:false}" href="/gui/game_detail.php?id=<?= urlencode('GAME019') ?>" class="promo_link" style="display: block;">
                                          <video loop muted autoplay playsinline preload="none'" id="home_video_desktop" alt="Feature red Promotion" class="fullscreen-bg__video">
                                   
                                               <source src="/Assets/Videos/BackGrounds/webm_page_bg_english.webm" type="video/mp4">

                                          </video>
                                          </a>
                                     </div>
                                
                           </section>

                           <!-- service -->
                           <section class="grid-col col-l-12 no-gutter margin-y-16 full-width">
                                <div class="services-container flex justify-center align-center">
                                     <div class="service-content grid-col col-l-3 col-m-3 col-s-6">
                                          <img src="/Assets/Images/Icons/Service/icon-sv1.jpg"
                                               alt="Thanh toán" />
                                          <div class="flex-direction-y padding-left-8">
                                               <h5 class="font-bold uppercase font-size-13">Thanh toán</h5>
                                               <p class="font-size-13 font-light capitalize">Giao dịch nhanh</p>
                                          </div>
                                     </div>

                                     <div class="service-content grid-col col-l-3 col-m-3 col-s-6">
                                          <img src="/Assets/Images/Icons/Service/icon-sv2.jpg"
                                               alt="Quà tặng" />
                                          <div class="flex-direction-y padding-left-8">
                                               <h5 class="font-bold uppercase font-size-13">Quà tặng</h5>
                                               <p class="font-size-13 font-light capitalize">Miễn phí</p>
                                          </div>
                                     </div>

                                     <div class="service-content grid-col col-l-3 col-m-3 col-s-6">
                                          <img src="/Assets/Images/Icons/Service/icon-sv3.jpg"
                                               alt="Bảo mật" />
                                          <div class="flex-direction-y padding-left-8">
                                               <h5 class="font-bold uppercase font-size-13">Bảo mật</h5>
                                               <p class="font-size-13 font-light capitalize">Thanh toán trực
                                                    tuyến</p>
                                          </div>
                                     </div>

                                     <div class="service-content grid-col col-l-3 col-m-3 col-s-6">
                                          <img src="/Assets/Images/Icons/Service/icon-sv4.jpg" alt="Hỗ trợ" />
                                          <div class="flex-direction-y padding-left-8">
                                               <h5 class="font-bold uppercase font-size-13">Hỗ trợ</h5>
                                               <p class="font-size-13 font-light capitalize">24/7</p>
                                          </div>
                                     </div>
                                </div>
                           </section>

                           <!-- best selling -->
                           <section id="best-selling-container" class="container flex grid-col col-l-12 col-m-12 col-s-12 no-gutter">
                              <div class="category-tab">
                                   <div class="heading">
                                        <div id="best-selling-label" class="heading-label"></div>
                                        <div class="uppercase font-bold font-size-20 padding-left-8" style="color: white;">Featured & Recommended</div>
                                   </div>

                                   <div class="product-container">
                                        <?php
                                             // Kết nối cơ sở dữ liệu
                                             require_once "database/connectDB.php"; 
                                             $conn = connectDB::getConnection();

                                             // Truy vấn lấy danh sách 5 sản phẩm bán chạy nhất
                                             $sql = "SELECT ProductID, ProductName, ProductImg, Price, Quantity FROM product ORDER BY Quantity DESC LIMIT 5";
                                             $result = mysqli_query($conn, $sql);

                                             // Kiểm tra và hiển thị sản phẩm   
                                             if (mysqli_num_rows($result) > 0) {
                                                  while ($product = mysqli_fetch_assoc($result)) {
                                                       $price = ($product['Price'] > 0) ? number_format($product['Price'], 0, ',', '.') . ' đ' : 'Miễn phí';


                                                       $availabilityClass = ($product['Quantity'] > 0) ? '' : 'active'; // Kiểm tra số lượng

                                                       echo '<div class="product-item grid-col col-l-2-4 col-m-3 col-s-6">';
                                                       echo '  <div class="block-product product-resize">';
                                                       echo '    <a href="/gui/game_detail.php?id=' . $product['ProductID'] . '" class="product-image js-item">';
                                                  echo '      <img src="' . $product['ProductImg'] . '" alt="' . $product['ProductName'] . '">';
                                                  echo '    </a>';
                                                       echo '    <div class="sale-off font-bold capitalize ' . $availabilityClass . '">hết hàng</div>';
                                                       echo '    <div class="info-inner flex justify-center align-center line-height-1-6">';
                                                       echo '      <h4 class="font-light capitalize" title="' . $product['ProductName'] . '">' . $product['ProductName'] . '</h4>';
                                                       echo '      <div class="margin-y-4">';
                                                       echo '        <span class="price font-bold">' . $price . '</span>';
                                                       echo '      </div>';
                                                       echo '    </div>';
                                                       echo '  </div>';
                                                       echo '  <div class="action ' . ($product['Quantity'] > 0 ? '' : 'disable') . '">';
                                                       echo '    <div class="add-to-cart">';
                                                       echo '      <div title="Thêm vào giỏ hàng" class="button add-to-cart-btn" data-id="' . $product['ProductID'] . '">';

                                                       echo '        <i class="fa-solid fa-plus" style="color: var(--primary-white);"></i>';
                                                       echo '      </div>';
                                                       echo '    </div>';
                                                       echo '  </div>';
                                                       echo '</div>';
                                                  }
                                             } else {
                                                  echo '<p>Không có sản phẩm nào thuộc danh mục này.</p>';
                                             }                                             
                                        ?>
                                   </div>

                                   <div class="flex justify-center align-center font-bold capitalize margin-bottom-16">
                                        <a href="gui/shop.php?featured=true" class="category-btn button">Xem thêm</a>
                                   </div>
                              </div>
                              </section>


                           <!-- action -->
                           <section id="action-game-container" class="container flex grid-col col-l-12 col-m-12 col-s-12 no-gutter">
                              <div class="category-tab">
                                   <div class="heading">
                                        <div id="action-game-label" class="heading-label"></div>
                                        <div class="uppercase font-bold font-size-20 padding-left-8" style="color: white;">ACTION</div>
                                   </div>

                                   <div class="product-container">
                                        <?php
                                        // Truy vấn lấy 5 sản phẩm thuộc thể loại "Hành động" (AC001)
                                        $sql = "SELECT p.ProductID, p.ProductName, p.ProductImg, p.Price, p.Quantity 
                                                  FROM product p
                                                  JOIN type_product tp ON p.ProductID = tp.ProductID
                                                  WHERE tp.TypeID = 'AC001'
                                                  LIMIT 5";

                                        $result = mysqli_query($conn, $sql);

                                        // Kiểm tra và hiển thị sản phẩm   
                                        if (mysqli_num_rows($result) > 0) {
                                             while ($product = mysqli_fetch_assoc($result)) {
                                                  $price = ($product['Price'] > 0) ? number_format($product['Price'], 0, ',', '.') . ' đ' : 'Miễn phí';

                                                  $availabilityClass = ($product['Quantity'] > 0) ? '' : 'active'; // Kiểm tra số lượng

                                                  echo '<div class="product-item grid-col col-l-2-4 col-m-3 col-s-6">';
                                                  echo '  <div class="block-product product-resize">';
                                                  echo '    <a href="/gui/game_detail.php?id=' . $product['ProductID'] . '" class="product-image js-item">';
                                                  echo '      <img src="' . $product['ProductImg'] . '" alt="' . $product['ProductName'] . '">';
                                                  echo '    </a>';
                                                  echo '    <div class="sale-off font-bold capitalize ' . $availabilityClass . '">Hết hàng</div>';
                                                  echo '    <div class="info-inner flex justify-center align-center line-height-1-6">';
                                                  echo '      <h4 class="font-light capitalize" title="' . $product['ProductName'] . '">' . $product['ProductName'] . '</h4>';
                                                  echo '      <div class="margin-y-4">';
                                                  echo '        <span class="price font-bold">' . $price . '</span>'; // Sửa lại hiển thị tiền Việt
                                                  echo '      </div>';
                                                  echo '    </div>';
                                                  echo '  </div>';
                                                  echo '  <div class="action ' . ($product['Quantity'] > 0 ? '' : 'disable') . '">';
                                                 
                                                  echo '    <div class="add-to-cart">';
                                                  echo '      <div title="Thêm vào giỏ hàng" class="button add-to-cart-btn" data-id="' . $product['ProductID'] . '">';
                                                  echo '        <i class="fa-solid fa-plus" style="color: var(--primary-white);"></i>';
                                                  echo '      </div>';
                                                  echo '    </div>';
                                                  echo '  </div>';
                                                  echo '</div>';
                                             }
                                        } else {
                                             echo '<p>Không có sản phẩm nào thuộc thể loại này.</p>';
                                        }
                                        ?>
                                   </div>

                                   <div class="flex justify-center align-center font-bold capitalize margin-bottom-16">
                                        <a href="gui/shop.php?type=AC001" class="category-btn button">Xem thêm</a>
                                   </div>
                              </div>
                              </section>
                        

                           <!-- Role-playing  -->
                           <section id="role-playing-container" class="container flex grid-col col-l-12 col-m-12 col-s-12 no-gutter">
                              <div class="category-tab">
                                   <div class="heading">
                                        <div id="role-playing-label" class="heading-label"></div>
                                        <div class="uppercase font-bold font-size-20 padding-left-8" style="color: white;">ROLE-PLAYING</div>
                                   </div>

                                   <div class="product-container">
                                        <?php

                                        // Truy vấn lấy 5 sản phẩm thuộc thể loại "Nhập vai" (RP001)
                                        $sql = "SELECT p.ProductID, p.ProductName, p.ProductImg, p.Price, p.Quantity 
                                                  FROM product p
                                                  JOIN type_product tp ON p.ProductID = tp.ProductID
                                                  WHERE tp.TypeID = 'RPG001'
                                                  LIMIT 5";

                                        $result = mysqli_query($conn, $sql);

                                        // Kiểm tra và hiển thị sản phẩm   
                                        if (mysqli_num_rows($result) > 0) {
                                             while ($product = mysqli_fetch_assoc($result)) {
                                                  $price = ($product['Price'] > 0) ? number_format($product['Price'], 0, ',', '.') . ' đ' : 'Miễn phí';
                                                  $availabilityClass = ($product['Quantity'] > 0) ? '' : 'active'; // Kiểm tra số lượng

                                                  echo '<div class="product-item grid-col col-l-2-4 col-m-3 col-s-6">';
                                                  echo '  <div class="block-product product-resize">';
                                                  echo '    <a href="/gui/game_detail.php?id=' . $product['ProductID'] . '" class="product-image js-item">';
                                                  echo '      <img src="' . $product['ProductImg'] . '" alt="' . $product['ProductName'] . '">';
                                                  echo '    </a>';
                                                  echo '    <div class="sale-off font-bold capitalize ' . $availabilityClass . '">Hết hàng</div>';
                                                  echo '    <div class="info-inner flex justify-center align-center line-height-1-6">';
                                                  echo '      <h4 class="font-light capitalize" title="' . $product['ProductName'] . '">' . $product['ProductName'] . '</h4>';
                                                  echo '      <div class="margin-y-4">';
                                                  echo '        <span class="price font-bold">' . $price . '</span>'; // Sửa lại hiển thị tiền Việt
                                                  echo '      </div>';
                                                  echo '    </div>';
                                                  echo '  </div>';
                                                  echo '  <div class="action ' . ($product['Quantity'] > 0 ? '' : 'disable') . '">';
                                             
                                                  echo '    <div class="add-to-cart">';
                                                  echo '      <div title="Thêm vào giỏ hàng" class="button add-to-cart-btn" data-id="' . $product['ProductID'] . '">';
                                                  echo '        <i class="fa-solid fa-plus" style="color: var(--primary-white);"></i>';
                                                  echo '      </div>';
                                                  echo '    </div>';
                                                  echo '  </div>';
                                                  echo '</div>';
                                             }
                                        } else {
                                             echo '<p>Không có sản phẩm nào thuộc thể loại này.</p>';
                                        }                                   
                                        ?>
                                   </div>

                                   <div class="flex justify-center align-center font-bold capitalize margin-bottom-16">
                                        <a href="gui/shop.php?type=RPG001" class="category-btn button">Xem thêm</a>
                                   </div>
                              </div>
                              </section>


                           <!-- free to play -->
                           <section id="free-to-play-container" class="container flex grid-col col-l-12 col-m-12 col-s-12 no-gutter">
                              <div class="category-tab">
                                   <div class="heading">
                                        <div id="free-to-play-label" class="heading-label"></div>
                                        <div class="uppercase font-bold font-size-20 padding-left-8" style="color: white;">FREE TO PLAY</div>
                                   </div>

                                   <div class="product-container">
                                        <?php                                             
                                             // Truy vấn lấy 5 sản phẩm Free-to-Play (F2P001)
                                             $sql = "SELECT p.ProductID, p.ProductName, p.ProductImg, p.Price, p.Quantity 
                                                  FROM product p
                                                  JOIN type_product tp ON p.ProductID = tp.ProductID
                                                  WHERE tp.TypeID = 'F2P001'
                                                  LIMIT 5";

                                             $result = mysqli_query($conn, $sql);

                                             // Kiểm tra và hiển thị sản phẩm   
                                             if (mysqli_num_rows($result) > 0) {
                                                  while ($product = mysqli_fetch_assoc($result)) {
                                                  $availabilityClass = ($product['Quantity'] > 0) ? '' : 'active'; // Kiểm tra số lượng

                                                  echo '<div class="product-item grid-col col-l-2-4 col-m-3 col-s-6">';
                                                  echo '  <div class="block-product product-resize">';
                                                  echo '    <a href="/gui/game_detail.php?id=' . $product['ProductID'] . '" class="product-image js-item">';
                                                  echo '      <img src="' . $product['ProductImg'] . '" alt="' . $product['ProductName'] . '">';
                                                  echo '    </a>';
                                                  echo '    <div class="sale-off font-bold capitalize ' . $availabilityClass . '">hết hàng</div>';
                                                  echo '    <div class="info-inner flex justify-center align-center line-height-1-6">';
                                                  echo '      <h4 class="font-light capitalize" title="' . $product['ProductName'] . '">' . $product['ProductName'] . '</h4>';
                                                  echo '      <div class="margin-y-4">';
                                                  echo '        <span class="price font-bold">Miễn phí</span>';
                                                  echo '      </div>';
                                                  echo '    </div>';
                                                  echo '  </div>';
                                                  echo '  <div class="action ' . ($product['Quantity'] > 0 ? '' : 'disable') . '">';
                                             
                                                  echo '    <div class="add-to-cart">';
                                                  echo '      <div title="Thêm vào giỏ hàng" class="button add-to-cart-btn" data-id="' . $product['ProductID'] . '">';
                                                  echo '        <i class="fa-solid fa-plus" style="color: var(--primary-white);"></i>';
                                                  echo '      </div>';
                                                  echo '    </div>';
                                                  echo '  </div>';
                                                  echo '</div>';
                                                  }
                                             } else {
                                                  echo '<p>Không có sản phẩm nào thuộc thể loại này.</p>';
                                             }                                             
                                        ?>
                                   </div>

                                   <div class="flex justify-center align-center font-bold capitalize margin-bottom-16">
                                        <a href="gui/shop.php?type=F2P001" class="category-btn button">Xem thêm</a>
                                   </div>
                              </div>
                              </section>

                           <!-- open word -->                            
                           <section id="open-world-container" class="container flex grid-col col-l-12 col-m-12 col-s-12 no-gutter">
                              <div class="category-tab">
                                   <div class="heading">
                                        <div id="open-world-label" class="heading-label"></div>
                                        <div class="uppercase font-bold font-size-20 padding-left-8" style="color: white;">OPEN WORLD</div>
                                   </div>

                                   <div class="product-container">
                                        <?php                                             
                                             // Truy vấn lấy 5 sản phẩm thuộc thể loại Open World (OW001)
                                             $sql = "SELECT p.ProductID, p.ProductName, p.ProductImg, p.Price, p.Quantity 
                                                  FROM product p
                                                  JOIN type_product tp ON p.ProductID = tp.ProductID
                                                  WHERE tp.TypeID = 'OW001'
                                                  LIMIT 5";

                                             $result = mysqli_query($conn, $sql);

                                             // Kiểm tra và hiển thị sản phẩm   
                                             if (mysqli_num_rows($result) > 0) {
                                                  while ($product = mysqli_fetch_assoc($result)) {
                                                  $price = number_format($product['Price'], 0, ',', '.') . "₫"; // Định dạng giá tiền
                                                  $availabilityClass = ($product['Quantity'] > 0) ? '' : 'active'; // Kiểm tra số lượng

                                                  echo '<div class="product-item grid-col col-l-2-4 col-m-3 col-s-6">';
                                                  echo '  <div class="block-product product-resize">';
                                                  echo '    <a href="/gui/game_detail.php?id=' . $product['ProductID'] . '" class="product-image js-item">';
                                                  echo '      <img src="' . $product['ProductImg'] . '" alt="' . $product['ProductName'] . '">';
                                                  echo '    </a>';
                                                  echo '    <div class="sale-off font-bold capitalize ' . $availabilityClass . '">hết hàng</div>';
                                                  echo '    <div class="info-inner flex justify-center align-center line-height-1-6">';
                                                  echo '      <h4 class="font-light capitalize" title="' . $product['ProductName'] . '">' . $product['ProductName'] . '</h4>';
                                                  echo '      <div class="margin-y-4">';
                                                  echo '        <span class="price font-bold">' . $price . '</span>';
                                                  echo '      </div>';
                                                  echo '    </div>';
                                                  echo '  </div>';
                                                  echo '  <div class="action ' . ($product['Quantity'] > 0 ? '' : 'disable') . '">';
                                                 
                                                  echo '    <div class="add-to-cart">';
                                                  echo '      <div title="Thêm vào giỏ hàng" class="button add-to-cart-btn" data-id="' . $product['ProductID'] . '">';
                                                  echo '        <i class="fa-solid fa-plus" style="color: var(--primary-white);"></i>';
                                                  echo '      </div>';
                                                  echo '    </div>';
                                                  echo '  </div>';
                                                  echo '</div>';
                                                  }
                                             } else {
                                                  echo '<p>Không có sản phẩm nào thuộc thể loại này.</p>';
                                             }

                                             // Đóng kết nối
                                             connectDB::closeConnection($conn);
                                        ?>
                                   </div>

                                   <div class="flex justify-center align-center font-bold capitalize margin-bottom-16">
                                        <a href="gui/shop.php?type=OW001" class="category-btn button">Xem thêm</a>
                                   </div>
                              </div>
                              </section>

                      </div>
        <?php include('gui/header_footer/new_blog.php') ?>
                      
        <?php include('gui/header_footer/footer.php') ?>