import { IconName } from '@fortawesome/fontawesome-svg-core';

export interface MenuItem {
  groupName: string;
  groupPath: string;
  icon: IconName;
  items?: {
    itemName: string;
    itemPath: string;
  }[];
}

const sideBarMenu: MenuItem[] = [
  {
    groupName: 'labels.home',
    groupPath: '/dashboard/home',
    icon: 'gauge',
  },
  {
    groupName: 'labels.customer',
    groupPath: '/dashboard/customers',
    icon: 'users',
  },
  {
    groupName: 'labels.shop',
    groupPath: '/dashboard/shops',
    icon: 'shop',
  },
  {
    groupName: 'labels.product',
    groupPath: '/dashboard/products',
    icon: 'layer-group',
  },
  {
    groupName: 'labels.order',
    groupPath: '/dashboard/orders',
    icon: 'receipt',
  },
];

export { sideBarMenu };
