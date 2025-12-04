import { useState } from "react";
import { changeTrainerStatus } from "../../api/trainerService";

export default function ChangeTrainerStatus({ username, currentStatus, onStatusChange, className = "" }) {
  // Inicializamos el estado del interruptor con el estado actual del prop
  const [isActive, setIsActive] = useState(currentStatus);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleToggle = async () => {
    // Deshabilitar el interruptor si la operación ya está en curso
    if (loading) return; 

    const newStatus = !isActive;
    setLoading(true);
    setError(null);

    try {
      await changeTrainerStatus({ username, isActive: newStatus });

      // Actualizar el estado local y notificar al padre
      setIsActive(newStatus);
      onStatusChange(newStatus);
      
    } catch (err) {
      // Manejo de error
      setError(`Error al ${newStatus ? 'activar' : 'desactivar'} el estado.`);
      // Opcional: console.error(err);
    } finally {
      setLoading(false);
    }
  };

  // Definición de clases para el interruptor: Verde para Activo, Rojo para Inactivo
  const switchClasses = isActive
    ? "bg-green-500 justify-end" // Fondo verde y el "pulgar" a la derecha
    : "bg-gray-400 justify-start"; // Fondo gris y el "pulgar" a la izquierda
    
  const buttonText = isActive ? "Activo" : "Inactivo";

  return (
    <div className={`flex flex-col items-start ${className}`}>
        {/* Toggle Switch */}
        <div className="flex items-center space-x-3">
            <label className="text-sm font-medium text-gray-700">Estado de Trainer:</label>
            <button
                onClick={handleToggle}
                disabled={loading}
                // Contenedor del interruptor
                className={`relative w-20 h-8 rounded-full flex items-center p-1 transition-colors duration-300 shadow-inner ${switchClasses} ${
                    loading ? 'opacity-70 cursor-wait' : 'hover:shadow-md'
                }`}
            >
                {/* El "Pulgar" del interruptor */}
                <span
                    className={`w-6 h-6 bg-white rounded-full shadow-md transform transition-transform duration-300`}
                />
                {/* Texto dentro del interruptor */}
                <span className={`absolute text-xs font-bold text-white px-2 ${isActive ? 'left-2' : 'right-2'}`}>
                    {buttonText}
                </span>
            </button>
        </div>

        {/* Mensaje de Carga y Error */}
        {loading && (
            <p className="text-sm text-green-500 mt-2 flex items-center">
                <span className="animate-spin mr-1">🔄</span> Guardando...
            </p>
        )}
        {error && (
            <p className="text-sm text-red-600 mt-2 font-medium">{error}</p>
        )}
    </div>
  );
}