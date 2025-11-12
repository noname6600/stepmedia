import React from 'react';
import { CustomersApi, OrdersApi, ProductsApi, ShopsApi } from '@api/api';

import { TableFilter } from '../tables/TableView';

import SearchFieldSelector from './inputs/SearchFieldSelector';

export const customerSearchFilter: TableFilter = {
  variable: 'customerId',
  asyncFormat: async value => {
    return new CustomersApi()
      .customerControllerGetDetailById({ id: value || '' })
      .then(response => `${response.data.data?.fullName} - ${response.data.data?.email}`)
      .catch(() => undefined);
  },
  customInput: (value, onChange) => (
    <SearchFieldSelector
      value={value}
      onChange={onChange}
      placeholder="search"
      rootPath="/api/v1/customers"
      format={data => `${data.fullName} - ${data.email}`}
    />
  ),
};

export const shopSearchFilter: TableFilter = {
  variable: 'shopId',
  asyncFormat: async value => {
    return new ShopsApi()
      .shopControllerGetDetailById({ id: value || '' })
      .then(response => `${response.data.data?.name} - ${response.data.data?.location}`)
      .catch(() => undefined);
  },
  customInput: (value, onChange) => (
    <SearchFieldSelector
      value={value}
      onChange={onChange}
      placeholder="search"
      rootPath="/api/v1/shops"
      format={data => `${data.name} - ${data.location}`}
    />
  ),
};

export const productSearchFilter: TableFilter = {
  variable: 'productId',
  asyncFormat: async value => {
    return new ProductsApi()
      .productControllerGetDetailById({ id: value || '' })
      .then(response => `${response.data.data?.name} - ${response.data.data?.price}`)
      .catch(() => undefined);
  },
  customInput: (value, onChange) => (
    <SearchFieldSelector
      value={value}
      onChange={onChange}
      placeholder="search"
      rootPath="/api/v1/products"
      format={data => `${data.name} - ${data.price}`}
    />
  ),
};

export const orderSearchFilter: TableFilter = {
  variable: 'orderId',
  asyncFormat: async value => {
    return new OrdersApi()
      .orderControllerGetDetailById({ id: value || '' })
      .then(
        response =>
          `[${response.data.data?.product?.name} - ${response.data.data?.product?.price}] ${response.data.data?.customer?.fullName} @ ${response.data.data?.product?.shop?.name}`,
      )
      .catch(() => undefined);
  },
  customInput: (value, onChange) => (
    <SearchFieldSelector
      value={value}
      onChange={onChange}
      placeholder="search"
      rootPath="/api/v1/orders"
      format={data =>
        `[${data.product?.name} - ${data.product?.price}] ${data.customer?.fullName} @ ${data.product?.shop?.name}`
      }
    />
  ),
};
