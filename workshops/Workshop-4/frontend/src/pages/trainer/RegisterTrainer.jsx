import { useState } from "react";
import { registerTrainer } from "../../api/trainerService";
import { Link, useNavigate } from "react-router-dom";

// Lista de especializaciones
const SPECIALIZATIONS = [
  { id: 1, name: "Cardio" },
  { id: 2, name: "Strength" },
  { id: 3, name: "Flexibility" },
  { id: 4, name: "HIIT" },
  { id: 5, name: "Yoga" },
];

// Función de validación para asegurar solo letras y espacios
const validateName = (name) => {
  // Regex: Solo letras (incluyendo acentos) y espacios. Mínimo 2 caracteres.
  const nameRegex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]{2,}$/;
  return nameRegex.test(name);
};

// Componente auxiliar para un campo de formulario con feedback de error
const FormInput = ({ name, type = "text", placeholder, form, validationError, handleChange }) => (
  <div className="mb-4">
    <input
      type={type}
      name={name}
      id={name}
      placeholder={placeholder}
      value={form[name]}
      onChange={handleChange}
      className={`w-full p-3 border rounded-lg focus:outline-none focus:ring-2 transition duration-150 bg-white ${
        validationError[name] 
          ? "border-red-500 focus:ring-red-600" 
          : "border-gray-300 focus:ring-green-600" // Acento verde
      }`}
      aria-label={placeholder}
    />
    {validationError[name] && (
      <p className="text-red-500 text-xs mt-1 font-semibold">{validationError[name]}</p>
    )}
  </div>
);


