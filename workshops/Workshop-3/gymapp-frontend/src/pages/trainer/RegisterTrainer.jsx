import { useState } from "react";
import { registerTrainer } from "../../api/trainerService";

export default function RegisterTrainer() {
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    specialization: "",
  });
  const [credentials, setCredentials] = useState(null);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await registerTrainer(form);
      setCredentials(res.data);
    } catch {
      alert("Error al registrar entrenador");
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 bg-white p-6 rounded shadow">
      <h2 className="text-xl font-bold mb-4">Registrar entrenador</h2>
      <form onSubmit={handleSubmit}>
        <input name="firstName" placeholder="Nombre" value={form.firstName} onChange={handleChange} className="w-full mb-3 p-2 border rounded" />
        <input name="lastName" placeholder="Apellido" value={form.lastName} onChange={handleChange} className="w-full mb-3 p-2 border rounded" />
        <input name="specialization" placeholder="Especialización" value={form.specialization} onChange={handleChange} className="w-full mb-3 p-2 border rounded" />
        <button type="submit" className="w-full bg-green-600 text-white p-2 rounded">Registrar</button>
      </form>

      {credentials && (
        <div className="mt-4 p-3 bg-green-100 border border-green-400 rounded">
          <p><strong>Usuario:</strong> {credentials.username}</p>
          <p><strong>Contraseña:</strong> {credentials.password}</p>
        </div>
      )}
    </div>
  );
}