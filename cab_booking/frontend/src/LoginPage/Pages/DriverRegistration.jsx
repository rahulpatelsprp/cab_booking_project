import { Link } from "react-router-dom";
import React, { useState } from "react";
import DriverImage from "../../assets/images/driver-img.png";
import {
  validMob,
  validEmail,
  validName,
  validPassword,
  validLnumber,
} from "../Components/Regex";
import { registerDriver } from "../../api/DriverController";
import Header from "../../common/Header";
import UserContainer from "../../common/UserContainer";

const Apply = () => {
  const [user, setUser] = useState({
    name: "",
    phone: "",
    vehicleDetails: "",
    email: "",
    licenseNumber: "",
    passwordHash: "",
  });
  const [msg, setMsg] = useState({
    nameMsg: "",
    mobMsg: "",
    mailMsg: "",
    LnoMsg: "",
    pwordMsg: "",
    successMsg: "",
    errorMsg: "",
  });

  const handleSubmit = async (e) => {
    e.preventDefault();

    setMsg((c) => ({
      ...c,
      successMsg: "",
      errorMsg: "",
      nameMsg: "",
      mobMsg: "",
      mailMsg: "",
      LnoMsg: "",
      pwordMsg: "",
    }));

    let hasError = false;

    if (!validName.test(user.name)) {
      setMsg((prev) => ({ ...prev, nameMsg: "Name invalid (min 3 chars)" }));
      hasError = true;
    }
    if (!validMob.test(user.phone)) {
      setMsg((prev) => ({
        ...prev,
        mobMsg: "Invalid Mobile number (10 digits)",
      }));
      hasError = true;
    }
    if (!validEmail.test(user.email)) {
      setMsg((prev) => ({ ...prev, mailMsg: "Invalid Email format" }));
      hasError = true;
    }
    if (!validLnumber.test(user.licenseNumber)) {
      setMsg((prev) => ({ ...prev, LnoMsg: "Invalid License Number" }));
      hasError = false;
    }
    // Check password message state to see if it's currently invalid
    if (msg.pwordMsg && msg.pwordMsg !== null) {
      hasError = true;
    }

    if (hasError) return;

    try {
      const save = await registerDriver(user);

      if (save && save.data) {
        setMsg((c) => ({
          ...c,
          successMsg: "Driver Registration Successful.",
        }));
        setUser({
          name: "",
          phone: "",
          vehicleDetails: "",
          email: "",
          licenseNumber: "",
          passwordHash: "",
        });
      } else {
        setMsg((c) => ({
          ...c,
          errorMsg:
            save.data.message || "Registration failed. Check your details.",
        }));
      }
    } catch (error) {
      setMsg((c) => ({
        ...c,
        errorMsg: "An unexpected error occurred during application.",
      }));
    }
  };

  const handleChangeName = (e) => {
    const rawValue = e.target.value;
    const textValue = rawValue.replace(/[0-9]/g, "");
    setUser((prev) => ({ ...prev, name: textValue }));
    const validationMsg =
      textValue === "" || validName.test(textValue)
        ? ""
        : "Name invalid (min 3 chars)";
    setMsg((prev) => ({
      ...prev,
      nameMsg: validationMsg,
      successMsg: "",
      errorMsg: "",
    }));
  };

  const handleChangePhone = (e) => {
    const rawValue = e.target.value;
    const numericValue = rawValue.replace(/[^0-9]/g, "").slice(0, 10);
    setUser((prev) => ({ ...prev, phone: numericValue }));
    const validationMsg =
      numericValue === "" || validMob.test(numericValue)
        ? ""
        : "Invalid Mobile Number";
    setMsg((prev) => ({
      ...prev,
      mobMsg: validationMsg,
      successMsg: "",
      errorMsg: "",
    }));
  };

  const handleChangeVmodel = (e) => {
    setUser((prev) => ({ ...prev, vehicleDetails: e.target.value }));
  };

  const handleChangeEmail = (e) => {
    setUser((prev) => ({ ...prev, email: e.target.value }));
    const validationMsg =
      e.target.value === "" || validEmail.test(e.target.value)
        ? ""
        : "Invalid Email";
    setMsg((prev) => ({
      ...prev,
      mailMsg: validationMsg,
      successMsg: "",
      errorMsg: "",
    }));
  };

  const handleChangeLno = (e) => {
    setUser((prev) => ({ ...prev, licenseNumber: e.target.value }));
    const validationMsg =
      e.target.value === "" || validLnumber.test(e.target.value)
        ? ""
        : "Invalid License Number";
    setMsg((prev) => ({
      ...prev,
      LnoMsg: validationMsg,
      successMsg: "",
      errorMsg: "",
    }));
  };

  const handleChangePassword = (e) => {
    let pmsg = "";
    const value = e.target.value;
    setUser((prev) => ({ ...prev, passwordHash: value }));

    if (value.length > 0) {
      if (!value.match(new RegExp(".{5,}"))) {
        pmsg = "Password must be at least 5 characters long.";
      } else if (!value.match(new RegExp("[A-Z]"))) {
        pmsg = "Must contain at least one capital letter.";
      } else if (!value.match(new RegExp("[a-z]"))) {
        pmsg = "Must contain at least one small letter.";
      } else if (!value.match(new RegExp("[0-9]"))) {
        pmsg = "Must contain at least one number.";
      } else {
        pmsg = null;
      }
    }
    setMsg((prev) => ({
      ...prev,
      pwordMsg: pmsg,
      successMsg: "",
      errorMsg: "",
    }));
  };

  const statusMessage = msg.successMsg || msg.errorMsg;
  const isSuccess = !!msg.successMsg;

  return (
    <UserContainer>
      <Header />
      <div className="flex flex-col min-h-[calc(100vh-70px)] justify-center items-center py-4 sm:py-6 bg-gray-50">
        <form
          onSubmit={handleSubmit}
          className="flex flex-col lg:flex-row lg:gap-20 justify-center items-center w-full max-w-6xl mx-auto flex-grow px-4 sm:px-6"
        >
          <div className="flex flex-col gap-5 lg:gap-10 w-full max-w-md order-1 lg:order-1">
            <h1 className="text-2xl sm:text-3xl lg:text-4xl text-center lg:text-left font-medium text-blue-600 mt-10">
              Drive. Earn. Enjoy. Repeat.
            </h1>

            <div className="flex flex-col gap-4 w-full">
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 w-full">
                <div className="w-full flex flex-col">
                  <input
                    type="text"
                    placeholder="Enter Your Name"
                    className="bg-blue-500 p-3 text-white w-full placeholder-gray-200 rounded-lg focus:outline-none focus:ring-4 focus:ring-yellow-300 shadow-md transition duration-200"
                    onChange={handleChangeName}
                    name="name"
                    value={user.name}
                    required
                  />
                  {msg.nameMsg && (
                    <p className="text-yellow-600 text-xs mt-1 font-semibold">
                      {msg.nameMsg}
                    </p>
                  )}
                </div>

                <div className="w-full">
                  <input
                    type="tel"
                    placeholder="Enter Mobile Number"
                    className="bg-blue-500 p-3 text-white w-full placeholder-gray-200 rounded-lg focus:outline-none focus:ring-4 focus:ring-yellow-300 shadow-md transition duration-200"
                    onChange={handleChangePhone}
                    name="phone"
                    value={user.phone}
                    required
                  />
                  {msg.mobMsg && (
                    <p className="text-yellow-600 text-xs mt-1 font-semibold">
                      {msg.mobMsg}
                    </p>
                  )}
                </div>
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 w-full">
                <div className="w-full">
                  <input
                    type="email"
                    placeholder="Enter Email Address"
                    className="bg-blue-500 p-3 text-white w-full placeholder-gray-200 rounded-lg focus:outline-none focus:ring-4 focus:ring-yellow-300 shadow-md transition duration-200"
                    onChange={handleChangeEmail}
                    name="email"
                    value={user.email}
                    required
                  />
                  {msg.mailMsg && (
                    <p className="text-yellow-600 text-xs mt-1 font-semibold">
                      {msg.mailMsg}
                    </p>
                  )}
                </div>

                <div className="w-full">
                  <input
                    type="password"
                    placeholder="Set up the Password"
                    className="bg-blue-500 p-3 text-white w-full placeholder-gray-200 rounded-lg focus:outline-none focus:ring-4 focus:ring-yellow-300 shadow-md transition duration-200"
                    onChange={handleChangePassword}
                    name="passwordHash"
                    value={user.passwordHash}
                    required
                  />
                  {msg.pwordMsg && msg.pwordMsg !== null && (
                    <p className="text-yellow-600 text-xs mt-1 font-semibold">
                      {msg.pwordMsg}
                    </p>
                  )}
                </div>
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 w-full">
                <div className="w-full">
                  <input
                    type="text"
                    placeholder="Driver License Number"
                    className="bg-blue-500 p-3 text-white w-full placeholder-gray-200 rounded-lg focus:outline-none focus:ring-4 focus:ring-yellow-300 shadow-md transition duration-200"
                    onChange={handleChangeLno}
                    name="licenseNumber"
                    value={user.licenseNumber}
                    required
                  />
                  {msg.LnoMsg && (
                    <p className="text-yellow-600 text-xs mt-1 font-semibold">
                      {msg.LnoMsg}
                    </p>
                  )}
                </div>
                {/* Vehicle Model */}
                <div className="w-full">
                  <input
                    type="text"
                    placeholder="Vehicle Model"
                    className="bg-blue-500 p-3 text-white w-full placeholder-gray-200 rounded-lg focus:outline-none focus:ring-4 focus:ring-yellow-300 shadow-md transition duration-200"
                    onChange={handleChangeVmodel}
                    name="vehicleDetails"
                    value={user.vehicleDetails}
                  />
                </div>
              </div>

              {/* Buttons: Side-by-side on all screens for clean horizontal alignment */}
              <div className="flex gap-4 pt-2 w-full justify-start">
                <button
                  type="submit"
                  className="text-white bg-blue-500 px-2 py-2 cursor-pointer hover:bg-black hover:text-yellow-300 rounded-lg font-bold transition duration-300 w-1/2 sm:w-auto shadow-lg"
                >
                  Apply to Drive
                </button>
                <Link
                  to={"/SignUp"}
                  className="sec-btn text-center text-blue-600 border-2 border-blue-600 px-2 py-2 rounded-lg hover:bg-blue-50 transition duration-200 w-1/2 sm:w-auto font-medium shadow-sm"
                >
                  Become a Rider ?
                </Link>
              </div>

              {/* Status messages */}
              {statusMessage && (
                <p
                  className={`text-center py-1 text-sm rounded-lg font-bold ${
                    isSuccess
                      ? "text-green-600 bg-green-100"
                      : "text-red-600 bg-red-100"
                  }`}
                >
                  {statusMessage}
                </p>
              )}
            </div>
          </div>

          {/* Image Section: Order 2 on mobile, Order 1 on laptop (to put image on the right) */}
          <div className="w-fit flex justify-center p-2 max-h-[40vh] lg:max-h-[70vh] lg:order-2">
            <img
              src={DriverImage}
              alt="Driver-img"
              // Image sizing adjusts: smaller on mobile, larger on laptop
              className="border-4 border-yellow-300 rounded-t-full w-48 h-auto sm:w-64 lg:w-96 shadow-2xl object-contain transition duration-300"
            />
          </div>
        </form>

        {/* Footer (Simplified and kept minimal) */}
        <p className="ack text-center text-xs text-gray-500 mt-10 w-full px-4 pt-2">
          "By providing your phone number and clicking 'Apply to Drive,' you
          consent to receive text messages from Auto Cars."
        </p>
      </div>
    </UserContainer>
  );
};

export default Apply;
