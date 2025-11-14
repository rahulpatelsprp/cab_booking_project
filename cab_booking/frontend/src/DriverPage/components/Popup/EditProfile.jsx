import React, { useEffect, useState } from "react";
import { updateDriverProfile } from "../../../api/DriverController.jsx";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";

const EditProfile = ({ EditingStatus, driverDetails }) => {
  const navigate = useNavigate();
  const inputStyle =
    "mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 text-base";
  const buttonBaseStyle =
    "py-2 px-4 rounded-lg font-medium transition-colors duration-200";

  const [formData, setFormData] = driverDetails
    ? useState({
        name: driverDetails.name || "",
        phone: driverDetails.phone || "",
        email: driverDetails.email || "",
        vehicleDetails: driverDetails.vehicleDetails || "",
        licenseNumber: driverDetails.licenseNumber || "",
        passwordHash: "",
      })
    : useState({
        name: "",
        phone: "",
        email: "",
        vehicleDetails: "",
        licenseNumber: "",
        passwordHash: "",
      });

  const ClosePopup = () => {
    EditingStatus(false);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
  };

  const FormSubmit = async (e) => {
    e.preventDefault();
    updateDriverProfile(formData).then((response) => {
      if (response.status === 200) {
        toast.success("updated successfully");
        ClosePopup();
      } else {
        toast.error("Failed to update profile");
      }
    });
  };

  return (
    <div
      className="fixed inset-0 bg-gray-900 bg-opacity-70 backdrop-blur-sm z-50 flex justify-center items-center p-4"
      onClick={ClosePopup}
    >
      <div
        className="bg-white rounded-xl shadow-2xl w-full max-w-md p-6 sm:p-8 transform transition-all"
        onClick={(e) => e.stopPropagation()}
      >
        <h2 className="text-2xl font-bold text-gray-900 mb-6 border-b pb-3">
          Edit Profile Details
        </h2>

        <div className="mb-4 p-3 bg-red-100 text-red-700 rounded-lg text-sm font-medium hidden">
          Validation messages go here.
        </div>

        <form onSubmit={FormSubmit}>
          <div className="space-y-4">
            <div>
              <label
                htmlFor="name"
                className="block text-sm font-medium text-gray-700"
              >
                Name
              </label>
              <input
                type="text"
                name="name"
                id="name"
                placeholder="Enter full name"
                className={inputStyle}
                value={formData.name}
                onChange={handleChange}
              />
            </div>

            <div>
              <label
                htmlFor="phone"
                className="block text-sm font-medium text-gray-700"
              >
                Mobile Number
              </label>
              <input
                type="text"
                name="phone"
                id="phone"
                placeholder="Enter mobile number"
                className={inputStyle}
                value={formData.phone}
                onChange={handleChange}
              />
            </div>

            <div>
              <label
                htmlFor="email"
                className="block text-sm font-medium text-gray-700"
              >
                Email Address
              </label>
              <input
                type="email"
                name="email"
                id="email"
                placeholder="Enter email address"
                className={inputStyle}
                value={formData.email}
                onChange={handleChange}
                disabled
              />
            </div>

            <div>
              <label
                htmlFor="vehicleDetails"
                className="block text-sm font-medium text-gray-700"
              >
                Vehicle/Car Number
              </label>
              <input
                type="text"
                name="vehicleDetails"
                id="vehicleDetails"
                placeholder="Enter vehicle number"
                className={inputStyle}
                value={formData.vehicleDetails}
                onChange={handleChange}
              />
            </div>

            <div>
              <label
                htmlFor="name"
                className="block text-sm font-medium text-gray-700"
              >
                License Number
              </label>
              <input
                type="text"
                name="licenseNumber"
                id="licenseNumber"
                placeholder="Enter the License Number"
                className={inputStyle}
                value={formData.licenseNumber}
                onChange={handleChange}
              />
            </div>

            <div>
              <label
                htmlFor="passwordHash"
                className="block text-sm font-medium text-gray-700"
              >
                New Password (Optional)
              </label>
              <input
                type="password"
                name="passwordHash"
                id="passwordHash"
                placeholder="••••••••"
                className={inputStyle}
                value={formData.passwordHash}
                onChange={handleChange}
              />
              <p className="mt-1 text-xs text-gray-500">
                Leave blank to keep current password.
              </p>
            </div>
          </div>

          <div className="mt-8 flex justify-end space-x-3">
            <button
              type="button"
              onClick={ClosePopup}
              className={`${buttonBaseStyle} bg-gray-300 text-gray-800 hover:bg-gray-400`}
            >
              Cancel
            </button>
            <button
              type="submit"
              className={`${buttonBaseStyle} bg-indigo-600 text-white hover:bg-indigo-700`}
            >
              Save Profile
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EditProfile;
