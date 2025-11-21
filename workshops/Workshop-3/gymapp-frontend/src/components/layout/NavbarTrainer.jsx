import { Link } from "react-router-dom";

export default function NavbarTrainer() {
  const username = localStorage.getItem("user");

  return (
    <nav className="bg-green-700 text-white px-6 py-3 flex justify-between items-center shadow">
      <Link to="/trainer-dashboard" className="font-bold text-lg">GymApp</Link>
      <div className="space-x-4">
        <Link to={`/trainers/${username}`}>Perfil</Link>
        <Link to={`/trainers/${username}/trainees`}>Trainees</Link>
        <Link to={`/trainers/${username}/trainings`}>Entrenamientos</Link>
        <Link to="/trainings/create">Nuevo</Link>
        <Link to="/training-types">Tipos</Link>
        <Link to="/change-password">Contraseña</Link>
      </div>
    </nav>
  );
}