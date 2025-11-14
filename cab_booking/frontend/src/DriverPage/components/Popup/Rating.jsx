import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import commentImage from "../../../assets/images/userIcons/icon1.png";
import { getRideByPaymentId } from "../../../api/PaymentController";
import { rideRating } from "../../../api/RatingController";
import toast from "react-hot-toast";
const Popup = ({ onClose, onSubmit }) => {
  const [rating, setRating] = useState(0);
  const [comment, setComment] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const payment = location.state || {};
  const [riderId, setRiderId] = useState();
  const [ride, setRide] = useState();
  const role = sessionStorage.getItem("role");

  const subject = role == "DRIVER" ? "User" : "Driver";
  useEffect(() => {
    getRideByPaymentId(payment.payment.paymentId).then((resp) => {
      console.log(resp.data);
      setRiderId(resp.data);
    });
  }, []);

  const handleSubmit = (e) => {
    e.preventDefault();
    setIsSubmitting(true);

    const formData = {
      score: rating,
      comment,
    };

    setTimeout(() => {
      rideRating(riderId, formData)
        .then((resp1) => {
          setIsSubmitting(false);
          if (resp1.status == "200") {
            toast.success("Thank you for your comment!");
            if (role === "USER") {
              navigate("/user/homepage");
            } else {
              navigate("/driver/rides");
            }
          } else {
            toast.error("Failed to submit comment. Please try again.");
          }
        })
        .catch((error) => {
          setIsSubmitting(false);
          console.error("Submission error:", error);
          toast.error("An error occurred. Please try again later.");
        });
    }, 1500);
  };

  const StarRating = () => (
    <div className="flex justify-center space-x-1">
      {[1, 2, 3, 4, 5].map((star) => (
        <button
          key={star}
          type="button"
          onClick={() => setRating(star)}
          className={`
            text-4xl transition-colors duration-200 focus:outline-none
            ${star <= rating ? "text-yellow-400" : "text-gray-300"}
          `}
          aria-label={`${star} stars`}
        >
          ★
        </button>
      ))}
    </div>
  );

  const closing = () => {
    const role = sessionStorage.getItem("role");
    if (role == "USER") {
      navigate("/user/homepage");
    } else if (role == "DRIVER") {
      navigate("/driver/rides");
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center backdrop-blur-sm bg-transparent bg-opacity-40 p-4">
      <div className="relative w-full max-w-md transform transition-all duration-300 scale-100 opacity-100">
        <div className="absolute inset-x-0 -top-20 flex justify-center z-10">
          <img
            src={commentImage}
            alt={`${subject} profile`}
            className="w-35 h-35 rounded-full border-4 border-white shadow-xl object-cover"
          />
        </div>

        <div className="bg-white p-6 pt-16 rounded-xl shadow-2xl relative">
          <button
            onClick={closing}
            className="absolute top-3 right-3 text-gray-400 hover:text-gray-600 transition z-10"
            aria-label="Close comment form"
          >
            <svg
              className="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M6 18L18 6M6 6l12 12"
              ></path>
            </svg>
          </button>

          <h2 className="text-2xl font-bold text-center mb-1 text-gray-800">
            Rate Your {subject}
          </h2>
          <p className="text-center text-sm text-gray-500 mb-6">
            Ride ID:{" "}
            <span className="font-semibold text-indigo-600">{riderId}</span>
          </p>

          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="text-center">
              <StarRating />
            </div>

            <div>
              <label
                htmlFor="comment"
                className="block text-sm font-medium text-gray-700 mb-2"
              >
                Share your comment:
              </label>
              <textarea
                id="comment"
                name="comment"
                rows="4"
                required
                value={comment}
                onChange={(e) => setComment(e.target.value)}
                className="w-full px-3 py-2 text-gray-700 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 transition duration-150"
                placeholder={`Tell us about your ${subject} experience... (e.g., driver was friendly, smooth ride)`}
                disabled={isSubmitting}
              ></textarea>
            </div>

            <button
              type="submit"
              disabled={rating === 0 || isSubmitting}
              className={`w-full py-3 rounded-lg text-lg font-semibold shadow-md transition-all duration-300 
                ${
                  rating === 0 || isSubmitting
                    ? "bg-gray-300 text-gray-500 cursor-not-allowed"
                    : "bg-indigo-600 text-white hover:bg-indigo-700 active:bg-indigo-800"
                }`}
            >
              {isSubmitting ? (
                <div className="flex items-center justify-center">
                  <svg
                    className="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                  >
                    <circle
                      className="opacity-25"
                      cx="12"
                      cy="12"
                      r="10"
                      stroke="currentColor"
                      strokeWidth="4"
                    ></circle>
                    <path
                      className="opacity-75"
                      fill="currentColor"
                      d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                    ></path>
                  </svg>
                  Sending comment...
                </div>
              ) : (
                `Submit comment`
              )}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Popup;
