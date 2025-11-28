import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getTraineeProfile, updateTraineeProfile } from "../../api/traineeService";

export default function EditTraineeProfile() {
  const { username } = useParams();
  const navigate = useNavigate();
  const [form, setForm] = useState({
    username: "",
    firstName: "",
    lastName: "",
    dateOfBirth: "",
    address: "",
    isActive: true,
  });

  useEffect(() => {
    getTraineeProfile(username).then((res) => {
      const { firstName, lastName, dateOfBirth, address, isActive } = res.data;
      setForm({ username, firstName, lastName, dateOfBirth, address, isActive });
    });
  }, [username]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm({ ...form, [name]: type === "checkbox" ? checked : value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await updateTraineeProfile(form);
      alert("Perfil actualizado");
      navigate(`/trainees/${username}`);
    } catch {
      alert("Error al actualizar");
    }
  };

  return (
    <div className="max-w-xl mx-auto mt-10 bg-white p-6 rounded shadow">
      <h2 className="text-2xl font-bold mb-4">Editar perfil</h2>
      <form onSubmit={handleSubmit}>
        <input name="firstName" value={form.firstName} onChange={handleChange} placeholder="Nombre" className="w-full mb-3 p-2 border rounded" />
        <input name="lastName" value={form.lastName} onChange={handleChange} placeholder="Apellido" className="w-full mb-3 p-2 border rounded" />
        <input name="dateOfBirth" type="date" value={form.dateOfBirth} onChange={handleChange} className="w-full mb-3 p-2 border rounded" />
        <input name="address" value={form.address} onChange={handleChange} placeholder="Dirección" className="w-full mb-3 p-2 border rounded" />
        <label className="block mb-3">
          <input type="checkbox" name="isActive" checked={form.isActive} onChange={handleChange} className="mr-2" />
          Activo
        </label>
        <button type="submit" className="w-full bg-blue-600 text-white p-2 rounded">Guardar cambios</button>
      </form>
    </div>
  );
}