import React, { HTMLAttributes, useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useLocation, useNavigate } from 'react-router-dom';
import IconButton from '@components/common/IconButton';
import Header from '@components/header/Header';
import Sidebar from '@components/sidebar/Sidebar';
import { sideBarMenu } from '@components/sidebar/sidebarMenu';
import { AppServiceProvider, useAppService } from '@src/AppService';

import { getTableDefinitionFromPath } from '../utils/pathUtils';

interface Breadcrumb {
  label?: string;
  path?: string;
}

export default function DashboardPage({ children }: HTMLAttributes<HTMLDivElement>) {
  const { t } = useTranslation();
  const location = useLocation();
  const navigate = useNavigate();
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const pathTable = useMemo(() => getTableDefinitionFromPath(location.pathname), [location.pathname]);

  const SubHeader = () => {
    const { subHeaderExtension } = useAppService();

    const getMenuName = () => {
      const group = sideBarMenu.find(g => location.pathname.startsWith(g.groupPath));
      if (group) {
        if (group.items) {
          const item = group.items.find(i => location.pathname.startsWith(`${group.groupPath}${i.itemPath}`));
          if (item)
            return { name: `${t(group.groupName)} - ${t(item.itemName)}`, path: `${group.groupPath}${item.itemPath}` };
        }
        return { name: `${t(group.groupName)}`, path: group.groupPath };
      }
    };

    const menu = getMenuName();
    const breadcrumbs: Breadcrumb[] = [];

    breadcrumbs.push({
      label: 'Trang chủ',
      path: '/dashboard/home',
    });

    if (menu && menu?.path !== '/dashboard/home') {
      breadcrumbs.push({
        label: '/',
      });

      breadcrumbs.push({
        label: menu.name,
        path: menu.path,
      });
    }

    if (pathTable?.id) {
      breadcrumbs.push({
        label: '/',
      });

      breadcrumbs.push({
        label:
          pathTable?.id === 'new'
            ? t(`labels.new.${pathTable?.tableDefinition.tableCodeName}`).toString()
            : t(`labels.detail.${pathTable?.tableDefinition.tableCodeName}`).toString(),
        path: `${menu?.path}/detail/${pathTable?.id}`,
      });
    }

    return (
      <div className=" bg-white shadow p-4 z-10">
        <div className="w-full max-w-9xl mx-auto px-4 sm:px-6 lg:px-8 grid grid-cols-12 gap-4 items-center">
          <div className="col-span-12 sm:col-span-6">
            {menu && <div className="text-slate-700 text-xl font-medium">{menu.name}</div>}
            <nav className="bg-grey-light rounded-md w-full text-sm">
              <div className="list-reset">
                {breadcrumbs.map((item, k) =>
                  item.path ? (
                    <span key={`breadcrumbs_${k}`} className="w-max">
                      <a href={`#${item.path}`} className="text-blue-600 hover:text-blue-700 mr-1">
                        {item.label}
                      </a>
                    </span>
                  ) : (
                    <span key={`breadcrumbs_${k}`} className="text-gray-500 mr-1 w-max">
                      {item.label}
                    </span>
                  ),
                )}
              </div>
            </nav>
          </div>
          <div className="col-span-12 sm:col-span-6 flex justify-end">
            {pathTable && pathTable.id !== undefined && subHeaderExtension}
            {pathTable && pathTable.id === undefined && (
              <div className="flex flex-row space-x-2">
                {pathTable?.tableDefinition.buttons?.map((button, k) => (
                  <div key={`button${k}`}>{button}</div>
                ))}
                {!pathTable?.tableDefinition.createDisable && (
                  <IconButton
                    label="add"
                    icon="plus"
                    type="button"
                    onClick={() => navigate(`${location.pathname}/detail/new`)}
                  />
                )}
              </div>
            )}
          </div>
        </div>
      </div>
    );
  };

  return (
    <AppServiceProvider>
      <div className="flex h-screen overflow-hidden bg-slate-100">
        <Sidebar sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen} />
        <div className="relative flex flex-col flex-1 overflow-hidden scroll-smooth">
          <Header sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen} />
          <SubHeader />
          <main className="overflow-y-auto overflow-x-hidden">
            <div className="px-4 sm:px-6 lg:px-8 py-8 w-full max-w-9xl mx-auto mb-40">{children}</div>
          </main>
        </div>
      </div>
    </AppServiceProvider>
  );
}
