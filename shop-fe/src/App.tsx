import React from 'react';
import { HashRouter, Navigate, Route, Routes } from 'react-router-dom';
import CustomerDetail from '@components/dashboard/detailPages/CustomerDetail';
import OrderDetail from '@components/dashboard/detailPages/OrderDetail';
import ProductDetail from '@components/dashboard/detailPages/ProductDetail';
import ShopDetail from '@components/dashboard/detailPages/ShopDetail';
import HomePage from '@components/homePage/HomePage';
import TableView from '@components/tables/TableView';
import { library } from '@fortawesome/fontawesome-svg-core';
import {
  faAngleLeft,
  faAngleRight,
  faAnglesLeft,
  faAnglesRight,
  faBell,
  faBus,
  faBusSimple,
  faChartSimple,
  faChevronDown,
  faCircleCheck,
  faCircleInfo,
  faCircleQuestion,
  faCircleXmark,
  faCopy,
  faCreditCard,
  faEye,
  faEyeSlash,
  faFilter,
  faFolderOpen,
  faGauge,
  faHandPointRight,
  faLayerGroup,
  faMagnifyingGlass,
  faMapLocation,
  faPlus,
  faReceipt,
  faShop,
  faTaxi,
  faTicket,
  faTrash,
  faTriangleExclamation,
  faUser,
  faUsers,
  faUserShield,
  faUserTie,
  faWallet,
  faXmark,
} from '@fortawesome/free-solid-svg-icons';
import DashboardPage from '@pages/DashboardPage';

import '@i18n/config';

import './css/style.css';

library.add(
  faBusSimple,
  faBus,
  faHandPointRight,
  faUserTie,
  faChevronDown,
  faCopy,
  faUserShield,
  faUser,
  faWallet,
  faTicket,
  faUsers,
  faTaxi,
  faCreditCard,
  faBell,
  faCircleQuestion,
  faChartSimple,
  faGauge,
  faEye,
  faEyeSlash,
  faFolderOpen,
  faXmark,
  faMagnifyingGlass,
  faPlus,
  faFilter,
  faMapLocation,
  faTrash,
  faCircleInfo,
  faTriangleExclamation,
  faCircleCheck,
  faCircleXmark,
  faAngleRight,
  faAngleLeft,
  faAnglesRight,
  faAnglesLeft,
  faShop,
  faReceipt,
  faLayerGroup,
);

export default function App() {
  return (
    <HashRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/dashboard/home" replace />} />
        <Route path="/dashboard/home" element={<DashboardPage children={<HomePage />} />} />
        <Route path="/dashboard/customers" element={<DashboardPage children={<TableView />} />} />
        <Route path="/dashboard/shops" element={<DashboardPage children={<TableView />} />} />
        <Route path="/dashboard/products" element={<DashboardPage children={<TableView />} />} />
        <Route path="/dashboard/orders" element={<DashboardPage children={<TableView />} />} />
        /** Detail page **/
        <Route
          path="/dashboard/customers/detail/:id"
          element={<DashboardPage children={<CustomerDetail title="customer" />} />}
        />
        <Route path="/dashboard/shops/detail/:id" element={<DashboardPage children={<ShopDetail title="shop" />} />} />
        <Route
          path="/dashboard/products/detail/:id"
          element={<DashboardPage children={<ProductDetail title="product" />} />}
        />
        <Route
          path="/dashboard/orders/detail/:id"
          element={<DashboardPage children={<OrderDetail title="order" />} />}
        />
      </Routes>
    </HashRouter>
  );
}
