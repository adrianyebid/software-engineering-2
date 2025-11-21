import { useState } from "react";
import { changeTrainerStatus } from "../../api/trainerService";

export default function ChangeTrainerStatus({ username, currentStatus }) {
  const [isActive, setIsActive] = useState(currentStatus);

  const handleToggle = async () => {
    try {
      await changeTrainerStatus({ username, isActive: !isActive });
      setIsActive(!isActive);
      alert("Estado actualizado");
    } catch {
      alert("Error al cambiar estado");
    }
  };

  return (
    <button
      onClick={handleToggle}
      className={`px-4 py-2 rounded text-white ${
        isActive ? "bg-yellow-500 hover:bg-yellow-600" : "bg-green-600 hover:bg-green-700"
      }`}
    >
      {isActive ? "Desactivar" : "Activar"}
    </button>
  );
}