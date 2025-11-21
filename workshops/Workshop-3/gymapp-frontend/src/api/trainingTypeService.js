import axiosClient from "./axiosClient";

export const getAllTrainingTypes = () =>
  axiosClient.get("/training-types");