import axiosClient from "./axiosClient";

// Público
export const registerTrainee = (data) =>
  axiosClient.post("/trainees", data);

// Privados
export const getTraineeProfile = (username) =>
  axiosClient.get(`/trainees/${username}`);

export const updateTraineeProfile = (data) =>
  axiosClient.put("/trainees", data);

export const deleteTrainee = (username) =>
  axiosClient.delete(`/trainees/${username}`);

export const getUnassignedTrainers = (username) =>
  axiosClient.get(`/trainees/${username}/trainers/not-assigned`);

export const assignTrainers = (data) =>
  axiosClient.put("/trainees/trainers", data);

export const getTraineeTrainings = (params) =>
  axiosClient.get("/trainees/trainings", { params });

export const changeTraineeStatus = (data) =>
  axiosClient.patch("/trainees/status", data);