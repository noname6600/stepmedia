import React from 'react';
import { useTranslation } from 'react-i18next';

import Badge from './Badge';
import { BadgeVariant } from './Badge';

export interface EnumBadgeProps {
  enumType: string;
  enumValue: string;
}

export const enumsVariants: {
  [key: string]: {
    [key: string]: BadgeVariant;
  };
} = {
  language: {
    VI: 'lightRed',
    EN: 'sky',
  },
  boolean: {
    true: 'green',
    false: 'lightRed',
  },
  orderStatus: {
    N: 'green',
    A: 'purple',
    C: 'lightRed',
  },
};

export default function EnumBadge({ enumType, enumValue }: EnumBadgeProps) {
  const { t } = useTranslation();
  return enumValue !== undefined ? (
    <Badge text={t(`${enumType}.${enumValue}`)} variant={enumsVariants[enumType]?.[enumValue]} />
  ) : (
    <></>
  );
}
