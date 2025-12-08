import os
import pytest
from fastapi.testclient import TestClient
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

from app.main import app, get_db
from app.models import Base

# --- Configurar BD temporal ---
SQLITE_URL = "sqlite:///./test_notes.db"

# Antes de cada corrida, borrar la BD de pruebas
if os.path.exists("./test_notes.db"):
    os.remove("./test_notes.db")

engine_test = create_engine(SQLITE_URL, connect_args={"check_same_thread": False})
TestingSessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine_test)

Base.metadata.create_all(bind=engine_test)


# --- Override de dependencia ---
def override_get_db():
    db = TestingSessionLocal()
    try:
        yield db
    finally:
        db.close()

app.dependency_overrides[get_db] = override_get_db


# --- FIX: TestClient con context manager ---
@pytest.fixture
def client():
    with TestClient(app) as c:
        yield c


# ------------ PRUEBA FUNCIONAL COMPLETA --------------
def test_full_note_functionality(client):

    # 1. Crear una nota
    payload = {"username": "adrian", "content": "Hola mundo", "important": True}
    r = client.post("/notes", json=payload)

    assert r.status_code == 201
    created_note = r.json()
    assert created_note["username"] == "adrian"
    assert created_note["content"] == "Hola mundo"
    assert created_note["important"] is True
    assert "id" in created_note

    note_id = created_note["id"]

    # 2. Listar notas
    r2 = client.get("/notes/adrian")
    assert r2.status_code == 200
    notes = r2.json()
    assert len(notes) == 1
    assert notes[0]["id"] == note_id

    # 3. Eliminar nota
    r3 = client.delete(f"/notes/{note_id}")
    assert r3.status_code == 204

    # 4. Comprobar que ya no existe
    r4 = client.get("/notes/adrian")
    assert r4.status_code == 200
    assert r4.json() == []
