import React, { useCallback, useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useSearchParams } from 'react-router-dom';
import ChipTag from '@components/common/ChipTag';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { parseParams } from '@utils/Parsers';

import FilterModal from './FilterModal';
import { TableDefinition, TableFilter } from './TableView';

export interface FiltersSectionProps {
  table: TableDefinition;
}

const FiltersSection = ({ table }: FiltersSectionProps) => {
  const { t } = useTranslation();
  const [searchParams, setSearchParams] = useSearchParams();
  const [filterModalOpen, setFilterModalOpen] = useState(false);
  const [filters, setFilters] = useState<TableFilter[]>([]);

  useEffect(() => {
    const params = parseParams(searchParams);
    const newFilters: TableFilter[] = [];

    Object.keys(params).forEach(async key => {
      const tableFilter = table.filters?.find(item => item.variable === key);

      tableFilter &&
        newFilters.push({
          ...tableFilter,
          value: tableFilter.format ? tableFilter.format(params[key]) : tableFilter.asyncFormat ? '...' : params[key],
        });

      tableFilter &&
        tableFilter.asyncFormat &&
        tableFilter.asyncFormat(params[key]).then(value => {
          setFilters(oldFilters => {
            const newFetchedFilters = [...oldFilters];
            const currentFilter = newFilters.find(filter => filter.variable === tableFilter.variable);
            if (currentFilter) {
              currentFilter.value = value;
            }
            return newFetchedFilters;
          });
        });
    });

    setFilters(newFilters);
  }, [searchParams, table.filters]);

  const onRemoveFilter = useCallback(
    (variable: string) => {
      searchParams.delete(variable);
      setSearchParams(searchParams);
    },
    [setSearchParams, searchParams],
  );

  const onAddFilter = useCallback(
    (variable: string, value: string) => {
      searchParams.set(variable, value);
      searchParams.set('number', '0');
      setSearchParams(searchParams);
    },
    [searchParams, setSearchParams],
  );

  return (
    <div className="flex flex-row">
      <div className="grow flex flex-row items-center flex-wrap space-2">
        {filters?.map(item => (
          <ChipTag key={`filters.${item.variable}`} onClose={() => onRemoveFilter(item.variable)}>
            {`${t(`${table.tableCodeName}.${item.variable}`)}: ${item.value}`}
          </ChipTag>
        ))}
        {table.filters && table.filters.length > 0 && (
          <ChipTag onClick={() => setFilterModalOpen(true)}>
            <FontAwesomeIcon icon="filter" className="pr-2" />
            <span>{t('common.filter')}</span>
          </ChipTag>
        )}
      </div>
      <FilterModal
        tableCodeName={table.tableCodeName}
        modalOpen={filterModalOpen}
        setModalOpen={setFilterModalOpen}
        addFilter={onAddFilter}
        filters={table.filters || []}
      />
    </div>
  );
};

export default FiltersSection;
