import React, { useContext, useEffect, useState } from "react";

import Header from "../../common/Header";
import {
  changePaymentStatus,
  getPaymentDetailByPaymentId,
  newPayment,
} from "../../api/PaymentController";
import { useLocation, useNavigate, useNavigation } from "react-router-dom";
import UserContainer from "../../common/UserContainer";
import Popup from "../../DriverPage/components/Popup/Rating";
export default function Payment() {
  const [paymentStatus, setPaymentStatus] = useState("");
  const [pop, setPop] = useState(false);

  const location = useLocation();
  const payment = location.state.payment || {};
  const [rideDetails, setRideDetails] = useState();
  const [paymentDetails, setPaymentDetails] = useState({});
  // const { user } = useContext(UserCtx);
  const navigate = useNavigate();

  useEffect(() => {
    console.log(payment.paymentId);
    getPaymentDetailByPaymentId(payment.paymentId)
      .then((resp) => {
        console.log(resp.data);
        setPaymentDetails(resp.data);
      })
      .catch(new Error("User payment not fetched"));
  }, []);

  const checkingPay = (data) => {
    if (data == "back") {
      console.log("back to payment");
      setPop(true);
    } else {
      console.log("Payment details ", paymentDetails);
      navigate("/user/popup", {
        state: {
          payment: paymentDetails,
        },
      });
    }
  };

  const payNowHandler = (method) => {
    if (!payment?.paymentId) {
      console.error("Missing payment ID");
      return;
    }

    const payload = {
      paymentId: payment.paymentId,
      method: method,
    };
    console.log(payload);

    changePaymentStatus(payload)
      .then((resp) => {
        console.log("Payment response:", resp.data);
        if (resp.data.status === "SUCCESS") {
          setPaymentDetails(resp.data);
          setPaymentStatus("SUCCESS");
        } else {
          setPaymentStatus("FAILED");
        }
      })

      .catch((err) => {
        console.error("Payment error:", err.response?.data || err.message);
        setPaymentStatus("FAILED");
      });
  };

  if (paymentStatus != "") {
    return (
      <UserContainer>
        <Header />
        <div className="flex justify-center items-center text-neutral-700">
          <div className="border border-neutral-300 p-4 lg:p-8 rounded-2xl lg:w-[60%] w-[95%] text-center">
            <p className="my-16 md:my-8">
              {paymentStatus == "SUCCESS"
                ? "Payment Successful!"
                : "Payment Failed!"}
            </p>

            <div>
              {paymentStatus == "SUCCESS" ? (
                <button
                  className="bg-amber-300 font-bold px-4 py-2 rounded-sm w-full md:w-[50%] mb-4 hover:bg-amber-400 transition"
                  onClick={() => checkingPay("Done")}
                >
                  Done
                </button>
              ) : (
                <button
                  className="bg-amber-300 font-bold px-4 py-2 rounded-sm w-full md:w-[50%] mb-4 hover:bg-amber-400 transition"
                  onClick={() => checkingPay("back")}
                >
                  Back to payment
                </button>
              )}
            </div>
          </div>
        </div>
        {pop && <Popup />}
      </UserContainer>
    );
  }
  return (
    <UserContainer>
      <Header />
      <div className="flex justify-center items-center text-neutral-700">
        <div className="border border-neutral-300 p-4 lg:p-8 rounded-2xl lg:w-[60%] w-[95%]">
          <h2 className="text-xl my-8">Payment</h2>
          <div className="bg-amber-200 p-4 rounded-lg mb-8">
            <div className="bg-white rounded-lg p-4 mb-4 flex justify-between">
              <p className="text-sm text-neutral-600">Fare to pay</p>
              <p>
                <span className="text-sm">Rs. </span>
                <span className="text-3xl font-bold">{payment.amount}.00</span>
              </p>
            </div>
            <div className="flex justify-between mb-2 px-8">
              <p>User Name</p>
              <p>{paymentDetails?.userId}</p>
            </div>
            <div className="flex justify-between mb-2 px-8">
              <p>Driver Name</p>
              <p>{paymentDetails?.driver}</p>
            </div>
            <div className="flex justify-between mb-2 px-8">
              <p>Driver ID</p>
              <p>{paymentDetails?.driverId}</p>
            </div>
          </div>
          <div className="flex flex-col items-end">
            <div className="md:w-[40%] w-full">
              <button
                className="bg-amber-300 font-bold px-4 py-2 rounded-sm w-full mb-4 hover:bg-amber-400 transition"
                onClick={() => payNowHandler("UPI")}
              >
                Pay with UPI
              </button>
              <br />
              <button
                className="text-neutral-800 w-full hover:text-amber-700 transition"
                onClick={() => payNowHandler("CASH")}
              >
                Pay with Cash
              </button>
            </div>
          </div>
        </div>
      </div>
    </UserContainer>
  );
}
