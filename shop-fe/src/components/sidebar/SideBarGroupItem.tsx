import React from 'react';
import { Link, useLocation } from 'react-router-dom';

export interface SideBarGroupItemProps {
  name: string;
  path: string;
}

export default function SideBarGroupItem({ name, path }: SideBarGroupItemProps) {
  const location = useLocation();

  return (
    <Link
      to={path}
      className={`hover:text-slate-200 hover:bg-slate-800 hover:cursor-pointer m-1 py-2 pl-4 truncate rounded ${
        location.pathname.startsWith(path) ? 'bg-slate-700 text-white' : 'text-slate-400'
      }`}
    >
      {name}
    </Link>
  );
}
