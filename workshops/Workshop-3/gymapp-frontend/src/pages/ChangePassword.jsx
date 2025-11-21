import { useState } from "react";
import { useAuth } from "../hooks/useAuth";

export default function ChangePassword() {
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const { changePassword } = useAuth();

  const handleChange = async (e) => {
    e.preventDefault();
    try {
      await changePassword(oldPassword, newPassword);
      alert("Contraseña actualizada");
    } catch (err) {
      alert("Error al cambiar la contraseña");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <form onSubmit={handleChange} className="bg-white p-6 rounded shadow-md w-80">
        <h2 className="text-xl font-bold mb-4">Cambiar contraseña</h2>
        <input
          type="password"
          placeholder="Contraseña actual"
          value={oldPassword}
          onChange={(e) => setOldPassword(e.target.value)}
          className="w-full mb-3 p-2 border rounded"
        />
        <input
          type="password"
          placeholder="Nueva contraseña"
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
          className="w-full mb-3 p-2 border rounded"
        />
        <button type="submit" className="w-full bg-green-500 text-white p-2 rounded">
          Actualizar
        </button>
      </form>
    </div>
  );
}