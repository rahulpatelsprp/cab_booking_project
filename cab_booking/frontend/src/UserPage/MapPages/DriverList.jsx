import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import icon1 from '../../assets/images/userIcons/icon1.png';
import Header from '../../common/Header.jsx';
import UserContainer from '../../common/UserContainer.jsx';

export default function DriverList() {
  const navigate = useNavigate();
  const location = useLocation();
  const { pickup, drop } = location.state || {};

  const [route, setRoute] = useState(null);
  const [drivers, setDrivers] = useState([]);

  useEffect(() => {
    if (pickup && drop) {
      fetch(`http://localhost:3001/routes?from=${pickup}&to=${drop}`)
        .then(res => res.json())
        .then(data => setRoute(data[0]))
        .catch(err => console.error("Failed to fetch route:", err));
    }
  }, [pickup, drop]);

  useEffect(() => {
    fetch('http://localhost:3001/drivers')
      .then(res => res.json())
      .then(data => setDrivers(data))
      .catch(err => console.error("Failed to fetch drivers:", err));
  }, []);

  if (!pickup || !drop) {
    return <h2>Please go back and select pickup and drop locations.</h2>;
  }

  return (
    <UserContainer>
      <Header msgBody={`Available Drivers for ${pickup} → ${drop}`} />

      <div className="flex flex-wrap lg:px-[200px] gap-8 justify-around pt-8">
        {drivers.length === 0 ? (
          <h2 className="text-6xl">No Drivers Are Available At This Moment</h2>
        ) : (
          drivers.map((driver) => {
            const fare = route?.fares?.driverSpecific?.[driver.id] || route?.fares?.default || driver.baseFare;
            return (
              <div key={driver.id} className="lg:w-[calc(50%_-_16px)] lg:my-10 mb-12 w-[90%]">
                <div className="bg-amber-400 pb-8">
                  <img className="rounded-full lg:w-[100px] w-[120px] border-8 border-white transform-[translateY(-50%)] mx-auto" src={icon1} alt="Driver" />
                  <h3 className="text-center font-bold mb-4">{driver.name}</h3>
                  <p className="text-center bg-amber-100 px-2 w-max mx-auto rounded-sm mb-4">{driver.vehicle.number}</p>
                  <div className="flex justify-around">
                    <div>
                      <p>Time For Pickup</p>
                      <p>5 <span className="text-xs">Minutes</span></p>
                    </div>
                    <div className="text-right">
                      <p>Ride Fare</p>
                      <p><span className="text-xs">Rs</span> {fare}</p>
                    </div>
                  </div>
                </div>
                <div className="flex shadow mb-4 py-2 text-sm justify-center gap-4 items-center">
                  Trusted
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-shield-fill-check" viewBox="0 0 16 16">
                    <path fillRule="evenodd" d="M8 0c-.69 0-1.843.265-2.928.56-1.11.3-2.229.655-2.887.87a1.54 1.54 0 0 0-1.044 1.262c-.596 4.477.787 7.795 2.465 9.99a11.8 11.8 0 0 0 2.517 2.453c.386.273.744.482 1.048.625.28.132.581.24.829.24s.548-.108.829-.24a7 7 0 0 0 1.048-.625 11.8 11.8 0 0 0 2.517-2.453c1.678-2.195 3.061-5.513 2.465-9.99a1.54 1.54 0 0 0-1.044-1.263 63 63 0 0 0-2.887-.87C9.843.266 8.69 0 8 0m2.146 5.146a.5.5 0 0 1 .708.708l-3 3a.5.5 0 0 1-.708 0l-1.5-1.5a.5.5 0 1 1 .708-.708L7.5 7.793z"/>
                  </svg>
                </div>
                
                <button
                  className="w-full bg-amber-400 rounded-full py-2 text-sm"
                  onClick={() => {
                    navigate('/startRide', {
                      state: {
                        pickup,
                        drop,
                        driver,
                        route
                      }
                    });
                  }}
                >
                  Confirm Driver
                </button>

              </div>
            );
          })
        )}
      </div>

      <p className="text-neutral-500 mt-8 pb-8 px-2 lg:px-16 text-xs">
        "Please patiently wait for the driver to arrive. Cancellation fees may apply if the ride is canceled while the driver is within a 200m radius..."
      </p>
    </UserContainer>
  );
}
