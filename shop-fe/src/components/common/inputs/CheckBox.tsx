import React, { forwardRef, InputHTMLAttributes } from 'react';
import { useFormFieldContext } from '@components/tables/FormField';

export interface CheckBoxProps extends InputHTMLAttributes<HTMLInputElement> {
  label: string;
}

const CheckBox = forwardRef<HTMLInputElement, CheckBoxProps>(
  ({ label, value, checked, disabled, onChange, ...props }: CheckBoxProps, ref) => {
    const { fieldDisabled } = useFormFieldContext();
    return (
      <div className="flex flex-row p-1 items-center">
        <input
          ref={ref}
          className="mx-2 rounded border-solid border-gray-400 border-2 text-primary disabled:opacity-60"
          type="checkbox"
          value={value !== undefined && value !== null ? value : ''}
          checked={checked ? checked : false}
          onChange={onChange}
          disabled={fieldDisabled || disabled}
          {...props}
        />
        <label className="inline-block hover:cursor-pointer truncate">{label}</label>
      </div>
    );
  },
);

export default CheckBox;
