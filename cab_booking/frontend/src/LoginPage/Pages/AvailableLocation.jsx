import React from "react";
import Navbar from "../../common/Navbar";
import UserContainer from "../../common/UserContainer";
import Header from "../../common/Header";

const userLocation = "RS Puram";
const AvailableLocation = () => {
  const supportedLocations = [
    "IT Park",
    "Prozone Mall",
    "GandhiPuram",
    "Keerantham",
    "Rs Puram",
  ];
  const isAvailable = supportedLocations.includes(userLocation);

  return (
    <UserContainer>
      <Header />
      <div className="flex">
        <div className="w-full flex flex-col items-center justify-center px-6 py-10">
          <div className="w-full max-w-3xl p-8 bg-white rounded-2xl shadow-2xl border border-gray-200 text-center animate-fade-in">
            <h2 className="text-4xl font-extrabold text-blue-700 mb-6">
              Coimbatore Service Zones
            </h2>

            <div className="mb-6">
              {isAvailable ? (
                <p className="text-green-600 text-xl font-semibold">
                  Service is available in your location:{" "}
                  <span className="underline">{userLocation}</span>
                </p>
              ) : (
                <p className="text-red-600 text-xl font-semibold">
                  Service is not available in your location:{" "}
                  <span className="underline">{userLocation}</span>
                </p>
              )}
            </div>

            <h3 className="text-xl font-semibold text-gray-800 mb-4">
              Top {supportedLocations.length} Famous Locations
            </h3>

            <ul className="grid grid-cols-2 sm:grid-cols-3 gap-4 text-left">
              {supportedLocations.map((location, index) => (
                <li
                  key={index}
                  className={`p-3 rounded-lg border ${
                    location === userLocation
                      ? "bg-green-100 border-green-400 font-bold"
                      : "bg-gray-100 hover:bg-gray-200"
                  } transition duration-300`}
                >
                  {location}
                </li>
              ))}
            </ul>
          </div>
        </div>
      </div>
    </UserContainer>
  );
};

export default AvailableLocation;
