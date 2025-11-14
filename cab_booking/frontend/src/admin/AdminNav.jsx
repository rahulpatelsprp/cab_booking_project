import React from "react";
import { FiUsers, FiTruck, FiList, FiLogOut } from "react-icons/fi";
import styles from "./adminDashboard.module.css";

const NAV_OPTIONS = [
  { key: "userdetails", label: "User Details", icon: <FiUsers /> },
  { key: "driverdetails", label: "Driver Details", icon: <FiTruck /> },
  { key: "ridehistory", label: "Ride History", icon: <FiList /> },
  { key: "logout", label: "Logout", icon: <FiLogOut /> },
];

const AdminNav = ({ selected, setSelected }) => {
  const handleLogout = () => {
    sessionStorage.clear();
    window.location.href = "/login";
  };

  return (
    <nav className={styles.sidebar}>
      <div className={styles.sidebarLogo}></div>
      <div className={styles.navLinks}>
        {NAV_OPTIONS.map((opt) => (
          <button
            key={opt.key}
            className={`${styles.navButton} ${
              selected === opt.key ? styles.active : ""
            }`}
            onClick={() =>
              opt.key === "logout" ? handleLogout() : setSelected(opt.key)
            }
            title={opt.label}
            aria-label={opt.label}
          >
            <span className={styles.navIcon}>{opt.icon}</span>
          </button>
        ))}
      </div>
    </nav>
  );
};

export default AdminNav;
