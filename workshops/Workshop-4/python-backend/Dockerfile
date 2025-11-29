FROM python:3.11-slim
WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Instalas pytest si no está incluido
RUN pip install pytest

COPY app/ ./app
COPY tests/ ./tests   

# Ejecutar tests durante la construcción
RUN pytest

EXPOSE 8000
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]
