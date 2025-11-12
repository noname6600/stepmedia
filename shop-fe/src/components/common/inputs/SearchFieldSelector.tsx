import React, { forwardRef, InputHTMLAttributes, useCallback, useEffect, useMemo, useRef, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import { useFormFieldContext } from '@components/tables/FormField';
import { useFormViewContext } from '@components/tables/FormView';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { setNativeValue } from '@utils/Parsers';
import { useForwardRef } from '@utils/useForwardRef';
import globalAxios from 'axios';
import { debounce } from 'lodash';

export interface SearchFieldSelectorProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  rootPath: string;
  rootPage?: string;
  variable?: string;
  size?: number;
  format?: (data: any) => string;
  onSelectOption?: (option: any) => void;
}

const SearchFieldSelector = forwardRef<HTMLInputElement, SearchFieldSelectorProps>(
  (
    {
      label,
      rootPath,
      rootPage,
      variable = 'id',
      size = 10,
      format = data => data.name,
      onSelectOption,
      placeholder = 'search',
      value,
      disabled,
      onChange,
      ...props
    }: SearchFieldSelectorProps,
    ref,
  ) => {
    const { t } = useTranslation();
    const { formData, form, setExtraData } = useFormViewContext();
    const { fieldName, fieldLabel, fieldDisabled, required } = useFormFieldContext();
    const [options, setOptions] = useState<any[]>([]);
    const [search, setSearch] = useState('');
    const [selected, setSelected] = useState('');
    const [open, setOpen] = useState(false);
    const containerRef = useRef<HTMLDivElement>(null);
    const inputRef = useForwardRef(ref);
    const error = useMemo(() => {
      if (!fieldName) return undefined;
      const errorMsg = formData.fieldErrors?.[fieldName.toString()];
      return fieldLabel && errorMsg ? errorMsg.replaceAll(`'${fieldName}'`, fieldLabel) : errorMsg;
    }, [fieldLabel, fieldName, formData.fieldErrors]);

    useEffect(() => {
      error && containerRef.current?.scrollIntoView({ block: 'center', inline: 'center' });
    }, [error]);

    // eslint-disable-next-line
    const onSearchChange = useCallback(
      debounce(debounceValue => {
        const controller = new AbortController();
        debounceValue &&
          debounceValue.length >= 2 &&
          formData.editMode &&
          globalAxios
            .get(`${rootPath}/search`, { params: { searchText: debounceValue, size: size }, signal: controller.signal })
            .then(response => {
              if (response) {
                setOpen(true);
                setOptions(response.data?.data?.content || []);
              }
            })
            .catch(() => {
              setOptions([]);
            });
        return () => controller.abort();
      }, 500),
      [],
    );

    useEffect(() => {
      if (value && !selected) {
        const controller = new AbortController();
        globalAxios
          .get(`${rootPath}/${value}`, { signal: controller.signal })
          .then(response => {
            if (response) {
              setSelected(format(response.data?.data));
              fieldName && setExtraData(fieldName.toString(), response.data?.data);
            }
          })
          .catch(() => {
            setSelected(value.toString());
          });
      }
    }, [format, rootPath, value, formData.editMode, selected, fieldName, form, setExtraData]);

    useEffect(() => {
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
    }, []);

    useEffect(() => {
      if (search.length < 2 && options.length === 0) {
        const controller = new AbortController();
        formData.editMode &&
          globalAxios
            .get(`${rootPath}/search`, { params: { searchText: '', size: size }, signal: controller.signal })
            .then(response => {
              if (response) {
                setOptions(response.data?.data?.content || []);
              }
            })
            .catch(() => {
              setOptions([]);
            });

        return () => controller.abort();
      }
    }, [formData.editMode, options.length, rootPath, search, size]);

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
            className="w-full flex flex-row"
            onClick={() => !(fieldDisabled || disabled) && formData.editMode && setOpen(oldOpen => !oldOpen)}
          >
            <input
              type="text"
              className={`block w-full px-3 py-1.5 text-base font-normal text-gray-700 disabled:text-gray-700 bg-white border border-r-0 border-solid border-gray-300 rounded-l transition ease-in-out m-0 focus:text-gray-700 focus:bg-white focus:border-gray-300 focus:ring-0 ${
                (fieldDisabled || disabled || !formData.editMode) && 'sr-only'
              } ${error && 'border-red-500'}`}
              placeholder={selected && selected.length > 0 ? selected : `${t(`labels.${placeholder}`)}`}
              value={search}
              disabled={fieldDisabled || disabled || !formData.editMode}
              onChange={e => {
                onSearchChange(e.target.value);
                setSearch(e.target.value);
                if (inputRef.current) {
                  setNativeValue(inputRef.current, '');
                  inputRef.current.dispatchEvent(new Event('input', { bubbles: true }));
                  setSelected('');
                }
              }}
              {...props}
            />
            {(fieldDisabled || disabled || !formData.editMode) && (
              <span className="block w-full px-3 py-1.5 text-base font-normal text-gray-700 bg-slate-100 border border-solid border-slate-100 rounded transition ease-in-out m-0 focus:text-gray-700 focus:bg-white focus:border-gray-300 focus:ring-0">
                {selected && rootPage ? (
                  <Link to={`${rootPage}/detail/${value}`}>{selected}</Link>
                ) : (
                  <>{selected || ''}</>
                )}
              </span>
            )}
            <input
              type="text"
              className="sr-only"
              ref={inputRef}
              value={value !== undefined && value !== null ? value : ''}
              onChange={onChange}
            />
            {!(fieldDisabled || disabled) && formData.editMode && (
              <div className="inline-block">
                <div
                  className={`flex h-full items-center justify-center pr-2.5 border border-solid border-gray-300 border-l-0 rounded-r ${
                    error && 'border-red-500'
                  }`}
                >
                  <FontAwesomeIcon icon="magnifying-glass" className="text-gray-500" />
                </div>
              </div>
            )}
          </div>
          {error && <label className="form-label inline-block text-red-500">{error}</label>}
          <div className="relative z-60">
            <div className="absolute w-full">
              <div className="relative">
                {open && options.length > 0 && (
                  <ul className="p-1 bg-white rounded shadow-default border">
                    {options.map((option: any) => (
                      <li
                        key={option.id}
                        className="hover:bg-slate-100 hover:cursor-pointer rounded active:bg-slate-200 relative text-gray-700"
                        onClick={() => {
                          setSelected(format(option));
                          onSelectOption && onSelectOption(option);
                          if (inputRef.current) {
                            setNativeValue(inputRef.current, option[variable]);
                            inputRef.current.dispatchEvent(new Event('input', { bubbles: true }));
                          }
                          fieldName && setExtraData(fieldName.toString(), option);
                          setSearch('');
                          setOptions([]);
                          setOpen(false);
                        }}
                      >
                        <div className="px-3 py-1 truncate">{format(option)}</div>
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
  },
);

export default SearchFieldSelector;
