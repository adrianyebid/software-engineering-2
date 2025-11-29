import axios from "axios";

const pythonClient = axios.create({
  baseURL: "http://localhost:8000",
  headers: { "Content-Type": "application/json" },
});

export const createNote = (username, content, important = false) =>
  pythonClient.post("/notes", { username, content, important });

export const getNotes = (username) =>
  pythonClient.get(`/notes/${username}`);

export const deleteNote = (noteId) =>
  pythonClient.delete(`/notes/${noteId}`);