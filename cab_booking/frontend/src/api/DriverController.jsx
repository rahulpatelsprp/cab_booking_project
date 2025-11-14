import React, { use } from "react";

import axios from "axios";

export const API_URL = "http://localhost:8082/api/drivers";

export const registerDriver = async (driver) => {
  return axios.post(`${API_URL}/register`, driver);
};

export const allDriversDetails = async () => {
  return axios.get(`${API_URL}`);
};

export const getCurrentDriverDetails = async () => {
  return axios.get(`${API_URL}/profile`);
};

export const updateDriverProfile = async (updatedData) => {
  return axios.put(`${API_URL}`, updatedData);
};
