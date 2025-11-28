import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getUnassignedTrainers, assignTrainers } from "../../api/traineeService";

export default function AssignTrainers() {
  const { username } = useParams();
  const [trainers, setTrainers] = useState([]);
  const [selected, setSelected] = useState([]);

  useEffect(() => {
    getUnassignedTrainers(username)
      .then((res) => setTrainers(res.data))
      .catch(() => alert("Error al cargar entrenadores"));
  }, [username]);

  const toggleTrainer = (trainerUsername) => {
    setSelected((prev) =>
      prev.includes(trainerUsername)
        ? prev.filter((u) => u !== trainerUsername)
        : [...prev, trainerUsername]
    );
  };

  const handleAssign = async () => {
    try {
      await assignTrainers({ traineeUsername: username, trainerUsernames: selected });
      alert("Entrenadores asignados");
    } catch {
      alert("Error al asignar entrenadores");
    }
  };

  return (
    <div className="max-w-2xl mx-auto mt-10 bg-white p-6 rounded shadow">
      <h2 className="text-2xl font-bold mb-4">Asignar entrenadores a {username}</h2>
      {trainers.length === 0 ? (
        <p>No hay entrenadores disponibles</p>
      ) : (
        <ul className="mb-4">
          {trainers.map((t) => (
            <li key={t.username} className="flex items-center justify-between border-b py-2">
              <span>{t.firstName} {t.lastName} — {t.specialization}</span>
              <input
                type="checkbox"
                checked={selected.includes(t.username)}
                onChange={() => toggleTrainer(t.username)}
              />
            </li>
          ))}
        </ul>
      )}
      <button
        onClick={handleAssign}
        disabled={selected.length === 0}
        className="bg-blue-600 text-white px-4 py-2 rounded"
      >
        Asignar seleccionados
      </button>
    </div>
  );
}