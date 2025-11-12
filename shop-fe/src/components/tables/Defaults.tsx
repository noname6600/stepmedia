import React from 'react';
import DatePicker from '@components/common/inputs/DatePicker';
import moment from 'moment';

import { TableField, TableFilter } from './TableView';

export const defaultDateField: Readonly<TableField> = {
  variable: 'defaultDateField',
  valueFormatter: value => {
    return <>{moment(parseInt(value)).format('DD/MM/YYYY')}</>;
  },
};

export const defaultDateTimeField: Readonly<TableField> = {
  variable: 'defaultDateTimeField',
  valueFormatter: value => {
    return <>{moment(parseInt(value)).format('DD/MM/YYYY HH:mm:ss')}</>;
  },
};

export const defaultTimeField: Readonly<TableField> = {
  variable: 'defaultTimeField',
  valueFormatter: value => {
    return <>{moment(parseInt(value)).format('HH:mm:ss')}</>;
  },
};

export const defaultIntFilter: Readonly<TableFilter> = {
  variable: 'defaultIntFilter',
  inputProps: {
    type: 'number',
    inputMode: 'numeric',
  },
};

export const defaultFloatFilter: Readonly<TableFilter> = {
  variable: 'defaultFloatFilter',
  inputProps: {
    type: 'number',
    inputMode: 'decimal',
  },
};

export const defaultStartDateFilter: Readonly<TableFilter> = {
  variable: 'defaultStartDateFilter',
  format: value => (value ? moment(parseInt(value)).format('DD/MM/YYYY') : ''),
  toValue: value => moment(value, 'DD/MM/YYYY').hours(0).minutes(0).seconds(0).milliseconds(0).valueOf().toString(),
  customInput: (value, onChange) => <DatePicker value={value} onChange={onChange} />,
};

export const defaultEndDateFilter: Readonly<TableFilter> = {
  variable: 'defaultEndDateFilter',
  format: value => (value ? moment(parseInt(value)).format('DD/MM/YYYY') : ''),
  toValue: value =>
    moment(value, 'DD/MM/YYYY').hours(23).minutes(59).seconds(59).milliseconds(999).valueOf().toString(),
  customInput: (value, onChange) => <DatePicker value={value} onChange={onChange} />,
};

export const defaultPagingFilters: readonly TableFilter[] = [
  {
    ...defaultStartDateFilter,
    variable: 'from',
  },
  {
    ...defaultEndDateFilter,
    variable: 'to',
  },
];
