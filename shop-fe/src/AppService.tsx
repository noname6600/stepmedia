/* eslint-disable import/no-extraneous-dependencies */
import React, { ReactNode, useCallback, useLayoutEffect, useRef, useState } from 'react';
import { useTranslation } from 'react-i18next';
import ModalView, { ModalsController } from '@components/common/Modal';
import globalAxios from 'axios';
import { useNotification } from 'rc-notification';

import Toast, { ToastProps } from './components/common/Toast';

export const BASE_URL: string = import.meta.env.REACT_APP_BASE_URL || 'http://localhost:8080';

export interface UserDetail {}

globalAxios.defaults.baseURL = BASE_URL;

export interface UserContext {
  accessToken?: string;
  refreshToken?: string;
  userDetail?: UserDetail;
}

export interface UserPermissions {
  permissions: Set<string>;
  permissionGroups: Set<string>;
}

const AppServiceContext = React.createContext<{
  modalsController: React.RefObject<ModalsController>;
  sendToast: (toast: ToastProps) => void;
  subHeaderExtension: JSX.Element | React.ReactNode;
  setSubHeaderExtension: React.Dispatch<React.SetStateAction<React.ReactNode | JSX.Element>>;
} | null>(null);

export function AppServiceProvider({ children }: React.PropsWithChildren) {
  const { t } = useTranslation();
  const modalsController = useRef<ModalsController>(null);
  const [subHeaderExtension, setSubHeaderExtension] = useState<JSX.Element | ReactNode>(<></>);
  const [notification, notiElement] = useNotification({
    maxCount: 3,
    className: () =>
      'fixed top-0 left-0 right-0 max-w-sm md:max-w-md z-50 mx-auto flex flex-col justify-center mt-2 p-2 space-y-2',
  });

  const sendToast = useCallback(
    (toast: ToastProps) => {
      const key = Date.now();
      toast.title &&
        toast.content &&
        notification.open({
          key,
          duration: 3,
          content: (
            <div className="w-full flex flex-col mx-auto">
              <Toast {...toast} onClose={() => notification.close(key)} />
            </div>
          ),
        });
    },
    [notification],
  );

  useLayoutEffect(() => {
    const createAxiosResponseInterceptor = () => {
      globalAxios.interceptors.response.use(
        response => response,
        async error => {
          if (globalAxios.isCancel(error)) {
            return Promise.resolve();
          }

          if (error.response && error.response.data === undefined && error.response.status > 500) {
            sendToast({
              variant: 'error',
              title: t('errors.servererror.title').toString(),
              content: t('errors.servererror.content').toString(),
            });
            return Promise.reject(error);
          }

          if (error.response && error.response.status !== 401) {
            !error.response.config?.url?.endsWith('s') &&
              sendToast({
                variant: 'error',
                title: error.response?.data?.code,
                content: error.response?.data?.message,
              });
            return Promise.reject(error);
          }
        },
      );
    };

    createAxiosResponseInterceptor();
  }, [sendToast, t]);

  return (
    <AppServiceContext.Provider
      value={{
        modalsController,
        sendToast,
        subHeaderExtension,
        setSubHeaderExtension,
      }}
    >
      {children}
      {notiElement}
      <ModalView ref={modalsController} />
    </AppServiceContext.Provider>
  );
}

export const useAppService = () => {
  const context = React.useContext(AppServiceContext);
  if (!context) {
    throw new Error('useService Error');
  }
  return context;
};
