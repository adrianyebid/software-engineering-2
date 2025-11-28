from unittest.mock import MagicMock
import pytest

from app.main import delete_note
from app.models import Note
from fastapi import HTTPException

def test_delete_note_exists():
    mock_db = MagicMock()

    mock_db.query().filter().first.return_value = Note(
        id=10, username="adrian", content="Eliminar", important=False
    )

    response = delete_note(10, db=mock_db)

    assert response is None

    mock_db.delete.assert_called_once()
    mock_db.commit.assert_called_once()

def test_delete_note_not_found():
    mock_db = MagicMock()
    mock_db.query().filter().first.return_value = None

    with pytest.raises(HTTPException) as exc:
        delete_note(999, db=mock_db)

    assert exc.value.status_code == 404
    assert exc.value.detail == "Note not found"
