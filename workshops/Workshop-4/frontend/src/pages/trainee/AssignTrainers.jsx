import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
// Importamos los servicios (asumiendo que existen)
import { getUnassignedTrainers, assignTrainers } from "../../api/traineeService"; 

export default function AssignTrainers() {
  const { username } = useParams();
  const navigate = useNavigate();
  
  const [trainers, setTrainers] = useState([]);
  const [selected, setSelected] = useState([]);
  const [loading, setLoading] = useState(false);
  const [loadError, setLoadError] = useState(null);
  const [assignMessage, setAssignMessage] = useState(null);
  const [isAssignError, setIsAssignError] = useState(false);

  // --- Carga de Entrenadores No Asignados ---
  useEffect(() => {
    setLoading(true);
    setLoadError(null);
    getUnassignedTrainers(username)
      .then((res) => {
        setTrainers(res.data);
        setLoading(false);
      })
      .catch(() => {
        setLoadError("❌ Error al cargar la lista de entrenadores disponibles.");
        setLoading(false);
      });
  }, [username]);

  // --- Lógica de Selección ---
  const toggleTrainer = (trainerUsername) => {
    setSelected((prev) =>
      prev.includes(trainerUsername)
        ? prev.filter((u) => u !== trainerUsername)
        : [...prev, trainerUsername]
    );
  };

  // --- Lógica de Asignación ---
  const handleAssign = async () => {
    if (selected.length === 0) return;
    setLoading(true);
    setAssignMessage(null);
    setIsAssignError(false);
    
    try {
      await assignTrainers({ traineeUsername: username, trainerUsernames: selected });
      
      setAssignMessage(`✅ ¡${selected.length} entrenadores asignados con éxito a ${username}!`);
      setSelected([]); // Limpiar la selección
      // Actualizar la lista de disponibles (simulando una recarga)
      // Idealmente, recargaríamos la lista para eliminar a los recién asignados
      // loadTrainers(); // Se ejecutaría si tuviéramos una función de recarga
      setTrainers(trainers.filter(t => !selected.includes(t.username)));
      
    } catch (err) {
      setAssignMessage("⚠️ Error al asignar los entrenadores. Intenta de nuevo.");
      setIsAssignError(true);
    } finally {
      setLoading(false);
    }
  };

  // --- Renderizado del Componente ---
  return (
    <div className="min-h-screen bg-gray-900 p-4 sm:p-8">
      <div className="max-w-4xl mx-auto bg-white p-8 rounded-xl shadow-2xl">
        
        {/* Encabezado */}
        <h2 className="text-3xl font-extrabold text-gray-800 mb-6 border-b pb-3 flex items-center">
          <span className="mr-2 text-blue-500">➕</span> Asignar Entrenadores a {username}
        </h2>

        {/* Mensaje de Asignación (Éxito/Error) */}
        {assignMessage && (
          <div
            className={`px-4 py-3 rounded-lg relative mb-6 font-semibold ${
              isAssignError
                ? "bg-red-100 border border-red-400 text-red-700"
                : "bg-green-100 border border-green-400 text-green-700"
            }`}
          >
            {assignMessage}
          </div>
        )}

        {/* Carga y Errores de Carga */}
        {loading && trainers.length === 0 && !loadError ? (
            <p className="text-center text-gray-500 py-10 font-medium">Cargando entrenadores disponibles... 🏃‍♂️</p>
        ) : loadError ? (
             <p className="text-center text-red-600 py-10 font-medium">{loadError}</p>
        ) : (
            // Lista de Entrenadores
            <div className="mb-8">
                <h3 className="text-xl font-bold mb-4 text-gray-700">
                    Entrenadores Disponibles ({trainers.length})
                </h3>

                {trainers.length === 0 ? (
                    <p className="text-gray-500 italic p-4 bg-gray-50 rounded-lg">
                        Actualmente no hay entrenadores sin asignar a este trainee.
                    </p>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        {trainers.map((t) => {
                            const isSelected = selected.includes(t.username);
                            return (
                                <div
                                    key={t.username}
                                    onClick={() => toggleTrainer(t.username)}
                                    // Tarjeta de selección
                                    className={`p-4 rounded-xl shadow-md border-2 cursor-pointer transition duration-200 
                                                ${isSelected 
                                                  ? "border-blue-500 bg-blue-50 transform scale-[1.02] shadow-lg" 
                                                  : "border-gray-200 hover:border-blue-300 hover:bg-gray-50"
                                                }`}
                                >
                                    <div className="flex justify-between items-center">
                                        <div>
                                            <p className="font-semibold text-gray-900">{t.firstName} {t.lastName}</p>
                                            <p className="text-sm text-gray-500">Especialización: <span className="font-medium text-blue-600">{t.specialization}</span></p>
                                        </div>
                                        {/* Checkmark visual en lugar del checkbox nativo */}
                                        <div className={`w-6 h-6 rounded-full flex items-center justify-center border-2 ${isSelected ? 'bg-blue-500 border-blue-500' : 'border-gray-400'}`}>
                                            {isSelected && (
                                                <svg className="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
                                            )}
                                        </div>
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                )}
            </div>
        )}

        {/* Botón de Asignación */}
        <div className="border-t pt-6">
            <button
                onClick={handleAssign}
                disabled={selected.length === 0 || loading}
                className={`w-full bg-blue-600 text-white font-bold py-3 rounded-lg shadow-lg transition duration-200 ease-in-out 
                            ${selected.length === 0 || loading ? 'opacity-50 cursor-not-allowed' : 'hover:bg-blue-700 transform hover:scale-[1.01]'}`}
            >
                {loading ? (
                    <>
                        <span className="animate-spin mr-2">🔄</span> Asignando...
                    </>
                ) : (
                    `Asignar ${selected.length} Entrenador${selected.length !== 1 ? 'es' : ''} Seleccionado${selected.length !== 1 ? 's' : ''}`
                )}
            </button>
        </div>
      </div>
    </div>
  );
}