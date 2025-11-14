import React, { useEffect, useRef, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

import {
  changeRideStatus,
  getRideByrideId,
  getUserByrideId,
} from "../../../api/RiderController";
import UserContainer from "../../../common/UserContainer";
import Header from "../../../common/Header";
import { getPaymentDetailByRideId } from "../../../api/PaymentController";
import toast from "react-hot-toast";

export default function DriverStartRide() {
  const [isRideStarted, setIsRideStarted] = useState(false);
  const [showPinModal, setShowPinModal] = useState(false);
  const [enteredPin, setEnteredPin] = useState("");

  const location = useLocation();
  const navigate = useNavigate();
  const [user, setUser] = useState();
  const { ride } = location.state || {};
  const [payment, setPayment] = useState({});
  const paymentRef = useRef(payment);

  useEffect(() => {
    if (showPinModal) {
      document.body.style.overflow = "hidden";
    } else {
      document.body.style.overflow = "auto";
    }
  }, [showPinModal]);

  useEffect(() => {
    getUserByrideId(ride.riderId)
      .then((resp) => {
        setUser(resp.data);
      })
      .catch((err) => console.log(err));
    getPaymentDetailByRideId(ride.riderId).then((resp) => {
      setPayment(resp.data);
    });
  }, []);

  useEffect(() => {
    paymentRef.current = payment;
    console.log(paymentRef);
  }, [payment]);

  useEffect(() => {
    const interval = setInterval(() => {
      getRideByrideId(ride.riderId).then((resp) => {
        if (resp.data.status == "CANCELLED") {
          navigate("/driver/rides");
        }
      });
    }, 5000);
    return () => {
      clearInterval(interval);
    };
  }, []);

  const handleRideEnd = () => {
    changeRideStatus(ride, "SUCCESS").then((resp) => {
      if (resp && resp.data.status === "SUCCESS") {
        console.log("Navigating with payment:", paymentRef.current);
        navigate("/driver/paymentwaiting", {
          state: {
            payment: paymentRef.current,
          },
        });
      }
    });
  };
  const getImageName = (from, to) => {
    const places = [
      from.toLowerCase().replace(/\s+/g, ""),
      to.toLowerCase().replace(/\s+/g, ""),
    ];
    places.sort();
    return `${places[0]}_${places[1]}.png`;
  };

  const imagePath = `../../../public/images/${getImageName(
    ride?.pickUpLocation,
    ride?.dropOffLocation
  )}`;

  console.log(imagePath);
  return (
    <UserContainer>
      <Header msgBody={"New Ride Assigned"} />
      <div className="flex h-screen flex-1 px-4 lg:px-16 items-center justify-center gap-8 flex-col lg:flex-row lg:h-[70vh]">
        <div className="w-full lg:w-1/2">
          <h2 className="lg:text-[3.5vw] text-4xl mb-4">Pickup Passenger</h2>

          <div className="bg-blue-100 w-full h-fit p-6 rounded-lg">
            <div className="flex justify-between mb-4 border-b pb-4 border-blue-200">
              <div>
                <p className="text-neutral-500">Passenger Name</p>
                <p className="mt-1 font-semibold text-lg">{user?.name}</p>
              </div>
              <div>
                <div className="flex items-center justify-center py-4 px-4 bg-blue-200 rounded-full">
                  <span className="text-xs">Rs&nbsp;</span>
                  <span className="font-bold">{payment?.amount}</span>
                </div>
              </div>
            </div>

            <div className="mb-4">
              <div className="flex items-center mb-2">
                <span className="text-red-500 text-xl mr-3">●</span>
                <div>
                  <p className="text-neutral-500 text-sm">Pickup Location</p>
                  <p className="font-medium">{ride?.pickUpLocation}</p>
                </div>
              </div>
              <div className="border-l-2 border-dashed border-neutral-400 h-6 ml-3"></div>
              <div className="flex items-center">
                <span className="text-green-500 text-xl mr-3">■</span>
                <div>
                  <p className="text-neutral-500 text-sm">Drop-off Location</p>
                  <p className="font-medium">{ride?.dropOffLocation}</p>
                </div>
              </div>
            </div>

            <div className="mt-6 flex gap-4">
              {!isRideStarted ? (
                <button
                  className="bg-green-500 py-2 px-4 text-white font-bold"
                  onClick={() => setShowPinModal(true)}
                >
                  Start Ride
                </button>
              ) : (
                <button
                  className="bg-yellow-500 py-2 px-4 text-white font-bold"
                  onClick={handleRideEnd}
                >
                  End Ride
                </button>
              )}
            </div>
          </div>
        </div>

        <div className="w-full h-fit lg:w-1/2 border-4 border-neutral-300 bg-blue-200 rounded-lg overflow-hidden">
          <img
            src={imagePath}
            alt="Route Map"
            className="object-cover w-full h-full max-h-[55vh]"
          />
        </div>
      </div>

      <p className="text-neutral-500 mt-16 px-2 lg:px-16 text-xs">
        "Follow the map for the best route. Always prioritize passenger safety
        and traffic rules."
      </p>

      {/* PIN Modal */}
      {showPinModal && (
        <div className="fixed inset-0 backdrop-blur-md bg-white/10 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg shadow-lg w-80 border border-white/30">
            <h3 className="text-lg font-semibold mb-4">Enter Ride PIN</h3>
            <input
              type="text"
              value={enteredPin}
              onChange={(e) => setEnteredPin(e.target.value)}
              className="w-full border px-3 py-2 rounded mb-4"
              placeholder="Enter PIN"
            />
            <div className="flex justify-end gap-2">
              <button
                className="bg-gray-300 px-4 py-2 rounded"
                onClick={() => setShowPinModal(false)}
              >
                Cancel
              </button>
              <button
                className="bg-green-500 text-white px-4 py-2 rounded"
                onClick={() => {
                  if (enteredPin == ride?.startPin) {
                    console.log("PIN verified. Starting ride...");
                    setShowPinModal(false);
                    setIsRideStarted(true);
                    // Navigate or update ride status here
                  } else {
                    toast.error("Incorrect PIN. Please try again.");
                  }
                }}
              >
                Confirm
              </button>
            </div>
          </div>
        </div>
      )}
    </UserContainer>
  );
}
