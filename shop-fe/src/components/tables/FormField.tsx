import React from 'react';
import { useTranslation } from 'react-i18next';
import { Field } from 'rc-field-form';
import { FieldProps } from 'rc-field-form/es/Field';
import { NamePath } from 'rc-field-form/es/interface';

import { useTableFormViewContext } from './TableFormView';

export interface FormFieldProps extends FieldProps {
  label?: string;
  colSpan?: 'half' | 'full';
  hideField?: boolean;
  hideLabel?: boolean;
  fieldDisabled?: boolean;
  disableFieldWrapper?: boolean;
  disableRequiredLabel?: boolean;
}

const FormFieldContext = React.createContext<{
  fieldName?: NamePath;
  fieldLabel?: string;
  fieldDisabled?: boolean;
  required?: boolean;
} | null>(null);

export default function FormField({
  label,
  colSpan = 'full',
  hideField = false,
  hideLabel = false,
  fieldDisabled = false,
  disableFieldWrapper = false,
  disableRequiredLabel = false,
  children,
  name,
  rules,
  ...props
}: FormFieldProps) {
  const { t } = useTranslation();
  const { tableDefinition } = useTableFormViewContext();
  return hideField === true ? (
    <></>
  ) : (
    <FormFieldContext.Provider
      value={{
        fieldName: name,
        fieldLabel:
          hideLabel === true
            ? undefined
            : label ||
              (tableDefinition &&
                `${t(`${tableDefinition.tableCodeName}.${Array.isArray(name) ? name.join('.') : name}`)}`),
        required: !disableRequiredLabel && rules && rules.find((rule: any) => rule.required) !== undefined,
        fieldDisabled,
      }}
    >
      <div className={`col-span-full ${colSpan === 'half' ? 'sm:col-span-6' : ''}`}>
        <>
          {disableFieldWrapper ? (
            children
          ) : (
            <Field {...props} name={name} rules={!fieldDisabled ? rules : undefined}>
              {children}
            </Field>
          )}
        </>
      </div>
    </FormFieldContext.Provider>
  );
}

export const useFormFieldContext = () => {
  const context = React.useContext(FormFieldContext);
  if (!context) {
    return {};
  }
  return context;
};
