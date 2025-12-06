import { useState } from "react";
import { registerTrainee } from "../../api/traineeService";
import { Link, useNavigate } from "react-router-dom";

// Validar solo letras (pero sin bloquear en tiempo real)
const validateName = (name) => {
  const nameRegex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]*$/;  
  return nameRegex.test(name);
};

// FormInput reutilizable
const FormInput = ({ name, type = "text", placeholder, label, form, validationError, handleChange }) => (
  <div className="mb-4">
    {label && <label className="text-sm font-medium text-gray-700 block mb-1">{label}</label>}
    
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

// Validar edad mínima
const isAdult = (dateString) => {
  if (!dateString) return false;
  const birthDate = new Date(dateString);
  const today = new Date();
  const minAgeDate = new Date(today.getFullYear() - 18, today.getMonth(), today.getDate());
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
  const [loading, setLoading] = useState(false);
  const [copyFeedback, setCopyFeedback] = useState(null);
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;

    // No permitir caracteres inválidos en nombres
    if ((name === "firstName" || name === "lastName") && !validateName(value)) {
      return;
    }

    setForm({ ...form, [name]: value });

    setValidationError({ ...validationError, [name]: null });
    setApiError(null);
  };

  // Copiar credenciales
  const copyToClipboard = (text) => {
    const temp = document.createElement("textarea");
    temp.value = text;
    document.body.appendChild(temp);
    temp.select();

    try {
      document.execCommand("copy");
      setCopyFeedback("✅ Credenciales copiadas al portapapeles.");
    } catch {
      setCopyFeedback("❌ Error al copiar.");
    }

    document.body.removeChild(temp);
    setTimeout(() => setCopyFeedback(null), 3000);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setCredentials(null);
    setApiError(null);
    setLoading(true);

    let errors = {};

    // Validaciones finales
    if (form.firstName.trim().length < 2) {
      errors.firstName = "El nombre debe tener al menos 2 letras.";
    }
    if (form.lastName.trim().length < 2) {
      errors.lastName = "El apellido debe tener al menos 2 letras.";
    }
    if (!isAdult(form.dateOfBirth)) {
      errors.dateOfBirth = "Debes ser mayor de 18 años.";
    }
    if (form.address.trim().length < 5) {
      errors.address = "La dirección debe tener al menos 5 caracteres.";
    }

    if (Object.keys(errors).length > 0) {
      setValidationError(errors);
      setLoading(false);
      return;
    }

    // Envío a backend
    try {
      const res = await registerTrainee(form);
      setCredentials(res.data);

      setForm({
        firstName: "",
        lastName: "",
        dateOfBirth: "",
        address: "",
      });

      setTimeout(() => navigate("/"), 7000);

    } catch (err) {
      setApiError("Fallo en el registro. Verifica que el usuario no exista e intenta de nuevo.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900 p-4">
      <div className="bg-white p-8 rounded-xl shadow-2xl w-full max-w-sm md:max-w-lg">

        <h2 className="text-3xl font-extrabold text-center mb-2 text-gray-800">
          📝 Registrar Nuevo Socio
        </h2>
        <p className="text-center text-gray-500 mb-6">
          Completa los datos para crear una nueva cuenta de Trainee.
        </p>

        {/* Error de API */}
        {apiError && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg mb-6">
            {apiError}
          </div>
        )}

        {/* Credenciales */}
        {credentials ? (
          <div className="mt-6 p-5 bg-blue-50 border border-blue-500 rounded-lg shadow-xl text-center">
            <h3 className="text-2xl font-bold text-blue-700 mb-3">🎉 ¡Registro Exitoso!</h3>

            <div className="bg-blue-100 p-4 rounded-md border-l-4 border-blue-500 mb-2 flex justify-between items-center font-mono">
              <div>
                <p><strong>Usuario:</strong> {credentials.username}</p>
                <p className="mt-1"><strong>Contraseña:</strong> {credentials.password}</p>
              </div>

              <button
                onClick={() =>
                  copyToClipboard(
                    `Usuario: ${credentials.username}\nContraseña: ${credentials.password}`
                  )
                }
                className="ml-4 px-3 py-1 text-sm bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-lg"
              >
                📋 Copiar
              </button>
            </div>

            {copyFeedback && (
              <p className={`text-sm font-semibold ${
                copyFeedback.startsWith("✅") ? "text-green-700" : "text-red-500"
              }`}>
                {copyFeedback}
              </p>
            )}

            <button
              className="w-full mt-3 bg-green-500 hover:bg-green-600 text-white font-bold py-2 rounded-lg"
              onClick={() => navigate("/")}
            >
              Continuar al Login
            </button>
          </div>
        ) : (
          // Formulario
          <form onSubmit={handleSubmit}>

            <FormInput
              name="firstName"
              placeholder="Nombre del Socio"
              form={form}
              validationError={validationError}
              handleChange={handleChange}
            />

            <FormInput
              name="lastName"
              placeholder="Apellido del Socio"
              form={form}
              validationError={validationError}
              handleChange={handleChange}
            />

            <FormInput
              name="dateOfBirth"
              type="date"
              label="Fecha de Nacimiento"
              form={form}
              validationError={validationError}
              handleChange={handleChange}
            />

            <FormInput
              name="address"
              placeholder="Dirección Completa"
              form={form}
              validationError={validationError}
              handleChange={handleChange}
            />

            <button
              type="submit"
              disabled={loading}
              className={`w-full font-bold py-3 rounded-lg shadow-lg mt-4 flex items-center justify-center ${
                loading ? "bg-gray-400" : "bg-blue-600 hover:bg-blue-700 text-white"
              }`}
            >
              {loading ? "Procesando..." : "💪 Crear Cuenta de Trainee"}
            </button>

            <p className="mt-6 text-sm text-center text-gray-600">
              ¿Eres Admin o Trainer?{" "}
              <Link to="/" className="text-blue-600 font-semibold">Inicia sesión</Link>
            </p>
          </form>
        )}
      </div>
    </div>
  );
}