import React from "react";

import axios from "axios";
export const API_URL = "http://localhost:8082/api/users";

export const login = async (user) => {
  return await axios.post(
    `${API_URL}/login`,
    {
      username: user.email,
      password: user.passwordHash,
    },
    {
      validateStatus: (status) => status < 500,
    }
  );
};

axios.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // Token expired or invalid
      sessionStorage.clear();
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);
