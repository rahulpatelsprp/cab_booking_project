import React, { useState, useEffect } from "react";
import { allRide } from "../api/RiderController";
import styles from "./adminDashboard.module.css";
import { FiMapPin, FiEye, FiTrash2 } from "react-icons/fi";

const RIDE_FILTERS = ["all", "cancelled", "success"];

const RideHistory = () => {
  const [rides, setRides] = useState([]);
  const [rideFilter, setRideFilter] = useState("all");
  const [isLoading, setIsLoading] = useState(true);
  useEffect(() => {
    setIsLoading(true);
    allRide()
      .then((res) => {
        if (Array.isArray(res.data)) {
          setRides(res.data);
        } else {
          console.error("API did not return an array:", res.data);
          setRides([]);
        }
        setIsLoading(false);
      })
      .catch((err) => {
        console.error("Failed to fetch rides:", err);
        setIsLoading(false);
      });
  }, []);

  const filteredRides = rides.filter((ride) => {
    if (rideFilter === "all") return true;
    return (
      ride.status && ride.status.toLowerCase() === rideFilter.toLowerCase()
    );
  });

  const handleViewRide = (ride) => {
    console.log("View Ride:", ride);
    alert(`Viewing details for Ride ${ride.riderId}`);
  };

  const handleDeleteRide = (rideId) => {
    console.log("Delete Ride:", rideId);
    setRides(rides.filter((r) => r.riderId !== rideId));
    alert(`Ride ${rideId} deleted! (simulated)`);
  };

  return (
    <div>
      <h2>Ride History</h2>

      <div className={styles.rideFilterButtons}>
        {RIDE_FILTERS.map((filter) => (
          <button
            key={filter}
            className={rideFilter === filter ? styles.active : ""}
            onClick={() => setRideFilter(filter)}
            aria-label={`Filter by ${filter}`} // Added for accessibility
          >
            {filter.charAt(0).toUpperCase() + filter.slice(1)}
          </button>
        ))}
      </div>

      {isLoading ? (
        <p>Loading rides...</p>
      ) : filteredRides.length > 0 ? (
        <div className={styles.profileCardContainer}>
          {filteredRides.map((ride) => (
            <div key={ride.riderId} className={styles.profileCard}>
              <div
                className={styles.profileImage}
                style={{
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  fontSize: "40px",
                  backgroundColor: "#e6f2ff",
                  color: "#007bff",
                }}
              >
                <FiMapPin />
              </div>

              <h3 className={styles.profileName}>Ride {ride.riderId}</h3>
              <p className={styles.profileDetail}>
                <strong>From:</strong> {ride.pickUpLocation}
              </p>
              <p className={styles.profileDetail}>
                <strong>To:</strong> {ride.dropOffLocation}
              </p>
              <p className={styles.profileDetail}>
                <strong>Status:</strong> {ride.status}
              </p>

              <div className={styles.profileButtons}>
                <button
                  className={`${styles.profileButton} ${styles.edit}`}
                  onClick={() => handleViewRide(ride)}
                  aria-label={`View details for ride ${ride.riderId}`}
                >
                  <FiEye /> View
                </button>
                <button
                  className={`${styles.profileButton} ${styles.delete}`}
                  onClick={() => handleDeleteRide(ride.riderId)}
                  aria-label={`Delete ride ${ride.riderId}`}
                >
                  <FiTrash2 /> Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <p>No rides found for selected filter.</p>
      )}
    </div>
  );
};

export default RideHistory;
