import { useNavigate } from "react-router-dom";
import DeleteTraineeButton from "../../components/trainee/DeleteTraineeButton";
import NotesPanel from "../../components/NotesPanel";

export default function DashboardTrainee() {
  const username = localStorage.getItem("user");
  const navigate = useNavigate();

  // Definición de las tarjetas de navegación con íconos
  const DASHBOARD_CARDS = [
    { title: "Ver Mi Perfil", path: `/trainees/${username}`, icon: "👤" },
    { title: "Editar Mi Perfil", path: `/trainees/${username}/edit`, icon: "⚙️" },
    { title: "Mis Entrenamientos", path: `/trainees/${username}/trainings`, icon: "🏃‍♀️" },
    { title: "Asignar Entrenador", path: `/trainees/${username}/assign`, icon: "🤝" },
    { title: "Crear Nuevo Plan", path: "/trainings/create", icon: "➕" },
    { title: "Tipos de Entrenamiento", path: "/training-types", icon: "🏷️" },
    { title: "Seguridad (Contraseña)", path: "/change-password", icon: "🔑" },
  ];

  // Componente Card mejorado con estilos de fitness (color azul para Trainee)
  const Card = ({ title, path, icon }) => (
    <button
      onClick={() => navigate(path)}
      // Estilos de la tarjeta: fondo blanco, esquinas redondeadas, sombra y efecto hover
      className="flex flex-col items-start justify-center text-left bg-white p-6 rounded-xl shadow-lg border border-gray-100 
                 hover:shadow-2xl hover:border-blue-500 transition duration-300 transform hover:scale-[1.02] min-h-[120px]"
    >
      <div className="text-4xl mb-2">{icon}</div> {/* Ícono grande */}
      <h3 className="text-lg font-bold text-gray-800">{title}</h3>
      <p className="text-sm text-gray-500 mt-1">Navegar a la sección</p>
    </button>
  );

  return (
    // Diseño consistente: Fondo oscuro con padding superior
    <div className="min-h-screen bg-gray-900 text-white p-4 sm:p-8">
      <div className="max-w-6xl mx-auto">
        
        {/* Encabezado */}
        <header className="mb-8 p-4 bg-gray-800 rounded-xl shadow-inner">
            <h1 className="text-3xl font-extrabold text-blue-400">
                Panel de Socio: <span className="text-white">{username}</span>
            </h1>
            <p className="text-gray-400 mt-1">Aquí puedes gestionar tus entrenamientos y tu perfil.</p>
        </header>

        {/* Sección de Navegación (Tarjetas) */}
        <section className="mb-10">
            <h2 className="text-2xl font-bold mb-4 text-blue-300">Navegación Rápida</h2>
            <div className="grid grid-cols-2 md:grid-cols-3 gap-6">
                {DASHBOARD_CARDS.map((card) => (
                    <Card key={card.title} {...card} />
                ))}
            </div>
        </section>

        {/* Sección de Notas y Opciones Peligrosas */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
            
            {/* Notas (Panel) - Ocupa 2/3 en desktop */}
            <div className="lg:col-span-2">
                <NotesPanel username={username} /> 
            </div>
            
            {/* Eliminar Cuenta (Zona de Peligro) - Ocupa 1/3 en desktop */}
            <div className="lg:col-span-1">
                <div className="bg-red-900 border border-red-700 rounded-xl shadow-xl p-6 h-full flex flex-col justify-between">
                    <div>
                        <h3 className="text-xl font-bold mb-2 text-red-300">🛑 Zona de Peligro</h3>
                        <p className="text-sm text-red-200 mb-4">
                            Eliminará tu cuenta y tu historial de entrenamientos de forma permanente.
                        </p>
                    </div>
                    {/* Botón de eliminación estilizado */}
                    <DeleteTraineeButton username={username} 
                        className="w-full bg-red-600 text-white font-bold py-3 rounded-lg hover:bg-red-700 transition duration-150 shadow-lg mt-4" 
                    />
                </div>
            </div>

        </div>
      </div>
    </div>
  );
}