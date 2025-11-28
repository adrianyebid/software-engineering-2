import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useAuth } from "./context/authContext.jsx";

// Público
import Login from "./pages/Login";
import RegisterTrainee from "./pages/trainee/RegisterTrainee";
import RegisterTrainer from "./pages/trainer/RegisterTrainer";

// Trainee
import DashboardTrainee from "./pages/trainee/DashboardTrainee";
import TraineeProfile from "./pages/trainee/TraineeProfile";
import EditTraineeProfile from "./pages/trainee/EditTraineeProfile";
import AssignTrainers from "./pages/trainee/AssignTrainers";
import TraineeTrainings from "./pages/trainee/TraineeTrainings";

// Trainer
import DashboardTrainer from "./pages/trainer/DashboardTrainer";
import TrainerProfile from "./pages/trainer/TrainerProfile";
import EditTrainerProfile from "./pages/trainer/EditTrainerProfile";
import AssignedTrainees from "./pages/trainer/AssignedTrainees";
import TrainerTrainings from "./pages/trainer/TrainerTrainings";

// Rutas compartidas
import CreateTraining from "./pages/training/CreateTraining";
import TrainingTypeList from "./pages/training/TrainingTypeList";
import ChangePassword from "./pages/ChangePassword";

// Layouts
import LayoutTrainee from "./layout/LayoutTrainee";
import LayoutTrainer from "./layout/LayoutTrainer";
import PrivateRoute from "./components/PrivateRoute";

function App() {
  const { user, role } = useAuth();

  return (
    <BrowserRouter>
      <Routes>

        {/* =======================
            RUTAS PÚBLICAS
        ======================== */}
        <Route path="/" element={<Login />} />
        <Route path="/register" element={<RegisterTrainee />} />
        <Route path="/register-trainer" element={<RegisterTrainer />} />


        {/* =======================
            RUTAS PRIVADAS - TRAINEE
        ======================== */}
        <Route
          element={
            <PrivateRoute user={user} role={role} allowedRole="ROLE_TRAINEE">
              <LayoutTrainee />
            </PrivateRoute>
          }
        >
          <Route path="/dashboard" element={<DashboardTrainee />} />
          <Route path="/trainees/:username" element={<TraineeProfile />} />
          <Route path="/trainees/:username/edit" element={<EditTraineeProfile />} />
          <Route path="/trainees/:username/assign" element={<AssignTrainers />} />
          <Route path="/trainees/:username/trainings" element={<TraineeTrainings />} />
        </Route>


        {/* =======================
            RUTAS PRIVADAS - TRAINER
        ======================== */}
        <Route
          element={
            <PrivateRoute user={user} role={role} allowedRole="ROLE_TRAINER">
              <LayoutTrainer />
            </PrivateRoute>
          }
        >
          <Route path="/trainer-dashboard" element={<DashboardTrainer />} />
          <Route path="/trainers/:username" element={<TrainerProfile />} />
          <Route path="/trainers/:username/edit" element={<EditTrainerProfile />} />
          <Route path="/trainers/:username/trainees" element={<AssignedTrainees />} />
          <Route path="/trainers/:username/trainings" element={<TrainerTrainings />} />
        </Route>


        {/* ===========================================
            RUTAS COMPARTIDAS ENTRE TRAINER Y TRAINEE
        ============================================ */}
        <Route
          element={
            <PrivateRoute user={user} role={role}>
              {role === "ROLE_TRAINER" ? <LayoutTrainer /> : <LayoutTrainee />}
            </PrivateRoute>
          }
        >
          <Route path="/trainings/create" element={<CreateTraining />} />
          <Route path="/training-types" element={<TrainingTypeList />} />
          <Route path="/change-password" element={<ChangePassword />} />
        </Route>

      </Routes>
    </BrowserRouter>
  );
}

export default App;
