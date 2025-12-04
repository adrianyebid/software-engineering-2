import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { deleteTrainer } from "../../api/trainerService";

export default function DeleteTrainerButton({ username, onDelete, className = "" }) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showConfirm, setShowConfirm] = useState(false); // Nuevo estado para la confirmación
  const navigate = useNavigate();

  const handleConfirm = () => {
    setShowConfirm(true);
    setError(null);
  };

  const handleDelete = async () => {
    setLoading(true);
    setError(null);

    try {
      await deleteTrainer(username);
      
      // Manejo de éxito
      localStorage.removeItem("user");
      localStorage.removeItem("token");
      
      // Si se proporciona una función onDelete, la llamamos (ej. en TraineeProfile)
      if (onDelete) {
        onDelete();
      } else {
        // Si no, navegamos por defecto (ej. en Dashboard)
        navigate("/");
      }
      
    } catch (err) {
      // Manejo de error
      setError("❌ Error al eliminar el perfil. Intenta de nuevo.");
      setLoading(false);
      setShowConfirm(false);
    }
  };

  // Clases base y clases pasadas por prop (para usar con la Zona de Peligro)
  const baseClasses = "font-bold py-3 rounded-lg transition duration-200 shadow-md transform hover:scale-[1.01] flex items-center justify-center";

  return (
    <div className="w-full">
      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-3 py-2 rounded mb-3 text-sm font-medium text-center">
          {error}
        </div>
      )}

      {/* Bloque de Confirmación */}
      {showConfirm ? (
        <div className="bg-red-500 p-3 rounded-lg text-white text-center">
          <p className="font-semibold mb-2">¿Confirmas la eliminación?</p>
          <div className="flex justify-center gap-3">
            <button
              onClick={handleDelete}
              disabled={loading}
              className={`bg-red-800 hover:bg-red-900 px-4 py-2 rounded-lg font-bold transition ${loading ? 'opacity-50' : ''}`}
            >
              {loading ? "Eliminando..." : "Sí, Eliminar Permanentemente"}
            </button>
            <button
              onClick={() => setShowConfirm(false)}
              disabled={loading}
              className="bg-gray-700 hover:bg-gray-800 px-4 py-2 rounded-lg font-bold transition"
            >
              Cancelar
            </button>
          </div>
        </div>
      ) : (
        /* Botón Inicial */
        <button
          onClick={handleConfirm}
          disabled={loading}
          // Combina las clases base con las clases pasadas por prop
          className={`w-full bg-red-600 text-white hover:bg-red-700 ${baseClasses} ${className} ${loading ? 'opacity-50 cursor-not-allowed' : ''}`}
        >
          {loading ? (
            <>
              <span className="animate-spin mr-2">🔄</span> Procesando...
            </>
          ) : (
            <>
              <span className="mr-2">🗑️</span> Eliminar Cuenta
            </>
          )}
        </button>
      )}
    </div>
  );
}