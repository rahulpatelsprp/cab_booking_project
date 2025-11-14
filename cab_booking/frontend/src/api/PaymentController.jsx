import React, { use } from "react";

import axios from "axios";
const API_URL = "http://localhost:8082/api/payments";

export function newPayment(paymentDetails) {
  return axios.post(API_URL, paymentDetails);
}

export const getPaymentDetailByRideId = async (rideId) => {
  return await axios.get(`${API_URL}/byrideid/${rideId}`);
};
export const getPaymentDetailByPaymentId = async (paymentId) => {
  return await axios.get(`${API_URL}/getPaymentDetails/${paymentId}`);
};

export function changePaymentStatus(paymentDetails) {
  return axios.put(API_URL, paymentDetails);
}
export const getPaymentByPaymentId = async (paymentId) => {
  return await axios.get(`${API_URL}/${paymentId}`);
};

export const getRideByPaymentId = async (paymentId) => {
  return await axios.get(`${API_URL}/getRide/${paymentId}`);
};
