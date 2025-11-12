import React, { useEffect, useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useLocation, useNavigate } from 'react-router-dom';
import IconButton from '@components/common/IconButton';
import { getTableDefinitionFromPath } from '@utils/pathUtils';
import globalAxios from 'axios';
import { FormInstance } from 'rc-field-form';
import Form from 'rc-field-form';

import { useAppService } from '../../AppService';

import FormView, { FormViewData, FormViewProps } from './FormView';
import { TableDefinition } from './TableView';

export interface TableFormViewProps extends FormViewProps {
  form?: FormInstance<any>;
  editMode?: boolean;
  canEdit?: (data: any) => boolean;
  canDelete?: (data: any) => boolean;
  onPreSubmit?: (data: any) => Promise<any>;
}

const TableFormViewContext = React.createContext<{
  tableDefinition?: TableDefinition;
  id?: string;
} | null>(null);

export default function TableFormView({
  form,
  editMode = true,
  canEdit,
  canDelete,
  onPreSubmit,
  children,
  ...props
}: TableFormViewProps) {
  const { t } = useTranslation();
  const location = useLocation();
  const pathTable = useMemo(() => getTableDefinitionFromPath(location.pathname), [location.pathname]);
  const { sendToast, setSubHeaderExtension, modalsController } = useAppService();
  const navigate = useNavigate();
  const [internalForm] = Form.useForm(form);
  const [formData, setFormData] = useState<FormViewData>({ editMode, loading: true, isNew: pathTable?.id === 'new' });

  useEffect(() => {
    const controller = new AbortController();
    if (pathTable?.id) {
      if (pathTable.id !== 'new') {
        globalAxios
          .get(`${pathTable?.tableDefinition.rootPath}/${pathTable.id}`, { signal: controller.signal })
          .then(response => response && setFormData(oldFormData => ({ ...oldFormData, detail: response.data.data })))
          .catch(error => {
            error?.response?.status === 403 &&
              sendToast({
                variant: 'error',
                title: error.response?.data?.code,
                content: error.response?.data?.message,
              });
          })
          .finally(() => {
            setFormData(oldFormData => ({ ...oldFormData, loading: false }));
          });
      } else {
        setFormData(oldFormData => ({ ...oldFormData, loading: false, editMode: true }));
      }
    }
    return () => controller.abort();
  }, [formData.editMode, pathTable?.id, pathTable?.tableDefinition.rootPath, sendToast]);

  useEffect(() => {
    setFormData(oldFormData => ({ ...oldFormData, isNew: pathTable?.id === 'new' }));
  }, [pathTable?.id]);

  const submitDetail = async (values: any) => {
    const data = onPreSubmit === undefined ? values : await onPreSubmit(values);
    return (
      pathTable &&
      globalAxios
        .request({
          url: pathTable.tableDefinition.rootPath,
          method: pathTable.id === 'new' ? 'POST' : 'PUT',
          data,
        })
        .then(response => {
          sendToast({
            variant: 'success',
            title: pathTable.id === 'new' ? 'Tạo mới thành công' : 'Cập nhật thành công',
            content:
              pathTable.id === 'new'
                ? `Bạn vừa thực hiện thao tác tạo mới ${t(
                  `${pathTable.tableDefinition.tableCodeName}.objectName`,
                )} thành công.`
                : `Bạn vừa thực hiện thao tác cập nhật ${t(
                  `${pathTable.tableDefinition.tableCodeName}.objectName`,
                )} thành công.`,
          });
          setFormData(oldFormData => ({ ...oldFormData, detail: response.data.data, editMode: false }));
          navigate(`${pathTable.tableDefinition.rootPage}/detail/${response.data.data.id}`, {
            replace: true,
            preventScrollReset: true,
          });
        })
    );
  };

  useEffect(() => {
    const deleteDetail = () => {
      setFormData(oldFormData => ({ ...oldFormData, loading: true }));
      return (
        pathTable &&
        globalAxios
          .request({
            url: `${pathTable.tableDefinition.rootPath}/${pathTable.id}`,
            method: 'DELETE',
          })
          .then(() => {
            sendToast({
              variant: 'success',
              title: 'Xóa thành công',
              content: `Bạn vừa thực hiện thao tác xóa ${t(
                `${pathTable.tableDefinition.tableCodeName}.objectName`,
              )} thành công.`,
            });
            navigate(pathTable.tableDefinition.rootPage, { replace: true });
          })
          .catch(() => undefined)
          .finally(() => {
            modalsController.current?.close('deleteDetail');
            setFormData(oldFormData => ({ ...oldFormData, loading: false }));
          })
      );
    };

    const openConfirmModal = () => {
      modalsController.current?.open({
        key: 'deleteDetail',
        content: (
          <div className="p-4 max-w-md flex flex-col space-y-2">
            <div className="font-bold">Xóa dữ liệu</div>
            <div>Dữ liệu bị xóa sẽ không thể khôi phục. Bạn có thực sự muốn xóa dữ liệu này không?</div>
            <div className="mt-4 flex space-x-2 justify-end">
              <IconButton
                label="cancel"
                variant="light"
                onClick={() => modalsController.current?.close('deleteDetail')}
              />
              <IconButton label="delete" variant="lightRed" onClick={() => deleteDetail()} />
            </div>
          </div>
        ),
      });
    };

    pathTable &&
      setSubHeaderExtension(
        <>
          <div className="col-span-full flex justify-end space-x-2">
            {!formData.editMode &&
              !pathTable?.tableDefinition.deleteDisable &&
              (canDelete === undefined || canDelete(formData.detail)) && (
                <IconButton label="delete" type="button" variant="lightRed" onClick={() => openConfirmModal()} />
            )}
            {!formData.editMode &&
              !pathTable?.tableDefinition.editDisable &&
              ((canEdit && canEdit(formData.detail)) || true) && (
                <IconButton
                  label="edit"
                  type="button"
                  onClick={() => setFormData(oldFormData => ({ ...oldFormData, editMode: true }))}
                />
            )}
            {formData.editMode && (
              <>
                <IconButton
                  label="cancel"
                  type="button"
                  variant="light"
                  onClick={() => {
                    if (formData.isNew) {
                      navigate(-1);
                    } else {
                      setFormData(oldFormData => ({ ...oldFormData, editMode: false }));
                      internalForm?.setFieldsValue(formData.detail);
                    }
                  }}
                />
                <IconButton label="save" onClick={() => internalForm.submit()} />
              </>
            )}
          </div>
        </>,
      );
  }, [
    canDelete,
    canEdit,
    formData,
    internalForm,
    location.pathname,
    navigate,
    sendToast,
    setSubHeaderExtension,
    pathTable?.tableDefinition.deleteDisable,
    pathTable?.tableDefinition.editDisable,
    pathTable?.tableDefinition.rootPage,
    pathTable?.tableDefinition.rootPath,
    pathTable?.tableDefinition.tableCodeName,
    pathTable,
    onPreSubmit,
    modalsController,
    t,
  ]);

  return (
    <TableFormViewContext.Provider value={{ tableDefinition: pathTable?.tableDefinition, id: pathTable?.id }}>
      <div className="pb-16 w-full flex flex-col items-center">
        <FormView
          {...props}
          form={internalForm}
          formData={formData}
          onFinish={values => {
            setFormData(oldFormData => ({ ...oldFormData, loading: true }));
            submitDetail(values).finally(() => {
              setFormData(oldFormData => ({ ...oldFormData, loading: false }));
            });
          }}
        >
          {children}
        </FormView>
      </div>
    </TableFormViewContext.Provider>
  );
}

export const useTableFormViewContext = () => {
  const context = React.useContext(TableFormViewContext);
  if (!context) {
    return { tableDefinition: undefined, id: undefined } as {
      tableDefinition?: TableDefinition;
      id?: string;
    };
  }
  return context;
};
