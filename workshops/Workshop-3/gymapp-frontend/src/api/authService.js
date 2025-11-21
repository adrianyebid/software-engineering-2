import axiosClient from "./axiosClient";

export const login = (username, password) =>
  axiosClient.post("/auth/login", { username, password });

export const changePassword = (username, oldPassword, newPassword) =>
  axiosClient.put("/auth/change-password", { username, oldPassword, newPassword });