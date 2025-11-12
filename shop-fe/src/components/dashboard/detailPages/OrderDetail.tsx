import React from 'react';
import DatePicker from 'react-flatpickr';
import { useParams } from 'react-router-dom';
import { AuditFormField } from '@components/common/AuditFormField';
import EnumSelect from '@components/common/inputs/EnumSelect';
import InputField from '@components/common/inputs/InputField';
import { CustomerSearchField, ProductSearchField, ShopSearchField } from '@components/common/ObjectSearchField';
import DetailView, { DetailViewProps } from '@components/tables/DetailView';
import FormField from '@components/tables/FormField';
import TableFormView from '@components/tables/TableFormView';

export default function OrderDetail({ title }: DetailViewProps) {
  const { id } = useParams();

  const onPreSubmit = (values: any) => {
    values.status = values.status || 'N';
    return Promise.resolve(values);
  };

  return (
    <DetailView title={title}>
      <TableFormView editMode={false} onPreSubmit={onPreSubmit}>
        <FormField name="id" fieldDisabled={true} hideField={id === 'new'}>
          <InputField />
        </FormField>
        <FormField name="status" colSpan="half" rules={[{ required: true, whitespace: true }]} hideField={id === 'new'}>
          <EnumSelect enumType="orderStatus" />
        </FormField>
        <FormField name="customerId" colSpan="half" rules={[{ required: true, whitespace: true }]}>
          <CustomerSearchField />
        </FormField>
        <FormField name="productId" colSpan="half" rules={[{ required: true, whitespace: true }]}>
          <ProductSearchField />
        </FormField>
        <FormField name="shopId" colSpan="half" rules={[{ required: true, whitespace: true }]} hideField={id === 'new'}>
          <ShopSearchField />
        </FormField>
        <FormField name="deliveryDate" colSpan="half" hideField={id === 'new'}>
          <DatePicker />
        </FormField>
        <AuditFormField />
      </TableFormView>
    </DetailView>
  );
}
