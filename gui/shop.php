<?php include("header_footer/header.php"); ?>
<div class="price-filter-container">
    <button class="price-filter" data-min="0" data-max="100000">Miễn phí - 100.000đ</button>
    <button class="price-filter" data-min="100000" data-max="550000">100.000đ - 550.000đ</button>
    <button class="price-filter" data-min="550000" data-max="950000">550.000đ - 950.000đ</button>
    <button class="price-filter" data-min="950000" data-max="1350000">950.000đ - 1.350.000đ</button>
    <button class="price-filter" data-min="1350000" data-max="1950000">1.350.000đ - 1.950.000đ</button>
</div>
<div id="results" class="grid-col col-l-12 col-m-12 col-s-12">

    <div class="category-tab">
        <div class="heading">
            <div id="shop-game-label" class="heading-label"></div>
            <div class="uppercase font-bold font-size-20 padding-left-8" style="color: white;">Các sản phẩm</div>
        </div>

        <div class="product-container"></div> <!-- Sản phẩm sẽ được tải động -->
    </div>
</div>

<?php include("header_footer/footer.php"); ?>
