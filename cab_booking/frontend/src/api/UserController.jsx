import React, { use } from "react";

import axios from "axios";
export const API_URL = "http://localhost:8082/api/users";

export const register = async (user) => {
  return axios.post(`${API_URL}/register`, user);
};

export const allUserDetails = async () => {
  return axios.get(`${API_URL}`);
};

export const getCurrentUser=()=>{
  return axios.get(`${API_URL}/current`);
}
export const updateUser= async (user)=>{
  if(user.passwordHash)user.passwordHash.trim()
      if(user.passwordHash=="")
        delete user.passwordHash
      const resp=await axios.put(`${API_URL}`,user)
      return resp;
      
}
