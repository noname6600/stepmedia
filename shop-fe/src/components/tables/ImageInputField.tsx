import React, { forwardRef, InputHTMLAttributes, useImperativeHandle, useRef, useState } from 'react';
import { useFormViewContext } from '@components/tables/FormView';
import { AvatarEmpty } from '@images/index';
import { useAppService } from '@src/AppService';
import { setNativeValue } from '@utils/Parsers';
import globalAxios from 'axios';

import { useFormFieldContext } from './FormField';
import ImageModal from './ImageModal';

export interface ImageInputFieldProps extends InputHTMLAttributes<HTMLInputElement> {
  defaultValue?: string;
  onIconClick?: () => void;
  label?: string;
}

export interface ImageInputHandle {
  upload: () => Promise<string | undefined>;
}

const ImageInputField = forwardRef<ImageInputHandle, ImageInputFieldProps>(
  ({ value, defaultValue = AvatarEmpty, label, onChange, ...props }: ImageInputFieldProps, ref) => {
    const containerRef = useRef<HTMLDivElement>(null);
    const inputRef = useRef<HTMLInputElement>(null);
    const { formData } = useFormViewContext();
    const [openModal, setOpenModal] = useState<boolean>(false);
    const { fieldLabel, required } = useFormFieldContext();
    const { sendToast } = useAppService();

    const getAccessToken = () => {
      return localStorage.getItem('accessToken') || '';
    };

    useImperativeHandle(ref, () => ({
      upload() {
        const image = inputRef.current?.value;
        if (image && image.startsWith('data')) {
          const arr = image.split(',');
          const mime = arr[0].match(/:(.*?);/)?.[1];
          const bstr = atob(arr[1]);
          var n = bstr.length;
          const u8arr = new Uint8Array(n);
          while (n--) {
            u8arr[n] = bstr.charCodeAt(n);
          }

          const file = new File([u8arr], 'avatar', { type: mime });
          const body = new FormData();
          body.append('file', file);

          return globalAxios
            .create()
            .post('api/v1/system/media/upload', body, {
              params: { accessLevel: 'PUBLIC', storageType: 'AVATAR' },
              headers: {
                'Content-Type': 'multipart/form-data',
                Authorization: `Bearer ${getAccessToken()}`,
              },
            })
            .then(response => (response?.data?.data?.path ? (response.data.data.path as string) : undefined))
            .catch(e => {
              sendToast({
                variant: 'error',
                title: 'Lỗi upload ảnh',
                content: 'Đã có lỗi xảy ra khi upload ảnh.',
              });
              return Promise.reject(e);
            });
        }
        return Promise.resolve(image);
      },
    }));

    return (
      <div ref={containerRef} className="flex justify-center relative">
        <div className="flex flex-col justify-center items-center">
          <div className="w-48 h-48">
            <img
              className="w-full h-full rounded-full cursor-pointer border-3 border-primary object-center"
              src={value ? `${value}` : defaultValue}
              onClick={() => setOpenModal(true)}
            />
          </div>
          {(label || fieldLabel) && (
            <>
              <label className="form-label inline-block text-gray-700 mt-2">{label || fieldLabel}</label>
              {required && <label className="form-label inline-block text-red-500 pl-1">(*)</label>}
            </>
          )}
          <ImageModal src={value ? `${value}` : defaultValue} modalOpen={openModal} setModalOpen={setOpenModal} />
          {formData.editMode && (
            <label className="cursor-pointer mt-2 px-4 py-2 bg-blue-400 hover:bg-blue-500 focus:bg-blue-500 active:bg-blue-600 text-white text-sm rounded-full">
              <span className="text-base leading-normal">Choose file</span>
              <input
                type="file"
                accept=".jpg, .jpeg, .png"
                className="hidden"
                onChange={e => {
                  if (e.target.files && e.target.files[0]) {
                    if (!e.target.files[0].type.startsWith('image/')) {
                      const parts = e.target.files[0].type.split('/');
                      sendToast({
                        variant: 'warning',
                        title: 'Tệp tin không hợp lệ',
                        content: 'Không hỗ trợ file ' + parts[parts.length - 1],
                      });
                      return;
                    }
                    const reader = new FileReader();
                    reader.onload = event => {
                      inputRef.current && setNativeValue(inputRef.current, event.target?.result?.toString());
                      inputRef.current?.dispatchEvent(new Event('input', { bubbles: true }));
                    };
                    reader.readAsDataURL(e.target.files[0]);
                  }
                }}
              />
              <input
                type="text"
                className="sr-only"
                ref={inputRef}
                value={value !== undefined && value !== null ? value : ''}
                onChange={onChange}
                {...props}
              />
            </label>
          )}
        </div>
      </div>
    );
  },
);
export default ImageInputField;
