import { TableDefinition } from '@components/tables/TableView';

import { customersTableDefinition } from './customersTableDefinition';
import { ordersTableDefinition } from './ordersTableDefinition';
import { productsTableDefinition } from './productsTableDefinition';
import { shopsTableDefinition } from './shopsTableDefinition';

export const allTableDefinition: TableDefinition[] = [
  customersTableDefinition,
  shopsTableDefinition,
  productsTableDefinition,
  ordersTableDefinition,
];
