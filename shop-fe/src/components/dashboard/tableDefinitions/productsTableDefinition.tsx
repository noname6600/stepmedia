import React from 'react';
import { Link } from 'react-router-dom';
import { shopSearchFilter } from '@components/common/ObjectSearchFilters';
import { defaultDateTimeField } from '@components/tables/Defaults';
import { TableDefinition } from '@components/tables/TableView';
import { currency } from '@utils/Parsers';

export const productsTableDefinition: TableDefinition = {
  tableCodeName: 'product',
  rootPath: '/api/v1/products',
  rootPage: '/dashboard/products',
  defaultPage: 0,
  defaultSize: 20,
  fields: [
    {
      variable: 'id',
      valueFormatter: value => <Link to={`/dashboard/products/detail/${value}`}>{value}</Link>,
    },
    {
      variable: 'name',
    },
    {
      variable: 'description',
    },
    {
      variable: 'price',
      valueFormatter: value => <>{`${currency(value || 0)} đ`}</>,
    },
    {
      variable: 'shop.name',
      valueFormatter: (value, rowData) => <Link to={`/dashboard/shops/detail/${rowData.shop?.id}`}>{value}</Link>,
    },
    {
      ...defaultDateTimeField,
      variable: 'createdDate',
    },
  ],
  filters: [
    {
      variable: 'name',
    },
    {
      ...shopSearchFilter,
    },
  ],
};
