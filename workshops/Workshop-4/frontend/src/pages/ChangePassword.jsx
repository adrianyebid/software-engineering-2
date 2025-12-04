import { useState } from "react";
import { useAuth } from "../context/authContext.jsx";

// Constante para la seguridad mínima de la contraseña
const MIN_PASSWORD_LENGTH = 6;

export default function ChangePassword() {
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [message, setMessage] = useState(null); // Para mostrar mensajes de éxito/error
  const [isError, setIsError] = useState(false); // Bandera para indicar si el mensaje es un error
  const { changePassword } = useAuth();

  // Función de validación de la nueva contraseña
  const validatePassword = (password) => {
    if (password.length < MIN_PASSWORD_LENGTH) {
      return `La nueva contraseña debe tener al menos ${MIN_PASSWORD_LENGTH} caracteres.`;
    }
    if (password === oldPassword) {
      return "La nueva contraseña no puede ser igual a la anterior.";
    }
    return null; // Null si es válida
  };

  const handleChange = async (e) => {
    e.preventDefault();
    setMessage(null);
    setIsError(false);

    // 1. Validar inputs
    const validationMessage = validatePassword(newPassword);
    if (validationMessage) {
      setMessage(validationMessage);
      setIsError(true);
      return;
    }

    try {
      // 2. Intentar cambio de contraseña
      await changePassword(oldPassword, newPassword);
      
      // 3. Éxito
      setMessage("✅ Contraseña actualizada con éxito.");
      setIsError(false);
      
      // Limpiar campos después del éxito
      setOldPassword("");
      setNewPassword("");

    } catch (err) {
      // 4. Fallo de la API (ej. contraseña antigua incorrecta)
      setMessage("⚠️ Error al cambiar la contraseña. Verifica la contraseña actual.");
      setIsError(true);
      // Opcional: console.error(err);
    }
  };

  return (
    // Diseño consistente: Fondo oscuro
    <div className="min-h-screen flex items-center justify-center bg-gray-900 p-4">
      <form 
        onSubmit={handleChange} 
        // Formulario más elegante y responsive
        className="bg-white p-8 rounded-xl shadow-2xl w-full max-w-sm md:max-w-md"
      >
        <h2 className="text-3xl font-extrabold text-center mb-6 text-gray-800">
          🔑 Cambiar Contraseña
        </h2>

        {/* --- Mensaje de Éxito o Error --- */}
        {message && (
          <div
            // Clases dinámicas según si es error o éxito
            className={`px-4 py-3 rounded-lg relative mb-4 font-semibold ${
              isError
                ? "bg-red-100 border border-red-400 text-red-700"
                : "bg-green-100 border border-green-400 text-green-700"
            }`}
            role="alert"
          >
            {message}
          </div>
        )}
        {/* ---------------------------------- */}

        {/* Contraseña Actual */}
        <input
          type="password"
          placeholder="Contraseña actual"
          value={oldPassword}
          onChange={(e) => setOldPassword(e.target.value)}
          // Estilos de input mejorados
          className="w-full mb-4 p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600 transition duration-150"
          aria-label="Contraseña actual"
        />

        {/* Nueva Contraseña */}
        <input
          type="password"
          placeholder="Nueva contraseña"
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
          className="w-full mb-4 p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600 transition duration-150"
          aria-label="Nueva contraseña"
        />
        
        {/* Ayuda de seguridad */}
        <p className="text-xs text-gray-500 mb-6">
            La nueva contraseña debe tener al menos **{MIN_PASSWORD_LENGTH}** caracteres.
        </p>


        {/* Botón de Actualizar */}
        <button 
          type="submit" 
          // Botón con estilo consistente
          className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 rounded-lg shadow-md transition duration-200 ease-in-out transform hover:scale-[1.01]"
        >
          Actualizar Contraseña
        </button>
      </form>
    </div>
  );
}