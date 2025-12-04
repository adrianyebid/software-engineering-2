import { useState } from "react";
import { registerTrainee } from "../../api/traineeService";
import { Link, useNavigate } from "react-router-dom";

// Función de validación para asegurar solo letras y espacios
const validateName = (name) => {
  const nameRegex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]{2,}$/;
  return nameRegex.test(name);
};

// Función de validación de edad (mayor o igual a 18 años)
const isAdult = (dateString) => {
  if (!dateString) return false;
  const birthDate = new Date(dateString);
  const today = new Date();
  
  // Calcular la edad mínima (18 años atrás desde hoy)
  const minAgeDate = new Date(today.getFullYear() - 18, today.getMonth(), today.getDate());
  
  // Si la fecha de nacimiento es anterior o igual a la fecha de edad mínima, es mayor de edad.
  return birthDate <= minAgeDate;
};


export default function RegisterTrainee() {
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    dateOfBirth: "",
    address: "",
  });

  const [credentials, setCredentials] = useState(null);
  const [validationError, setValidationError] = useState({});
  const [apiError, setApiError] = useState(null);
  const [loading, setLoading] = useState(false); // Estado de carga para el envío
  const [copyFeedback, setCopyFeedback] = useState(null); // Nuevo: Feedback de copia al portapapeles
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    // Limpiar error específico al cambiar
    setValidationError({ ...validationError, [e.target.name]: null }); 
    setApiError(null); // Limpiar error de API al cambiar el formulario
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
    
    // 1. Validaciones de campos
    if (!validateName(form.firstName)) {
      errors.firstName = "El nombre solo debe contener letras (mín. 2).";
    }
    if (!validateName(form.lastName)) {
      errors.lastName = "El apellido solo debe contener letras (mín. 2).";
    }
    if (!isAdult(form.dateOfBirth)) {
      errors.dateOfBirth = "Debes ser mayor de 18 años para registrarte.";
    }
    if (form.address.length < 5) {
      errors.address = "La dirección es demasiado corta (mín. 5 caracteres).";
    }

    if (Object.keys(errors).length > 0) {
      setValidationError(errors);
      setLoading(false); // Detener carga si la validación falla
      return; // Detener el envío
    }

    // 2. Envío de datos
    try {
      const res = await registerTrainee({
        ...form,
        dateOfBirth: form.dateOfBirth || null,
      });

      setCredentials(res.data);
      setForm({ firstName: "", lastName: "", dateOfBirth: "", address: "" }); // Resetear el formulario
      
      // Dar tiempo al usuario para copiar credenciales antes de redirigir
      setTimeout(() => navigate("/"), 7000); 

    } catch (err) {
      setApiError("Error al registrar el Trainee. Verifica que el usuario no exista e intenta de nuevo.");
    } finally {
      setLoading(false); // Detener carga
    }
  };

  // Componente auxiliar para un campo de formulario con feedback de error
  // Nota: Utiliza las variables del closure (form, handleChange, validationError)
  const FormInput = ({ name, type = "text", placeholder, label }) => (
    <div className="mb-4">
      {label && <label htmlFor={name} className="text-sm font-medium text-gray-700 block mb-1">{label}</label>}
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
            : "border-gray-300 focus:ring-blue-600"
        }`}
      />
      {validationError[name] && (
        <p className="text-red-500 text-xs mt-1 font-semibold">{validationError[name]}</p>
      )}
    </div>
  );

  return (
    // Diseño consistente: Fondo oscuro para look de gimnasio
    <div className="min-h-screen flex items-center justify-center bg-gray-900 p-4">
      <div className="bg-white p-8 rounded-xl shadow-2xl w-full max-w-sm md:max-w-lg">
        
        <h2 className="text-3xl font-extrabold text-center mb-2 text-gray-800">
          📝 Nuevo Socio
        </h2>
        <p className="text-center text-gray-500 mb-6">Completa los datos para crear una nueva cuenta de Trainee.</p>

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
          <div className="mt-6 p-5 bg-blue-50 border border-blue-500 rounded-lg shadow-xl text-center">
            <h3 className="text-2xl font-bold text-blue-700 mb-3">🎉 ¡Registro Exitoso!</h3>
            <p className="text-gray-700 mb-4">
                El nuevo Trainee ha sido registrado. Pídele que <span className="font-bold">copie estas credenciales</span> para acceder:
            </p>

            {/* Contenedor de Credenciales y Botón de Copia */}
            <div className="bg-blue-100 p-4 rounded-md text-left font-mono border-l-4 border-blue-500 mb-2 flex justify-between items-center">
                <div>
                    <p className="text-sm truncate">
                        <strong>Usuario:</strong> <span className="text-blue-800 font-extrabold">{credentials.username}</span>
                    </p>
                    <p className="text-sm truncate mt-1">
                        <strong>Contraseña:</strong> <span className="text-blue-800 font-extrabold">{credentials.password}</span>
                    </p>
                </div>
                <button
                    type="button"
                    onClick={() => copyToClipboard(`Usuario: ${credentials.username}\nContraseña: ${credentials.password}`)}
                    className="ml-4 px-3 py-1 text-sm bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-lg shadow-md transition duration-150 transform hover:scale-105"
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
                className="w-full bg-green-500 hover:bg-green-600 text-white font-bold py-2 rounded-lg transition duration-150 mt-3"
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
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    <FormInput name="firstName" placeholder="Nombre del Socio" />
                    <FormInput name="lastName" placeholder="Apellido del Socio" />
                </div>
                
                <FormInput 
                    name="dateOfBirth" 
                    type="date" 
                    label="Fecha de Nacimiento (Mayor de 18)" 
                />
                
                <FormInput name="address" placeholder="Dirección Completa" />
                
                {/* Botón de Registrar */}
                <button
                    type="submit"
                    disabled={loading}
                    className={`w-full font-bold py-3 rounded-lg shadow-lg transition duration-200 ease-in-out mt-4 transform hover:scale-[1.01] flex items-center justify-center
                                ${loading 
                                    ? 'bg-gray-400 cursor-not-allowed' 
                                    : 'bg-blue-600 hover:bg-blue-700 text-white'}`}
                >
                    {loading ? (
                        <>
                            <span className="animate-spin mr-3">🔄</span> Procesando Registro...
                        </>
                    ) : (
                        <>
                            <span className="mr-2">💪</span> Crear Cuenta de Trainee
                        </>
                    )}
                </button>

                {/* Enlace a Login */}
                <p className="mt-6 text-sm text-center text-gray-600">
                    ¿Eres un Trainer o Admin?{" "}
                    <Link to="/" className="text-blue-600 hover:text-blue-800 font-semibold">
                        Inicia sesión aquí
                    </Link>
                </p>
            </form>
        )}
      </div>
    </div>
  );
}