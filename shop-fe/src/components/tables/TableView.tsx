import React, {
  ChangeEventHandler,
  HTMLProps,
  InputHTMLAttributes,
  ReactElement,
  ReactNode,
  useEffect,
  useMemo,
  useState,
} from 'react';
import { useTranslation } from 'react-i18next';
import { useLocation, useSearchParams } from 'react-router-dom';
import EmptyData from '@components/common/EmptyData';
import Spinner from '@components/common/Spinner';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { parseParams } from '@utils/Parsers';
import { getTableDefinitionFromPath } from '@utils/pathUtils';
import globalAxios from 'axios';
import _ from 'lodash';
// eslint-disable-next-line import/no-extraneous-dependencies
import Table from 'rc-table';

import FiltersSection from './FiltersSection';

interface PaginationNodeProps extends HTMLProps<HTMLDivElement> {
  active?: boolean;
}

export interface TableField {
  variable: string;
  overrideTranslate?: string;
  valueFormatter?: (value: any, rowData?: any) => ReactNode;
  sticky?: 'left' | 'right' | true;
}

export interface TableFilter {
  variable: string;
  value?: string;
  format?: (value?: string) => string;
  asyncFormat?: (value?: string) => Promise<string | undefined>;
  toValue?: (value?: string) => string;
  customInput?: (value: string, onChange: ChangeEventHandler<HTMLInputElement | HTMLTextAreaElement>) => ReactElement;
  inputProps?: InputHTMLAttributes<HTMLInputElement | HTMLTextAreaElement>;
}

export interface TableDefinition {
  tableCodeName: string;
  rootPath: string;
  rootPage: string;
  fields: TableField[];
  filters?: TableFilter[];
  defaultPage: number;
  defaultSize: number;
  createDisable?: boolean;
  editDisable?: boolean;
  deleteDisable?: boolean;
  buttons?: JSX.Element[];
}

