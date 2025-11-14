import React, { useState } from "react";
import styles from "./adminDashboard.module.css";
import AdminNav from "./AdminNav";
import UserDetails from "./UserDetails";
import DriverDetails from "./DriverDetails";
import RideHistory from "./RideHistory";

const AdminDashboard = () => {
  const [selected, setSelected] = useState("userdetails");

  // Helper function to render the correct component
  const renderContent = () => {
    switch (selected) {
      case "userdetails":
        return <UserDetails />;
      case "driverdetails":
        return <DriverDetails />;
      case "ridehistory":
        return <RideHistory />;
      default:
        return <UserDetails />;
    }
  };

  return (
    <div className={styles.adminDashboard}>
      <AdminNav selected={selected} setSelected={setSelected} />
      <div className={styles.mainContent}>{renderContent()}</div>
    </div>
  );
};

export default AdminDashboard;
