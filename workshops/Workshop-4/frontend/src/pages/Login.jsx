import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../context/authContext.jsx";
// Icono para el mensaje de error (simulamos usar un paquete como Lucide o Heroicons)
// En un proyecto real, necesitarías instalar un paquete de iconos.
// Aquí usaremos una clase para simular el espacio del ícono.

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null); // Nuevo estado para el mensaje de error
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError(null); // Limpiar error anterior

    try {
      await login(username, password);
      const role = localStorage.getItem("role");

      // Limpiar las credenciales tras un login exitoso (opcional, pero buena práctica)
      setUsername("");
      setPassword("");

      if (role === "ROLE_TRAINER") {
        navigate("/trainer-dashboard");
      } else {
        navigate("/dashboard");
      }
    } catch (err) {
      // Mostrar el mensaje de error de forma más estética
      setError("Credenciales inválidas. Por favor, revisa tu usuario y contraseña.");
      // Opcional: console.error(err);
    }
  };

  return (
    // Fondo más oscuro para un look más "Gym" y centrado total
    <div className="min-h-screen flex items-center justify-center bg-gray-900 p-4">
      
      <form
        onSubmit={handleLogin}
        // Estilos del formulario: fondo claro, esquinas más redondeadas, sombra elegante
        className="bg-white p-8 rounded-xl shadow-2xl w-full max-w-sm md:max-w-md lg:max-w-lg"
      >
        <h2 className="text-3xl font-extrabold text-center mb-6 text-gray-800">
          Gymapp - Iniciar Sesión
        </h2>

        {/* --- Mensaje de Error Bonito --- */}
        {error && (
          <div
            className="flex items-center bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4"
            role="alert"
          >
            {/* Simulación de Icono de Alerta */}
            <span className="inline-block w-4 h-4 mr-2">
              <svg fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clipRule="evenodd"></path></svg>
            </span>
            <span className="block sm:inline font-medium text-sm">{error}</span>
          </div>
        )}
        {/* ------------------------------- */}

        {/* Campo de Usuario */}
        <input
          type="text"
          placeholder="👤 Nombre de Usuario"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          // Estilos de input mejorados: foco con color de acento
          className="w-full mb-4 p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600 transition duration-150"
          aria-label="Nombre de Usuario"
        />

        {/* Campo de Contraseña */}
        <input
          type="password"
          placeholder="🔒 Contraseña"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          // Estilos de input mejorados: foco con color de acento
          className="w-full mb-6 p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600 transition duration-150"
          aria-label="Contraseña"
        />

        {/* Botón de Entrar */}
        <button
          type="submit"
          // Botón con color de acento fuerte, hover, y estilos de fitness
          className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 rounded-lg shadow-md transition duration-200 ease-in-out transform hover:scale-[1.01]"
        >
          Entrar
        </button>

        {/* Enlaces de Registro */}
        <div className="text-center mt-6 pt-4 border-t border-gray-100">
          <p className="text-gray-600 text-sm mb-2">¿Aún no eres parte del equipo?</p>
          
          <Link
            to="/register"
            // Enlace con color de acento
            className="block text-sm text-blue-600 hover:text-blue-800 font-semibold mb-1"
          >
            ➡️ Registrar como Trainee (Socio)
          </Link>

          <Link
            to="/register-trainer"
            // Enlace con color de acento
            className="block text-sm text-blue-600 hover:text-blue-800 font-semibold"
          >
            ➡️ Registrar como Trainer (Entrenador)
          </Link>
        </div>
      </form>
    </div>
  );
}