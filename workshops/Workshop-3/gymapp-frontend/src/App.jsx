import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import ChangePassword from "./pages/ChangePassword";

// Trainee
import RegisterTrainee from "./pages/trainee/RegisterTrainee";
import DashboardTrainee from "./pages/trainee/DashboardTrainee";
import TraineeProfile from "./pages/trainee/TraineeProfile";
import EditTraineeProfile from "./pages/trainee/EditTraineeProfile";
import AssignTrainers from "./pages/trainee/AssignTrainers";
import TraineeTrainings from "./pages/trainee/TraineeTrainings";

// Trainer
import RegisterTrainer from "./pages/trainer/RegisterTrainer";
import DashboardTrainer from "./pages/trainer/DashboardTrainer";
import TrainerProfile from "./pages/trainer/TrainerProfile";
import EditTrainerProfile from "./pages/trainer/EditTrainerProfile";
import AssignedTrainees from "./pages/trainer/AssignedTrainees";
import TrainerTrainings from "./pages/trainer/TrainerTrainings";

// Training
import CreateTraining from "./pages/training/CreateTraining";
import TrainingTypeList from "./pages/training/TrainingTypeList";

import LayoutTrainee from "./layout/LayoutTrainee";
import LayoutTrainer from "./layout/LayoutTrainer";
import PrivateRoute from "./components/PrivateRoute";
import { useAuth } from "./hooks/useAuth";

function App() {
  const { user } = useAuth();

  return (
    <BrowserRouter>
      <Routes>
        {/* Público */}
        <Route path="/" element={<Login />} />
        <Route path="/register" element={<RegisterTrainee />} />
        <Route path="/register-trainer" element={<RegisterTrainer />} />

        {/* Privado - Trainee */}
        <Route element={<PrivateRoute user={user}><LayoutTrainee /></PrivateRoute>}>
          <Route path="/dashboard" element={<DashboardTrainee />} />
          <Route path="/trainees/:username" element={<TraineeProfile />} />
          <Route path="/trainees/:username/edit" element={<EditTraineeProfile />} />
          <Route path="/trainees/:username/assign" element={<AssignTrainers />} />
          <Route path="/trainees/:username/trainings" element={<TraineeTrainings />} />
          <Route path="/trainings/create" element={<CreateTraining />} />
          <Route path="/training-types" element={<TrainingTypeList />} />
          <Route path="/change-password" element={<ChangePassword />} />
        </Route>

        {/* Privado - Trainer */}
        <Route element={<PrivateRoute user={user}><LayoutTrainer /></PrivateRoute>}>
          <Route path="/trainer-dashboard" element={<DashboardTrainer />} />
          <Route path="/trainers/:username" element={<TrainerProfile />} />
          <Route path="/trainers/:username/edit" element={<EditTrainerProfile />} />
          <Route path="/trainers/:username/trainees" element={<AssignedTrainees />} />
          <Route path="/trainers/:username/trainings" element={<TrainerTrainings />} />
          <Route path="/trainings/create" element={<CreateTraining />} />
          <Route path="/training-types" element={<TrainingTypeList />} />
          <Route path="/change-password" element={<ChangePassword />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;