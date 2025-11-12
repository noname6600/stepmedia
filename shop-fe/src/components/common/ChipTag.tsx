import React, { HTMLProps } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export interface Chip extends HTMLProps<HTMLSpanElement> {
  onClose?: () => void;
}

export default function ChipTag({ children, onClose, ...props }: Chip) {
  return (
    <span
      {...props}
      className="m-1 px-2 py-1 rounded-full text-slate-700 bg-slate-200 hover:bg-slate-500 hover:text-white active:bg-slate-600 font-semibold text-sm flex items-center cursor-pointer align-center w-max"
    >
      <span className="px-2">{children}</span>
      {onClose && (
        <FontAwesomeIcon
          icon="xmark"
          className="w-4 h-4 p-1 hover:rounded-full hover:bg-slate-300 hover:text-slate-700"
          onClick={() => onClose()}
        />
      )}
    </span>
  );
}
