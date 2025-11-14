import React, { use } from "react";

import axios from "axios";
const API_URL = "http://localhost:8082/api/ratings";

export const rideRating = (rideId, payload) => {
  return axios.post(`${API_URL}/${rideId}`, payload);
};
export const getRatings = () => {
  return axios.get(`${API_URL}`);
};
export const getMyRating = () => {
  return axios.get(`${API_URL}/my`);
};