export default function TableView() {
  const { t } = useTranslation();
  const location = useLocation();
  const [searchParams, setSearchParams] = useSearchParams();
  const [data, setData] = useState<{ total: number | undefined; contents?: any[] | undefined }>();
  const pathTable = useMemo(() => getTableDefinitionFromPath(location.pathname), [location.pathname]);

  useEffect(() => {
    const controller = new AbortController();
    setData({
      total: 0,
    });
    searchParams.has('size') &&
      globalAxios
        .get(`${pathTable?.tableDefinition.rootPath}`, {
          params: parseParams(searchParams),
          signal: controller.signal,
        })
        .then(response => {
          setData({
            contents: response.data.data?.content,
            total: response.data.data?.totalElements,
          });
        })
        .catch(() => {
          setData({
            total: 0,
          });
        });
    return () => controller.abort();
  }, [searchParams, pathTable?.tableDefinition.rootPath]);

  useEffect(() => {
    if (!searchParams.has('number') || !searchParams.has('size')) {
      pathTable &&
        setSearchParams(
          oldFilters => ({
            ...oldFilters,
            number: pathTable.tableDefinition.defaultPage,
            size: pathTable.tableDefinition.defaultSize,
          }),
          {
            replace: true,
          },
        );
    }
  }, [
    searchParams,
    setSearchParams,
    pathTable?.tableDefinition.defaultPage,
    pathTable?.tableDefinition.defaultSize,
    pathTable,
  ]);

  const PaginationNode = ({ active = false, className, children, onClick }: PaginationNodeProps) => {
    return (
      <li className="page-item">
        <div
          onClick={onClick}
          className={`${className} text-sm md:text-md page-link relative block py-1.5 px-3 border-0 ${
            active
              ? 'bg-slate-500 text-white hover:text-white hover:bg-slate-700'
              : 'bg-transparent text-gray-800 hover:text-gray-800 hover:bg-gray-200'
          } outline-none transition-all duration-300 rounded hover:cursor-pointer focus:shadow-none`}
        >
          {children}
        </div>
      </li>
    );
  };

  const Pagination = () => {
    const globalFilters = parseParams(searchParams);
    const currentPage = parseInt(globalFilters.number);
    const pageMax = Math.floor(((data?.total || 1) - 1) / (pathTable?.tableDefinition.defaultSize || 10));

    const gotoPage = (page: number) => {
      if (page < 0 || page > pageMax) return;
      setSearchParams(oldFilters => {
        oldFilters.set('number', page.toString());
        return oldFilters;
      });
    };

    const pages: { index: number; autoHide: boolean }[] = [{ index: currentPage, autoHide: false }];
    for (var i = 1; i <= 5; i++) {
      if (currentPage - i >= 0) {
        pages.unshift({ index: currentPage - i, autoHide: pages.length >= 3 });
      }
      if (currentPage + i <= pageMax) {
        pages.push({ index: currentPage + i, autoHide: pages.length >= 3 });
      }
      if (pages.length === 5) break;
    }

    return data?.contents && !Number.isNaN(currentPage) && !Number.isNaN(pageMax) ? (
      <>
        <nav aria-label="Page navigation example">
          <ul className="flex list-style-none space-x-1">
            <PaginationNode onClick={() => gotoPage(0)}>
              <FontAwesomeIcon icon="angles-left" />
            </PaginationNode>
            <PaginationNode onClick={() => gotoPage(currentPage - 1)}>
              <FontAwesomeIcon icon="angle-left" />
            </PaginationNode>
            {pages.map((page, k) => (
              <PaginationNode
                key={`pagination.${k}`}
                className={`${page.autoHide ? 'hidden sm:block' : ''}`}
                active={currentPage === page.index}
                onClick={() => gotoPage(page.index)}
              >
                <>{page.index + 1}</>
              </PaginationNode>
            ))}
            <PaginationNode onClick={() => gotoPage(currentPage + 1)}>
              <FontAwesomeIcon icon="angle-right" />
            </PaginationNode>
            <PaginationNode onClick={() => gotoPage(pageMax)}>
              <FontAwesomeIcon icon="angles-right" />
            </PaginationNode>
          </ul>
        </nav>
        <div className="flex justify-center items-center md:justify-end text-sm font-medium md:text-md text-gray-800 mt-4 mb-2 px-3">
          <span>{`Tổng cộng: ${data?.total || 0}`}</span>
          <span className="ml-6 mr-2">
            <input
              className="rounded w-12 h-8 p-1 border-slate-300 text-right"
              type="number"
              min={1}
              max={pageMax + 1}
              defaultValue={currentPage + 1}
              onKeyDown={(e: any) => {
                if (e.key === 'Enter') {
                  const selectedPage = parseInt(e.target.value);
                  gotoPage(Number.isNaN(selectedPage) ? currentPage : selectedPage - 1);
                }
              }}
            />
          </span>
          <span>{`/ ${pageMax + 1} trang`}</span>
        </div>
      </>
    ) : (
      <></>
    );
  };

  return (
    <div
      key={pathTable?.tableDefinition.tableCodeName}
      className="bg-white rounded-xl shadow-default overflow-hidden p-3"
    >
      <div className="flex flex-row justify-between items-center">
        <span className="text-lg md:text-2xl font-medium text-slate-700 py-1 md:py-4">
          {t(`${pathTable?.tableDefinition.tableCodeName}.tableName`)}
        </span>
      </div>
      {pathTable?.tableDefinition.filters && <FiltersSection table={pathTable.tableDefinition} />}
      <div className="mt-2 flex flex-col">
        <Table
          scroll={{ x: true }}
          columns={pathTable?.tableDefinition.fields.map(field => ({
            key: `${pathTable?.tableDefinition.tableCodeName}.${field.variable}`,
            dataIndex: `${pathTable?.tableDefinition.tableCodeName}.${field.variable}`,
            title: t(`${pathTable?.tableDefinition.tableCodeName}.${field.overrideTranslate || field.variable}`),
            align: 'left',
            fixed: field.sticky,
            className: 'px-6 py-2 whitespace-nowrap truncate max-w-xs text-sm font-normal text-gray-900',
            onHeaderCell: () => ({
              className: 'whitespace-nowrap text-sm !font-bold text-gray-900 px-6 py-2 text-left bg-slate-200',
            }),
            render: (_value, row) =>
              field.valueFormatter ? (
                <span>{field.valueFormatter(_.get(row, field.variable), row)}</span>
              ) : (
                <span>{_.get(row, field.variable)}</span>
              ),
          }))}
          rowClassName="bg-white border-b transition duration-300 ease-in-out hover:bg-gray-100"
          data={data?.contents || []}
          rowKey={(_value, index) => `row.${index}`}
          onHeaderRow={() => ({
            className: 'border-b',
          })}
          emptyText={
            data?.contents === undefined ? (
              <div className="w-full flex justify-center items-center py-24">
                <Spinner />
              </div>
            ) : (
              <div className="w-full flex justify-center items-center py-24">
                <EmptyData />
              </div>
            )
          }
          footer={() => (
            <div className="flex justify-center md:justify-end mt-3">
              <div className="flex flex-col items-center md:items-end">
                <Pagination />
              </div>
            </div>
          )}
        />
      </div>
    </div>
  );
}
