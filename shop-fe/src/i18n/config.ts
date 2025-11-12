import { initReactI18next } from 'react-i18next';
import i18next from 'i18next';

import en from './en.json';
import vi from './vi.json';

export const resources = {
  en: {
    translation: en,
  },
  vi: {
    translation: vi,
  },
};

i18next.use(initReactI18next).init({
  lng: 'vi',
  debug: true,
  resources,
});
