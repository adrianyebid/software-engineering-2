# ===== Stage 1: Build React App =====
FROM node:18-alpine AS build
WORKDIR /app

# Solo copiar package.json primero para mejorar caché
COPY package*.json ./
RUN npm install --production=false

# Copiar el resto del código
COPY . .

# Generar el build optimizado
RUN npm run build

# ===== Stage 2: Serve with Nginx =====
FROM nginx:alpine
WORKDIR /usr/share/nginx/html

# Eliminar contenido por defecto
RUN rm -rf ./*

# Copiar build generado
COPY --from=build /app/dist .

# Copiar config personalizada
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
