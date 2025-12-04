import { useEffect, useState } from "react";
// Importamos un icono que simulamos usar (ej. X de Heroicons/Lucide)
// Para el botón de eliminar.
// import { X } from 'lucide-react'; 

export default function NotesPanel({ username }) {
	const [notes, setNotes] = useState([]);
	const [content, setContent] = useState("");
	const [loading, setLoading] = useState(false);
	const [error, setError] = useState(null);

	// Simulamos la API (asumiendo que getNotes, createNote, deleteNote existen)
	const getNotes = async (user) => { /* ... */ }; 
	const createNote = async (user, content, isPublic) => { /* ... */ };
	const deleteNote = async (id) => { /* ... */ };

	const loadNotes = async () => {
		setLoading(true);
		setError(null);
		try {
			// **Simulación de API**
			const res = { data: [
				{ id: 1, content: "Revisar plan de dieta del socio A", createdAt: "2025-01-01" },
				{ id: 2, content: "Necesito comprar proteína", createdAt: "2025-01-02" }
			]}; 
			// const res = await getNotes(username); 
			setNotes(res.data);
		} catch (err) {
			setError("Fallo al cargar las notas.");
		} finally {
			setLoading(false);
		}
	};

	useEffect(() => {
		loadNotes();
	}, [username]);

	const addNote = async () => {
		if (!content.trim()) return;
		setError(null);
		try {
			// await createNote(username, content, false);
			// **Simulación de la adición**
			setNotes([...notes, { id: Date.now(), content: content.trim(), createdAt: new Date().toISOString() }]);
			
			setContent("");
		// 	 loadNotes(); // Recargar notas después de la adición
		} catch (err) {
			setError("No se pudo crear la nota.");
		}
	};

	const removeNote = async (id) => {
		setError(null);
		try {
		// 	 await deleteNote(id);
			// **Simulación de la eliminación**
			setNotes(notes.filter(n => n.id !== id));
			
		// 	 loadNotes(); // Recargar notas después de la eliminación
		} catch (err) {
			setError("No se pudo eliminar la nota.");
		}
	};

	return (
		// Estilo de tarjeta con sombra y esquinas suaves
		<div className="bg-white rounded-xl shadow-xl p-6 h-full">
			<h3 className="text-xl font-bold text-gray-800 mb-4 flex items-center">
				<span className="mr-2 text-yellow-500">📌</span> Notas Rápidas
			</h3>
			
			{/* Mensaje de Error */}
			{error && (
				<div className="bg-red-100 border border-red-400 text-red-700 px-3 py-2 rounded mb-4 text-sm font-medium">
					{error}
				</div>
			)}

			{/* Formulario de Nueva Nota */}
			<div className="flex gap-3 mb-5">
				<input
					// FIX: Añadir text-gray-900 para asegurar que el texto de entrada sea visible.
					className="border border-gray-300 rounded-lg p-3 flex-1 focus:outline-none focus:ring-2 focus:ring-yellow-500 transition text-gray-900"
					placeholder="Escribe una nueva nota..."
					value={content}
					onChange={(e) => setContent(e.target.value)}
					onKeyDown={(e) => { // Añadir nota al presionar Enter
						if (e.key === 'Enter') addNote();
					}}
					aria-label="Nueva nota"
				/>
				<button 
					className="bg-yellow-500 hover:bg-yellow-600 text-white rounded-lg px-4 py-3 font-semibold transition duration-150 shadow-md disabled:opacity-50" 
					onClick={addNote}
					disabled={!content.trim()}
				>
					➕ Añadir
				</button>
			</div>

			{/* Lista de Notas */}
			{loading ? (
				<p className="text-gray-500 italic animate-pulse">Cargando notas...</p>
			) : notes.length === 0 ? (
				<p className="text-gray-500 italic p-3 bg-gray-50 rounded-lg">No hay notas pendientes para mostrar.</p>
			) : (
				<ul className="space-y-3">
					{notes.map((n) => (
						<li 
							key={n.id} 
							className="flex justify-between items-start bg-gray-50 p-3 rounded-lg border border-gray-200 hover:bg-yellow-50 transition shadow-sm"
						>
							<span className="text-gray-800 leading-snug pr-4 text-sm">
								{n.content}
							</span>
							<button 
								className="text-red-500 hover:text-red-700 transition shrink-0 ml-2 p-1" 
								onClick={() => removeNote(n.id)}
								aria-label={`Eliminar nota: ${n.content.substring(0, 20)}...`}
							>
								{/* Simulamos el icono de cierre (X) */}
								<svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12"></path></svg>
							</button>
						</li>
					))}
				</ul>
			)}
		</div>
	);
}