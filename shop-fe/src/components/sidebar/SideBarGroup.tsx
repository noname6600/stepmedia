import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { IconName } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import SideBarGroupItem from './SideBarGroupItem';

export interface SideBarGroupProps {
  name: string;
  path: string;
  icon: IconName;
  items?: {
    itemName: string;
    itemPath: string;
  }[];
  active?: boolean;
  sidebarOpen: boolean;
  setSidebarOpen: (sidebarOpen: boolean) => void;
}

export default function SideBarGroup({
  name,
  path,
  icon,
  items,
  active = false,
  sidebarOpen,
  setSidebarOpen,
}: SideBarGroupProps) {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const [open, setOpen] = useState(active);

  const handleClick = () => {
    items && items.length ? setOpen(!open) : navigate(path);
    setSidebarOpen && setSidebarOpen(true);
  };

  return (
    <li className={`text-slate-400 rounded !mt-1 bg-slate-900 ${active && 'text-white'}`}>
        <div
          className={
            'py-3 flex flex-row justify-between rounded hover:cursor-pointer hover:text-slate-200 truncate hover:bg-slate-700'
          }
          onClick={handleClick}
        >
          <div className="flex flex-row">
            <span className="w-12 flex justify-center items-center">
              <FontAwesomeIcon icon={icon} />
            </span>
            {sidebarOpen && <span className="ml-2 truncate flex items-center">{t(name)}</span>}
          </div>
          {sidebarOpen && items && items.length && (
            <span className={`w-12 flex justify-center items-center transition-transform ${open && 'rotate-180'}`}>
              <FontAwesomeIcon icon="chevron-down" />
            </span>
          )}
        </div>
        <ul className={`flex flex-col transition-all overflow-y-hidden ${open ? 'max-h-96' : 'max-h-0'}`}>
          {items &&
            sidebarOpen === true &&
            items.map(item => (
              <SideBarGroupItem
                key={`${path}${item.itemPath}`}
                name={`${t(item.itemName)}`}
                path={`${path}${item.itemPath}`}
              />
            ))}
        </ul>
      </li>
  );
}
