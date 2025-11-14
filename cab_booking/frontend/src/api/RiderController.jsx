import axios from "axios";

export const API_URL = "http://localhost:8082/api/rides";


export const register = async (ride) => {
  return axios.post(`${API_URL}/register`, ride);
};
export const allRide = async () => {
  return axios.get(`${API_URL}`);
};

export const allRequestedRide = async () => {
    const result = await axios.get(`${API_URL}/byStatus/PENDING`);
    return result;
};

export const getAllRidesByUserId = () => {
  return axios.get(`${API_URL}/user`);
};

export const getAllRidesByDriverId = () => {
  return axios.get(`${API_URL}/driver`);
};

export const acceptRequestRide = async (ride) => {
  return axios.put(`${API_URL}/accept`, ride);
};

export const getUserByrideId = async (rideId) => {
  return await axios.get(
    `${API_URL}/userByRiderId/${rideId}`);
};

export const getRideByrideId = async (rideId) => {
  return await axios.get(`${API_URL}/${rideId}`);
};

export const getDriverByrideId = async (rideId) => {
  return await axios.get(
    `${API_URL}/driverByRiderId/${rideId}`
  );
};

export const changeRideStatus = async (ride, status) => {
  return axios.put(`${API_URL}/byStatus/${status}`, ride);
};

export const getPendingRide=()=>{
  return axios.get(`${API_URL}/pending`);
}