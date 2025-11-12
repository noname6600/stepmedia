import React from 'react';
import { useParams } from 'react-router-dom';
import { AuditFormField } from '@components/common/AuditFormField';
import InputField from '@components/common/inputs/InputField';
import TextArea from '@components/common/inputs/TextArea';
import { ShopSearchField } from '@components/common/ObjectSearchField';
import DetailView, { DetailViewProps } from '@components/tables/DetailView';
import FormField from '@components/tables/FormField';
import TableFormView from '@components/tables/TableFormView';

export default function ProductDetail({ title }: DetailViewProps) {
  const { id } = useParams();

  return (
    <DetailView title={title}>
      <TableFormView editMode={false}>
        <FormField name="id" fieldDisabled={true} hideField={id === 'new'}>
          <InputField />
        </FormField>
        <FormField
          name="name"
          colSpan="half"
          rules={[{ required: true, type: 'string', min: 5, max: 100, whitespace: true }]}
        >
          <InputField />
        </FormField>
        <FormField
          name="price"
          colSpan="half"
          normalize={value => parseInt(value)}
          rules={[{ required: true, type: 'integer', min: 0 }]}
        >
          <InputField type="number" />
        </FormField>
        <FormField name="shopId" rules={[{ required: true, whitespace: true }]}>
          <ShopSearchField />
        </FormField>
        <FormField name="description">
          <TextArea rows={3} />
        </FormField>
        <AuditFormField />
      </TableFormView>
    </DetailView>
  );
}
