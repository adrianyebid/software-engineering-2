import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getTraineeProfile, updateTraineeProfile } from "../../api/traineeService";

// --- VALIDACIONES EXTRAÍDAS PARA ESTABILIDAD ---
const validateName = (name) => {
  const nameRegex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]{2,}$/;
  return nameRegex.test(name);
};

const validateDateOfBirth = (date) => {
    return date && new Date(date) < new Date();
}

// --- COMPONENTE AUXILIAR EXTRAÍDO PARA ESTABILIDAD ---
// Este componente ya no se recrea en cada render de EditTraineeProfile
const FormInput = ({ 
    name, 
    type = "text", 
    placeholder, 
    label, 
    readOnly = false,
    form, 
    handleChange, 
    validationError 
}) => {
  const isError = validationError[name];
  
  return (
    <div className="mb-4">
      {label && <label htmlFor={name} className="text-sm font-medium text-gray-700 block mb-1">{label}</label>}
      <input
        type={type}
        name={name}
        id={name}
        placeholder={placeholder}
        value={form[name]}
        onChange={handleChange}
        readOnly={readOnly}
        className={`w-full p-3 border rounded-lg focus:outline-none focus:ring-2 transition duration-150 bg-white 
          ${readOnly ? "bg-gray-100 cursor-not-allowed" : ""}
          ${isError 
            ? "border-red-500 focus:ring-red-600" 
            : "border-gray-300 focus:ring-blue-600"
          }`}
      />
      {isError && (
        <p className="text-red-500 text-xs mt-1 font-semibold">{isError}</p>
      )}
    </div>
  );
};
// ----------------------------------------------------


