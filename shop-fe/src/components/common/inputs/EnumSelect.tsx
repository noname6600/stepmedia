import React, { forwardRef, InputHTMLAttributes } from 'react';
import { useTranslation } from 'react-i18next';

import { enumsVariants } from '../EnumBadge';

import Select from './Select';

export interface EnumSelectProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  enumType: string;
}

const EnumSelect = forwardRef<HTMLInputElement, EnumSelectProps>(({ label, enumType, ...props }, ref) => {
  const { t } = useTranslation();
  return (
    <Select
      ref={ref}
      label={label}
      id={enumType}
      options={Object.keys(enumsVariants[enumType]).map(value => ({
        label: t(`${enumType}.${value}`),
        value: value,
      }))}
      {...props}
    />
  );
});

export default EnumSelect;
