import { Link, useNavigate } from "react-router-dom";
import Header from "../../common/Header";
import taxiImage from "../../assets/images/taxi.png";
import React, { useEffect, useContext } from "react";

const HomePage = () => {
  const navigate = useNavigate();
  useEffect(() => {
    const temp = sessionStorage.getItem("token");
    const role = sessionStorage.getItem("role");
    if (temp != null) {
      if (role == "DRIVER") {
        navigate("/driver/rides");
      } else if (role == "USER") {
        navigate("/user/homePage");
      } else if (role == "ADMIN") {
        navigate("/admin");
      }
    }
  }, []);
  return (
    <div className="flex">
      <div className="w-full h-screen bg-white flex flex-col justify-between">
        <Header msgBody={"Join Us"} />
        <div className="w-full flex flex-col gap-6 sm:gap-10 items-center mt-10 sm:mt-20 px-4">
          <h1 className="text-2xl sm:text-4xl font-medium text-center">
            Your Journey, Your Control
          </h1>
          <div className="flex flex-col sm:flex-row gap-4 sm:gap-10">
            <Link
              to={"/login"}
              className="text-white bg-blue-600 px-4 sm:px-5 py-2 sm:py-3 cursor-pointer hover:bg-black hover:text-white text-lg sm:text-xl text-center"
            >
              Login
            </Link>
            <Link
              to={"/signup"}
              className="text-black px-4 sm:px-5 py-2 sm:py-3 cursor-pointer hover:bg-black hover:text-yellow-300 text-lg sm:text-xl text-center"
            >
              Register
            </Link>
          </div>
        </div>
        <Link
          to={"/availableLocation"}
          className="border-b-2 border-amber-100 hover:border-amber-500 mx-auto w-max text-center transition px-4"
        >
          Available Locations
        </Link>
        <div className="w-full flex justify-center px-4">
          <img src={taxiImage} alt="Taxi" className="w-48 sm:w-80" />
        </div>
      </div>
    </div>
  );
};

export default HomePage;
