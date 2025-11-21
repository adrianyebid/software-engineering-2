import { Link } from "react-router-dom";

export default function NavbarTrainee() {
  const username = localStorage.getItem("user");

  return (
    <nav className="bg-blue-600 text-white px-6 py-3 flex justify-between items-center shadow">
      <Link to="/dashboard" className="font-bold text-lg">GymApp</Link>
      <div className="space-x-4">
        <Link to={`/trainees/${username}`}>Perfil</Link>
        <Link to={`/trainees/${username}/trainings`}>Entrenamientos</Link>
        <Link to="/trainings/create">Nuevo</Link>
        <Link to="/training-types">Tipos</Link>
        <Link to="/change-password">Contraseña</Link>
      </div>
    </nav>
  );
}