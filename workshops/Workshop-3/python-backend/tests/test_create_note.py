from unittest.mock import MagicMock
from app.main import create_note
from app.schemas import NoteCreate
from app.models import Note

def test_create_note_unit():
    # Arrange
    payload = NoteCreate(username="adrian", content="Hola mundo", important=True)
    mock_db = MagicMock()

    # Simula que refresh asigna un id
    def fake_refresh(note):
        note.id = 1

    mock_db.refresh.side_effect = fake_refresh

    # Act
    result = create_note(payload, db=mock_db)

    # Assert
    assert isinstance(result, Note)
    assert result.username == "adrian"
    assert result.content == "Hola mundo"
    assert result.important is True
    assert result.id == 1

    mock_db.add.assert_called_once()
    mock_db.commit.assert_called_once()
    mock_db.refresh.assert_called_once()
