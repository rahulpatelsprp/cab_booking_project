import React from "react";
import { Navigate, Outlet } from "react-router-dom";

const ProtectedLayout = ({ allowedRoles }) => {
  const token = sessionStorage.getItem("token");
  const role = sessionStorage.getItem("role");

  if (!token || !allowedRoles.includes(role)) {
    return <div className="flex items-center justify-center h-screen bg-gray-100">
        <div className="text-center">
          <h1 className="text-3xl font-bold text-red-600 mb-4">Access Denied</h1>
          <p className="text-gray-700 mb-6">You are not authorized to view this page.</p>
        </div>
      </div>;
  }
  return <Outlet />;
};

export default ProtectedLayout;
