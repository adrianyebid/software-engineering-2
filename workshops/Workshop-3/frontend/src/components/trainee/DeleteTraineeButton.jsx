import { useNavigate } from "react-router-dom";
import { deleteTrainee } from "../../api/traineeService";

export default function DeleteTraineeButton({ username }) {
  const navigate = useNavigate();

  const handleDelete = async () => {
    if (confirm("¿Seguro que deseas eliminar este perfil?")) {
      try {
        await deleteTrainee(username);
        alert("Perfil eliminado");
        localStorage.removeItem("user");
        localStorage.removeItem("token");
        navigate("/");
      } catch {
        alert("Error al eliminar");
      }
    }
  };

  return (
    <button
      onClick={handleDelete}
      className="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700 transition"
    >
      Eliminar cuenta
    </button>
  );
}