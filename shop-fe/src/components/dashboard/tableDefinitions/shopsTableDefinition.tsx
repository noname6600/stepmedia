import React from 'react';
import { Link } from 'react-router-dom';
import { defaultDateTimeField } from '@components/tables/Defaults';
import { TableDefinition } from '@components/tables/TableView';

export const shopsTableDefinition: TableDefinition = {
  tableCodeName: 'shop',
  rootPath: '/api/v1/shops',
  rootPage: '/dashboard/shops',
  defaultPage: 0,
  defaultSize: 20,
  fields: [
    {
      variable: 'id',
      valueFormatter: value => <Link to={`/dashboard/shops/detail/${value}`}>{value}</Link>,
    },
    {
      variable: 'name',
    },
    {
      variable: 'location',
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
      variable: 'location',
    },
  ],
};
