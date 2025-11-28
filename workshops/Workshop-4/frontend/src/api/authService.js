import axiosClient from "./axiosClient";

export const login = async (username, password) => {
  const res = await axiosClient.post("/auth/login", { username, password });
  return res.data; // { accessToken, refreshToken, tokenType }
};

export const logoutService = async (refreshToken) => {
  await axiosClient.post("/auth/logout", { refreshToken });
};

export const changePassword = async (username, oldPassword, newPassword) => {
  await axiosClient.put("/auth/change-password", { username, oldPassword, newPassword });
};

export const refreshService = async (refreshToken) => {
  const res = await axiosClient.post("/auth/refresh", { refreshToken });
  return res.data; // { accessToken, tokenType }
};