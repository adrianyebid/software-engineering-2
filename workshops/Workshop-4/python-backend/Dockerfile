FROM python:3.11-slim

WORKDIR /app

# Instalar dependencias
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Instalar pytest opcionalmente (solo para CI/CD)
RUN pip install pytest

# Copiar código
COPY app/ ./app

# Copiar tests para que un contenedor de tests pueda ejecutarlos
COPY tests/ ./tests

EXPOSE 8000

# Comando por defecto para correr la API
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]
