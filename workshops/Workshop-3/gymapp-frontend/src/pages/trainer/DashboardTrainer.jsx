import { useNavigate } from "react-router-dom";
import DeleteTrainerButton from "../../components/trainer/DeleteTrainerButton";

export default function DashboardTrainer() {
  const username = localStorage.getItem("user");
  const navigate = useNavigate();

  const Card = ({ title, path }) => (
    <button
      onClick={() => navigate(path)}
      className="bg-white border rounded shadow p-4 text-left hover:bg-gray-50 transition"
    >
      <h3 className="text-lg font-semibold">{title}</h3>
    </button>
  );

  return (
    <div className="max-w-4xl mx-auto mt-10 px-4">
      <h1 className="text-2xl font-bold mb-6">Panel de entrenador: {username}</h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
        <Card title="Ver perfil" path={`/trainers/${username}`} />
        <Card title="Editar perfil" path={`/trainers/${username}/edit`} />
        <Card title="Trainees asignados" path={`/trainers/${username}/trainees`} />
        <Card title="Buscar entrenamientos" path={`/trainers/${username}/trainings`} />
        <Card title="Crear entrenamiento" path="/trainings/create" />
        <Card title="Tipos de entrenamiento" path="/training-types" />
        <Card title="Cambiar contraseña" path="/change-password" />
        <div className="bg-white border rounded shadow p-4">
          <h3 className="text-lg font-semibold mb-2">Eliminar cuenta</h3>
          <DeleteTrainerButton username={username} />
        </div>
      </div>
    </div>
  );
}