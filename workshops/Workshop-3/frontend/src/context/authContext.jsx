import { createContext, useContext, useState } from "react";
import { login as loginService, logoutService, changePassword as changePasswordService } from "../api/authService";

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => localStorage.getItem("user"));
  const [role, setRole] = useState(() => localStorage.getItem("role"));

  const login = async (username, password) => {
    const { accessToken, refreshToken, role } = await loginService(username, password);
    localStorage.setItem("user", username);
    localStorage.setItem("role", role);
    localStorage.setItem("accessToken", accessToken);
    localStorage.setItem("refreshToken", refreshToken);

    setUser(username);
    setRole(role);
  };

  const logout = async () => {
    const refreshToken = localStorage.getItem("refreshToken");
    if (refreshToken) await logoutService(refreshToken);

    localStorage.clear();
    setUser(null);
    setRole(null);
  };

  const changePassword = async (oldPassword, newPassword) => {
  if (!user) throw new Error("No hay usuario autenticado");
  await changePasswordService(user, oldPassword, newPassword);
  };


  return (
    <AuthContext.Provider value={{ user, role, login, logout, changePassword }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
