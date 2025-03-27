-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: localhost:3306
-- Thời gian đã tạo: Th3 26, 2025 lúc 01:07 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `htttgame`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `account`
--

CREATE TABLE `account` (
  `Username` varchar(50) NOT NULL,
  `Password` varchar(50) NOT NULL,
  `RoleID` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `customer`
--

CREATE TABLE `customer` (
  `CustomerID` varchar(10) NOT NULL,
  `Fullname` varchar(50) DEFAULT NULL,
  `Username` varchar(50) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL,
  `Address` varchar(255) DEFAULT NULL,
  `Phone` varchar(25) DEFAULT NULL,
  `TotalSpending` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `detail_import_invoice`
--

CREATE TABLE `detail_import_invoice` (
  `DetailImportID` varchar(10) NOT NULL,
  `ImportID` varchar(10) DEFAULT NULL,
  `ProductID` varchar(10) DEFAULT NULL,
  `Quantity` int(11) DEFAULT NULL,
  `Price` double DEFAULT NULL,
  `TotalPrice` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `detail_sales_invoice`
--

CREATE TABLE `detail_sales_invoice` (
  `DetailSalesID` varchar(10) NOT NULL,
  `SalesID` varchar(10) DEFAULT NULL,
  `ProductID` varchar(10) DEFAULT NULL,
  `Quantity` int(11) DEFAULT NULL,
  `Price` double DEFAULT NULL,
  `TotalPrice` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `employee`
--

CREATE TABLE `employee` (
  `EmployeeID` varchar(10) NOT NULL,
  `Fullname` varchar(50) DEFAULT NULL,
  `Username` varchar(50) DEFAULT NULL,
  `BirthDay` date DEFAULT NULL,
  `Phone` varchar(10) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL,
  `Address` varchar(255) DEFAULT NULL,
  `Gender` enum('Nam','Nữ') DEFAULT NULL,
  `Salary` double DEFAULT NULL,
  `StartDate` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `genre`
--

CREATE TABLE `genre` (
  `GenreID` varchar(10) NOT NULL,
  `GenreName` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `genre_detail`
--

CREATE TABLE `genre_detail` (
  `ProductID` varchar(10) NOT NULL,
  `GenreID` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `import_invoice`
--

CREATE TABLE `import_invoice` (
  `ImportID` varchar(10) NOT NULL,
  `EmployeeID` varchar(10) DEFAULT NULL,
  `SupplierID` varchar(10) DEFAULT NULL,
  `Date` date DEFAULT NULL,
  `TotalPrice` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `permissions`
--

CREATE TABLE `permissions` (
  `Permission_id` varchar(10) NOT NULL,
  `Permission_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `product`
--

CREATE TABLE `product` (
  `ProductID` varchar(10) NOT NULL,
  `ProductName` varchar(50) DEFAULT NULL,
  `ProductImg` varchar(255) DEFAULT NULL,
  `Author` varchar(50) DEFAULT NULL,
  `Publisher` varchar(50) DEFAULT NULL,
  `Quantity` int(11) DEFAULT NULL,
  `Price` double DEFAULT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `SupplierID` varchar(10) DEFAULT NULL,
  `Status` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `role`
--

CREATE TABLE `role` (
  `RoleID` varchar(10) NOT NULL,
  `RoleName` varchar(50) NOT NULL,
  `Description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `rolepermissions`
--

CREATE TABLE `rolepermissions` (
  `RoleID` varchar(10) NOT NULL,
  `Permission_id` varchar(10) NOT NULL,
  `Prop` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sales_invoice`
--

CREATE TABLE `sales_invoice` (
  `SalesID` varchar(10) NOT NULL,
  `EmployeeID` varchar(10) DEFAULT NULL,
  `CustomerID` varchar(10) DEFAULT NULL,
  `Date` date DEFAULT NULL,
  `TotalPrice` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `supplier`
--

CREATE TABLE `supplier` (
  `SupplierID` varchar(10) NOT NULL,
  `SupplierName` varchar(50) DEFAULT NULL,
  `Phone` varchar(10) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL,
  `Address` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `type_product`
--

CREATE TABLE `type_product` (
  `TypeID` varchar(10) NOT NULL,
  `ProductID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`Username`);

--
-- Chỉ mục cho bảng `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`CustomerID`),
  ADD UNIQUE KEY `Username` (`Username`);

--
-- Chỉ mục cho bảng `detail_import_invoice`
--
ALTER TABLE `detail_import_invoice`
  ADD PRIMARY KEY (`DetailImportID`),
  ADD KEY `ImportID` (`ImportID`),
  ADD KEY `ProductID` (`ProductID`);

--
-- Chỉ mục cho bảng `detail_sales_invoice`
--
ALTER TABLE `detail_sales_invoice`
  ADD PRIMARY KEY (`DetailSalesID`),
  ADD KEY `SalesID` (`SalesID`),
  ADD KEY `ProductID` (`ProductID`);

--
-- Chỉ mục cho bảng `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`EmployeeID`),
  ADD UNIQUE KEY `Username` (`Username`);

--
-- Chỉ mục cho bảng `genre`
--
ALTER TABLE `genre`
  ADD PRIMARY KEY (`GenreID`);

--
-- Chỉ mục cho bảng `genre_detail`
--
ALTER TABLE `genre_detail`
  ADD PRIMARY KEY (`ProductID`,`GenreID`),
  ADD KEY `GenreID` (`GenreID`);

--
-- Chỉ mục cho bảng `import_invoice`
--
ALTER TABLE `import_invoice`
  ADD PRIMARY KEY (`ImportID`),
  ADD KEY `EmployeeID` (`EmployeeID`),
  ADD KEY `SupplierID` (`SupplierID`);

--
-- Chỉ mục cho bảng `permissions`
--
ALTER TABLE `permissions`
  ADD PRIMARY KEY (`Permission_id`);

--
-- Chỉ mục cho bảng `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`ProductID`),
  ADD KEY `SupplierID` (`SupplierID`);

--
-- Chỉ mục cho bảng `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`RoleID`);

--
-- Chỉ mục cho bảng `rolepermissions`
--
ALTER TABLE `rolepermissions`
  ADD PRIMARY KEY (`RoleID`,`Permission_id`),
  ADD KEY `Permission_id` (`Permission_id`);

--
-- Chỉ mục cho bảng `sales_invoice`
--
ALTER TABLE `sales_invoice`
  ADD PRIMARY KEY (`SalesID`),
  ADD KEY `EmployeeID` (`EmployeeID`),
  ADD KEY `CustomerID` (`CustomerID`);

--
-- Chỉ mục cho bảng `supplier`
--
ALTER TABLE `supplier`
  ADD PRIMARY KEY (`SupplierID`);

--
-- Chỉ mục cho bảng `type_product`
--
ALTER TABLE `type_product`
  ADD PRIMARY KEY (`TypeID`),
  ADD KEY `ProductID` (`ProductID`);

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `customer`
--
ALTER TABLE `customer`
  ADD CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`Username`) REFERENCES `account` (`Username`);

--
-- Các ràng buộc cho bảng `detail_import_invoice`
--
ALTER TABLE `detail_import_invoice`
  ADD CONSTRAINT `detail_import_invoice_ibfk_1` FOREIGN KEY (`ImportID`) REFERENCES `import_invoice` (`ImportID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `detail_import_invoice_ibfk_2` FOREIGN KEY (`ProductID`) REFERENCES `product` (`ProductID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `detail_sales_invoice`
--
ALTER TABLE `detail_sales_invoice`
  ADD CONSTRAINT `detail_sales_invoice_ibfk_1` FOREIGN KEY (`SalesID`) REFERENCES `sales_invoice` (`SalesID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `detail_sales_invoice_ibfk_2` FOREIGN KEY (`ProductID`) REFERENCES `product` (`ProductID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `employee`
--
ALTER TABLE `employee`
  ADD CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`Username`) REFERENCES `account` (`Username`);

--
-- Các ràng buộc cho bảng `genre_detail`
--
ALTER TABLE `genre_detail`
  ADD CONSTRAINT `genre_detail_ibfk_1` FOREIGN KEY (`ProductID`) REFERENCES `product` (`ProductID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `genre_detail_ibfk_2` FOREIGN KEY (`GenreID`) REFERENCES `genre` (`GenreID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `import_invoice`
--
ALTER TABLE `import_invoice`
  ADD CONSTRAINT `import_invoice_ibfk_1` FOREIGN KEY (`EmployeeID`) REFERENCES `employee` (`EmployeeID`),
  ADD CONSTRAINT `import_invoice_ibfk_2` FOREIGN KEY (`SupplierID`) REFERENCES `supplier` (`SupplierID`);

--
-- Các ràng buộc cho bảng `product`
--
ALTER TABLE `product`
  ADD CONSTRAINT `product_ibfk_1` FOREIGN KEY (`SupplierID`) REFERENCES `supplier` (`SupplierID`);

--
-- Các ràng buộc cho bảng `rolepermissions`
--
ALTER TABLE `rolepermissions`
  ADD CONSTRAINT `rolepermissions_ibfk_1` FOREIGN KEY (`RoleID`) REFERENCES `role` (`RoleID`),
  ADD CONSTRAINT `rolepermissions_ibfk_2` FOREIGN KEY (`Permission_id`) REFERENCES `permissions` (`Permission_id`);

--
-- Các ràng buộc cho bảng `sales_invoice`
--
ALTER TABLE `sales_invoice`
  ADD CONSTRAINT `sales_invoice_ibfk_1` FOREIGN KEY (`EmployeeID`) REFERENCES `employee` (`EmployeeID`),
  ADD CONSTRAINT `sales_invoice_ibfk_2` FOREIGN KEY (`CustomerID`) REFERENCES `customer` (`CustomerID`);

--
-- Các ràng buộc cho bảng `type_product`
--
ALTER TABLE `type_product`
  ADD CONSTRAINT `type_product_ibfk_1` FOREIGN KEY (`ProductID`) REFERENCES `product` (`ProductID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
