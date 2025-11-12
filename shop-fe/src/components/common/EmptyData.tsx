import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { t } from 'i18next';

export default function EmptyData() {
  return (
    <div className="text-slate-400 text-xl font-medium flex flex-col justify-center items-center">
      <FontAwesomeIcon icon="folder-open" className="w-12 h-12" />
      <span className="mt-4">{t('common.notFoundData')}</span>
    </div>
  );
}
