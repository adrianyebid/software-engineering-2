import axiosClient from "./axiosClient";

// Público
export const registerTrainer = (data) =>
  axiosClient.post("/trainers", data);

// Privados
export const getTrainerProfile = (username) =>
  axiosClient.get(`/trainers/${username}`);

export const updateTrainerProfile = (data) =>
  axiosClient.put("/trainers", data);

export const changeTrainerStatus = (data) =>
  axiosClient.patch("/trainers/status", data);

export const deleteTrainer = (username) =>
  axiosClient.delete(`/trainers/${username}`);

export const getAssignedTrainees = (username) =>
  axiosClient.get(`/trainers/${username}/trainees`);

export const getTrainerTrainings = (params) =>
  axiosClient.get("/trainers/trainings", { params });