import { useState } from "react";
import { getTraineeTrainings } from "../../api/traineeService";

export default function TraineeTrainings() {
  const [filters, setFilters] = useState({
    username: "",
    periodFrom: "",
    periodTo: "",
    trainerUsername: "",
    trainingType: "",
  });
  
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [searched, setSearched] = useState(false); // Para saber si ya se ejecutó una búsqueda

  const trainingTypes = ["Cardio", "Strength", "Flexibility", "HIIT", "Yoga"];

  const handleChange = (e) => {
    setFilters({ ...filters, [e.target.name]: e.target.value });
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSearched(true);
    setResults([]);

    try {
      const res = await getTraineeTrainings(filters);
      setResults(res.data);
    } catch (err) {
      setError("❌ Error al buscar entrenamientos. Verifica la conexión o los filtros.");
    } finally {
      setLoading(false);
    }
  };

  const handleReset = () => {
    setFilters({
      username: "",
      periodFrom: "",
      periodTo: "",
      trainerUsername: "",
      trainingType: "",
    });
    setResults([]);
    setError(null);
    setSearched(false);
  };

  // Helper para mostrar campos de fecha con etiqueta
  const DateInput = ({ name, placeholder, value }) => (
    <div className="flex flex-col">
      <label htmlFor={name} className="text-xs font-semibold text-gray-600 mb-1">
        {placeholder}
      </label>
      <input
        name={name}
        id={name}
        type="date"
        value={value}
        onChange={handleChange}
        className="p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 transition duration-150"
      />
    </div>
  );

  return (
    <div className="min-h-screen bg-gray-900 p-4 sm:p-8">
      <div className="max-w-4xl mx-auto bg-white p-8 rounded-xl shadow-2xl">
        
        {/* Encabezado */}
        <h2 className="text-3xl font-extrabold text-gray-800 mb-6 border-b pb-3 flex items-center">
          <span className="mr-2 text-blue-500">🗓️</span> Historial de Entrenamientos
        </h2>

        {/* --- Formulario de Búsqueda --- */}
        <form onSubmit={handleSearch} className="space-y-6 mb-8 p-6 border border-gray-200 rounded-xl bg-gray-50 shadow-inner">
            <h3 className="text-xl font-bold text-gray-700 mb-4">Filtros de Búsqueda</h3>
            
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                {/* Usuario y Entrenador */}
                <input
                    name="username"
                    placeholder="Usuario del Trainee (ej. 'juanperez')"
                    value={filters.username}
                    onChange={handleChange}
                    className="p-3 border rounded-lg focus:ring-2 focus:ring-blue-500 transition duration-150 col-span-1 md:col-span-2"
                />
                <input
                    name="trainerUsername"
                    placeholder="Usuario del Entrenador"
                    value={filters.trainerUsername}
                    onChange={handleChange}
                    className="p-3 border rounded-lg focus:ring-2 focus:ring-blue-500 transition duration-150"
                />
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                {/* Tipo de Entrenamiento */}
                <select
                    name="trainingType"
                    value={filters.trainingType}
                    onChange={handleChange}
                    className="p-3 border rounded-lg focus:ring-2 focus:ring-blue-500 transition duration-150 bg-white"
                >
                    <option value="">Todos los tipos de entrenamiento</option>
                    {trainingTypes.map((type) => (
                        <option key={type} value={type}>{type}</option>
                    ))}
                </select>

                {/* Filtros de Fecha */}
                <DateInput name="periodFrom" placeholder="Desde la Fecha" value={filters.periodFrom} />
                <DateInput name="periodTo" placeholder="Hasta la Fecha" value={filters.periodTo} />
            </div>

            {/* Botones de Acción */}
            <div className="flex gap-4 pt-2">
                <button
                    type="submit"
                    disabled={loading}
                    className={`flex-grow flex items-center justify-center font-bold py-3 rounded-lg shadow-md transition duration-200 
                                ${loading 
                                    ? 'bg-gray-400 text-gray-700 cursor-not-allowed' 
                                    : 'bg-blue-600 hover:bg-blue-700 text-white transform hover:scale-[1.005]'}`}
                >
                    {loading ? (
                        <>
                            <span className="animate-spin mr-3">🔄</span> Buscando...
                        </>
                    ) : (
                        <>
                            <span className="mr-2">🔍</span> Buscar Entrenamientos
                        </>
                    )}
                </button>
                <button
                    type="button"
                    onClick={handleReset}
                    className="w-1/4 bg-gray-500 hover:bg-gray-600 text-white font-bold py-3 rounded-lg shadow-md transition duration-200"
                >
                    Limpiar
                </button>
            </div>
        </form>

        {/* --- Resultados y Feedback --- */}
        {error && (
            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg mb-6 font-medium">
                {error}
            </div>
        )}

        {searched && !loading && results.length === 0 ? (
            <div className="text-center py-10 border-dashed border-2 border-gray-300 rounded-xl bg-gray-50">
                <p className="text-xl font-medium text-gray-600">
                    No se encontraron entrenamientos con los filtros aplicados.
                </p>
                <p className="text-gray-500 mt-2">
                    Intenta ajustar las fechas o los nombres de usuario.
                </p>
            </div>
        ) : (
            results.length > 0 && (
                <div className="mt-8">
                    <h3 className="text-xl font-bold text-gray-700 mb-4">
                        Resultados Encontrados ({results.length})
                    </h3>
                    <ul className="space-y-4">
                        {results.map((t, i) => (
                            <li 
                                key={i} 
                                className="bg-white p-5 border border-gray-200 rounded-xl shadow-md hover:shadow-lg transition duration-200"
                            >
                                <div className="flex justify-between items-start mb-2">
                                    <p className="text-lg font-bold text-blue-600">{t.trainingName}</p>
                                    <span className="text-sm font-semibold bg-blue-100 text-blue-800 px-3 py-1 rounded-full">
                                        {t.trainingType}
                                    </span>
                                </div>
                                <p className="text-gray-700 text-sm">
                                    <span className="font-medium">Trainer:</span> {t.trainerName}
                                </p>
                                <p className="text-gray-600 text-sm">
                                    <span className="font-medium">Fecha:</span> {t.trainingDate} | 
                                    <span className="font-medium ml-2">Duración:</span> {t.trainingDuration} min
                                </p>
                            </li>
                        ))}
                    </ul>
                </div>
            )
        )}
      </div>
    </div>
  );
}