import * as React from 'react';
import FormField from '@components/tables/FormField';
import { useFormViewContext } from '@components/tables/FormView';
import { t } from 'i18next';
import moment from 'moment';

import DateTimePicker from './inputs/DateTimePicker';

export function AuditFormField() {
  const { formData } = useFormViewContext();

  return (
    <>
      {!formData.editMode && (
        <div className="col-span-full grid grid-cols-12 gap-4">
          <FormField colSpan="half" name="createdDate" label={t('audit.createdDate') || ''}>
            <DateTimePicker
              formatters={{
                format: value => (value ? moment(parseInt(value)).format('DD/MM/YYYY HH:mm:ss') : undefined),
                deFormat: value => (value ? moment(value, 'DD/MM/YYYY HH:mm:ss').valueOf().toString() : undefined),
              }}
            />
          </FormField>
          <FormField colSpan="half" name="lastModifiedDate" label={t('audit.lastModifiedDate') || ''}>
            <DateTimePicker
              formatters={{
                format: value => (value ? moment(parseInt(value)).format('DD/MM/YYYY HH:mm:ss') : undefined),
                deFormat: value => (value ? moment(value, 'DD/MM/YYYY HH:mm:ss').valueOf().toString() : undefined),
              }}
            />
          </FormField>
        </div>
      )}
    </>
  );
}
