import { useState } from "react";
import { registerTrainer } from "../../api/trainerService";
import { Link, useNavigate } from "react-router-dom";

const SPECIALIZATIONS = [
  { id: 1, name: "Cardio" },
  { id: 2, name: "Strength" },
  { id: 3, name: "Flexibility" },
  { id: 4, name: "HIIT" },
  { id: 5, name: "Yoga" }
];

export default function RegisterTrainer() {
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    specialization: "",
  });

  const [credentials, setCredentials] = useState(null);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await registerTrainer(form);
      setCredentials(res.data);

      setTimeout(() => navigate("/"), 5000);
    } catch {
      alert("Error al registrar entrenador");
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 bg-white p-6 rounded shadow">
      <h2 className="text-xl font-bold mb-4">Registrar entrenador</h2>

      <form onSubmit={handleSubmit}>
        <input
          name="firstName"
          placeholder="Nombre"
          value={form.firstName}
          onChange={handleChange}
          className="w-full mb-3 p-2 border rounded"
        />

        <input
          name="lastName"
          placeholder="Apellido"
          value={form.lastName}
          onChange={handleChange}
          className="w-full mb-3 p-2 border rounded"
        />

        {/* SELECT en lugar de input */}
        <select
          name="specialization"
          value={form.specialization}
          onChange={handleChange}
          className="w-full mb-3 p-2 border rounded"
        >
          <option value="">Selecciona una especialización</option>
          {SPECIALIZATIONS.map((s) => (
            <option key={s.id} value={s.name}>
              {s.name}
            </option>
          ))}
        </select>

        <button
          type="submit"
          className="w-full bg-green-600 text-white p-2 rounded"
        >
          Registrar
        </button>

        <p className="mt-3 text-sm text-center">
          ¿Ya tienes cuenta?{" "}
          <Link to="/" className="text-blue-600">
            Inicia sesión
          </Link>
        </p>
      </form>

      {credentials && (
        <div className="mt-4 p-3 bg-green-100 border border-green-400 rounded">
          <p><strong>Usuario:</strong> {credentials.username}</p>
          <p><strong>Contraseña:</strong> {credentials.password}</p>
          <p className="text-xs mt-2 text-gray-700">
            Serás redirigido al login en 5 segundos...
          </p>
        </div>
      )}
    </div>
  );
}
