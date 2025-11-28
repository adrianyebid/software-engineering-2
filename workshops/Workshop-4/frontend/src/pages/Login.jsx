import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../context/authContext.jsx";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      await login(username, password);
      const role = localStorage.getItem("role");
      if (role === "ROLE_TRAINER") {
        navigate("/trainer-dashboard");
      } else {
        navigate("/dashboard");
      }
    } catch {
      alert("Credenciales inválidas");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <form onSubmit={handleLogin} className="bg-white p-6 rounded shadow-md w-80">
        <h2 className="text-xl font-bold mb-4">Iniciar sesión</h2>

        <input
          type="text"
          placeholder="Usuario"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          className="w-full mb-3 p-2 border rounded"
        />

        <input
          type="password"
          placeholder="Contraseña"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="w-full mb-3 p-2 border rounded"
        />

        <button type="submit" className="w-full bg-blue-500 text-white p-2 rounded">
          Entrar
        </button>

        <div className="text-center mt-4">
          <p className="text-sm">¿No tienes cuenta?</p>
          <Link to="/register" className="text-blue-600 text-sm">
            Registrar como Trainee
          </Link>
          <br />
          <Link to="/register-trainer" className="text-blue-600 text-sm">
            Registrar como Trainer
          </Link>
        </div>
      </form>
    </div>
  );
}
