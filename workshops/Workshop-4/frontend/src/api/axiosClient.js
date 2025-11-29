import axios from "axios";

const axiosClient = axios.create({
  baseURL: "/api/v1", // <-- ¡SOLO LA RUTA!
  headers: { "Content-Type": "application/json" },
});

// Incluir accessToken en cada request
axiosClient.interceptors.request.use((config) => {
  const token = localStorage.getItem("accessToken");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Interceptor de respuesta para manejar 401
axiosClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      const refreshToken = localStorage.getItem("refreshToken");
      if (refreshToken) {
        try {
          // Usar axiosClient para que pase por el proxy
          const res = await axiosClient.post("/auth/refresh", { refreshToken });
          const { accessToken } = res.data;
          localStorage.setItem("accessToken", accessToken);
          originalRequest.headers.Authorization = `Bearer ${accessToken}`;
          return axiosClient(originalRequest); // reintenta la request original
        } catch (err) {
          localStorage.removeItem("accessToken");
          localStorage.removeItem("refreshToken");
          window.location.href = "/";
        }
      }
    }
    return Promise.reject(error);
  }
);


export default axiosClient;