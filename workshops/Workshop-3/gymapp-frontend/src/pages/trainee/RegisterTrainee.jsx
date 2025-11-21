import { useState } from "react";
import { registerTrainee } from "../../api/traineeService";

export default function RegisterTrainee() {
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    dateOfBirth: "",
    address: "",
  });
  const [credentials, setCredentials] = useState(null);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await registerTrainee({
        ...form,
        dateOfBirth: form.dateOfBirth || null,
      });
      setCredentials(res.data);
    } catch (err) {
      alert("Error al registrar usuario");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <form onSubmit={handleSubmit} className="bg-white p-6 rounded shadow-md w-96">
        <h2 className="text-xl font-bold mb-4">Registro de nuevo usuario</h2>
        <input
          type="text"
          name="firstName"
          placeholder="Nombre"
          value={form.firstName}
          onChange={handleChange}
          className="w-full mb-3 p-2 border rounded"
        />
        <input
          type="text"
          name="lastName"
          placeholder="Apellido"
          value={form.lastName}
          onChange={handleChange}
          className="w-full mb-3 p-2 border rounded"
        />
        <input
          type="date"
          name="dateOfBirth"
          value={form.dateOfBirth}
          onChange={handleChange}
          className="w-full mb-3 p-2 border rounded"
        />
        <input
          type="text"
          name="address"
          placeholder="Dirección"
          value={form.address}
          onChange={handleChange}
          className="w-full mb-3 p-2 border rounded"
        />
        <button type="submit" className="w-full bg-green-600 text-white p-2 rounded">
          Registrar
        </button>

        {credentials && (
          <div className="mt-4 p-3 bg-green-100 border border-green-400 rounded">
            <p><strong>Usuario:</strong> {credentials.username}</p>
            <p><strong>Contraseña:</strong> {credentials.password}</p>
          </div>
        )}
      </form>
    </div>
  );
}