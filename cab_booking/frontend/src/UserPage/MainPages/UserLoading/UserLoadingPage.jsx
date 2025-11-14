import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import styles from "../UserLoading/UserLoadingPage.module.css";
import {
  changeRideStatus,
  getRideByrideId,
} from "../../../api/RiderController";
import toast from "react-hot-toast";
import { rideRating } from "../../../api/RatingController";

const statusMessages = [
  "Searching for Driver…",
  "Waiting for Driver Response…",
  "Almost there…",
];

const UserLoadingPage = ({ fare }) => {
  const [statusIdx, setStatusIdx] = useState(0);
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [error, setError] = useState(null);
  const [cancelReason, setCancelReason] = useState("");
  const navigate = useNavigate();
  const { state } = useLocation();

  const handleCancelButton = (cancelReason) => {
    changeRideStatus(state?.ride, "CANCELLED").then((resp) => {
      if (resp.data.status === "CANCELLED") {
        const payload = {
          comment: cancelReason,
          score: 1,
        };
        rideRating(state?.ride.riderId, payload).then((resp1) => {
          console.log(resp1);
          if (resp1.status == "200") {
            navigate("/user/homepage");
          }
        });
      }
    });
  };

  useEffect(() => {
    if (!state?.ride?.riderId) {
      setError("Invalid ride data.");
      return;
    }

    const messageInterval = setInterval(() => {
      setStatusIdx((prev) => (prev + 1) % statusMessages.length);
    }, 5000);

    const statusInterval = setInterval(async () => {
      try {
        const response = await getRideByrideId(state.ride.riderId);
        if (!response.data) {
          console.error("Failed to fetch ride status:", response.status);
          setError("Unable to fetch ride status.");
          return;
        }

        const data = response.data;
        if (data.status == "ACCEPTED") {
          toast.success("Accepted");
          clearInterval(messageInterval);
          clearInterval(statusInterval);
          navigate("/startRide", { state: { ride: data } });
        }
      } catch (err) {
        console.error("Error checking ride status:", err);
        setError("Network error while checking ride status.");
      }
    }, 5000);

    return () => {
      clearInterval(messageInterval);
      clearInterval(statusInterval);
    };
  }, [navigate, state?.ride?.riderId]);

  return (
    <>
      <div className={styles.loadingpage}>
        <div className={styles.loadingspinner}></div>
        <h2>{statusMessages[statusIdx]}</h2>
        <div className="mt-6 flex gap-4">
          <button
            className="bg-red-500 py-2 px-4 text-white font-bold"
            onClick={() => setShowCancelModal(true)}
          >
            Cancel Ride
          </button>
        </div>
        {fare && (
          <div className={styles.loadingfare}>
            Estimated Fare:{" "}
            <span className={styles.loadingfarevalue}>Rs {fare}</span>
          </div>
        )}

        {error && (
          <div className={styles.loadingerror}>
            <p>{error}</p>
          </div>
        )}
      </div>

      {showCancelModal && (
        <div className="fixed inset-0 backdrop-blur-md bg-black/30 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg shadow-lg w-96">
            <h3 className="text-lg font-semibold mb-4">Confirm Cancellation</h3>
            <p className="mb-2">Do you really want to cancel this ride?</p>
            <textarea
              className="w-full border px-3 py-2 rounded mb-4"
              placeholder="Enter reason for cancellation"
              value={cancelReason}
              onChange={(e) => setCancelReason(e.target.value)}
            />
            <div className="flex justify-end gap-2">
              <button
                className="bg-gray-300 px-4 py-2 rounded"
                onClick={() => setShowCancelModal(false)}
              >
                No
              </button>
              <button
                className="bg-red-500 text-white px-4 py-2 rounded"
                onClick={() => {
                  console.log("Ride cancelled for reason:", cancelReason);
                  setShowCancelModal(false);
                  handleCancelButton(cancelReason);
                }}
              >
                Yes, Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default UserLoadingPage;
