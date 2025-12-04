import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/authContext";

export default function NavbarTrainee() {
  const [isMenuOpen, setIsMenuOpen] = useState(false); // Estado para el menú móvil
  const [logoutError, setLogoutError] = useState(null); // Estado para error de logout
  
  const username = localStorage.getItem("user");
  const { logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    setLogoutError(null);
    try {
      await logout();
      navigate("/");
    } catch (err) {
      setLogoutError("⚠️ Fallo al cerrar sesión. Inténtalo de nuevo.");
      // Opcional: console.error(err);
      // El error se mostrará debajo de la barra de navegación si implementamos
      // un componente de alerta global, por ahora solo lo guardamos en estado.
    }
  };

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  return (
    <>
      <nav className="bg-gray-800 text-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            
            {/* Logo de la aplicación */}
            <Link to="/dashboard" className="font-extrabold text-2xl tracking-wider text-blue-400 hover:text-blue-300 transition duration-150">
              <span className="mr-2">💪</span> GymTracker
            </Link>

            {/* Enlaces de Escritorio (Ocultos en móvil) */}
            <div className="hidden md:flex items-center space-x-6">
              <Link to={`/trainees/${username}`} className="text-gray-300 hover:text-white transition duration-150 flex items-center">
                <span className="mr-1">👤</span> Perfil
              </Link>
              <Link to={`/trainees/${username}/trainings`} className="text-gray-300 hover:text-white transition duration-150 flex items-center">
                <span className="mr-1">🏃‍♂️</span> Entrenamientos
              </Link>
              <Link to="/trainings/create" className="text-gray-300 hover:text-white transition duration-150 flex items-center">
                <span className="mr-1">➕</span> Nuevo
              </Link>
              <Link to="/training-types" className="text-gray-300 hover:text-white transition duration-150 flex items-center">
                <span className="mr-1">🏷️</span> Tipos
              </Link>
              <Link to="/change-password" className="text-gray-300 hover:text-white transition duration-150 flex items-center">
                <span className="mr-1">🔑</span> Contraseña
              </Link>
              <button
                onClick={handleLogout}
                className="bg-red-600 px-4 py-2 rounded-lg font-semibold hover:bg-red-700 transition duration-150 flex items-center shadow-md"
              >
                <span className="mr-1">🚪</span> Cerrar sesión
              </button>
            </div>

            {/* Botón de Menú Móvil (Oculto en escritorio) */}
            <div className="md:hidden">
              <button
                onClick={toggleMenu}
                className="inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-white hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white"
                aria-expanded={isMenuOpen}
              >
                {/* Ícono de hamburguesa o cerrar */}
                <svg className="h-6 w-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
                  {isMenuOpen ? (
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
                  ) : (
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16" />
                  )}
                </svg>
              </button>
            </div>
          </div>
        </div>

        {/* Menú Móvil (Se muestra solo en pantallas pequeñas) */}
        <div className={`md:hidden ${isMenuOpen ? 'block' : 'hidden'}`} id="mobile-menu">
          <div className="px-2 pt-2 pb-3 space-y-1 sm:px-3">
            <Link onClick={toggleMenu} to={`/trainees/${username}`} className="text-gray-300 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium">
              👤 Perfil
            </Link>
            <Link onClick={toggleMenu} to={`/trainees/${username}/trainings`} className="text-gray-300 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium">
              🏃‍♂️ Entrenamientos
            </Link>
            <Link onClick={toggleMenu} to="/trainings/create" className="text-gray-300 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium">
              ➕ Nuevo Entrenamiento
            </Link>
            <Link onClick={toggleMenu} to="/training-types" className="text-gray-300 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium">
              🏷️ Tipos de Entrenamiento
            </Link>
            <Link onClick={toggleMenu} to="/change-password" className="text-gray-300 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium">
              🔑 Cambiar Contraseña
            </Link>
            <button
              onClick={() => { handleLogout(); toggleMenu(); }}
              className="w-full text-left bg-red-600 text-white block px-3 py-2 rounded-md text-base font-medium hover:bg-red-700 transition duration-150 mt-2"
            >
              🚪 Cerrar sesión
            </button>
          </div>
        </div>
      </nav>

      {/* Alerta de Error de Logout (si ocurre) */}
      {logoutError && (
          <div className="bg-red-500 text-white p-3 text-center font-medium">
            {logoutError}
          </div>
      )}
    </>
  );
}