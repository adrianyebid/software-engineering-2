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

  const trainingTypes = ["Cardio", "Strength", "Flexibility", "HIIT", "Yoga"];

  const handleChange = (e) => {
    setFilters({ ...filters, [e.target.name]: e.target.value });
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    try {
      const res = await getTraineeTrainings(filters);
      setResults(res.data);
    } catch {
      alert("Error al buscar entrenamientos");
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
  };

  return (
    <div className="max-w-3xl mx-auto mt-10 bg-white p-6 rounded shadow">
      <h2 className="text-2xl font-bold mb-4">Buscar entrenamientos</h2>
      <form onSubmit={handleSearch} className="grid grid-cols-2 gap-4 mb-6">
        <input
          name="username"
          placeholder="Usuario"
          value={filters.username}
          onChange={handleChange}
          className="p-2 border rounded"
        />
        <input
          name="trainerUsername"
          placeholder="Entrenador"
          value={filters.trainerUsername}
          onChange={handleChange}
          className="p-2 border rounded"
        />
        <select
          name="trainingType"
          value={filters.trainingType}
          onChange={handleChange}
          className="p-2 border rounded"
        >
          <option value="">Todos los tipos</option>
          {trainingTypes.map((type) => (
            <option key={type} value={type}>{type}</option>
          ))}
        </select>
        <input
          name="periodFrom"
          type="date"
          value={filters.periodFrom}
          onChange={handleChange}
          className="p-2 border rounded"
        />
        <input
          name="periodTo"
          type="date"
          value={filters.periodTo}
          onChange={handleChange}
          className="p-2 border rounded"
        />
        <button
          type="submit"
          className="bg-blue-600 text-white p-2 rounded"
        >
          Buscar
        </button>
        <button
          type="button"
          onClick={handleReset}
          className="bg-gray-400 text-white p-2 rounded"
        >
          Reset
        </button>
      </form>

      {results.length > 0 && (
        <ul className="space-y-3">
          {results.map((t, i) => (
            <li key={i} className="border p-3 rounded">
              <p><strong>{t.trainingName}</strong> — {t.trainingType}</p>
              <p>{t.trainingDate} • {t.trainingDuration} min • {t.trainerName}</p>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
