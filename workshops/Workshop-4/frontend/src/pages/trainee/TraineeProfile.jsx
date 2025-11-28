import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getTraineeProfile } from "../../api/traineeService";
import ChangeTraineeStatus from "../../components/trainee/ChangeTraineeStatus";
import DeleteTraineeButton from "../../components/trainee/DeleteTraineeButton";

export default function TraineeProfile() {
  const { username } = useParams();
  const [profile, setProfile] = useState(null);
  const navigate = useNavigate();

  const loadProfile = () => {
    getTraineeProfile(username)
      .then((res) => setProfile(res.data))
      .catch(() => alert("No se pudo cargar el perfil"));
  };

  useEffect(() => {
    loadProfile();
  }, [username]);

  if (!profile) return <p className="text-center mt-10">Cargando perfil...</p>;

  return (
    <div className="max-w-2xl mx-auto mt-10 bg-white p-6 rounded shadow">
      <h2 className="text-2xl font-bold mb-4">Perfil de {username}</h2>
      <p><strong>Nombre:</strong> {profile.firstName} {profile.lastName}</p>
      <p><strong>Fecha de nacimiento:</strong> {profile.dateOfBirth}</p>
      <p><strong>Dirección:</strong> {profile.address}</p>
      <p><strong>Estado:</strong> {profile.isActive ? "Activo" : "Inactivo"}</p>

      <div className="flex gap-4 mt-4">
        <ChangeTraineeStatus
          username={username}
          currentStatus={profile.isActive}
          onStatusChange={(newStatus) =>
            setProfile({ ...profile, isActive: newStatus })
          }
        />

        <DeleteTraineeButton
          username={username}
          onDelete={() => navigate("/")}
        />
      </div>

      <h3 className="mt-4 font-semibold">Entrenadores asignados:</h3>
      <ul className="list-disc list-inside">
        {profile.trainers.map((t) => (
          <li key={t.username}>
            {t.firstName} {t.lastName} — {t.specialization}
          </li>
        ))}
      </ul>
    </div>
  );
}
