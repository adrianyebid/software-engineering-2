import { Outlet } from "react-router-dom";
import NavbarTrainer from "../components/layout/NavbarTrainer";

export default function LayoutTrainer() {
  return (
    <div className="min-h-screen bg-gray-100">
      <NavbarTrainer />
      <main className="0">
        <Outlet />
      </main>
    </div>
  );
}