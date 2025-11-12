import React from 'react';
import { useParams } from 'react-router-dom';
import { AuditFormField } from '@components/common/AuditFormField';
import DatePicker from '@components/common/inputs/DatePicker';
import InputField from '@components/common/inputs/InputField';
import DetailView, { DetailViewProps } from '@components/tables/DetailView';
import FormField from '@components/tables/FormField';
import TableFormView from '@components/tables/TableFormView';

export default function CustomerDetail({ title }: DetailViewProps) {
  const { id } = useParams();

  return (
    <DetailView title={title}>
      <TableFormView editMode={false}>
        <FormField name="id" fieldDisabled={true} hideField={id === 'new'}>
          <InputField />
        </FormField>
        <FormField
          name="fullName"
          colSpan="half"
          rules={[{ required: true, type: 'string', min: 5, max: 100, whitespace: true }]}
        >
          <InputField />
        </FormField>
        <FormField name="dob" colSpan="half" rules={[{ required: true }]}>
          <DatePicker />
        </FormField>
        <FormField name="email" colSpan="half" rules={[{ required: true, type: 'email', max: 100 }]}>
          <InputField />
        </FormField>
        <AuditFormField />
      </TableFormView>
    </DetailView>
  );
}
