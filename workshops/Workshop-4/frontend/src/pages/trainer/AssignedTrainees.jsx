import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom"; // Importamos Link para la navegación
import { getAssignedTrainees } from "../../api/trainerService";

export default function AssignedTrainees() {
  const { username } = useParams();
  const [trainees, setTrainees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // --- Función de Carga ---
  useEffect(() => {
    setLoading(true);
    setError(null);
    getAssignedTrainees(username)
      .then((res) => {
        setTrainees(res.data);
        setLoading(false);
      })
      .catch(() => {
        setError("❌ Error al cargar la lista de trainees asignados.");
        setLoading(false);
      });
  }, [username]);

  // --- Renderizado de Estados ---
  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-900 text-white">
        <p className="text-xl font-semibold animate-pulse">
          Cargando trainees para {username}... 🏋️
        </p>
      </div>
    );
  }

  // --- Renderizado del Componente ---
  return (
    <div className="min-h-screen bg-gray-900 p-4 sm:p-8">
      <div className="max-w-4xl mx-auto bg-white p-8 rounded-xl shadow-2xl">
        
        {/* Encabezado */}
        <h2 className="text-3xl font-extrabold text-gray-800 mb-6 border-b pb-3 flex items-center">
          <span className="mr-2 text-green-500">🧑‍🤝‍🧑</span> Trainees Asignados a {username}
        </h2>
        
        {/* Mensaje de Error */}
        {error && (
            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-6 font-medium">
                {error}
            </div>
        )}

        {/* Lista de Trainees */}
        {trainees.length === 0 ? (
          <div className="text-center py-10 border-dashed border-2 border-gray-300 rounded-xl bg-gray-50">
            <p className="text-xl font-medium text-gray-600">
              ¡Aún no tienes trainees asignados!
            </p>
            <p className="text-gray-500 mt-2">
                Consulta con tu administrador para empezar a crear planes.
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {trainees.map((t) => (
              // Usamos Link para hacer cada trainee navegable
              <Link
                key={t.username}
                to={`/trainees/${t.username}`}
                // Tarjeta de Trainee (color verde de acento)
                className="p-4 rounded-xl shadow-md border-2 border-gray-200 cursor-pointer transition duration-200 
                           hover:border-green-500 hover:bg-green-50 transform hover:scale-[1.01] flex justify-between items-center"
              >
                <div>
                  <p className="font-semibold text-gray-900">
                    {t.firstName} {t.lastName}
                  </p>
                  <p className="text-sm text-gray-500">
                    Usuario: <span className="font-medium text-green-600">@{t.username}</span>
                  </p>
                </div>
                {/* Flecha indicando que es clicable */}
                <span className="text-green-500 text-2xl ml-4">→</span>
              </Link>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}