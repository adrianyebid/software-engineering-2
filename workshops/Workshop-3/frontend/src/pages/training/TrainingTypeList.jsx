import useTrainingTypes from "../../hooks/useTrainingTypes";

export default function TrainingTypeList() {
  const { types, loading } = useTrainingTypes();

  return (
    <div className="max-w-2xl mx-auto mt-10 bg-white p-6 rounded shadow">
      <h2 className="text-2xl font-bold mb-4">Tipos de entrenamiento disponibles</h2>
      {loading ? (
        <p className="text-gray-500">Cargando tipos...</p>
      ) : types.length === 0 ? (
        <p className="text-gray-500">No hay tipos registrados.</p>
      ) : (
        <ul className="list-disc list-inside space-y-2">
          {types.map((t) => (
            <li key={t.id} className="text-gray-800">
              {t.name}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}