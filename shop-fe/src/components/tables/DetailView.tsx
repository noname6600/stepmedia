import React, { HTMLProps } from 'react';
import { useTranslation } from 'react-i18next';
import { useParams } from 'react-router-dom';
import Spinner from '@components/common/Spinner';

export interface DetailViewProps extends HTMLProps<HTMLDivElement> {
  title: string;
  loading?: boolean;
}

export default function DetailView({ title, loading, children }: DetailViewProps) {
  const { t } = useTranslation();
  const { id } = useParams();

  return (
    <div key={id} className="bg-white rounded-xl shadow-default p-3">
      <div className="text-lg md:text-2xl font-mediumr text-slate-700 py-1 md:py-4">
        {id === 'new' ? t(`labels.new.${title}`) : t(`labels.detail.${title}`)}
      </div>
      <div className="mt-2 flex flex-col items-center">
        {loading ? (
          <div className="flex justify-center items-center py-8">
            <Spinner />
          </div>
        ) : (
          children
        )}
      </div>
    </div>
  );
}
