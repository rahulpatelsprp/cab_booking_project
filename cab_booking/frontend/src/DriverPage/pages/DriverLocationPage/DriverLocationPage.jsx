import { useState } from "react";
import Icon from "../../components/Icon/Icon";
import "./DriverLocationPage.css";
import React from "react";
import toast from "react-hot-toast";

const DriverLocationPage = () => {
  const [locationText, setLocationText] = useState("");

  const searchCurrentLocation = async () => {
    toast.loading("Location is searching...");

    if (!("geolocation" in navigator)) {
      toast.error("Geolocation is not supported by this browser.");
      return;
    }

    try {
      const position = await new Promise((resolve, reject) =>
        navigator.geolocation.getCurrentPosition(resolve, reject, {
          enableHighAccuracy: true,
          timeout: 10000,
          maximumAge: 0,
        })
      );

      const latitude = position.coords.latitude;
      const longitude = position.coords.longitude;

      const url = new URL(
        "https://api.bigdatacloud.net/data/reverse-geocode-client"
      );
      url.searchParams.set("latitude", latitude.toString());
      url.searchParams.set("longitude", longitude.toString());
      url.searchParams.set("localityLanguage", "en");

      const response = await fetch(url.href);
      if (!response.ok) {
        throw new Error(`Reverse geocoding failed: ${response.status}`);
      }

      const data = await response.json();
      const city = data.city || data.locality || "Unknown location";

      setLocationText(city);
    } catch (error) {
      toast.error("Error fetching location: " + error.message);
    }
  };

  return (
    <div className="page-container page-centered">
      <div className="location-content">
        <h1>Hello, I'm Here</h1>
        <div className="input-group">
          <span className="input-icon left">
            <Icon path="M15 10.5a3 3 0 11-6 0 3 3 0 016 0z" />
          </span>
          <input
            type="text"
            placeholder="Enter Current Location"
            className="location-input"
            value={locationText}
            onChange={(e) => setLocationText(e.target.value)}
          />
          <span className="input-icon right " onClick={searchCurrentLocation}>
            <Icon path="M12 12m-9 0a9 9 0 1 0 18 0a9 9 0 1 0 -18 0M12 3v3M12 18v3M3 12h3M18 12h3" />
          </span>
        </div>
        <button className="btn btn-primary">Send Location</button>
      </div>
      <button className="btn find-ride-btn">
        <Icon path="M6 12L3.269 3.126A59.768 59.768 0 0121.485 12 59.77 59.77 0 013.27 20.876L5.999 12zm0 0h7.5" />
        Find a Ride
      </button>
      <p className="footer-notice">
        By providing your phone number and clicking 'Request Now,' you consent
        to receive text messages from Auto Cars...
      </p>
    </div>
  );
};

export default DriverLocationPage;
