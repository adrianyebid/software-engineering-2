import useTrainingTypes from "../../hooks/useTrainingTypes";

export default function TrainingTypeList() {
  const { types, loading, error } = useTrainingTypes(); // Asumiendo que el hook devuelve 'error'

  return (
    <div className="min-h-screen bg-gray-900 p-4 sm:p-8">
      <div className="max-w-3xl mx-auto bg-white p-8 rounded-xl shadow-2xl">
        
        {/* Encabezado */}
        <h2 className="text-3xl font-extrabold text-gray-800 mb-6 border-b pb-3 flex items-center">
          <span className="mr-2 text-blue-500">🔖</span> Tipos de Entrenamiento Disponibles
        </h2>

        {/* --- Manejo de Estados (Carga, Error, Vacío) --- */}
        {loading ? (
          <div className="flex items-center justify-center py-10 text-blue-600">
            <span className="animate-spin text-3xl mr-3">🌀</span>
            <p className="text-xl font-semibold">Cargando la lista de tipos de entrenamiento...</p>
          </div>
        ) : error ? (
           <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg font-medium">
             ❌ Error al cargar los tipos de entrenamiento. Por favor, inténtalo de nuevo más tarde.
           </div>
        ) : types.length === 0 ? (
          <div className="text-center py-10 border-dashed border-2 border-gray-300 rounded-xl bg-gray-50">
            <p className="text-xl font-medium text-gray-600">
              No hay tipos de entrenamiento registrados actualmente.
            </p>
            <p className="text-gray-500 mt-2">
                Ponte en contacto con el administrador para añadir nuevas categorías.
            </p>
          </div>
        ) : (
          /* --- Lista de Tipos de Entrenamiento --- */
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 mt-6">
            {types.map((t) => (
              <div 
                key={t.id} 
                className="bg-blue-50 border border-blue-200 p-4 rounded-lg shadow-md transition duration-200 hover:shadow-lg hover:border-blue-500"
              >
                <p className="text-lg font-semibold text-blue-800">
                  {t.name}
                </p>
                {/* Opcional: Si el tipo tuviera una descripción, iría aquí */}
                {/* <p className="text-sm text-gray-600 mt-1">Definición corta...</p> */}
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}