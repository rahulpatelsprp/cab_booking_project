import React, { useState, useEffect } from "react";
import styles from "./UserHomePage.module.css";
import { useNavigate } from "react-router-dom";
import {
  allRequestedRide,
  getPendingRide,
  register,
} from "../../api/RiderController";
import UserContainer from "../../common/UserContainer";
import toast from "react-hot-toast";
import Header from "../../common/Header";

const UserHomePage = () => {
  const navigate = useNavigate();
  const [pickup, setPickup] = useState("");
  const [drop, setDrop] = useState("");
  const [rate, setRate] = useState(0);
  const [hasPendingRide, setHasPendingRide] = useState();

  const places = [
    { id: 1, name: "IT park", price: 50 },
    { id: 2, name: "Keeranatham", price: 100 },
    { id: 3, name: "Saravanampatti", price: 150 },
    { id: 4, name: "Prozone mall", price: 200 },
    { id: 5, name: "Gandhipuram", price: 250 },
  ];

  const handleRequest = () => {
    if (pickup && drop && pickup !== drop) {
      const newRide = { pickUpLocation: pickup, dropOffLocation: drop, rate };
      register(newRide).then((resp) => {
        navigate("/user/loading", { state: { ride: resp.data } });
        setPickup("");
        setDrop("");
      });
    } else {
      toast.error("Please select valid pickup and drop points.");
    }
  };

  useEffect(() => {
    getPendingRide().then((resp) => setHasPendingRide(resp.data));
    if (pickup && drop && pickup !== drop) {
      const pickupPlace = places.find((p) => p.name === pickup);
      const dropPlace = places.find((p) => p.name === drop);
      if (pickupPlace && dropPlace) {
        setRate(pickupPlace.price + dropPlace.price);
      }
    }
  }, [pickup, drop]);

  if (hasPendingRide) {
    navigate("/user/loading", { state: { ride: hasPendingRide } });
  }
  return (
    <UserContainer>
      <Header />
      <div className="flex h-screen bg-[#f9f7f1] p-8 flex-col items-center">
        <h2 className="text-3xl font-bold mb-8 text-center">
          Need a quick ride?
        </h2>

        <div className={styles.inputSection}>
          <div className={styles.inputGroup}>
            <label>Pickup Point</label>
            <select
              value={pickup}
              onChange={(e) => setPickup(e.target.value)}
              className={styles.fullInput}
            >
              <option value="">Select Pickup</option>
              {places.map((place) => (
                <option key={place.id} value={place.name}>
                  {place.name}
                </option>
              ))}
            </select>
          </div>

          <div className={styles.inputGroup}>
            <label>Drop Point</label>
            <select
              value={drop}
              onChange={(e) => setDrop(e.target.value)}
              className={styles.fullInput}
            >
              <option value="">Select Drop</option>
              {places.map((place) => (
                <option key={place.id} value={place.name}>
                  {place.name}
                </option>
              ))}
            </select>
          </div>

          <div className={styles.fareCard}>
            {pickup && drop && pickup !== drop ? (
              <>
                <h3 className={styles.fareTitle}>Fare Details</h3>
                <p className={styles.fareText}>
                  Rate from <strong>{pickup}</strong> to <strong>{drop}</strong>{" "}
                  is: ₹{rate}
                </p>
              </>
            ) : (
              <p className={styles.fareWarning}>
                Please select different pickup and drop locations.
              </p>
            )}
          </div>

          <div className={styles.buttonGroup}>
            <button onClick={handleRequest} className={styles.requestButton}>
              Request Now
            </button>
          </div>
        </div>

        <p className={styles.consentText}>
          By providing your phone number and clicking 'Request Now,' you consent
          to receive text messages from Auto Cars...
        </p>
      </div>
    </UserContainer>
  );
};

export default UserHomePage;
