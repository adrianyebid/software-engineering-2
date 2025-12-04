import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getTrainerProfile } from "../../api/trainerService";
import ChangeTrainerStatus from "../../components/trainer/ChangeTrainerStatus";
import DeleteTrainerButton from "../../components/trainer/DeleteTrainerButton";

export default function TrainerProfile() {
  const { username } = useParams();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const loadProfile = () => {
    setLoading(true);
    setError(null);
    getTrainerProfile(username)
      .then((res) => {
        setProfile(res.data);
        setLoading(false);
      })
      .catch(() => {
        setError("❌ No se pudo cargar el perfil del Trainer. Intenta de nuevo.");
        setLoading(false);
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
                onClick={() => navigate("/trainer-dashboard")}
                className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition"
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
          <span className="mr-2 text-green-500">🏋️</span> Perfil de Trainer: {username}
        </h2>

        {/* Sección de Información Personal */}
        <div className="mb-8 p-6 bg-gray-50 rounded-lg border">
            <h3 className="text-xl font-semibold mb-3 text-gray-700">Información Profesional</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-gray-700">
                <p>
                    <strong className="font-medium text-gray-900">Nombre Completo:</strong> 
                    <span className="ml-2">{profile.firstName} {profile.lastName}</span>
                </p>
                <p>
                    <strong className="font-medium text-gray-900">Especialización:</strong> 
                    <span className="ml-2 bg-green-100 text-green-800 text-sm font-semibold px-2 py-1 rounded-full">
                        {profile.specialization}
                    </span>
                </p>
                <p className="md:col-span-2">
                    <strong className="font-medium text-gray-900">Estado de la Cuenta:</strong> 
                    <span className={`ml-2 font-bold ${profile.isActive ? "text-green-600" : "text-red-600"}`}>
                        {profile.isActive ? "Activo ✅" : "Inactivo 🛑"}
                    </span>
                </p>
            </div>
        </div>

        {/* Sección de Acciones (Botones) */}
        <div className="flex flex-wrap gap-4 mt-8 mb-8 border-t pt-4">
            
            <button
                onClick={() => navigate(`/trainers/${username}/edit`)}
                className="bg-gray-600 text-white px-4 py-2 rounded-lg hover:bg-gray-700 transition"
            >
                ✏️ Editar Perfil
            </button>

            {/* Botón de Cambiar Estado (Estilizado) */}
            <ChangeTrainerStatus
                username={username}
                currentStatus={profile.isActive}
                onStatusChange={(newStatus) =>
                    setProfile({ ...profile, isActive: newStatus })
                }
                // Añadimos una clase base para estilizar el componente
                className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition"
            />
            
            {/* Botón de Eliminar (Estilizado y separado) */}
            <DeleteTrainerButton 
                username={username} 
                // Añadimos una clase base para estilizar el componente
                className="bg-red-600 text-white px-4 py-2 rounded-lg hover:bg-red-700 transition ml-auto"
            />
        </div>

        {/* Sección de Trainees Asignados */}
        <div className="mt-8 p-6 bg-green-50 rounded-lg border border-green-200">
          <h3 className="text-xl font-bold text-green-700 mb-4 flex items-center">
            <span className="mr-2">👥</span> Trainees Asignados ({profile.trainees.length})
          </h3>
          {profile.trainees && profile.trainees.length > 0 ? (
            <ul className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              {profile.trainees.map((t) => (
                <li key={t.username} className="p-3 bg-white rounded-md shadow-sm border border-green-100 hover:bg-green-50 transition">
                  <strong className="text-gray-800">{t.firstName} {t.lastName}</strong>
                  <span className="text-sm text-gray-500 block">Usuario: @{t.username}</span>
                </li>
              ))}
            </ul>
          ) : (
            <p className="text-gray-600">Este entrenador aún no tiene trainees asignados.</p>
          )}
        </div>
        
      </div>
    </div>
  );
}