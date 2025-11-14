import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import "./api/Interceptors.js";
import "./App.css";

// Pages
import HomePage from "./LoginPage/Pages/HomePage.jsx";
import Login from "./LoginPage/Pages/Login.jsx";
import SignUp from "./LoginPage/Pages/UserRegistration.jsx";
import Apply from "./LoginPage/Pages/DriverRegistration.jsx";
import AvailableLocation from "./LoginPage/Pages/AvailableLocation.jsx";

// User Pages
import UserHomePage from "./UserPage/MainPages/UserHomePage.jsx";
import StartRide from "./UserPage/MapPages/StartRide.jsx";
import DriverList from "./UserPage/MapPages/DriverList.jsx";
import UserProfile from "./UserPage/MapPages/UserProfile.jsx";
import RideHistory from "./UserPage/MapPages/RideHistory.jsx";
import UserLoadingPage from "./UserPage/MainPages/UserLoading/UserLoadingPage.jsx";

// Driver Pages
import DriverLocationPage from "./DriverPage/pages/DriverLocationPage/DriverLocationPage.jsx";
import RidesMenuPage from "./DriverPage/pages/RidesMenuPage/RidesMenuPage.jsx";
import DriverProfilePage from "./DriverPage/pages/DriverProfilePage/DriverProfilePage.jsx";
import DriverStartRide from "./DriverPage/pages/DriverStartRide/DriverStartRide.jsx";

// Admin Page
import AdminDashboard from "./admin/adminDashboard.jsx";

import ProtectedLayout from "./ProtectedLayout.jsx";
import Payment from "./UserPage/MapPages/Payment.jsx";
import PaymentWaitingPage from "./DriverPage/pages/WaitingPage/PaymentWaitingPage.jsx";
import Popup from "./DriverPage/components/Popup/Rating.jsx";
import RatingHistory from "./UserPage/MapPages/RatingHistory.jsx";
import { Toaster } from "react-hot-toast";

function App() {
  return (
    <BrowserRouter>
      <Toaster position="top-center" />
      <Routes>
        <Route index path="/" element={<HomePage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/apply" element={<Apply />} />
        <Route path="/availableLocation" element={<AvailableLocation />} />

        <Route element={<ProtectedLayout allowedRoles={["USER"]} />}>
          <Route path="/user/homePage" element={<UserHomePage />} />
          <Route path="/startRide" element={<StartRide />} />
          <Route path="/user/loading" element={<UserLoadingPage />} />
          <Route path="/user/availableDrivers" element={<DriverList />} />
          <Route path="/user/profile" element={<UserProfile />} />
          <Route path="/user/ridehistory" element={<RideHistory />} />
          <Route path="/user/payment" element={<Payment />} />
          <Route path="/user/popup" element={<Popup />} />
          <Route path="/user/ratings" element={<RatingHistory />} />
        </Route>

        <Route element={<ProtectedLayout allowedRoles={["DRIVER"]} />}>
          <Route path="/driver/location" element={<DriverLocationPage />} />
          <Route path="/driver/rides" element={<RidesMenuPage />} />
          <Route path="/driver/profile" element={<DriverProfilePage />} />
          <Route path="/driver/startRide" element={<DriverStartRide />} />
          <Route path="/driver/ridehistory" element={<RideHistory />} />
          <Route path="/driver/ratings" element={<RatingHistory />} />
          <Route
            path="/driver/paymentwaiting"
            element={<PaymentWaitingPage />}
          />
          <Route path="/driver/popup" element={<Popup />} />
        </Route>

        <Route element={<ProtectedLayout allowedRoles={["DRIVER", "USER"]} />}>
          <Route path="/userProfile" element={<UserProfile />} />
          <Route path="/ridehistory" element={<RideHistory />} />
        </Route>

        <Route element={<ProtectedLayout allowedRoles={["ADMIN"]} />}>
          <Route path="/admin" element={<AdminDashboard />} />
        </Route>

        <Route path="*" element={<Login />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
