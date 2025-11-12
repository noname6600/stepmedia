import React, { forwardRef, HTMLProps, useEffect, useMemo, useRef } from 'react';
import { useFormFieldContext } from '@components/tables/FormField';
import { useFormViewContext } from '@components/tables/FormView';

export interface TextAreaProps extends HTMLProps<HTMLTextAreaElement> {
  label?: string;
}

const TextArea = forwardRef<HTMLTextAreaElement, TextAreaProps>(
  ({ label, value, disabled, ...props }: TextAreaProps, ref) => {
    const { formData } = useFormViewContext();
    const { fieldName, fieldLabel, required } = useFormFieldContext();
    const error = useMemo(() => {
      if (!fieldName) return undefined;
      const errorMsg = formData.fieldErrors?.[fieldName.toString()];
      return fieldLabel && errorMsg ? errorMsg.replaceAll(`'${fieldName}'`, fieldLabel) : errorMsg;
    }, [fieldLabel, fieldName, formData.fieldErrors]);
    const containerRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
      error && containerRef.current?.scrollIntoView({ block: 'center', inline: 'center' });
    }, [error]);

    return (
      <div ref={containerRef} className="flex justify-center relative">
        <div className="w-full">
          {(label || fieldLabel) && (
            <>
              <label className="form-label inline-block text-gray-700">{label || fieldLabel}</label>
              {required && <label className="form-label inline-block text-red-500 pl-1">(*)</label>}
            </>
          )}
          <textarea
            ref={ref}
            type="text"
            className={`block w-full px-3 py-1.5 text-base font-normal text-gray-700 disabled:text-gray-700 bg-clip-padding border border-solid rounded transition ease-in-out m-0 focus:text-gray-700 focus:bg-white focus:border-blue-600 focus:outline-none ${
              !disabled && formData.editMode ? 'bg-white border-gray-300' : 'bg-slate-100 border-slate-100'
            } ${error && 'border-red-500'}`}
            value={value !== undefined && value !== null ? value : ''}
            disabled={disabled || !formData.editMode}
            {...props}
          />
          {error && <label className="form-label inline-block text-red-500">{error}</label>}
        </div>
      </div>
    );
  },
);

export default TextArea;
