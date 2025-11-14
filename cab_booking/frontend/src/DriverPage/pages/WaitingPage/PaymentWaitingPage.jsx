import React, { useEffect } from "react";
import "./PaymentWaitingPage.css"; // For styling
import { useLocation, useNavigate } from "react-router-dom";
import { getPaymentByPaymentId } from "../../../api/PaymentController";

const PaymentWaitingPage = () => {
  const location = useLocation();
  const payment = location.state || {};
  const navigate = useNavigate();

  useEffect(() => {
    const interval = setInterval(() => {
      console.log("Checking payment status for:", payment);
      getPaymentByPaymentId(payment.payment.paymentId).then((resp) => {
        console.log(resp);

        if (resp.data.status === "SUCCESS") {
          navigate("/driver/popup", {
            state: {
              payment: payment.payment,
            },
          });
        }
      });
    }, 3000);

    return () => {
      clearInterval(interval);
    };
  }, [payment.paymentId, navigate]);

  return (
    <div className="payment-waiting-container">
      <div className="spinner"></div>
      <h2>Please wait while we process your payment...</h2>
      <p>This may take a few moments. Do not refresh or close the page.</p>
    </div>
  );
};

export default PaymentWaitingPage;
