import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getAssignedTrainees } from "../../api/trainerService";

export default function AssignedTrainees() {
  const { username } = useParams();
  const [trainees, setTrainees] = useState([]);

  useEffect(() => {
    getAssignedTrainees(username)
      .then((res) => setTrainees(res.data))
      .catch(() => alert("Error al cargar trainees"));
  }, [username]);

  return (
    <div className="max-w-2xl mx-auto mt-10 bg-white p-6 rounded shadow">
      <h2 className="text-2xl font-bold mb-4">Trainees asignados a {username}</h2>
      {trainees.length === 0 ? (
        <p>No hay trainees asignados</p>
      ) : (
        <ul className="list-disc list-inside">
          {trainees.map((t) => (
            <li key={t.username}>
              {t.firstName} {t.lastName} ({t.username})
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}