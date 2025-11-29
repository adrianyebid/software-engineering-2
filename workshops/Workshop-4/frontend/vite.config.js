import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import tailwindcss from '@tailwindcss/vite'


export default defineConfig({
  plugins: [react(),tailwindcss()],
  server: {
    proxy: {
      '/api': {
        target: 'http://java-backend:8080', // <-- ¡DEBE SER ESTO!
        changeOrigin: true,
        secure: false,
      },
    },
  },
});

