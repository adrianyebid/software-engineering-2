import { useEffect, useState } from "react";
import { getNotes, createNote, deleteNote } from "../api/pythonService";

export default function NotesPanel({ username }) {
  const [notes, setNotes] = useState([]);
  const [content, setContent] = useState("");

  const loadNotes = async () => {
    const res = await getNotes(username);
    setNotes(res.data);
  };

  useEffect(() => {
    loadNotes();
  }, [username]);

  const addNote = async () => {
    if (!content.trim()) return;
    await createNote(username, content, false);
    setContent("");
    loadNotes();
  };

  const removeNote = async (id) => {
    await deleteNote(id);
    loadNotes();
  };

  return (
    <div className="bg-white border rounded p-4 mt-4">
      <h3 className="font-semibold mb-2">Notas (Python)</h3>
      <div className="flex gap-2 mb-3">
        <input
          className="border rounded p-2 flex-1"
          placeholder="Nueva nota..."
          value={content}
          onChange={(e) => setContent(e.target.value)}
        />
        <button className="bg-blue-600 text-white rounded px-3" onClick={addNote}>
          Añadir
        </button>
      </div>
      <ul className="list-disc list-inside">
        {notes.map((n) => (
          <li key={n.id} className="flex justify-between">
            <span>{n.content}</span>
            <button className="text-red-600" onClick={() => removeNote(n.id)}>
              eliminar
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}