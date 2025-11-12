import React from 'react';
import { IconName } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export type ToastVariant = 'info' | 'success' | 'warning' | 'error';

export interface ToastProps {
  title?: string;
  content?: string;
  variant?: ToastVariant;
  onClose?: () => void;
}

const variants: { [key: string]: { icon: IconName; bg: string; text: string; border: string } } = {
  info: {
    icon: 'circle-info',
    bg: 'bg-primary-100',
    text: 'text-primary-700',
    border: 'border-primary-200',
  },
  success: {
    icon: 'circle-check',
    bg: 'bg-success-100',
    text: 'text-success-700',
    border: 'border-success/20',
  },
  warning: {
    icon: 'triangle-exclamation',
    bg: 'bg-warning-100',
    text: 'text-warning-700',
    border: 'border-warning-200',
  },
  error: {
    icon: 'circle-xmark',
    bg: 'bg-danger-100',
    text: 'text-danger-700',
    border: 'border-danger-200',
  },
};

export default function Toast({ title, content, variant = 'info', onClose }: ToastProps) {
  return (
    <div
      className={`w-full max-w-full rounded-lg bg-clip-padding text-sm shadow-default ${variants[variant].bg} ${variants[variant].text}`}
    >
      <div
        className={`flex items-center justify-between rounded-t-lg border-b-2 bg-clip-padding px-4 pt-2.5 pb-2 ${variants[variant].bg} ${variants[variant].text} ${variants[variant].border}`}
      >
        <p className={`flex items-center font-bold ${variants[variant].text}`}>
          <FontAwesomeIcon icon={variants[variant].icon} className="mr-2 text-lg" />
          {title}
        </p>
        <div className="flex items-center">
          <button
            type="button"
            className="ml-2 box-content rounded-none border-none opacity-80 hover:no-underline hover:opacity-75 focus:opacity-100 focus:shadow-none focus:outline-none"
            onClick={() => onClose && onClose()}
          >
            <span className="w-[1em] focus:opacity-100 disabled:pointer-events-none disabled:select-none disabled:opacity-25 [&.disabled]:pointer-events-none [&.disabled]:select-none [&.disabled]:opacity-25">
              <FontAwesomeIcon icon="xmark" className="text-lg" />
            </span>
          </button>
        </div>
      </div>
      <div className={`break-words rounded-b-lg py-4 px-4 ${variants[variant].bg} ${variants[variant].text}`}>
        {content}
      </div>
    </div>
  );
}
