import React, { useEffect, useState } from 'react';
import Spinner from '@components/common/Spinner';
import { defaultValidateMessages } from '@utils/defaultValidateMessages';
import type { FormInstance, FormProps } from 'rc-field-form';
import Form from 'rc-field-form';
import { NamePath } from 'rc-field-form/es/interface';

export interface FormViewProps extends FormProps {
  formData?: FormViewData;
}

export interface FieldErrors {
  [key: string]: string | undefined;
}

export interface FormViewData {
  loading: boolean;
  isNew: boolean;
  editMode: boolean;
  detail?: any;
  extraData?: any;
  fieldErrors?: FieldErrors;
}

const FormViewContext = React.createContext<{
  formData: FormViewData;
  form?: FormInstance<any>;
  setExtraData: (key: NamePath, data: any) => void;
} | null>(null);

export default function FormView({ formData, form, onFinish, children, ...props }: FormViewProps) {
  const [internalFormData, setInternalFormData] = useState<FormViewData>(
    formData || { loading: false, isNew: false, editMode: true },
  );

  const setExtraData = (key: NamePath, data: any) => {
    setInternalFormData(oldData => {
      const copy = { ...oldData };
      if (!copy.extraData) copy.extraData = {};
      copy.extraData[key.toString()] = data;
      return copy;
    });
  };

  useEffect(() => {
    setInternalFormData(oldData =>
      formData ? { ...formData, extraData: oldData?.extraData } : { loading: false, isNew: false, editMode: true },
    );
  }, [formData]);

  useEffect(() => {
    internalFormData.detail && form?.setFieldsValue(internalFormData.detail);
  }, [internalFormData.detail, internalFormData.editMode, form]);

  return (
    <FormViewContext.Provider value={{ formData: internalFormData, form, setExtraData }}>
      {internalFormData.loading ? (
        <div className="flex justify-center items-center py-8 mx-auto my-8">
          <Spinner />
        </div>
      ) : (
        <Form
          {...props}
          form={form}
          autoComplete="off"
          validateMessages={defaultValidateMessages}
          className="w-full max-w-4xl px-4 grid grid-cols-12 gap-4"
          onFinish={values => {
            onFinish && onFinish(values);
          }}
          onFinishFailed={errors => {
            setInternalFormData(oldData => {
              const fieldErrors: FieldErrors = {};
              errors.errorFields.forEach(fieldError => {
                fieldErrors[fieldError.name.toString()] = fieldError.errors[0];
              });
              return { ...oldData, fieldErrors };
            });
          }}
          onFieldsChange={changedFields => {
            setInternalFormData(oldData => {
              const fieldErrors = { ...oldData.fieldErrors };
              for (const fieldError of changedFields) {
                fieldErrors[fieldError.name.toString()] = undefined;
              }
              return { ...oldData, fieldErrors };
            });
          }}
        >
          <>{children}</>
        </Form>
      )}
    </FormViewContext.Provider>
  );
}

export const useFormViewContext = () => {
  const context = React.useContext(FormViewContext);
  if (!context) {
    return { formData: { loading: false, isNew: false, editMode: true } } as {
      formData: FormViewData;
      form?: FormInstance<any>;
      setExtraData: (key: NamePath, data: any) => void;
    };
  }
  return context;
};
