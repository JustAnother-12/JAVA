
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link
      rel="shortcut icon"
      href="../Assets/Images/Icons/Web_logo/main-logo.png"
      type="image/x-icon"
    />
    <link
      rel="stylesheet"
      href="../../Assets/Font/fontawesome-6.6.0/css/all.min.css"
    />
    <link rel="stylesheet" href="../../Assets/CSS/index.css" />
    <link rel="stylesheet" href="../Assets/CSS/Responsive.css" />
    <script type="module" src="../Javascript/Index.js"></script>
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

                <div class="overlay">
                  <div class="s-m-nav-content s-m-content">
                    <div class="s-m-nav-btn flex align-center margin-y-12">
                      <button
                        type="button"
                        title="Đăng nhập"
                        class="lnw-btn js-login active"
                      >
                        <div class="font-bold uppercase">Đăng nhập</div>
                      </button>
                      <button
                        type="button"
                        title="Đăng ký"
                        class="lnw-btn js-register margin-left-16 active"
                      >
                        <div class="font-bold uppercase">Đăng ký</div>
                      </button>
                      <button
                        type="button"
                        title="Đăng xuất"
                        class="lnw-btn js-signout margin-left-16"
                      >
                        <div class="font-bold uppercase">Đăng xuất</div>
                      </button>
                    </div>

                    <ul class="s-m-nav-list">
                      <li class="web-logo">
                        <div>Trang chủ</div>
                      </li>

                      <li class="history-order-link">
                        <div>Lịch sử mua hàng</div>
                      </li>

                      <li class="books-store">
                        <div>Tủ sách thương hiệu</div>
                      </li>

                      <li class="news-nav">
                        <div>Tin tức & Sự kiện</div>
                      </li>

                      <li class="services">
                        <div>Liên hệ</div>
                      </li>
                    </ul>
                  </div>
                </div>
              </div>

              <div class="web-logo full-height">
                <div>
                  <img
                    src="../Assets/Images/Icons/Web_logo/MT3H.png"
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
              <div>
                <img
                  src="../Assets/Images/Icons/Web_logo/MT3H.png"
                  alt="Light Novel World"
                />
              </div>
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
              <!-- <div id="change-bg-btn" class="full-height">
                  <a href="#" onclick="return false" title="Change background color" >
                      <i class="fa-solid fa-circle-half-stroke fa-xl btn" style="color: var(--main-color);"></i>
                  </a>
            </div> -->

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

              <div class="cart-btn full-height">
                <div class="flex align-center full-height">
                  <div class="cart-count item-count">0</div>
                  <i
                    class="fa-solid fa-cart-shopping fa-xl"
                    style="color: var(--main-color)"
                  ></i>
                  <span class="padding-left-8">Giỏ hàng</span>
                </div>
              </div>

              <div id="user-account" class="account full-height">
                <div class="flex align-center full-height">
                  <div id="no-sign-in" class="flex justify-center align-center">
                    <i
                      class="fa-regular fa-circle-user fa-xl"
                      style="color: var(--main-color)"
                    ></i>
                    <p class="padding-left-8">Tài khoản</p>
                  </div>

                  <div
                    class="header-user-info flex justify-center align-center disable"
                  >
                    <i
                      class="fa-brands fa-napster"
                      style="color: var(--main-color)"
                    ></i>
                    <div class="user-name font-bold font-size-14">
                      user-profile
                    </div>
                  </div>

                  <!-- account -->
                  <nav
                    class="nav-account flex justify-center align-center flex-direction-y"
                  >
                    <button
                      type="button"
                      title="Đăng nhập"
                      class="lnw-btn active js-login margin-bottom-16"
                    >
                      <div class="font-bold uppercase">Đăng nhập</div>
                    </button>

                    <button
                      type="button"
                      title="Đăng ký"
                      class="lnw-btn js-register"
                    >
                      <div class="font-bold uppercase">Đăng ký</div>
                    </button>

                    <button
                      type="button"
                      title="Đăng xuất"
                      class="lnw-btn js-signout disable"
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
                        <a
                          href=""
                          class="manga flex full-width"
                          title="manga-container"
                        >
                          <img
                            src="../Assets/Images/Icons/Other_icons/Manga.webp"
                            alt="FREE TO PLAY"
                            width="20em"
                          />
                          <p class="padding-left-8 font-size-14 font-bold">
                            FREE TO PLAY
                          </p>
                        </a>
                      </div>
                      <div class="nav-item">
                        <a
                          href=""
                          class="light-novel flex full-width"
                          title="light-novel-container"
                        >
                          <img
                            src="../Assets/Images/Icons/Other_icons/LN.webp"
                            alt="ROLE-PLAYING"
                            width="20em"
                          />
                          <p class="padding-left-8 font-size-14 font-bold">
                            ROLE-PLAYING
                          </p>
                        </a>
                      </div>
                      <div class="nav-item">
                        <a
                          href=""
                          class="light-novel flex full-width"
                          title="light-novel-container"
                        >
                          <img
                            src="../Assets/Images/Icons/Other_icons/Collage-Maker-15-Sep-2023-02-43-PM-9410.jpg"
                            alt="OPEN WORLD"
                            width="20em"
                          />
                          <p class="padding-left-8 font-size-14 font-bold">
                            OPEN WORLD
                          </p>
                        </a>
                      </div>
                      <div class="nav-item">
                        <a
                          href=""
                          class="light-novel flex full-width"
                          title="light-novel-container"
                        >
                          <img
                            src="../Assets/Images/Icons/Other_icons/ExitLag-Best-Sports-Games-for-All-Platforms-in-2024.webp"
                            alt="ALL SPORTS"
                            width="20em"
                          />
                          <p class="padding-left-8 font-size-14 font-bold">
                            ALL SPORTS
                          </p>
                        </a>
                      </div>
                      <div class="nav-item">
                        <a
                          href=""
                          class="light-novel flex full-width"
                          title="light-novel-container"
                        >
                          <img
                            src="../Assets/Images/Icons/Other_icons/hq720.jpg"
                            alt="ACTION"
                            width="20em"
                          />
                          <p class="padding-left-8 font-size-14 font-bold">
                            ACTION
                          </p>
                        </a>
                      </div>
                      <div class="nav-item">
                        <a
                          href=""
                          class="light-novel flex full-width"
                          title="light-novel-container"
                        >
                          <img
                            src="../Assets/Images/Icons/Other_icons/survivalgames-1576274136.jpg"
                            alt="SURVIVAL"
                            width="20em"
                          />
                          <p class="padding-left-8 font-size-14 font-bold">
                            SURVIVAL
                          </p>
                        </a>
                      </div>
                      <div class="nav-item">
                        <a
                          href=""
                          class="light-novel flex full-width"
                          title="light-novel-container"
                        >
                          <img
                            src="../Assets/Images/Icons/Other_icons/images.jpg"
                            alt="HORROR"
                            width="20em"
                          />
                          <p class="padding-left-8 font-size-14 font-bold">
                            HORROR
                          </p>
                        </a>
                      </div>
                      <div class="nav-item">
                        <a
                          href=""
                          class="light-novel flex full-width"
                          title="light-novel-container"
                        >
                          <img
                            src="../Assets/Images/Icons/Other_icons/best-nintendo-switch-couch-co-op-games.webp"
                            alt="CO-OPERATTIVE"
                            width="20em"
                          />
                          <p class="padding-left-8 font-size-14 font-bold">
                            CO-OPERATTIVE
                          </p>
                        </a>
                      </div>
                      <div class="nav-item">
                        <a
                          href=""
                          class="light-novel flex full-width"
                          title="light-novel-container"
                        >
                          <img
                            src="../Assets/Images/Icons/Other_icons/simulation.webp"
                            alt="SIMULATOR"
                            width="20em"
                          />
                          <p class="padding-left-8 font-size-14 font-bold">
                            SIMULATOR
                          </p>
                        </a>
                      </div>
                      <div class="nav-item">
                        <a
                          href=""
                          class="light-novel flex full-width"
                          title="light-novel-container"
                        >
                          <img
                            src="../Assets/Images/Icons/Other_icons/fighting_martial_arts.webp"
                            alt="light novel"
                            width="20em"
                          />
                          <p class="padding-left-8 font-size-14 font-bold">
                            FIGHTING
                          </p>
                        </a>
                      </div>
                      <div class="nav-item">
                        <a
                          href=""
                          class="light-novel flex full-width"
                          title="light-novel-container"
                        >
                          <img
                            src="../Assets/Images/Icons/Other_icons/story_rich.webp"
                            alt="light novel"
                            width="20em"
                          />
                          <p class="padding-left-8 font-size-14 font-bold">
                            STORY-RICH
                          </p>
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
                    </span>
                    <p class="padding-left-8 font-size-13 font-bold">
                      Lịch sử mua hàng
                    </p>
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
                    <p class="padding-left-8 font-size-13 font-bold">
                      Tin tức & Sự kiện
                    </p>
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
                    <p class="padding-left-8 font-size-13 font-bold">MT3H</p>
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
                    <p class="padding-left-8 font-size-13 font-bold">Liên hệ</p>
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

       <!-- html for main content -->
       <section id="main-content">
            
            <!-- main items -->
            <section class="grid wide">
                 <div class="grid-row"></div>