export default function EditTraineeProfile() {
  const { username } = useParams();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    username: username || "",
    firstName: "",
    lastName: "",
    dateOfBirth: "",
    address: "",
    isActive: true,
  });

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [apiError, setApiError] = useState(null);
  const [successMessage, setSuccessMessage] = useState(null);
  const [validationError, setValidationError] = useState({});

  // 1. Carga inicial del perfil
  useEffect(() => {
    setLoading(true);
    getTraineeProfile(username)
      .then((res) => {
        const dateOfBirth = res.data.dateOfBirth ? res.data.dateOfBirth.split('T')[0] : '';
        const { firstName, lastName, address, isActive } = res.data;
        setForm({ username, firstName, lastName, dateOfBirth, address, isActive });
        setLoading(false);
      })
      .catch(() => {
        setApiError("Error al cargar el perfil del Trainee. Intente más tarde.");
        setLoading(false);
      });
  }, [username]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    // La lógica de actualización sigue siendo correcta:
    setForm({ ...form, [name]: type === "checkbox" ? checked : value });
    setValidationError({ ...validationError, [name]: null });
    setApiError(null);
    setSuccessMessage(null);
  };
  
  // 2. Validación de formulario antes de enviar
  const validateForm = () => {
    let errors = {};
    if (!validateName(form.firstName)) {
      errors.firstName = "El nombre solo debe contener letras (mín. 2).";
    }
    if (!validateName(form.lastName)) {
      errors.lastName = "El apellido solo debe contener letras (mín. 2).";
    }
    if (!validateDateOfBirth(form.dateOfBirth)) {
        errors.dateOfBirth = "La fecha de nacimiento es obligatoria y debe ser una fecha pasada.";
    }
    if (!form.address.trim() || form.address.length < 5) {
        errors.address = "La dirección es obligatoria (mín. 5 caracteres).";
    }

    setValidationError(errors);
    return Object.keys(errors).length === 0;
  };

  // 3. Envío del formulario
  const handleSubmit = async (e) => {
    e.preventDefault();
    setApiError(null);
    setSuccessMessage(null);

    if (!validateForm()) {
      return;
    }

    setSaving(true);

    try {
      await updateTraineeProfile(form);
      setSuccessMessage("✅ Perfil de Trainee actualizado exitosamente.");
      setTimeout(() => navigate(`/trainees/${username}`), 1500);
    } catch (err) {
      setApiError("❌ Error al guardar los cambios. Verifique los datos e intente de nuevo.");
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900 p-4">
      <div className="bg-white p-8 rounded-xl shadow-2xl w-full max-w-lg">
        <h2 className="text-3xl font-extrabold text-center mb-6 text-gray-800">
          ✏️ Editar Perfil de Trainee
        </h2>

        {/* --- Mensajes de Feedback --- */}
        {apiError && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg mb-6 font-medium" role="alert">
            {apiError}
          </div>
        )}
        
        {successMessage && (
          <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded-lg mb-6 font-medium" role="alert">
            {successMessage}
          </div>
        )}
        {/* --------------------------- */}
        
        {loading ? (
            // Loader de carga inicial
            <div className="flex items-center justify-center py-10 text-blue-600">
                <span className="animate-spin text-3xl mr-3">🌀</span>
                <p className="text-xl font-semibold">Cargando perfil...</p>
            </div>
        ) : (
            <form onSubmit={handleSubmit}>
                {/* Campo de Usuario (solo lectura) */}
                <div className="mb-4">
                    <label className="text-sm font-medium text-gray-700 block mb-1">Usuario</label>
                    <input
                        type="text"
                        value={form.username}
                        readOnly
                        className="w-full p-3 border rounded-lg bg-gray-100 cursor-not-allowed text-gray-600 font-mono"
                    />
                </div>
                
                {/* Nombre y Apellido */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <FormInput 
                        name="firstName" 
                        placeholder="Nombre" 
                        form={form} 
                        handleChange={handleChange} 
                        validationError={validationError} 
                    />
                    <FormInput 
                        name="lastName" 
                        placeholder="Apellido" 
                        form={form} 
                        handleChange={handleChange} 
                        validationError={validationError} 
                    />
                </div>
                
                {/* Fecha de Nacimiento y Dirección */}
                <FormInput 
                    name="dateOfBirth" 
                    type="date" 
                    label="Fecha de Nacimiento" 
                    form={form} 
                    handleChange={handleChange} 
                    validationError={validationError} 
                />
                <FormInput 
                    name="address" 
                    placeholder="Dirección Postal Completa" 
                    form={form} 
                    handleChange={handleChange} 
                    validationError={validationError} 
                />
                
                {/* Checkbox Activo */}
                <div className="flex items-center space-x-4 mb-6 mt-4 p-3 border border-gray-300 rounded-lg bg-gray-50">
                    <input
                        type="checkbox"
                        name="isActive"
                        id="isActive"
                        checked={form.isActive}
                        onChange={handleChange}
                        className="w-5 h-5 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500"
                    />
                    <label htmlFor="isActive" className="text-base font-medium text-gray-700">
                        Trainee Activo (Puede tomar entrenamientos)
                    </label>
                    <span className={`px-3 py-1 text-xs font-semibold rounded-full ml-auto ${
                        form.isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                    }`}>
                        {form.isActive ? 'ACTIVO' : 'INACTIVO'}
                    </span>
                </div>
                
                {/* Botón de Guardar */}
                <button 
                    type="submit" 
                    disabled={saving}
                    className={`w-full font-bold py-3 rounded-lg shadow-lg transition duration-200 ease-in-out mt-4 transform hover:scale-[1.005] flex items-center justify-center
                                ${saving 
                                    ? 'bg-gray-400 cursor-not-allowed' 
                                    : 'bg-blue-600 hover:bg-blue-700 text-white'}`}
                >
                    {saving ? (
                        <>
                            <span className="animate-spin mr-3">💾</span> Guardando...
                        </>
                    ) : (
                        <>
                            <span className="mr-2">✔️</span> Guardar Cambios
                        </>
                    )}
                </button>
            </form>
        )}
      </div>
    </div>
  );
}