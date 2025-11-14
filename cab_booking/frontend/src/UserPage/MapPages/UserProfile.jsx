import React, { useEffect, useState } from "react";
import icon1 from "../../assets/images/userIcons/icon1.png";
import Header from "../../common/Header";
import UserContainer from "../../common/UserContainer";
import { getCurrentUser, updateUser } from "../../api/UserController";
import { useNavigate } from "react-router-dom";
import { getMyRating } from "../../api/RatingController";
import toast from "react-hot-toast";

export default function UserProfile() {
  const [userData, setUserData] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [rating, setRating] = useState(0);
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    name: "",
    phone: "",
    email: "",
    passwordHash: "",
  });

  useEffect(() => {
    async function get() {
      const resp = await getCurrentUser();
      const data = resp.data;
      setUserData({ ...data });
      setFormData({
        userId: data.userId,
        name: data.name,
        phone: data.phone,
        email: data.email,
        passwordHash: "",
      });
    }
    get();
  }, [isEditing]);

  useEffect(() => {
    getMyRating().then((resp) => {
      console.log(resp);
      setRating(resp.data);
    });
  }, []);

  const handleChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  async function handleUpdate() {
    const resp = await updateUser(formData);
    if (resp && resp.status == 200)
      toast.success("Details have been Updated successfully");
    setIsEditing(false);
  }

  return (
    <UserContainer>
      <Header msgBody={"My Profile"} />
      {userData == null ? (
        <div className="text-2xl text-center my-10">Loading...</div>
      ) : (
        <div className="flex items-center justify-center">
          <div className="border-2 border-neutral-300 md:p-16 p-8 md:px-24 rounded-2xl relative mb-16 w-[95%] md:w-auto">
            {!isEditing ? (
              <button
                className="bg-amber-300 py-2 px-4 rounded-2xl absolute top-4 right-4"
                onClick={() => setIsEditing(true)}
              >
                Edit
              </button>
            ) : (
              <button
                className="bg-red-500 text-white h-8 w-8 rounded-full absolute top-4 right-4"
                onClick={() => setIsEditing(false)}
              >
                <i className="fa-solid fa-xmark"></i>
              </button>
            )}

            <div className="flex items-center mb-8 mt-4 justify-around lg:w-68">
              <img className="mb-4 w-[100px]" src={icon1} alt="UserIcon" />
              <div className="text-center">
                {isEditing ? (
                  <input
                    type="text"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    className="text-2xl lg:text-3xl mb-4 text-center border-b border-gray-400 md:w-30 w-20"
                  />
                ) : (
                  <h2 className="lg:text-4xl text-2xl mb-2">{userData.name}</h2>
                )}
                <p className="text-neutral-500 text-sm">{userData.role}</p>
              </div>
            </div>

            {!isEditing && (
              <div className="flex justify-around gap-2  border-1 border-neutral-200 p-2 rounded-2xl mb-8">
                <div className=" text-center rounded-lg bg-amber-100 w-30 p-6">
                  <p className="text-sm mb-2 text-neutral-600">My Rating</p>
                  <p className="text-2xl font-bold mb-2">
                    <i className="fa-solid fa-star text-amber-600"></i>{" "}
                    {rating && rating?.toFixed(1)}
                  </p>
                </div>
                <div
                  className=" text-center rounded-lg bg-amber-100 w-30 p-4"
                  onClick={() => navigate("/user/ratings")}
                >
                  <p className="text-2xl mb-2 text-amber-900 mt-2">
                    <i className="fa-solid fa-clock-rotate-left"></i>
                  </p>
                  <p className="text-sm mb-2 text-neutral-600">Feedbacks</p>
                </div>
              </div>
            )}

            <div className="flex gap-4">
              <i className="fa-solid fa-phone pt-4"></i>
              <dl className="mb-8">
                <dt className="text-sm">Mobile Number</dt>
                <dd className="text-xl">
                  {isEditing ? (
                    <input
                      type="text"
                      name="phone"
                      value={formData.phone}
                      onChange={handleChange}
                      className="border-b border-gray-400 text-sm md:w-40"
                    />
                  ) : (
                    userData.phone
                  )}
                </dd>
              </dl>
            </div>

            <div className="flex gap-4">
              <i className="fa-solid fa-envelope pt-4"></i>
              <dl>
                <dt className="text-sm">Email Address</dt>
                <dd className="text-xl">
                  {isEditing ? (
                    <input
                      type="email"
                      name="email"
                      value={formData.email}
                      onChange={handleChange}
                      className="border-b border-gray-400 md:w-40 text-sm"
                    />
                  ) : (
                    userData.email
                  )}
                </dd>
              </dl>
            </div>

            {isEditing && (
              <>
                <div className="flex gap-4 mt-8">
                  <i className="fa-solid fa-key pt-4"></i>
                  <dl>
                    <dt className="text-sm">Change Password</dt>
                    <dd className="text-xl">
                      <input
                        type="password"
                        name="passwordHash"
                        value={formData.passwordHash}
                        placeholder="New Password"
                        onChange={handleChange}
                        className="border-b border-gray-400 md:w-40 text-sm"
                      />
                    </dd>
                  </dl>
                </div>
                <div className="flex flex-col justify-end">
                  <button
                    className="bg-emerald-300 py-2 px-4 mt-8 rounded-lg"
                    onClick={() => handleUpdate()}
                  >
                    Save
                  </button>
                </div>
              </>
            )}
          </div>
        </div>
      )}
    </UserContainer>
  );
}
``;
