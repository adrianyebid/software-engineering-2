import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/authContext";

export default function NavbarTrainer() {
  const username = localStorage.getItem("user");
  const { logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await logout(); // llama al backend y limpia localStorage
      navigate("/");  // redirige al login
    } catch (err) {
      alert("Error al cerrar sesión");
    }
  };

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
        <button
          onClick={handleLogout}
          className="bg-red-500 px-3 py-1 rounded hover:bg-red-600"
        >
          Cerrar sesión
        </button>
      </div>
    </nav>
  );
}