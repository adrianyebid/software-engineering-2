import { Outlet } from "react-router-dom";
import NavbarTrainee from "../components/layout/NavbarTrainee";

export default function LayoutTrainee() {
  return (
    <div className="min-h-screen bg-gray-100">
      <NavbarTrainee />
      <main className="0">
        <Outlet />
      </main>
    </div>
  );
}