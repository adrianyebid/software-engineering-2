import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getTraineeProfile } from "../../api/traineeService";
import ChangeTraineeStatus from "../../components/trainee/ChangeTraineeStatus";
import DeleteTraineeButton from "../../components/trainee/DeleteTraineeButton";

export default function TraineeProfile() {
  const { username } = useParams();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const loadProfile = () => {
    setLoading(true);
    setError(null);
    getTraineeProfile(username)
      .then((res) => {
        setProfile(res.data);
        setLoading(false);
      })
      .catch((err) => {
        setError("❌ No se pudo cargar el perfil del Trainee. Intenta de nuevo.");
        setLoading(false);
        // Opcional: console.error(err);
      });
  };

  useEffect(() => {
    loadProfile();
  }, [username]);

  // --- Estados de Carga y Error Mejorados ---

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-900 text-white">
        <p className="text-xl font-semibold animate-pulse">
          Cargando perfil de {username}... 🚀
        </p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen flex flex-col items-center justify-center bg-gray-900 text-white p-4">
        <div className="bg-red-900 border border-red-700 p-6 rounded-xl shadow-xl text-center">
            <p className="text-xl font-bold text-red-300 mb-4">{error}</p>
            <button 
                onClick={() => navigate("/dashboard")}
                className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition"
            >
                Volver al Dashboard
            </button>
        </div>
      </div>
    );
  }

  // --- Renderizado del Perfil ---

  return (
    <div className="min-h-screen bg-gray-900 p-4 sm:p-8">
      <div className="max-w-4xl mx-auto bg-white p-8 rounded-xl shadow-2xl">
        
        {/* Encabezado */}
        <h2 className="text-3xl font-extrabold text-gray-800 mb-6 border-b pb-3 flex items-center">
          <span className="mr-2 text-blue-500">🏋️‍♀️</span> Perfil de Trainee: {username}
        </h2>

        {/* Sección de Información Personal */}
        <div className="mb-8 p-6 bg-gray-50 rounded-lg border">
            <h3 className="text-xl font-semibold mb-3 text-gray-700">Información Personal</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-gray-700">
                <p>
                    <strong className="font-medium text-gray-900">Nombre Completo:</strong> 
                    <span className="ml-2">{profile.firstName} {profile.lastName}</span>
                </p>
                <p>
                    <strong className="font-medium text-gray-900">Fecha de Nacimiento:</strong> 
                    <span className="ml-2">{profile.dateOfBirth}</span>
                </p>
                <p className="md:col-span-2">
                    <strong className="font-medium text-gray-900">Dirección:</strong> 
                    <span className="ml-2">{profile.address}</span>
                </p>
                <p>
                    <strong className="font-medium text-gray-900">Estado de la Cuenta:</strong> 
                    <span className={`ml-2 font-bold ${profile.isActive ? "text-green-600" : "text-red-600"}`}>
                        {profile.isActive ? "Activo ✅" : "Inactivo 🛑"}
                    </span>
                </p>
            </div>
        </div>

        {/* Sección de Acciones (Botones) */}
        <div className="flex flex-wrap gap-4 mt-8 mb-8 border-t pt-4">
            {/* Botón de Cambiar Estado (Estilizado) */}
            <ChangeTraineeStatus
                username={username}
                currentStatus={profile.isActive}
                onStatusChange={(newStatus) =>
                    setProfile({ ...profile, isActive: newStatus })
                }
                // Añadimos una clase base para estilizar el componente
                className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition"
            />
            
            <button
                onClick={() => navigate(`/trainees/${username}/edit`)}
                className="bg-gray-600 text-white px-4 py-2 rounded-lg hover:bg-gray-700 transition"
            >
                ✏️ Editar Perfil
            </button>

            {/* Botón de Eliminar (Estilizado y separado) */}
            <DeleteTraineeButton
                username={username}
                onDelete={() => navigate("/dashboard")} // Redirige al dashboard después de eliminar
                // Añadimos una clase base para estilizar el componente
                className="bg-red-600 text-white px-4 py-2 rounded-lg hover:bg-red-700 transition ml-auto"
            />
        </div>

        {/* Sección de Entrenadores Asignados */}
        <div className="mt-8 p-6 bg-blue-50 rounded-lg border border-blue-200">
          <h3 className="text-xl font-bold text-blue-700 mb-4 flex items-center">
            <span className="mr-2">👥</span> Entrenadores Asignados
          </h3>
          {profile.trainers && profile.trainers.length > 0 ? (
            <ul className="space-y-3">
              {profile.trainers.map((t) => (
                <li key={t.username} className="p-3 bg-white rounded-md shadow-sm border border-blue-100 flex justify-between items-center hover:bg-blue-50 transition">
                  <div>
                    <strong className="text-gray-800">{t.firstName} {t.lastName}</strong>
                    <span className="text-sm text-gray-500 ml-3">(@{t.username})</span>
                  </div>
                  <span className="bg-blue-100 text-blue-800 text-xs font-semibold px-2.5 py-0.5 rounded-full">
                    Especialidad: {t.specialization}
                  </span>
                </li>
              ))}
            </ul>
          ) : (
            <p className="text-gray-600">Aún no tienes entrenadores asignados.</p>
          )}
        </div>
        
      </div>
    </div>
  );
}