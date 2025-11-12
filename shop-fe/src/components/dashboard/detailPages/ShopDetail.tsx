import React from 'react';
import { useParams } from 'react-router-dom';
import { AuditFormField } from '@components/common/AuditFormField';
import InputField from '@components/common/inputs/InputField';
import TextArea from '@components/common/inputs/TextArea';
import DetailView, { DetailViewProps } from '@components/tables/DetailView';
import FormField from '@components/tables/FormField';
import TableFormView from '@components/tables/TableFormView';

export default function ShopDetail({ title }: DetailViewProps) {
  const { id } = useParams();

  return (
    <DetailView title={title}>
      <TableFormView editMode={false}>
        <FormField name="id" fieldDisabled={true} hideField={id === 'new'}>
          <InputField />
        </FormField>
        <FormField name="name" rules={[{ required: true, type: 'string', min: 5, max: 100, whitespace: true }]}>
          <InputField />
        </FormField>
        <FormField name="location" rules={[{ required: true, type: 'string', min: 5, max: 200, whitespace: true }]}>
          <TextArea rows={3} />
        </FormField>
        <AuditFormField />
      </TableFormView>
    </DetailView>
  );
}
