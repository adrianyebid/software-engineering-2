import { useState } from "react";
// Importar funciones de servicio que se simularán
// import { createTraining } from "../../api/trainingService"; 

// --- COMPONENTE AUXILIAR EXTRAÍDO PARA ESTABILIDAD ---
// Define el componente de input de forma independiente.
const FormInput = ({ name, type = "text", placeholder, form, handleChange, validationError }) => {
	const isError = validationError[name];
	const isDate = type === 'date';

	return (
		<div className="mb-4">
			{/* Título específico para la fecha */}
			{isDate && <label htmlFor={name} className="text-sm font-medium text-gray-700 block mb-1">Fecha del Entrenamiento</label>}
			
			<input
				type={type}
				name={name}
				id={name}
				placeholder={placeholder}
				value={form[name]}
				onChange={handleChange}
				// Usar 'number' pero evitar flechas de incremento/decremento en Chrome/Firefox (estilo Tailwind)
				className={`w-full p-3 border rounded-lg focus:outline-none focus:ring-2 transition duration-150 bg-white
					${isError 
						? "border-red-500 focus:ring-red-600" 
						: "border-gray-300 focus:ring-blue-600"
					}
					${type === 'number' ? '[appearance:textfield] [&::-webkit-inner-spin-button]:appearance-none [&::-webkit-outer-spin-button]:appearance-none' : ''}
				`}
			/>
			{isError && (
				<p className="text-red-500 text-xs mt-1 font-semibold">{isError}</p>
			)}
		</div>
	);
};
// ----------------------------------------------------


export default function CreateTraining() {
	const [form, setForm] = useState({
		traineeUsername: "",
		trainerUsername: "",
		trainingName: "",
		trainingDate: "",
		trainingDuration: "", // Se convierte a número en handleSubmit
	});

	const [loading, setLoading] = useState(false);
	const [apiError, setApiError] = useState(null);
	const [successMessage, setSuccessMessage] = useState(null);
	const [validationError, setValidationError] = useState({});
	
	// Simulación de la función de API
	const createTraining = (data) => new Promise((resolve, reject) => {
		setTimeout(() => {
			if (data.traineeUsername === 'error') {
				reject(new Error("Usuario no encontrado"));
			} else {
				resolve({ status: 201 });
			}
		}, 1000);
	});


	const handleChange = (e) => {
		const { name, value } = e.target;
		
		// **CORRECCIÓN:** Usar la actualización funcional (prevForm) para garantizar que 
		// siempre se use el estado anterior correcto al actualizar, previniendo el bug.
		setForm(prevForm => ({ ...prevForm, [name]: value }));
		
		// Limpiar el error de validación al cambiar el campo
		setValidationError(prevErrors => ({ ...prevErrors, [name]: null }));
		setApiError(null);
		setSuccessMessage(null);
	};

	const validateForm = () => {
		let errors = {};
		if (!form.traineeUsername.trim()) errors.traineeUsername = "El usuario del Trainee es obligatorio.";
		if (!form.trainerUsername.trim()) errors.trainerUsername = "El usuario del Trainer es obligatorio.";
		if (!form.trainingName.trim() || form.trainingName.length < 3) errors.trainingName = "El nombre es obligatorio (mín. 3 caracteres).";
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
		
		if (!validateForm()) {
			return;
		}

		setLoading(true);

		try {
			await createTraining({
				...form,
				trainingDuration: parseInt(form.trainingDuration),
			});

			setSuccessMessage("✅ Entrenamiento creado exitosamente.");
			// Limpiar el formulario después del éxito
			setForm({
				traineeUsername: "",
				trainerUsername: "",
				trainingName: "",
				trainingDate: "",
				trainingDuration: "",
			});
		} catch (err) {
			setApiError(`❌ Error al crear el entrenamiento: ${err.message || 'Verifica los usuarios y la conexión.'}`);
		} finally {
			setLoading(false);
		}
	};
	
	// Propiedades comunes para FormInput
	const commonProps = { form, handleChange, validationError };

	return (
		// Diseño consistente: Fondo oscuro para look de gimnasio
		<div className="min-h-screen flex items-center justify-center bg-gray-900 p-4 font-sans">
			<div className="bg-white p-8 rounded-xl shadow-2xl w-full max-w-lg">
				
				<h2 className="text-3xl font-extrabold text-center mb-6 text-gray-800">
					➕ Registrar Entrenamiento
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


				<form onSubmit={handleSubmit} className="space-y-4">
					<FormInput 
						name="traineeUsername" 
						placeholder="Usuario del Trainee (Socio)" 
						{...commonProps}
					/>
					<FormInput 
						name="trainerUsername" 
						placeholder="Usuario del Trainer (Entrenador)" 
						{...commonProps}
					/>
					<FormInput 
						name="trainingName" 
						placeholder="Nombre o Título del Entrenamiento" 
						{...commonProps}
					/>
					
					<div className="grid grid-cols-2 gap-4">
						<FormInput 
							name="trainingDate" 
							type="date" 
							{...commonProps}
						/>
						<FormInput 
							name="trainingDuration" 
							type="number" 
							placeholder="Duración (minutos)" 
							{...commonProps}
						/>
					</div>
					
					<button
						type="submit"
						disabled={loading}
						className={`w-full font-bold py-3 rounded-lg shadow-lg transition duration-200 ease-in-out mt-4 transform hover:scale-[1.005] flex items-center justify-center
										${loading 
											? 'bg-gray-400 cursor-not-allowed' 
											: 'bg-blue-600 hover:bg-blue-700 text-white'}`}
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