import React, { useState, useEffect } from "react";
import {
  acceptRequestRide,
  allRequestedRide,
} from "../../../api/RiderController";
import "./RidesMenuPage.css";
import { useNavigate } from "react-router-dom";
import UserContainer from "../../../common/UserContainer";
import toast from "react-hot-toast";

const RideCard = ({ ride, onAccept }) => (
  <div className="ride-card">
    <div className="ride-card-header">
      <h3>{ride.riderId}</h3>
      <span className="distance-tag">5Km</span>
    </div>
    <div className="ride-card-body">
      <div>
        <span className="label">Pickup - </span>
        <span className="location-tag">{ride.pickUpLocation}</span>
      </div>
      <div>
        <span className="label">Where To - </span>
        <span className="location-tag">{ride.dropOffLocation}</span>
      </div>
    </div>
    <div className="ride-card-footer">
      <button className="btn-dark" onClick={() => onAccept(ride)}>
        Accept Ride
      </button>
    </div>
  </div>
);

const RidesMenuPage = () => {
  const [rideData, setRideData] = useState([]);
  const navigate = useNavigate();
  const handleSubmit = async (ride) => {
    try {
      const response = await acceptRequestRide(ride);

      if (!response) {
        toast.error("Ride is not accepted");
      } else {
        console.log(response.data);
        toast.success("Ride accepted successfully" + response);
        navigate("/driver/startRide", {
          state: { ride: response.data },
        });
      }
    } catch (error) {
      toast.error("Something went wrong while accepting the ride.");
    }
  };

  useEffect(() => {
    const fetchRides = () => {
      allRequestedRide()
        .then((resp) => setRideData(resp.data))
        .catch((error) => console.error("Failed to fetch rides:", error));
    };

    fetchRides();

    const intervalId = setInterval(fetchRides, 10000);

    return () => clearInterval(intervalId);
  }, []);

  return (
    <UserContainer>
      <div className="page-container">
        <div className="rides-grid">
          {rideData && rideData.length > 0 ? (
            rideData.map((ride, index) => (
              <RideCard key={index} ride={ride} onAccept={handleSubmit} />
            ))
          ) : (
            <div className="no-rides-message">Wait for rides...</div>
          )}
        </div>
      </div>
    </UserContainer>
  );
};

export default RidesMenuPage;