export default function RegisterTrainer() {
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    specialization: "",
  });

  const [credentials, setCredentials] = useState(null);
  const [validationError, setValidationError] = useState({}); // Errores de validación de campos
  const [apiError, setApiError] = useState(null); // Error de la API
  const [loading, setLoading] = useState(false); // Estado de carga
  const [copyFeedback, setCopyFeedback] = useState(null); // Feedback de copia al portapapeles
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    // Limpiar el error de ese campo si empieza a escribir
    setValidationError({ ...validationError, [e.target.name]: null });
    setApiError(null); // Limpiar error de API
  };
  
  // Función robusta para copiar texto al portapapeles (compatible con iframes)
  const copyToClipboard = (text) => {
    const tempTextArea = document.createElement("textarea");
    tempTextArea.value = text;
    document.body.appendChild(tempTextArea);
    tempTextArea.select();
    
    try {
      const successful = document.execCommand('copy');
      if (successful) {
        setCopyFeedback("✅ Credenciales copiadas al portapapeles.");
      } else {
        setCopyFeedback("❌ No se pudo copiar automáticamente.");
      }
    } catch (err) {
      setCopyFeedback("❌ Error al copiar: Fallback fallido.");
    }
    
    document.body.removeChild(tempTextArea);
    
    // Limpiar el feedback después de un breve tiempo
    setTimeout(() => setCopyFeedback(null), 3000);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setCredentials(null);
    setApiError(null);
    setLoading(true); // Iniciar carga
    let errors = {};

    // 1. Validaciones
    if (!validateName(form.firstName)) {
      errors.firstName = "El nombre solo debe contener letras (mín. 2).";
    }
    if (!validateName(form.lastName)) {
      errors.lastName = "El apellido solo debe contener letras (mín. 2).";
    }
    if (!form.specialization) {
      errors.specialization = "Debes seleccionar una especialización.";
    }

    if (Object.keys(errors).length > 0) {
      setValidationError(errors);
      setLoading(false); // Detener carga si la validación falla
      return; // Detener el envío si hay errores de validación
    }

    // 2. Envío de datos
    try {
      const res = await registerTrainer(form);
      setCredentials(res.data);
      setForm({ firstName: "", lastName: "", specialization: "" }); // Resetear el formulario

      // Redirigir después de mostrar las credenciales
      setTimeout(() => navigate("/"), 7000); // 7 segundos para que el usuario pueda copiar
    } catch (err) {
      // Mostrar el mensaje de error de la API de forma estética
      setApiError("Fallo en el registro. Verifica que el usuario no exista e intenta de nuevo.");
    } finally {
        setLoading(false); // Detener carga
    }
  };

  return (
    // Diseño consistente: Fondo oscuro para look de gimnasio
    <div className="min-h-screen flex items-center justify-center bg-gray-900 p-4">
      <div className="bg-white p-8 rounded-xl shadow-2xl w-full max-w-sm md:max-w-lg">
        <h2 className="text-3xl font-extrabold text-center mb-2 text-gray-800">
          🏋️ Registrar Nuevo Trainer
        </h2>
        <p className="text-center text-gray-500 mb-6">Completa los datos para crear una nueva cuenta de entrenador.</p>
        
        {/* --- Mensaje de Error de API --- */}
        {apiError && (
          <div
            className="flex items-center bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg relative mb-6"
            role="alert"
          >
            <span className="inline-block w-5 h-5 mr-3">
              <svg fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clipRule="evenodd"></path></svg>
            </span>
            <span className="block sm:inline font-medium text-base">{apiError}</span>
          </div>
        )}
        {/* -------------------------------- */}
        
        {/* --- Mensaje de Credenciales (Éxito) Bonito --- */}
        {credentials ? (
           <div className="mt-6 p-5 bg-green-50 border border-green-500 rounded-lg shadow-xl text-center">
            <h3 className="text-2xl font-bold text-green-700 mb-3">✅ ¡Trainer Registrado!</h3>
            <p className="text-gray-700 mb-4">
                El nuevo Trainer ha sido registrado. Pídele que <span className="font-bold">copie estas credenciales</span> para acceder:
            </p>
            
            {/* Contenedor de Credenciales y Botón de Copia */}
            <div className="bg-green-100 p-4 rounded-md text-left font-mono border-l-4 border-green-500 mb-2 flex justify-between items-center">
                <div>
                    <p className="text-sm truncate">
                        <strong>Usuario:</strong> <span className="text-green-800 font-extrabold">{credentials.username}</span>
                    </p>
                    <p className="text-sm truncate mt-1">
                        <strong>Contraseña:</strong> <span className="text-green-800 font-extrabold">{credentials.password}</span>
                    </p>
                </div>
                <button
                    type="button"
                    onClick={() => copyToClipboard(`Usuario: ${credentials.username}\nContraseña: ${credentials.password}`)}
                    className="ml-4 px-3 py-1 text-sm bg-green-600 hover:bg-green-700 text-white font-semibold rounded-lg shadow-md transition duration-150 transform hover:scale-105"
                    aria-label="Copiar credenciales"
                >
                    📋 Copiar
                </button>
            </div>
            
            {/* Feedback de Copia */}
            {copyFeedback && (
                <div className={`text-sm font-semibold text-center py-1 ${
                    copyFeedback.startsWith('✅') ? 'text-green-700' : 'text-red-500'
                }`}>
                    {copyFeedback}
                </div>
            )}
            
            <button 
                className="w-full bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 rounded-lg transition duration-150 mt-3"
                onClick={() => {
                    // Redirección inmediata después de la confirmación visual
                    navigate("/");
                }}
            >
                Entendido, Continuar al Login
            </button>
            <p className="text-xs mt-3 text-gray-600 font-medium">
                Serás redirigido automáticamente si no haces clic.
            </p>
          </div>
        ) : (
             // --- Formulario de Registro ---
            <form onSubmit={handleSubmit}>
                <FormInput 
                  name="firstName" 
                  placeholder="Nombre del Trainer" 
                  form={form} 
                  validationError={validationError} 
                  handleChange={handleChange} 
                />
                <FormInput 
                  name="lastName" 
                  placeholder="Apellido del Trainer" 
                  form={form} 
                  validationError={validationError} 
                  handleChange={handleChange} 
                />

                {/* Select de Especialización */}
                <div className="mb-4">
                    <select
                        name="specialization"
                        value={form.specialization}
                        onChange={handleChange}
                        className={`w-full p-3 border rounded-lg focus:outline-none focus:ring-2 transition duration-150 bg-white ${
                            validationError.specialization 
                            ? "border-red-500 focus:ring-red-600" 
                            : "border-gray-300 focus:ring-green-600" // Acento verde
                        }`}
                        aria-label="Especialización"
                    >
                        <option value="">🏋️ Selecciona una especialización</option>
                        {SPECIALIZATIONS.map((s) => (
                            <option key={s.id} value={s.name}>
                                {s.name}
                            </option>
                        ))}
                    </select>
                    {validationError.specialization && (
                        <p className="text-red-500 text-xs mt-1 font-semibold">{validationError.specialization}</p>
                    )}
                </div>

                {/* Botón de Registrar */}
                <button
                    type="submit"
                    disabled={loading}
                    className={`w-full font-bold py-3 rounded-lg shadow-lg transition duration-200 ease-in-out mt-4 transform hover:scale-[1.01] flex items-center justify-center
                                ${loading 
                                    ? 'bg-gray-400 cursor-not-allowed' 
                                    : 'bg-green-600 hover:bg-green-700 text-white'}`}
                >
                    {loading ? (
                        <>
                            <span className="animate-spin mr-3">🔄</span> Procesando Registro...
                        </>
                    ) : (
                        <>
                            <span className="mr-2">🥇</span> Crear Cuenta de Trainer
                        </>
                    )}
                </button>

                {/* Enlace a Login */}
                <p className="mt-6 text-sm text-center text-gray-600">
                    ¿Ya tienes cuenta?{" "}
                    <Link to="/" className="text-blue-600 hover:text-blue-800 font-semibold">
                        Inicia sesión
                    </Link>
                </p>
            </form>
        )}
      </div>
    </div>
  );
}