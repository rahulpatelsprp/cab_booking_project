import React, { useEffect, useState } from "react";
import Header from "../../common/Header";
import {
  getAllRidesByUserId,
  getAllRidesByDriverId,
} from "../../api/RiderController";
 
import { getPaymentDetailByRideId } from "../../api/PaymentController";
 
import UserContainer from "../../common/UserContainer";
 
export default function RideHistory() {
  const [ridesList, setRidesList] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  // Using 'role' as the key role. The backend must ensure consistency.
  const role = sessionStorage.getItem("role");
 
  // Helper function for date formatting
  const formatTime = (isoString) => {
    if (!isoString) return "N/A";
    // Convert to a local readable string
    return new Date(isoString).toLocaleString();
  };
 
  useEffect(() => {
    setIsLoading(true);
    const fetchRides = async () => {
      // 1. Fetch the initial list of rides (User or Driver)
      const ridesResp =
        role === "USER"
          ? await getAllRidesByUserId()
          : await getAllRidesByDriverId();
 
      const ridesData = ridesResp.data || [];
 
      // 2. Fetch payment details for each ride concurrently
      const paymentPromises = ridesData.map(async (ride) => {
        const rideIdentifier = ride.rideId || ride.riderId;
 
        if (!rideIdentifier) {
          console.warn("Skipping payment fetch: Ride object is missing a valid ID.", ride);
          return { ...ride, payment: null };
        }
 
        try {
          const paymentResp = await getPaymentDetailByRideId(rideIdentifier);
         
          return {
            ...ride,
            // Augment the ride object with the payment details
            payment: paymentResp.data,
          };
        } catch (error) {
          console.warn("Could not fetch payment for ride:", rideIdentifier, "Error:", error.message);
          return {
            ...ride,
            payment: null, // Set to null if fetching fails (e.g., status code 500)
          };
        }
      });
 
      // 3. Wait for all payment fetches to complete
      const completeRidesList = await Promise.all(paymentPromises);
 
      setRidesList(completeRidesList);
      setIsLoading(false);
    };
    fetchRides();
  }, [role]);
 
  return (
    <UserContainer>
      <Header msgBody={"Ride History"} />
      <div className="flex flex-wrap lg:px-[200px] gap-8 justify-around pt-8">
        {isLoading ? (
          <h2 className="text-4xl text-neutral-400 mb-[40vh]">Loading...</h2>
        ) : ridesList.length === 0 ? (
          <div className="py-20">
            <h2 className="text-6xl">No Rides Found</h2>
          </div>
        ) : (
          ridesList.map((ride) => (
            <div
              className="lg:w-[calc(50%_-_16px)] lg:my-4 w-[90%]"
              // *** FIX for React Warning: Unique key prop ***
              key={ride.rideId || ride.riderId}
            >
              <div className="bg-amber-400 py-8 px-4 rounded-sm">
                <p className="mb-4 flex">
                  {/* SVG for Pick Up */}
                  <svg
                    className="w-6 mr-4"
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 640 640"
                  >
                    <path d="M286.7 96.1C291.7 113 282.1 130.9 265.2 135.9C185.9 159.5 128.1 233 128.1 320C128.1 426 214.1 512 320.1 512C426.1 512 512.1 426 512.1 320C512.1 233.1 454.3 159.6 375 135.9C358.1 130.9 348.4 113 353.5 96.1C358.6 79.2 376.4 69.5 393.3 74.6C498.9 106.1 576 204 576 320C576 461.4 461.4 576 320 576C178.6 576 64 461.4 64 320C64 204 141.1 106.1 246.9 74.6C263.8 69.6 281.7 79.2 286.7 96.1z" />
                  </svg>
                  {ride.pickUpLocation}
                </p>
                <p className="mb-4 flex">
                  {/* SVG for Drop Off */}
                  <svg
                    className="w-6 mr-4"
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 640 640"
                  >
                    <path d="M320 64C214 64 128 148.4 128 252.6C128 371.9 248.2 514.9 298.4 569.4C310.2 582.2 329.8 582.2 341.6 569.4C391.8 514.9 512 371.9 512 252.6C512 148.4 426 64 320 64z" />
                  </svg>
                  {ride.dropOffLocation}
                </p>
                <p className="mb-4 text-xs font-bold">
                  Ride ID: {ride.rideId || ride.riderId}
                </p>
 
                {/* --- START OF NEW PAYMENT DETAILS SECTION --- */}
                {/* Check if payment object exists and has an amount */}
                {ride.payment && ride.payment.amount !== undefined ? (
                    <div className="mb-4 bg-amber-100 p-3 rounded-md shadow-inner">
                        <h4 className="text-md font-extrabold mb-2 text-neutral-800 border-b border-amber-600 pb-1">Payment Summary 💰</h4>
                        <div className="flex justify-between text-sm">
                            <p className="font-semibold">Amount Paid:</p>
                            <p className="font-extrabold text-lg text-green-700">
                                {/* Format the amount as currency */}
                                ${Number(ride.payment.amount).toFixed(2)}
                            </p>
                        </div>
                        <div className="flex justify-between text-sm mt-1">
                            <p className="text-neutral-700">Method:</p>
                            <p className="font-medium">{ride.payment.method || 'N/A'}</p>
                        </div>
                        <div className="flex justify-between text-sm mt-1">
                            <p className="text-neutral-700">Payment Status:</p>
                            <p className={`font-bold ${
                                ride.payment.status === "SUCCESS" ? "text-green-700" : "text-red-700"
                            }`}>
                                {ride.payment.status || 'N/A'}
                            </p>
                        </div>
                        <p className="text-xs text-neutral-600 mt-2 text-right">
                            Time: {formatTime(ride.payment.time)}
                        </p>
                    </div>
                ) : (
                    <div className="mb-4 bg-red-200 p-3 rounded-md text-red-800 font-semibold">
                        Payment details not finalized or available.
                    </div>
                )}
                {/* --- END OF NEW PAYMENT DETAILS SECTION --- */}
 
                <div className="flex justify-around bg-amber-100 px-2 w-full rounded-sm py-4 flex-col md:flex-row">
                  <div className="flex md:block">
                    <p className="text-neutral-600 text-sm mb-2 md:text-center">
                      Ride Status
                    </p>
                    <p
                      className={`font-bold rounded-full py-1 px-3 mb-4 md:mb-0 ${
                        ride.status === "PENDING"
                          ? "bg-amber-500 text-neutral-800"
                          : ride.status === "SUCCESS"
                          ? "bg-emerald-400"
                          : "bg-red-400"
                      }`}
                    >
                      {ride.status === "SUCCESS"
                        ? "Completed"
                        : ride.status === "PENDING"
                        ? "On Going"
                        : "Cancelled"}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          ))
        )}
      </div>
      <p className="text-neutral-500 mt-8 pb-8 px-2 lg:px-16 text-xs">
        Review of past ride details, including pickup, dropoff, status, and payment.
      </p>
    </UserContainer>
  );
}