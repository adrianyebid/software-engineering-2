from unittest.mock import MagicMock
from app.main import list_notes
from app.models import Note

def test_list_notes_unit():
    mock_db = MagicMock()

    # Simula notas de la consulta
    mock_db.query().filter().all.return_value = [
        Note(id=1, username="adrian", content="Nota 1", important=False),
        Note(id=2, username="adrian", content="Nota 2", important=True),
    ]

    result = list_notes("adrian", db=mock_db)

    assert len(result) == 2
    assert result[0].content == "Nota 1"
    assert result[1].important is True
