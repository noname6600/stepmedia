import React, { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useSearchParams } from 'react-router-dom';
import IconButton from '@components/common/IconButton';
import InputField from '@components/common/inputs/InputField';
import Select, { SelectItem } from '@components/common/inputs/Select';
import Transition from '@components/common/Transition';
import Form from 'rc-field-form';

import FormField from './FormField';
import FormView from './FormView';
import { TableFilter } from './TableView';

export interface FilterModalProps {
  tableCodeName: string;
  filters: TableFilter[];
  modalOpen: boolean;
  setModalOpen: (modelOpen: boolean) => void;
  addFilter: (varialbe: string, value: string) => void;
}

export default function FilterModal({ tableCodeName, filters, modalOpen, setModalOpen, addFilter }: FilterModalProps) {
  const { t } = useTranslation();
  const [searchParams] = useSearchParams();
  const [variable, setVariable] = useState<string>('');
  const [form] = Form.useForm();
  const modalContentRef = useRef<HTMLDivElement>(null);

  const closeModal = useCallback(() => {
    setModalOpen(false);
  }, [setModalOpen]);

  const filterFields = useMemo(() => {
    const fields: SelectItem[] = [];
    for (const filter of filters) {
      if (!searchParams.has(filter.variable)) {
        fields.push({
          label: `${t(`${tableCodeName}.${filter.variable}`)}`,
          value: filter.variable,
        });
      }
    }
    return fields;
  }, [filters, searchParams, t, tableCodeName]);

  useEffect(() => {
    const clickHandler = ({ target }: MouseEvent) => {
      if (!modalOpen || (modalContentRef.current && modalContentRef.current.contains(target as Node))) return;
      closeModal();
    };
    document.addEventListener('mousedown', clickHandler);

    const keyHandler = ({ keyCode }: KeyboardEvent) => {
      if (!modalOpen || keyCode !== 27) return;
      closeModal();
    };
    document.addEventListener('keydown', keyHandler);

    return () => {
      document.removeEventListener('mousedown', clickHandler);
      document.removeEventListener('keydown', keyHandler);
    };
  }, [closeModal, modalOpen, setModalOpen]);

  const InputValue = ({ ...props }) => {
    const selectedFilter = filters.find(filter => filter.variable === variable);
    return selectedFilter?.customInput ? (
      <>{selectedFilter?.customInput(props.value, props.onChange)}</>
    ) : (
      <InputField placeholder="inputValue" {...props} />
    );
  };

  return (
    <>
      <Transition
        className="fixed inset-0 bg-slate-900 bg-opacity-30 z-40 transition-opacity"
        show={modalOpen}
        enter="transition ease-out duration-200"
        enterStart="opacity-0"
        enterEnd="opacity-100"
        leave="transition ease-out duration-100"
        leaveStart="opacity-100"
        leaveEnd="opacity-0"
        aria-hidden="true"
        appear={undefined}
      />
      <Transition
        id={`filtermodal.${tableCodeName}`}
        className="fixed inset-0 z-50 overflow-hidden flex items-start top-20 mb-4 justify-center transform px-4 sm:px-6"
        role="dialog"
        aria-modal="true"
        show={modalOpen}
        enter="transition ease-in-out duration-200"
        enterStart="opacity-0 translate-y-4"
        enterEnd="opacity-100 translate-y-0"
        leave="transition ease-in-out duration-200"
        leaveStart="opacity-100 translate-y-0"
        leaveEnd="opacity-0 translate-y-4"
        appear={undefined}
      >
        <div
          ref={modalContentRef}
          className="bg-white overflow-visible max-w-2xl w-full max-h-full rounded shadow-default"
        >
          <div className="flex flex-col items-center m-4">
            <FormView
              form={form}
              initialValues={{ value: '' }}
              autoComplete="off"
              className="w-full max-w-4xl px-4 grid grid-cols-12 gap-4"
              onFinish={values => {
                if (values.variable && values.value) {
                  const selectedFilter = filters.find(filter => filter.variable === values.variable);
                  addFilter(
                    values.variable,
                    selectedFilter?.toValue ? selectedFilter.toValue(values.value) : values.value,
                  );
                  closeModal();
                }
              }}
            >
              <FormField colSpan="half" name="variable">
                <Select
                  id={`filters.drowndown.${tableCodeName}`}
                  options={filterFields}
                  onChange={e => {
                    form.setFieldValue('value', '');
                    setVariable(e.target.value);
                  }}
                />
              </FormField>
              <FormField colSpan="half" name="value">
                <InputValue key={form.getFieldValue('variable')} />
              </FormField>
              <div className="col-span-full flex justify-end">
                <IconButton label="search" icon="magnifying-glass" />
              </div>
            </FormView>
          </div>
        </div>
      </Transition>
    </>
  );
}
