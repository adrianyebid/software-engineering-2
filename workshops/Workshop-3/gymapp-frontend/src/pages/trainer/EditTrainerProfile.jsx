import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getTrainerProfile, updateTrainerProfile } from "../../api/trainerService";

export default function EditTrainerProfile() {
  const { username } = useParams();
  const navigate = useNavigate();
  const [form, setForm] = useState({
    username: "",
    firstName: "",
    lastName: "",
    specialization: "",
    isActive: true,
  });

  useEffect(() => {
    getTrainerProfile(username).then((res) => {
      const { firstName, lastName, specialization, isActive } = res.data;
      setForm({ username, firstName, lastName, specialization, isActive });
    });
  }, [username]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm({ ...form, [name]: type === "checkbox" ? checked : value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await updateTrainerProfile(form);
      alert("Perfil actualizado");
      navigate(`/trainers/${username}`);
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
        <input name="specialization" value={form.specialization} onChange={handleChange} placeholder="Especialización" className="w-full mb-3 p-2 border rounded" />
        <label className="block mb-3">
          <input type="checkbox" name="isActive" checked={form.isActive} onChange={handleChange} className="mr-2" />
          Activo
        </label>
        <button type="submit" className="w-full bg-blue-600 text-white p-2 rounded">Guardar cambios</button>
      </form>
    </div>
  );
}