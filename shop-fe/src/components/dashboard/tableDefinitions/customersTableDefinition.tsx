import React from 'react';
import { Link } from 'react-router-dom';
import { defaultDateTimeField } from '@components/tables/Defaults';
import { TableDefinition } from '@components/tables/TableView';

export const customersTableDefinition: TableDefinition = {
  tableCodeName: 'customer',
  rootPath: '/api/v1/customers',
  rootPage: '/dashboard/customers',
  defaultPage: 0,
  defaultSize: 20,
  fields: [
    {
      variable: 'id',
      valueFormatter: value => <Link to={`/dashboard/customers/detail/${value}`}>{value}</Link>,
    },
    {
      variable: 'fullName',
    },
    {
      variable: 'dob',
    },
    {
      variable: 'email',
    },
    {
      ...defaultDateTimeField,
      variable: 'createdDate',
    },
  ],
  filters: [
    {
      variable: 'fullName',
    },
    {
      variable: 'email',
    },
  ],
};
