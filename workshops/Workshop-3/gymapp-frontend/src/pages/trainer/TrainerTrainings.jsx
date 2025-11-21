import { useState } from "react";
import { getTrainerTrainings } from "../../api/trainerService";

export default function TrainerTrainings() {
  const [filters, setFilters] = useState({
    username: "",
    periodFrom: "",
    periodTo: "",
    traineeName: "",
  });
  const [results, setResults] = useState([]);

  const handleChange = (e) => {
    setFilters({ ...filters, [e.target.name]: e.target.value });
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    try {
      const res = await getTrainerTrainings(filters);
      setResults(res.data);
    } catch {
      alert("Error al buscar entrenamientos");
    }
  };

  return (
    <div className="max-w-3xl mx-auto mt-10 bg-white p-6 rounded shadow">
      <h2 className="text-2xl font-bold mb-4">Buscar entrenamientos</h2>
      <form onSubmit={handleSearch} className="grid grid-cols-2 gap-4 mb-6">
        <input name="username" placeholder="Usuario" value={filters.username} onChange={handleChange} className="p-2 border rounded" />
        <input name="traineeName" placeholder="Trainee" value={filters.traineeName} onChange={handleChange} className="p-2 border rounded" />
        <input name="periodFrom" type="date" value={filters.periodFrom} onChange={handleChange} className="p-2 border rounded" />
        <input name="periodTo" type="date" value={filters.periodTo} onChange={handleChange} className="p-2 border rounded" />
        <button type="submit" className="col-span-2 bg-blue-600 text-white p-2 rounded">Buscar</button>
      </form>

      {results.length > 0 && (
        <ul className="space-y-3">
          {results.map((t, i) => (
            <li key={i} className="border p-3 rounded">
              <p><strong>{t.trainingName}</strong> — {t.trainingType}</p>
              <p>{t.trainingDate} • {t.trainingDuration} min • {t.traineeName}</p>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}