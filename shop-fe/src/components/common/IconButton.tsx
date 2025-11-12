import React, { ButtonHTMLAttributes } from 'react';
import { useTranslation } from 'react-i18next';
import { IconName } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export type IconButtonVariant = 'red' | 'green' | 'blue' | 'light' | 'dark' | 'sky' | 'yellow' | 'purple' | 'lightRed';

export interface IconButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  label?: string;
  icon?: IconName;
  variant?: IconButtonVariant;
}

const variants = {
  red: {
    bg: 'bg-red-600 hover:bg-red-700 focus:bg-red-700 active:bg-red-800',
    text: 'text-white',
  },
  lightRed: {
    bg: 'bg-red-400 hover:bg-red-500 focus:bg-red-500 active:bg-red-600',
    text: 'text-white',
  },
  green: {
    bg: 'bg-green-500 hover:bg-green-600 focus:bg-green-600 active:bg-green-700',
    text: 'text-white',
  },
  blue: {
    bg: 'bg-blue-600 hover:bg-blue-700 focus:bg-blue-700 active:bg-blue-800',
    text: 'text-white',
  },
  light: {
    bg: 'bg-gray-200 hover:bg-gray-300 focus:bg-gray-300 active:bg-gray-400',
    text: 'text-slate-700',
  },
  dark: {
    bg: 'bg-slate-800 hover:bg-slate-700 focus:bg-slate-700 active:bg-slate-600',
    text: 'text-white',
  },
  sky: {
    bg: 'bg-blue-400 hover:bg-blue-500 focus:bg-blue-500 active:bg-blue-600',
    text: 'text-white',
  },
  yellow: {
    bg: 'bg-yellow-500 hover:bg-yellow-600 focus:bg-yellow-600 active:bg-yellow-700',
    text: 'text-white',
  },
  purple: {
    bg: 'bg-purple-600 hover:bg-purple-700 focus:bg-purple-700 active:bg-purple-800',
    text: 'text-white',
  },
};

const IconButton = ({ label, icon, variant = 'sky', ...props }: IconButtonProps) => {
  const { t } = useTranslation();
  return (
    <button
      className={`flex flex-row items-center px-4 py-2 font-medium leading-tight rounded shadow-default focus:outline-none focus:ring-0 transition duration-100 ease-in-out ${variants[variant].bg} ${variants[variant].text}`}
      {...props}
    >
      {icon && <FontAwesomeIcon icon={icon} />}
      {label && <span className={`w-max inline-block ${icon ? 'pl-2' : ''}`}>{t(`buttons.${label}`)}</span>}
    </button>
  );
};

export default IconButton;
