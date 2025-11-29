import { useState } from "react";
import { createTraining } from "../../api/trainingService";

export default function CreateTraining() {
  const [form, setForm] = useState({
    traineeUsername: "",
    trainerUsername: "",
    trainingName: "",
    trainingDate: "",
    trainingDuration: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await createTraining({
        ...form,
        trainingDuration: parseInt(form.trainingDuration),
      });
      alert("Entrenamiento creado exitosamente");
      setForm({
        traineeUsername: "",
        trainerUsername: "",
        trainingName: "",
        trainingDate: "",
        trainingDuration: "",
      });
    } catch {
      alert("Error al crear entrenamiento");
    }
  };

  return (
    <div className="max-w-xl mx-auto mt-10 bg-white p-6 rounded shadow">
      <h2 className="text-2xl font-bold mb-4">Crear entrenamiento</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <input name="traineeUsername" placeholder="Usuario del trainee" value={form.traineeUsername} onChange={handleChange} className="w-full p-2 border rounded" />
        <input name="trainerUsername" placeholder="Usuario del trainer" value={form.trainerUsername} onChange={handleChange} className="w-full p-2 border rounded" />
        <input name="trainingName" placeholder="Nombre del entrenamiento" value={form.trainingName} onChange={handleChange} className="w-full p-2 border rounded" />
        <input name="trainingDate" type="date" value={form.trainingDate} onChange={handleChange} className="w-full p-2 border rounded" />
        <input name="trainingDuration" type="number" placeholder="Duración (minutos)" value={form.trainingDuration} onChange={handleChange} className="w-full p-2 border rounded" />
        <button type="submit" className="w-full bg-blue-600 text-white p-2 rounded">Crear</button>
      </form>
    </div>
  );
}