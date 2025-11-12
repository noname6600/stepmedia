import React from 'react';
import { Link } from 'react-router-dom';
import EnumBadge from '@components/common/EnumBadge';
import { customerSearchFilter, productSearchFilter, shopSearchFilter } from '@components/common/ObjectSearchFilters';
import { defaultDateTimeField } from '@components/tables/Defaults';
import { TableDefinition } from '@components/tables/TableView';
import { currency } from '@utils/Parsers';

export const ordersTableDefinition: TableDefinition = {
  tableCodeName: 'order',
  rootPath: '/api/v1/orders',
  rootPage: '/dashboard/orders',
  defaultPage: 0,
  defaultSize: 20,
  fields: [
    {
      variable: 'id',
      valueFormatter: value => <Link to={`/dashboard/orders/detail/${value}`}>{value}</Link>,
    },
    {
      variable: 'status',
      valueFormatter: value => <EnumBadge enumType="orderStatus" enumValue={value} />,
    },
    {
      variable: 'customer',
      valueFormatter: (_value, rowData) => <>{`${rowData?.customer?.fullName} - ${rowData?.customer?.email}`}</>,
    },
    {
      variable: 'shop',
      valueFormatter: (_value, rowData) => (
        <>{`${rowData?.product?.shop?.name} - ${rowData?.product?.shop?.location}`}</>
      ),
    },
    {
      variable: 'product',
      valueFormatter: (_value, rowData) => (
        <>{`${rowData?.product?.name} - ${currency(rowData?.product?.price || 0)} đ`}</>
      ),
    },
    {
      ...defaultDateTimeField,
      variable: 'createdDate',
    },
  ],
  filters: [
    {
      ...customerSearchFilter,
    },
    {
      ...shopSearchFilter,
    },
    {
      ...productSearchFilter,
    },
  ],
};
