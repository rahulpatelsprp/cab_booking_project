import UserContainer from "../../../common/UserContainer.jsx";
import Icon from "../../components/Icon/Icon.jsx";
import "./DriverProfilePage.css";
import React, { useState, useEffect } from "react";
import { getCurrentDriverDetails } from "../../../api/DriverController.jsx";
import EditProfile from "../../components/Popup/EditProfile.jsx";
import Header from "../../../common/Header.jsx";
import { useNavigate } from "react-router-dom";
import { getMyRating } from "../../../api/RatingController.jsx";

const DriverProfilePage = () => {
  const [driver, setDriver] = useState(null);
  const [rating,setRating]=useState(0);
  const navigate=useNavigate()
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await getCurrentDriverDetails();

        setDriver(response.data);
      } catch (err) {
        console.error("Error fetching driver profile:", err);
        setError("Failed to load profile. Please check your login status.");
      } finally {
        setLoading(false);
      }
    };

    fetchProfile();
  }, [isEditing]);

  useEffect(()=>{
    getMyRating().then(resp=>{
      console.log(resp)
      setRating(resp.data)})
  },[])

  const handleEditClick = () => {
    setIsEditing(true);
  };

  if (loading) {
    return (
      <UserContainer>
        <div className="page-centered">Loading profile...</div>
      </UserContainer>
    );
  }

  if (error) {
    return (
      <UserContainer>
        <div className="page-centered" style={{ color: "red" }}>
          Error: {error}
        </div>
      </UserContainer>
    );
  }

  if (!driver) {
    return (
      <UserContainer>
        <div className="page-centered">
          Profile data could not be retrieved.
        </div>
      </UserContainer>
    );
  }

  const { name, phone, email, vehicleDetails, driverSinceYear } = driver;

  // Example: Use first letters of name for placeholder if no avatar URL is provided
  const initials = (name || "NA")
    .split(" ")
    .map((n) => n[0])
    .join("");

  const ChangingStatus = () => {
    setIsEditing(false);
  };

  return (
    <UserContainer>
      <Header/>
      <div className="page-container page-centered">
        <div className="profile-card">
          <div className="profile-actions">
            {!isEditing && (
              <button className="edit-button" onClick={handleEditClick}>
                Edit Profile
              </button>
            )}
          </div>

          <div className="profile-content">
            <img
              src={`https://placehold.co/128x128/3B82F6/FFFFFF?text=${initials}`}
              alt={name || "User Avatar"}
              className="profile-avatar"
            />
            <h1>{name || "N/A"}</h1>

            <div className="profile-badges">
              <span className="badge">
                Driver Since {driverSinceYear || "N/A"}
              </span>
            </div>
            <div className="flex justify-around gap-2  border-1 border-neutral-200 p-2 rounded-2xl">
            <div className=" text-center rounded-lg bg-blue-100 w-30 p-6" >
              <p className="text-sm mb-2 text-neutral-600">My Rating</p>
              <p className="text-2xl font-bold mb-2"><i className="fa-solid fa-star text-amber-600"></i> {rating?rating.toFixed(1):5}</p>
            </div>
            <div className=" text-center rounded-lg bg-blue-100 w-30 p-4" onClick={()=>navigate("/driver/ratings")} >
              <p className="text-2xl mb-2 text-blue-900 mt-2"><i className="fa-solid fa-clock-rotate-left"></i></p>
              <p className="text-sm mb-2 text-neutral-600">Feedbacks</p>
            </div>
            </div>

            <div className="profile-details">
              <div className="detail-item">
                <p className="label">Mobile Number</p>
                <p>- {phone || "N/A"}</p>
              </div>
              <div className="detail-item">
                <p className="label">Email Address</p>
                <p>- {email || "N/A"}</p>
              </div>
            </div>

            <div className="profile-tags">
              {/* Dynamic Car Number */}
              <span className="tag">{vehicleDetails || "N/A Car"}</span>
            </div>
          </div>
        </div>
      </div>
      {isEditing && (
        <EditProfile EditingStatus={ChangingStatus} driverDetails={driver} />
      )}
    </UserContainer>
  );
};

export default DriverProfilePage;
