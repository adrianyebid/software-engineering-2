import { useState } from "react";
import { changeTraineeStatus } from "../../api/traineeService";

export default function ChangeTraineeStatus({ username, currentStatus }) {
  const [isActive, setIsActive] = useState(currentStatus);

  const handleToggle = async () => {
    try {
      await changeTraineeStatus({ username, isActive: !isActive });
      setIsActive(!isActive);
      alert("Estado actualizado");
    } catch {
      alert("Error al cambiar estado");
    }
  };

  return (
    <button onClick={handleToggle} className="bg-yellow-500 text-white px-3 py-1 rounded">
      {isActive ? "Desactivar" : "Activar"}
    </button>
  );
}