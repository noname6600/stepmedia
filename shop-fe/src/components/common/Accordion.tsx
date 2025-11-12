import React, { HTMLProps, useState } from 'react';
import { IconName } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export interface AccordionProps extends HTMLProps<HTMLDivElement> {
  label: string;
  defaultOpen?: boolean;
  icon?: IconName;
}

export default function Accordion({ label, icon, defaultOpen = true, children, ...props }: AccordionProps) {
  const [open, setOpen] = useState(defaultOpen);
  return (
    <div className="bg-slate-100 rounded" {...props}>
      <div
        className="py-3 px-4 flex justify-between items-center hover:cursor-pointer truncate"
        onClick={() => setOpen(old => !old)}
      >
        <div>
          {icon && <FontAwesomeIcon className="text-gray-700" icon={icon}></FontAwesomeIcon>}
          <span className="mx-3 text-lg text-gray-700 font-medium">{`${label}`}</span>
        </div>
        <span className={`flex justify-center items-center transition-transform ${open && 'rotate-180'}`}>
          <FontAwesomeIcon icon="chevron-down" />
        </span>
      </div>
      <div className={`transition-all ${open ? 'max-h-[1000px]' : 'max-h-0 overflow-y-hidden'}`}>{children}</div>
    </div>
  );
}
