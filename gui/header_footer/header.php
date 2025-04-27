<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link
      rel="shortcut icon"
      href="/Assets/Images/Icons/Web_logo/main-logo.png"
      type="image/x-icon"
    />
    <link
      rel="stylesheet"
      href="/Assets/Font/fontawesome-6.6.0/css/all.min.css"
    />
    <link rel="stylesheet" href="/Assets/CSS/index.css" />
    <link rel="stylesheet" href="/Assets/CSS/Responsive.css" />
    <script type="module" src="/Javascript/end_user/index.js"></script>
    <title>Welcome to MT3H</title>
  </head>

  <body>
    <div class="web-content">
      <!-- html for header -->
      <header id="header-container">
        <section class="grid wide">
          <section class="grid-row flex justify-center">
            <!-- header bar with most useful function for users -->
            <!-- header for mobile and tablet -->
            <div
              class="s-m-header s-m-content flex justify-center align-center grid-col col-m-12 col-s-12 no-gutter"
            >
              <div class="header-items full-height s-m-content s-m-nav-btn">
                <i
                  class="fa-solid fa-list fa-xl"
                  style="color: var(--main-color)"
                ></i>
              </div>
                <div class="web-logo full-height">
                  <div>
                    <img
                      src="/Assets/Images/Icons/Web_logo/MT3H.png"
                      alt="MT3H"
                    />
                  </div>
                </div>

              <div class="header-items full-height">
                <div class="cart-btn full-height">
                  <div class="flex align-center full-height">
                    <div class="cart-count item-count">0</div>
                    <i
                      class="fa-solid fa-cart-shopping fa-xl"
                      style="color: var(--main-color)"
                    ></i>
                    <span class="padding-left-8 font-size-14 s-m-hidden"
                      >Giỏ hàng</span
                    >
                  </div>
                </div>
              </div>
            </div>
            <!-- end header for mobile and tablet -->
            <div class="grid-col col-l-2 col-m-8 col-s-8 web-logo s-m-hidden">
              <a href="/index.php">  
                <div>
                  <img
                    src="/Assets/Images/Icons/Web_logo/MT3H.png"
                    alt="Web bán game"
                  />
                </div>
              </a>
            </div>

            <section
              class="search-container grid-col col-l-5 col-m-12 col-s-12"
            >
              <div
                class="search-form-container flex justify-center align-center"
              >
                <form method="get" autocomplete="on">
                  <label for="search-input">
                    <input
                      type="text"
                      name="query"
                      id="search-input"
                      class="no-outline"
                      size="100%"
                      placeholder="Tìm kiếm sản phẩm...."
                    />
                  </label>
                  <button
                    type="button"
                    class="search-btn button flex justify-center align-center"
                  >
                    <i
                      class="fa-solid fa-magnifying-glass fa-gl"
                      style="color: var(--primary-white)"
                    ></i>
                  </button>
                </form>
              </div>
            </section>

            <section
              class="header-items grid-col col-l-5 col-m-2 col-s-2 s-m-hidden font-size-14"
            >
              

              <div class="hotline full-height">
                <div class="flex align-center full-height">
                  <i
                    class="fa-solid fa-phone-volume fa-xl"
                    style="color: var(--main-color)"
                  ></i>
                  <div class="flex-direction-y padding-left-8">
                    <span>Hotline</span>
                    <span class="font-bold" style="color: var(--main-color)"
                      >0842498xxxx</span
                    >
                  </div>
                </div>
              </div>

              <a href="/gui/cart.php">
                <div class="cart-btn full-height">
                  <div class="flex align-center full-height">
                    <div class="cart-count item-count">0</div>
                    <i class="fa-solid fa-cart-shopping fa-xl" style="color: var(--main-color)"></i>
                    <span class="padding-left-8">Giỏ hàng</span>
                  </div>
                </div>
              </a>

              <div id="user-account" class="account full-height">
                <div class="flex align-center full-height">
                  <div id="no-sign-in" class="flex justify-center align-center">
                    <i
                      class="fa-regular fa-circle-user fa-xl"
                      style="color: var(--main-color)"
                    ></i>
                    <p class="padding-left-8">Tài khoản</p>
                  </div>
                  <a href="/gui/account/user_detail.php?featured=true">

                  <div class="header-user-info flex justify-center align-center disable">
                    
                    <i
                      class="fa-brands fa-napster fa-xl"
                      style="color: var(--main-color)"
                    ></i>
                    <div class="user-name padding-left-8 uppercase">
                      user-profile
                    </div>
                  </div></a>

                  <!-- account -->
                  <nav class="nav-account flex justify-center align-center flex-direction-y">
                    <button
                      type="button"
                      title="Đăng nhập"
                      class="lnw-btn active margin-bottom-16"
                      onclick="window.location.href='/gui/account/login.php'"
                    >
                      <div class="font-bold uppercase">Đăng nhập</div>
                    </button>

                    <button
                      type="button"
                      title="Đăng ký"
                      class="lnw-btn"
                      onclick="window.location.href='/gui/account/register.php'"
                    >
                      <div class="font-bold uppercase">Đăng ký</div>
                    </button>
                    <button
                      type="button"
                      title="Đăng xuất"
                      class="lnw-btn disable"
                      onclick="window.location.href='/gui/account/logout.php';"
                    >
                      <div class="font-bold uppercase">Đăng xuất</div>
                    </button>
                  </nav>

                </div>
              </div>
            </section>
          </section>
        </section>
      </header>
      

      <main id="main-container">
        <!-- html for sub header -->
        <section id="sub-header" class="s-m-hidden">
          <div class="grid wide">
            <div class="grid-row">
              <!-- Items in sub header  -->
              <div class="sub-menu min-height-40 grid-col col-l-12">
                <div
                  class="sub-menu-item menu-nav grid-col col-l-2-4 no-gutter"
                >
                  <!-- Nav button -->
                  <div
                    class="flex justify-center align-center list-btn min-height-40 full-width"
                  >
                    <span
                      ><i
                        class="fa-solid fa-list"
                        style="color: var(--primary-white)"
                      ></i
                    ></span>
                    <p class="padding-left-8 font-size-13 font-bold">
                      Danh mục sản phẩm
                    </p>

                    <!-- Nav content -->
                    <nav class="nav-categories js-bg-white full-width">
                    <div class="nav-item">
                          <a class="filter-btn flex full-width" data-type="AC001">
                              <img src="/Assets/Images/Icons/Other_icons/hq720.jpg" alt="ACTION" width="20em" />
                              <p class="padding-left-8 font-size-14 font-bold">ACTION</p>
                          </a>
                      </div>
                      <div class="nav-item">
                          <a class="filter-btn flex full-width" data-type="RPG001">
                              <img src="/Assets/Images/Icons/Other_icons/LN.webp" alt="ROLE-PLAYING" width="20em" />
                              <p class="padding-left-8 font-size-14 font-bold">ROLE-PLAYING</p>
                          </a>
                      </div>
                      <div class="nav-item">
                          <a class="filter-btn flex full-width" data-type="F2P001">
                              <img src="/Assets/Images/Icons/Other_icons/Manga.webp" alt="FREE TO PLAY" width="20em" />
                              <p class="padding-left-8 font-size-14 font-bold">FREE TO PLAY</p>
                          </a>
                      </div>                  
                      <div class="nav-item">
                          <a class="filter-btn flex full-width" data-type="OW001">
                              <img src="/Assets/Images/Icons/Other_icons/Collage-Maker-15-Sep-2023-02-43-PM-9410.jpg" alt="OPEN WORLD" width="20em" />
                              <p class="padding-left-8 font-size-14 font-bold">OPEN WORLD</p>
                          </a>
                      </div>                      
                  </nav>

                  </div>
                </div>

                <div
                  class="sub-menu-item history-order-link grid-col col-l-2-4"
                >
                  <div class="full-height full-width">
                    <span>
                      <i
                        class="fa-solid fa-file-invoice fa-lg"
                        style="color: var(--main-color)"
                      ></i>
                    </span><a href="/gui/history.php">
                    <p class="padding-left-8 font-size-13 font-bold">
                      Lịch sử mua hàng
                    </p></a>
                  </div>
                </div>

                <div class="sub-menu-item grid-col col-l-2-4 news-nav">
                  <div class="full-width full-height">
                    <span
                      ><i
                        class="fa-regular fa-newspaper fa-lg"
                        style="color: var(--main-color)"
                      ></i
                    ></span>
                    <a href="/gui/news.php"><p class="padding-left-8 font-size-13 font-bold">
                      Tin tức & Sự kiện
                    </p></a>
                  </div>
                </div>

                <div class="sub-menu-item grid-col col-l-2-4 lnw-store">
                  <div class="full-height full-width">
                    <span
                      ><i
                        class="fa-brands fa-fantasy-flight-games fa-lg"
                        style="color: var(--main-color)"
                      ></i
                    ></span>
                    <a href="/gui/MT3H.php">
                    <p class="padding-left-8 font-size-13 font-bold">MT3H</p></a>
                  </div>
                </div>

                <div class="sub-menu-item grid-col col-l-2-4 services">
                  <div class="full-width full-height">
                    <span>
                      <i
                        class="fa-solid fa-headset fa-lg"
                        style="color: var(--main-color)"
                      ></i>
                    </span>
                   <a href="/gui/contact.php"> <p class="padding-left-8 font-size-13 font-bold">Liên hệ</p></a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>
        <section class="breadcrumb-list disable">
            <div class="grid wide">
                 <div class="grid-row">
                      <div class="grid-col col-l-12">
                           <ol class="capitalize font-size-14">
                                <li>
                                     <a href="">
                                          <i class="fa-solid fa-house" style="color: var(--main-color)"></i>
                                          <span class="font-bold font-size-14"
                                               style="color: var(--main-color)">Trang chủ</span>
                                     </a>
                                </li>

                                <li>
                                     <span>Hello</span>
                                </li>
                           </ol>
                      </div>
                 </div>
            </div>
       </section>
       <!-- Thông báo địa chỉ -->
<div id="address-warning" class="address-warning hidden">Chưa có địa chỉ mặc định.</div>
<div id="cart-added-alert" class="cart-added-alert hidden">Đã thêm vào giỏ hàng!</div>
       <!-- html for main content -->
       <section id="main-content">
            
            <!-- main items -->
            <section class="grid wide">
                 <div class="grid-row"></div>