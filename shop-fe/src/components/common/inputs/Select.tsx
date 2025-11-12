import React, { forwardRef, InputHTMLAttributes, useEffect, useMemo, useRef, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useFormFieldContext } from '@components/tables/FormField';
import { useFormViewContext } from '@components/tables/FormView';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { setNativeValue } from '@utils/Parsers';
import { useForwardRef } from '@utils/useForwardRef';

export interface SelectItem {
  label: string;
  value: string;
}

export interface SelectProps extends InputHTMLAttributes<HTMLInputElement> {
  id: string;
  label?: string;
  options: SelectItem[];
}

const Select = forwardRef<HTMLInputElement, SelectProps>(({ id, label, options, disabled, value, onChange }, ref) => {
  const { t } = useTranslation();
  const { formData } = useFormViewContext();
  const { fieldName, fieldLabel, fieldDisabled, required } = useFormFieldContext();
  const containerRef = useRef<HTMLDivElement>(null);
  const inputRef = useForwardRef<HTMLInputElement>(ref);
  const [open, setOpen] = useState(false);
  const error = useMemo(() => {
    if (!fieldName) return undefined;
    const errorMsg = formData.fieldErrors?.[fieldName.toString()];
    return fieldLabel && errorMsg ? errorMsg.replaceAll(`'${fieldName}'`, fieldLabel) : errorMsg;
  }, [fieldLabel, fieldName, formData.fieldErrors]);

  useEffect(() => {
    error && containerRef.current?.scrollIntoView({ block: 'center', inline: 'center' });
  }, [error]);

  useEffect(() => {
    if (options[0]?.value) {
      if (value === undefined || value === '') {
        setNativeValue(inputRef.current, options[0].value);
        inputRef.current.dispatchEvent(new Event('input', { bubbles: true }));
      } else {
        setNativeValue(inputRef.current, value as string);
        inputRef.current.dispatchEvent(new Event('input', { bubbles: true }));
      }
    }

    const onClick = (event: MouseEvent) => {
      if (
        containerRef &&
        !containerRef.current?.contains(event.target as Node) &&
        !containerRef.current?.contains(event.target as Node)
      ) {
        setOpen(false);
      }
    };
    document.addEventListener('click', onClick);
    return () => document.removeEventListener('click', onClick);
  }, [inputRef, options, value]);

  return (
    <div ref={containerRef} className="flex justify-center relative">
      <div className="w-full">
        {(label || fieldLabel) && (
          <>
            <label className="form-label inline-block text-gray-700">{label || fieldLabel}</label>
            {required && <label className="form-label inline-block text-red-500 pl-1">(*)</label>}
          </>
        )}
        <div
          className={`w-full px-3 py-1.5 flex flex-grow text-base font-normal text-gray-700 bg-clip-padding border border-solid rounded transition ease-in-out m-0 focus:text-gray-700 focus:bg-white focus:border-blue-600 focus:outline-none ${
            !(fieldDisabled || disabled) && formData.editMode
              ? 'bg-white border-gray-300'
              : 'bg-slate-100 border-slate-100'
          } ${error && 'border-red-500'}`}
          onClick={() => !(fieldDisabled || disabled) && formData.editMode && setOpen(oldOpen => !oldOpen)}
        >
          <span className="grow truncate">
            {options.find(option => option.value === value?.toString())?.label || `${t('labels.select')}`}
          </span>
          {!(fieldDisabled || disabled) && formData.editMode && (
            <FontAwesomeIcon icon="chevron-down" className="ml-2 my-auto" />
          )}
        </div>
        {error && <label className="form-label inline-block text-red-500">{error}</label>}
        <input
          type="text"
          className="sr-only"
          ref={inputRef}
          value={value !== undefined && value !== null ? value : ''}
          onChange={onChange}
        />
        <div className="relative z-60">
          <div className="absolute w-full">
            <div className="relative">
              {open && options.length > 0 && (
                <ul className="p-1 bg-white rounded shadow">
                  {options
                    .filter(option => option.value !== value)
                    .map(option => (
                      <li
                        key={`filters.dropdown.${id}.${option.value}`}
                        className="hover:bg-slate-100 hover:cursor-pointer rounded active:bg-slate-200 relative text-gray-700"
                        onClick={() => {
                          setOpen(false);
                          if (inputRef.current) {
                            setNativeValue(inputRef.current, option.value);
                            inputRef.current.dispatchEvent(new Event('input', { bubbles: true }));
                          }
                        }}
                      >
                        <div className="px-3 py-1">{option.label}</div>
                      </li>
                    ))}
                </ul>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
});

export default Select;
