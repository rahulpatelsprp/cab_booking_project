import React, { useEffect, useRef, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import RideEnded from "./RideEnded.jsx";
import {
  changeRideStatus,
  getDriverByrideId,
  getRideByrideId,
} from "../../api/RiderController.jsx";
import Header from "../../common/Header.jsx";
import NavBarRider from "../../common/Navbar.jsx";
import UserContainer from "../../common/UserContainer.jsx";
import { getPaymentDetailByRideId } from "../../api/PaymentController.jsx";
// import { ridescore } from "../../api/RaController.jsx";
import { rideRating } from "../../api/RatingController.jsx";

// import img from "../../../assets/images/mapImages/gandhipuram_prozonemall.png";

export default function StartRide() {
  const location = useLocation();
  const { ride } = location.state || {};
  const [driver, setDriver] = useState();
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [cancelReason, setCancelReason] = useState("");
  const [isRideOngoing, setIsRideOngoing] = useState(false);
  const [isRideEnded, setIsRideEnded] = useState(false);
  const [payment, setPayment] = useState();

  const paymentRef = useRef();
  const navigate = useNavigate();

  useEffect(() => {
    if (!ride?.riderId) return;

    getDriverByrideId(ride.riderId).then((resp) => {
      setDriver(resp.data);
    });

    getPaymentDetailByRideId(ride.riderId).then((resp) => {
      setPayment(resp.data);
      paymentRef.current = resp.data; // ✅ Store latest payment in ref
    });
  }, [ride?.riderId]);

  useEffect(() => {
    if (!ride?.riderId) return;

    const intervalId = setInterval(() => {
      getRideByrideId(ride.riderId).then((resp) => {
        if (resp.data.status === "SUCCESS") {
          navigate("/user/payment", {
            state: {
              payment: paymentRef.current, // ✅ Use ref to get latest payment
            },
          });
        } else if (resp.data.status === "CANCELLED") {
          navigate("/user/homepage");
        }
      });
    }, 3000);

    return () => clearInterval(intervalId);
  }, [ride?.riderId]);

  const getImageName = (from, to) => {
    const places = [
      from.toLowerCase().replace(/\s+/g, ""),
      to.toLowerCase().replace(/\s+/g, ""),
    ];
    places.sort();
    return `${places[0]}_${places[1]}.png`;
  };

  const imagePath = `/images/${getImageName(
    ride?.pickUpLocation,
    ride?.dropOffLocation
  )}`;

  const handleCancelButton = (cancelReason) => {
    changeRideStatus(ride, "CANCELLED").then((resp) => {
      if (resp.data.status === "CANCELLED") {
        const payload = {
          comment: cancelReason,
          score: 1,
        };
        rideRating(ride.riderId, payload).then((resp1) => {
          if (resp1.status === "201") {
            navigate("/user/homepage");
          }
        });
      }
    });
  };

  return (
    <UserContainer>
      <Header msgBody={"Driver Info Sent"} />

      <div className="flex flex-col min-h-[calc(100vh-110px)] max-w-[1400px] mx-auto w-full">
        <div className="flex flex-1 px-4 lg:px-16 justify-between gap-8 flex-wrap lg:flex-nowrap my-8">
          {/* Left: Driver Info */}
          <div className="w-full lg:w-1/2">
            <h2 className="lg:text-[3.5vw] text-4xl mb-8">
              {!isRideOngoing
                ? "Date Set Ready To Ride"
                : "Buckle Up For The Ride"}
            </h2>

            <div className="bg-amber-300 w-full p-6 lg:p-8 rounded-lg shadow-md">
              <div className="flex justify-between mb-4">
                <div>
                  <p className="text-neutral-500">Driver Name</p>
                  <p className="mt-2 font-semibold">
                    {driver?.name || "Unknown Driver"}
                  </p>
                </div>
                <div className="text-sm flex items-center justify-center h-8 py-0 px-4 bg-amber-50 rounded-full">
                  <div className="mr-2">Trusted</div>
                </div>
              </div>

              <div className="flex justify-between mb-4">
                {!isRideOngoing ? (
                  <div>
                    <p className="text-neutral-500">Contact Details</p>
                    <p className="mt-2">{driver?.phone}</p>
                  </div>
                ) : (
                  <div>
                    <p className="text-neutral-500">Estimated Arrival</p>
                    <p className="mt-2">4–5 Minutes</p>
                  </div>
                )}
                <div className="flex items-center justify-center py-1 px-4 bg-amber-200 rounded-full">
                  <span className="text-xs">Rs {payment?.amount || "..."}</span>
                </div>
              </div>

              <div className="flex flex-wrap gap-4 text-sm text-neutral-700">
                <div className="bg-amber-50 py-2 px-4 flex items-center">
                  <div>Vehicle Color</div>
                  <div
                    className="w-4 h-4 rounded-full ml-2"
                    style={{
                      backgroundColor: driver?.vehicle?.color || "gray",
                    }}
                  ></div>
                </div>
                <div className="bg-amber-50 py-2 px-4">
                  Vehicle Type: {driver?.vehicleDetails || "N/A"}
                </div>
                <div className="bg-amber-50 py-2 px-4">
                  Pin:{" "}
                  <span className="text-black">{ride?.startPin || "N/A"}</span>
                </div>
              </div>

              <div className="mt-6 flex gap-4">
                <button
                  className="bg-red-500 py-2 px-4 text-white font-bold"
                  onClick={() => setShowCancelModal(true)}
                >
                  Cancel Ride
                </button>
              </div>
            </div>
          </div>

          <div className="w-full h-fit lg:w-1/2 border-4 border-neutral-300 bg-amber-300 rounded-lg overflow-hidden shadow-md">
            <img
              src={imagePath}
              alt="Route Map"
              className="object-cover w-full h-full max-h-[60vh]"
            />
          </div>
        </div>

        <p className="text-neutral-500 mt-auto mb-8 px-4 lg:px-16 text-xs max-w-3xl mx-auto text-center">
          Please patiently wait for the driver to arrive. Cancellation fees may
          apply...
        </p>

        {showCancelModal && (
          <div className="fixed inset-0 backdrop-blur-md bg-black/30 flex items-center justify-center z-50">
            <div className="bg-white p-6 rounded-lg shadow-lg w-96">
              <h3 className="text-lg font-semibold mb-4">
                Confirm Cancellation
              </h3>
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
      </div>

      <RideEnded open={isRideEnded} />
    </UserContainer>
  );
}
