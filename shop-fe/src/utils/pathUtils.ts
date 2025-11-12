import { TableDefinition } from '@components/tables/TableView';

import { allTableDefinition } from '../components/dashboard/tableDefinitions/allTableDefinition';

export const getTableDefinitionFromPath = (
  path: string,
): { tableDefinition: TableDefinition; id?: string } | undefined => {
  for (const tableDefinition of allTableDefinition) {
    if (path.startsWith(tableDefinition.rootPage)) {
      const id = path.startsWith(`${tableDefinition.rootPage}/detail/`)
        ? path.replace(`${tableDefinition.rootPage}/detail/`, '')
        : undefined;
      return { tableDefinition, id };
    }
  }
};
