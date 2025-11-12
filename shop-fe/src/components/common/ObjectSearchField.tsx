import React from 'react';

import SearchFieldSelector, { SearchFieldSelectorProps } from './inputs/SearchFieldSelector';

export const CustomerSearchField = ({ ...props }: Omit<SearchFieldSelectorProps, 'rootPath'>) => {
  return (
    <SearchFieldSelector
      {...props}
      placeholder="search"
      rootPath="/api/v1/customers"
      rootPage="/dashboard/customers"
      format={data => `${data.fullName} - ${data.email}`}
    />
  );
};

export const ShopSearchField = ({ ...props }: Omit<SearchFieldSelectorProps, 'rootPath'>) => {
  return (
    <SearchFieldSelector
      {...props}
      placeholder="search"
      rootPath="/api/v1/shops"
      rootPage="/dashboard/shops"
      format={data => `${data.name} - ${data.location}`}
    />
  );
};

export const ProductSearchField = ({ ...props }: Omit<SearchFieldSelectorProps, 'rootPath'>) => {
  return (
    <SearchFieldSelector
      {...props}
      placeholder="search"
      rootPath="/api/v1/products"
      rootPage="/dashboard/products"
      format={data => `${data.name} - ${data.price}`}
    />
  );
};

export const OrderSearchField = ({ ...props }: Omit<SearchFieldSelectorProps, 'rootPath'>) => {
  return (
    <SearchFieldSelector
      {...props}
      placeholder="search"
      rootPath="/api/v1/orders"
      rootPage="/dashboard/orders"
      format={data =>
        `[${data.product?.name} - ${data.product?.price}] ${data.customer?.fullName} @ ${data.product?.shop?.name}`
      }
    />
  );
};
