import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getTrainerProfile } from "../../api/trainerService";
import ChangeTrainerStatus from "../../components/trainer/ChangeTrainerStatus";
import DeleteTrainerButton from "../../components/trainer/DeleteTrainerButton";

export default function TrainerProfile() {
  const { username } = useParams();
  const [profile, setProfile] = useState(null);

  const loadProfile = () => {
    getTrainerProfile(username)
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
      <p><strong>Especialización:</strong> {profile.specialization}</p>
      <p><strong>Estado:</strong> {profile.isActive ? "Activo" : "Inactivo"}</p>

      <div className="flex gap-4 mt-4">
        <ChangeTrainerStatus
          username={username}
          currentStatus={profile.isActive}
          onStatusChange={(newStatus) =>
            setProfile({ ...profile, isActive: newStatus })
          }
        />

        <DeleteTrainerButton username={username} />
      </div>

      <h3 className="mt-6 font-semibold">Trainees asignados:</h3>
      <ul className="list-disc list-inside">
        {profile.trainees.map((t) => (
          <li key={t.username}>
            {t.firstName} {t.lastName} ({t.username})
          </li>
        ))}
      </ul>
    </div>
  );
}
