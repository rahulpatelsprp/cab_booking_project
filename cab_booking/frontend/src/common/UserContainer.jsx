import React from "react";
import NavBarRider from "./Navbar.jsx";
export default function UserContainer({ children }) {
  return (
    <div className="sm:w-[calc(100%_-_100px)] sm:h-[100vh] w-full ">
      <NavBarRider />
      {children}
    </div>
  );
}
