import { defineConfig } from 'vite';
import { resolve } from 'path';
import postcss from './postcss.config.js';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  define: {
    'process.env': process.env,
  },
  css: {
    postcss,
  },
  plugins: [react()],
  resolve: {
    alias: {
      '@src': resolve(__dirname, 'src'),
      '@api': resolve(__dirname, 'src', 'api'),
      '@charts': resolve(__dirname, 'src', 'charts'),
      '@components': resolve(__dirname, 'src', 'components'),
      '@css': resolve(__dirname, 'src', 'css'),
      '@i18n': resolve(__dirname, 'src', 'i18n'),
      '@images': resolve(__dirname, 'src', 'images'),
      '@pages': resolve(__dirname, 'src', 'pages'),
      '@utils': resolve(__dirname, 'src', 'utils'),
    },
  },
  build: {
    commonjsOptions: {
      transformMixedEsModules: process.env.TRANS_FORM_MIXEDES_MODULES || true,
    },
  },
  server: {
    host: 'localhost',
    port: process.env.PORT || 3000,
    open: process.env.BROWSER || true,
  },
  preview: {
    port: process.env.PORT || 8080,
    open: process.env.BROWSER || false,
  },
  envPrefix: ['REACT_APP_'],
});
