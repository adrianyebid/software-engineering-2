import { Navigate } from "react-router-dom";

export default function PrivateRoute({ user, role, allowedRole, children }) {
  if (!user) return <Navigate to="/" replace />;
  if (allowedRole && role !== allowedRole) return <Navigate to="/" replace />;
  return children;
}