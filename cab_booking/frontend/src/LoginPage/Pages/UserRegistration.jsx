import React, { useState } from "react";
import { Link } from "react-router-dom";
import RiderImage from "../../assets/images/rider-img.png";
import { register } from "../../api/UserController";
import {
  validMob,
  validEmail,
  validName,
  validPassword,
} from "../Components/Regex";
import Header from "../../common/Header";
import UserContainer from "../../common/UserContainer";

const SignUp = () => {
  const [user, setUser] = useState({
    name: "",
    phone: "",
    email: "",
    passwordHash: "",
  });

  const [msg, setMsg] = useState({
    nameMsg: "",
    phoneMsg: "",
    emailMsg: "",
    pwordMsg: "",
  });
  const [status, setStatus] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    setMsg((c) => ({
      ...c,
      successMsg: "",
      errorMsg: "",
      nameMsg: "",
      phoneMsg: "",
      emailMsg: "",
      pwordMsg: "",
    }));

    let hasError = false;

    if (!validName.test(user.name)) {
      const msgText = "Name should contain atleast 3 characters";
      setMsg((prev) => ({ ...prev, nameMsg: msgText }));
    } else {
      setMsg({ namMsg: "", phoneMsg: "", emailMsg: "", pwordMsg: "" });
      console.log("user registration start");
      setStatus(true);
      // console.log({user})
      setUser({ name: "", phone: "", email: "", passwordHash: "" });
      register(user).then((save) => {
        console.log(save);
        if (save && save.data == "SavedUser") {
          setMsg((c) => ({ ...c, namMsg: "User is Rigister Succesfully" }));
          console.log("Registration successfully");
          console.log(save.data);
        } else {
          setMsg((c) => ({ ...c, namMsg: "User is Already Found...." }));
          console.log("User is Already Found....");
        }
      });
    }
  };

  const handleChangeName = (e) => {
    const rawValue = e.target.value;
    const textValue = rawValue.replace(/[0-9]/g, "");

    setUser((prev) => ({ ...prev, name: textValue }));
    if (textValue == "") {
      setMsg((prev) => ({ ...prev, nameMsg: "" }));
      return;
    }
  };

  const handleChangePhone = (e) => {
    const rawValue = e.target.value;
    const numericValue = rawValue.replace(/[^0-9]/g, "");

    setUser((prev) => ({ ...prev, phone: numericValue }));
    if (numericValue == "") {
      setMsg((prev) => ({ ...prev, phoneMsg: "" }));
      return;
    }
    const msgText = validMob.test(numericValue) ? "" : "Invalid Mobile number";
    setMsg((prev) => ({ ...prev, phoneMsg: msgText }));
  };

  const handleChangeEmail = (e) => {
    setUser((prev) => ({ ...prev, email: e.target.value }));
    if (e.target.value == "") {
      setMsg((prev) => ({ ...prev, emailMsg: "" }));
      return;
    }
    const msgText = validEmail.test(e.target.value) ? "" : "Invalid Email";
    setMsg((prev) => ({ ...prev, emailMsg: msgText }));
  };

  const handleChangePassword = (e) => {
    let pmsg = "";
    setUser((prev) => ({ ...prev, passwordHash: e.target.value }));
    if (e.target.value == "") {
      setMsg((prev) => ({ ...prev, pwordMsg: "" }));
      return;
    }
    const hasSLetter = new RegExp("[a-z]");
    const hasLLetter = new RegExp("[A-Z]");
    const hasNumber = new RegExp("[0-9]");
    const hasMinLength = new RegExp(".{5,}");

    if (!hasMinLength.test(e.target.value)) {
      pmsg = "Password must be at least 5 characters long.";
    } else if (!hasLLetter.test(e.target.value)) {
      pmsg = "Password must contain at least one capital letter.";
    } else if (!hasSLetter.test(e.target.value)) {
      pmsg = "Password must contain at least one capital letter.";
    } else if (!hasNumber.test(e.target.value)) {
      pmsg = "Password must contain at least one number.";
    } else {
      pmsg = null;
    }

    setMsg((prev) => ({ ...prev, pwordMsg: pmsg }));
  };

  const statusMessage = msg.successMsg || msg.errorMsg;
  const isSuccess = !!msg.successMsg;

  return (
    <UserContainer>
      <Header />
      <div className="flex">
        <div className="w-full h-screen flex flex-col justify-between">
          <form
            onSubmit={handleSubmit}
            className="flex gap-20 justify-center items-center"
          >
            <div>
              <img
                src={RiderImage}
                alt="Rider Image"
                className="border-2 rounded-t-full w-96"
              />
            </div>

            <div className="flex flex-col gap-10">
              <h1 className="text-4xl">Travel with Trust and Ease</h1>
              <div className="flex flex-col gap-5">
                <div className="flex gap-5">
                  <div className="flex flex-col w-50">
                    <input
                      type="text"
                      placeholder="Enter Your Name"
                      className="bg-blue-600 p-3  text-white"
                      onChange={handleChangeName}
                      value={user.name}
                      name="name"
                      required
                    />
                    {msg.nameMsg && (
                      <p className="text-red-500">{msg.nameMsg}</p>
                    )}
                  </div>
                  <div className="flex flex-col w-50">
                    <input
                      type="text"
                      placeholder="Enter Mobile Number"
                      className="bg-blue-600 p-3  text-white"
                      onChange={handleChangePhone}
                      value={user.phone}
                      name="phone"
                      required
                    />
                    {msg.phoneMsg && (
                      <p className="text-red-500">{msg.phoneMsg}</p>
                    )}
                  </div>
                </div>
                <div className="flex flex-col gap-5 w-105">
                  <div className="flex flex-col w-full">
                    <input
                      type="email"
                      placeholder="Enter Email Address"
                      className="bg-blue-600 p-3  text-white"
                      onChange={handleChangeEmail}
                      value={user.email}
                      name="email"
                      required
                    />
                    {msg.emailMsg && (
                      <p className="text-red-500">{msg.emailMsg}</p>
                    )}
                  </div>
                  <div className="flex flex-col w-full">
                    <input
                      type="password"
                      placeholder="Set up your password"
                      className="bg-blue-600 p-3  text-white"
                      onChange={handleChangePassword}
                      value={user.passwordHash}
                      name="passwordHash"
                      required
                    />
                    {msg.pwordMsg && (
                      <p className="text-red-500 flex">{msg.pwordMsg}</p>
                    )}
                  </div>
                </div>

                <div className="flex gap-5 justify-end">
                  <Link to={"/apply"} className="sec-btn">
                    Become a Driver ?
                  </Link>
                  <button className="text-white bg-blue-600 px-3 py-1 cursor-pointer hover:bg-black hover:text-white">
                    Sign up to Ride
                  </button>
                </div>

                {status && (
                  <p className="text-green-500 w-full flex justify-center">
                    {msg.nameMsg}
                  </p>
                )}
              </div>
            </div>
          </form>

          <p className="ack">
            "By providing your phone number and clicking 'Sign Up to ride,' you
            consent to receive text messages from Auto Cars. Text messages may
            be autodialed, and standard messaging rates may apply. The frequency
            of text messages varies. You may text STOP to cancel at any time.
            Your participation is subject to Auto Cars' terms and conditions.
            Visit our website for more details."
          </p>
        </div>
      </div>
    </UserContainer>
  );
};

export default SignUp;
