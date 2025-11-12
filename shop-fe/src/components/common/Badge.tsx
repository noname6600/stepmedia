import React, { HTMLProps } from 'react';

export type BadgeVariant = 'red' | 'green' | 'blue' | 'light' | 'dark' | 'sky' | 'yellow' | 'purple' | 'lightRed';

export interface BadgeProps extends HTMLProps<HTMLSpanElement> {
  text: string;
  variant?: BadgeVariant;
}

const variants = {
  red: {
    bg: 'bg-red-600',
    text: 'text-white',
  },
  lightRed: {
    bg: 'bg-red-400',
    text: 'text-white',
  },
  green: {
    bg: 'bg-green-500',
    text: 'text-white',
  },
  blue: {
    bg: 'bg-blue-600',
    text: 'text-white',
  },
  light: {
    bg: 'bg-gray-200',
    text: 'text-slate-700',
  },
  dark: {
    bg: 'bg-slate-800',
    text: 'text-white',
  },
  sky: {
    bg: 'bg-blue-400',
    text: 'text-white',
  },
  yellow: {
    bg: 'bg-yellow-500',
    text: 'text-white',
  },
  purple: {
    bg: 'bg-purple-600',
    text: 'text-white',
  },
};

export default function Badge({ text, variant = 'light', ...props }: BadgeProps) {
  return (
    <span
      {...props}
      className={`text-xs inline-block py-1 px-2.5 leading-none text-center whitespace-nowrap align-baseline font-bold rounded ${variants[variant].bg} ${variants[variant].text}`}
    >
      {text}
    </span>
  );
}
