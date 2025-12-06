import { useState } from "react";
import { createTraining } from "../../api/trainingService"; 

// --- COMPONENTE AUXILIAR ---
const FormInput = ({ name, type = "text", placeholder, form, handleChange, validationError }) => {
    const isError = validationError[name];
    const isDate = type === 'date';

    return (
        <div className="mb-4">
            {isDate && (
                <label htmlFor={name} className="text-sm font-medium text-gray-700 block mb-1">
                    Fecha del Entrenamiento
                </label>
            )}

            <input
                type={type}
                name={name}
                id={name}
                placeholder={placeholder}
                value={form[name]}
                onChange={handleChange}
                className={`w-full p-3 border rounded-lg focus:outline-none focus:ring-2 transition duration-150 bg-white
                    ${isError ? "border-red-500 focus:ring-red-600" : "border-gray-300 focus:ring-blue-600"}
                    ${type === 'number'
                        ? '[appearance:textfield] [&::-webkit-inner-spin-button]:appearance-none [&::-webkit-outer-spin-button]:appearance-none'
                        : ''
                    }
                `}
            />

            {isError && (
                <p className="text-red-500 text-xs mt-1 font-semibold">{isError}</p>
            )}
        </div>
    );
};
// ---------------------------------------------

export default function CreateTraining() {
    const [form, setForm] = useState({
        traineeUsername: "",
        trainerUsername: "",
        trainingName: "",
        trainingDate: "",
        trainingDuration: "",
    });

    const [loading, setLoading] = useState(false);
    const [apiError, setApiError] = useState(null);
    const [successMessage, setSuccessMessage] = useState(null);
    const [validationError, setValidationError] = useState({});

    const handleChange = (e) => {
        const { name, value } = e.target;

        setForm(prev => ({ ...prev, [name]: value }));
        setValidationError(prev => ({ ...prev, [name]: null }));
        setApiError(null);
        setSuccessMessage(null);
    };

    const validateForm = () => {
        let errors = {};
        
        if (!form.traineeUsername.trim()) errors.traineeUsername = "El usuario del Trainee es obligatorio.";
        if (!form.trainerUsername.trim()) errors.trainerUsername = "El usuario del Trainer es obligatorio.";
        
        if (!form.trainingName.trim() || form.trainingName.length < 3) {
            errors.trainingName = "El nombre es obligatorio (mín. 3 caracteres).";
        }

        if (!form.trainingDate) errors.trainingDate = "La fecha es obligatoria.";

        const duration = parseInt(form.trainingDuration);
        if (isNaN(duration) || duration <= 0 || duration > 360) {
            errors.trainingDuration = "La duración debe ser un número positivo (entre 1 y 360 minutos).";
        }

        setValidationError(errors);
        return Object.keys(errors).length === 0;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        setApiError(null);
        setSuccessMessage(null);

        if (!validateForm()) return;

        setLoading(true);

        try {
            await createTraining({
                ...form,
                trainingDuration: parseInt(form.trainingDuration),
            });

            setSuccessMessage("✅ Entrenamiento creado exitosamente.");

            setForm({
                traineeUsername: "",
                trainerUsername: "",
                trainingName: "",
                trainingDate: "",
                trainingDuration: "",
            });

        } catch (err) {
            const message = err.response?.data?.message || err.message || "Error de servidor.";
            setApiError(`❌ Error al crear el entrenamiento: ${message}`);
        } finally {
            setLoading(false);
        }
    };

    const commonProps = { form, handleChange, validationError };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-900 p-4 font-sans">
            <div className="bg-white p-8 rounded-xl shadow-2xl w-full max-w-lg">

                <h2 className="text-3xl font-extrabold text-center mb-6 text-gray-800">
                    ➕ Registrar Entrenamiento
                </h2>

                {apiError && (
                    <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg mb-6 font-medium">
                        {apiError}
                    </div>
                )}

                {successMessage && (
                    <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded-lg mb-6 font-medium">
                        {successMessage}
                    </div>
                )}

                <form onSubmit={handleSubmit} className="space-y-4">
                    <FormInput name="traineeUsername" placeholder="Usuario del Trainee (Socio)" {...commonProps} />
                    <FormInput name="trainerUsername" placeholder="Usuario del Trainer (Entrenador)" {...commonProps} />
                    <FormInput name="trainingName" placeholder="Nombre o Título del Entrenamiento" {...commonProps} />

                    <div className="grid grid-cols-2 gap-4">
                        <FormInput name="trainingDate" type="date" {...commonProps} />
                        <FormInput name="trainingDuration" type="number" placeholder="Duración (minutos)" {...commonProps} />
                    </div>

                    <button
                        type="submit"
                        disabled={loading}
                        className={`w-full font-bold py-3 rounded-lg shadow-lg transition duration-200 transform hover:scale-[1.005] flex items-center justify-center
                            ${loading ? "bg-gray-400 cursor-not-allowed" : "bg-blue-600 hover:bg-blue-700 text-white"}`}
                    >
                        {loading ? (
                            <>
                                <span className="animate-spin mr-3">🔄</span> Registrando...
                            </>
                        ) : (
                            <>
                                <span className="mr-2">📝</span> Crear Entrenamiento
                            </>
                        )}
                    </button>
                </form>
            </div>
        </div>
    );
}
