import { useEffect, useState } from "react";
import { getNotes, createNote, deleteNote } from "../api/pythonService";

export default function NotesPanel({ username }) {
  const [notes, setNotes] = useState([]);
  const [content, setContent] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Cargar notas reales desde el backend
  const loadNotes = async () => {
    if (!username) return;

    setLoading(true);
    setError(null);

    try {
      const res = await getNotes(username);
      setNotes(res.data);
    } catch (err) {
      console.error(err);
      setError("❌ Error al cargar las notas.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadNotes();
  }, [username]);

  // Crear nota real
  const addNote = async () => {
    if (!content.trim()) return;

    setError(null);

    try {
      await createNote(username, content.trim(), false);
      setContent("");
      await loadNotes();  // Recargar desde el backend
    } catch (err) {
      console.error(err);
      setError("❌ No se pudo crear la nota.");
    }
  };

  // Eliminar nota real
  const removeNote = async (id) => {
    setError(null);

    try {
      await deleteNote(id);
      await loadNotes(); // Recargar desde el backend
    } catch (err) {
      console.error(err);
      setError("❌ No se pudo eliminar la nota.");
    }
  };

  return (
    <div className="bg-white rounded-xl shadow-xl p-6 h-full">
      <h3 className="text-xl font-bold text-gray-800 mb-4 flex items-center">
        <span className="mr-2 text-yellow-500">📌</span> Notas Rápidas
      </h3>

      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-3 py-2 rounded mb-4 text-sm font-medium">
          {error}
        </div>
      )}

      {/* Nueva nota */}
      <div className="flex gap-3 mb-5">
        <input
          className="border border-gray-300 rounded-lg p-3 flex-1 focus:outline-none focus:ring-2 
                     focus:ring-yellow-500 transition text-gray-900"
          placeholder="Escribe una nueva nota..."
          value={content}
          onChange={(e) => setContent(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && addNote()}
        />
        <button
          className="bg-yellow-500 hover:bg-yellow-600 text-white rounded-lg px-4 py-3 font-semibold 
                     transition shadow-md disabled:opacity-50"
          onClick={addNote}
          disabled={!content.trim()}
        >
          ➕ Añadir
        </button>
      </div>

      {/* Lista de notas */}
      {loading ? (
        <p className="text-gray-500 italic animate-pulse">Cargando notas...</p>
      ) : notes.length === 0 ? (
        <p className="text-gray-500 italic p-3 bg-gray-50 rounded-lg">
          No hay notas para mostrar.
        </p>
      ) : (
        <ul className="space-y-3">
          {notes.map((n) => (
            <li
              key={n.id}
              className="flex justify-between items-start bg-gray-50 p-3 rounded-lg 
                         border border-gray-200 hover:bg-yellow-50 transition shadow-sm"
            >
              <span className="text-gray-800 leading-snug pr-4 text-sm">
                {n.content}
              </span>

              <button
                className="text-red-500 hover:text-red-700 transition p-1"
                onClick={() => removeNote(n.id)}
              >
                <svg
                  className="w-5 h-5"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M6 18L18 6M6 6l12 12"
                  ></path>
                </svg>
              </button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
