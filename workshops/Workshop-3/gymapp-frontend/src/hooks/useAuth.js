import { useState, useEffect } from "react";
import { login as loginService, changePassword as changePasswordService } from "../api/authService";

export function useAuth() {
  const [user, setUser] = useState(() => localStorage.getItem("user"));

  const login = async (username, password) => {
    await loginService(username, password);
    localStorage.setItem("user", username);
    setUser(username);
  };

  const logout = () => {
    localStorage.removeItem("user");
    localStorage.removeItem("token"); // para futuro JWT
    setUser(null);
  };

  const changePassword = async (oldPassword, newPassword) => {
    if (!user) throw new Error("No hay usuario autenticado");
    await changePasswordService(user, oldPassword, newPassword);
  };

  return { user, login, logout, changePassword };
}