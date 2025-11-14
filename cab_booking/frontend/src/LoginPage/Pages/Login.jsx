import { Link, useNavigate } from "react-router-dom";
import Navbar from "../../common/Navbar";
import RideWithCall from "../Components/RideWithCall";
import React, { use, useEffect, useState } from "react";
import { login } from "../../api/LoginController";
import loginImage from "../../assets/images/login-cars1.png";
import UserHomePage from "../../UserPage/MainPages/UserHomePage";

import Header from "../../common/Header";
import UserContainer from "../../common/UserContainer";
import toast from "react-hot-toast";

const Login = () => {
  const navigate = useNavigate(null);
  const [user, setUser] = useState({ email: "", passwordHash: "" });

  const [error, setError] = useState("");
  const handleChange = (e) => {
    const { name, value } = e.target;
    setUser((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      console.log("Submitting:", user.email);
      const resp = await login(user);

      if (resp && resp.data != "Invalid credentials") {
        const { token, email, role, userId } = resp.data;
        sessionStorage.setItem("token", token);

        sessionStorage.setItem("role", role);

        toast.success("Login Success");

        if (sessionStorage.getItem("role") == "DRIVER") {
          navigate("/driver/rides");
        } else if (resp.data.role == "USER") {
          navigate("/user/homePage");
        } else if (resp.data.role == "ADMIN") {
          navigate("/admin");
        } else {
          setError("Inavalid Username and password");
          return;
        }
      } else {
        setError("User and email not matched");
      }
    } catch (error) {
      console.error("Login error:", error);
      setError("An error occurred during login.");
    }
  };

  return (
    <UserContainer>
      <Header />
      <div className="flex min-h-[calc(100vh-64px)] items-center">
        <div className="w-full flex flex-col p-4">
          <div className="flex flex-col gap-6 md:gap-10 justify-center items-center">
            <h1 className="text-2xl md:text-4xl text-center px-4">
              Access your personalized dashboard
            </h1>
            <h2 style={{ color: "red" }}>{error}</h2>
            <form
              onSubmit={handleSubmit}
              method="post"
              className="w-full max-w-md px-4"
            >
              <div className="flex flex-col gap-5 justify-center items-center">
                <div className="flex flex-col gap-5 items-center w-full">
                  <input
                    type="text"
                    name="email"
                    placeholder="Enter Your Email"
                    className="bg-blue-600 p-3 w-full text-white"
                    value={user.email}
                    onChange={handleChange}
                    required
                  />
                  <input
                    type="password"
                    name="passwordHash"
                    placeholder="Enter Your Password"
                    className="bg-blue-600 p-3 w-full text-white"
                    value={user.passwordHash}
                    onChange={handleChange}
                    required
                  />
                  <div className="flex flex-col sm:flex-row gap-4 sm:gap-5 w-full items-center">
                    <input
                      type="submit"
                      className="text-white bg-blue-600 px-3 py-2 cursor-pointer hover:bg-black hover:text-yellow-300 text-center"
                      value="Login"
                    />
                    <Link to={"/signup"} className="sec-btn text-center">
                      Don't have an account?
                    </Link>
                  </div>
                </div>
              </div>
            </form>
            <div className="flex justify-center items-center w-full px-4">
              <img
                src={loginImage}
                alt="Map"
                className="max-w-full h-auto md:max-w-lg"
              />
            </div>
          </div>
          <p className="ack text-sm md:text-base px-4 text-center mt-4">
            "By providing your phone number and clicking 'Sign Up to ride,' you
            consent to receive text messages from Auto Cars."
          </p>
        </div>
        {error == "" ? null : (
          <div className="fixed top-10 right-4 md:right-10 px-5 py-2 rounded bg-yellow-400">
            <p>{error}</p>
          </div>
        )}
      </div>
    </UserContainer>
  );
};

export default Login;
