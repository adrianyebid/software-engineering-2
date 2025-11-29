import axiosClient from "./axiosClient";

export const createTraining = (data) =>
  axiosClient.post("/trainings", data);