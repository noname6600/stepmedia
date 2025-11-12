import React, { forwardRef, InputHTMLAttributes, useEffect, useMemo, useRef } from 'react';
import { useTranslation } from 'react-i18next';
import { useFormViewContext } from '@components/tables/FormView';
import { IconName } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useFormFieldContext } from '../../tables/FormField';

export interface InputFieldProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  icon?: IconName;
  onIconClick?: () => void;
}

const InputField = forwardRef<HTMLInputElement, InputFieldProps>(
  ({ label, placeholder, icon, onIconClick, value, type, onChange, disabled, ...props }: InputFieldProps, ref) => {
    const { t } = useTranslation();
    const { formData } = useFormViewContext();
    const { fieldName, fieldLabel, fieldDisabled, required } = useFormFieldContext();
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
          <div className="relative flex items-center justify-end">
            {icon && (
              <div
                className="absolute mr-2 flex items-center justify-center hover:cursor-pointer"
                onClick={() => onIconClick && onIconClick()}
              >
                <FontAwesomeIcon className="w-4 h-4 p-1 text-gray-500" icon={icon} />
              </div>
            )}
            <input
              ref={ref}
              className={`block w-full px-3 py-1.5 text-base font-normal text-gray-700 disabled:text-gray-700 bg-clip-padding border border-solid rounded transition ease-in-out m-0 focus:text-gray-700 focus:bg-white focus:border-blue-600 focus:outline-none ${
                !(fieldDisabled || disabled) && formData.editMode
                  ? 'bg-white border-gray-300'
                  : 'bg-slate-100 border-slate-100'
              } ${error && 'border-red-500'} ${icon && 'pr-10'}`}
              type={formData.editMode ? type : 'text'}
              value={value !== undefined && value !== null ? value : ''}
              onChange={onChange}
              placeholder={placeholder && `${t(`labels.${placeholder}`)}`}
              disabled={fieldDisabled || disabled || !formData.editMode}
              {...props}
            />
          </div>

          {error && <label className="form-label inline-block text-red-500">{error}</label>}
        </div>
      </div>
    );
  },
);

export default InputField;
