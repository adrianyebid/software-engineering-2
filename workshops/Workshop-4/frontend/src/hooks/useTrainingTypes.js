import { useEffect, useState } from "react";
import { getAllTrainingTypes } from "../api/trainingTypeService";

export default function useTrainingTypes() {
  const [types, setTypes] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getAllTrainingTypes()
      .then((res) => setTypes(res.data))
      .catch(() => alert("Error al cargar tipos de entrenamiento"))
      .finally(() => setLoading(false));
  }, []);

  return { types, loading };
}