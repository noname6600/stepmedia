import React, { forwardRef, InputHTMLAttributes, useEffect, useMemo, useRef } from 'react';
import Flatpickr from 'react-flatpickr';
import { useFormFieldContext } from '@components/tables/FormField';
import { useFormViewContext } from '@components/tables/FormView';
import { setNativeValue } from '@utils/Parsers';
import { useForwardRef } from '@utils/useForwardRef';

interface Formatters {
  format: (value: string) => string | undefined;
  deFormat: (text: string) => string | undefined;
}

export interface DateTimePickerProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  formatters?: Formatters;
}

const DateTimePicker = forwardRef<HTMLInputElement, DateTimePickerProps>(
  ({ label, formatters, disabled, value, onChange, ...props }, ref) => {
    const inputRef = useForwardRef<HTMLInputElement>(ref);
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
      <div ref={containerRef} className="relative w-full">
        {(label || fieldLabel) && (
          <>
            <label className="form-label inline-block text-gray-700">{label || fieldLabel}</label>
            {required && <label className="form-label inline-block text-red-500 pl-1">(*)</label>}
          </>
        )}
        <div className="relative w-full">
          <Flatpickr
            className={`w-full form-input pl-9 text-gray-700 disabled:text-gray-700 hover:text-gray-700 focus:outline-none hover:outline-none ${
              !(disabled || fieldDisabled) && formData.editMode
                ? 'bg-white border-gray-300 focus:border-slate-300 hover:border-slate-300'
                : 'bg-slate-100 border-slate-100 focus:border-slate-100 hover:border-slate-100'
            } ${error && 'border-red-500'}`}
            value={value && formatters ? formatters.format(`${value}`) : value ? `${value}` : undefined}
            disabled={fieldDisabled || disabled || !formData.editMode}
            options={{
              mode: 'single',
              static: true,
              allowInput: true,
              enableTime: true,
              monthSelectorType: 'dropdown',
              dateFormat: 'd/m/Y H:i:S',
              clickOpens: formData.editMode,
              time_24hr: true,
              prevArrow:
                '<svg class="fill-current" width="7" height="11" viewBox="0 0 7 11"><path d="M5.4 10.8l1.4-1.4-4-4 4-4L5.4 0 0 5.4z" /></svg>',
              nextArrow:
                '<svg class="fill-current" width="7" height="11" viewBox="0 0 7 11"><path d="M1.4 10.8L0 9.4l4-4-4-4L1.4 0l5.4 5.4z" /></svg>',
              onChange: (_selectedDates: Date[], dateStr: string) => {
                setNativeValue(inputRef.current, formatters ? formatters.deFormat(dateStr) : dateStr);
                inputRef.current.dispatchEvent(new Event('input', { bubbles: true }));
              },
            }}
          />
          <div className="absolute w-full inset-0 right-auto flex items-center pointer-events-none">
            <svg className="w-4 h-4 fill-current text-slate-500 ml-3" viewBox="0 0 16 16">
              <path d="M15 2h-2V0h-2v2H9V0H7v2H5V0H3v2H1a1 1 0 00-1 1v12a1 1 0 001 1h14a1 1 0 001-1V3a1 1 0 00-1-1zm-1 12H2V6h12v8z" />
            </svg>
          </div>
          <input
            type="text"
            className="sr-only"
            ref={inputRef}
            value={value ? value : ''}
            onChange={onChange}
            {...props}
          />
        </div>
        {error && <label className="form-label inline-block text-red-500">{error}</label>}
      </div>
    );
  },
);

export default DateTimePicker;
