import React from "react";
import LogoImage from "../assets/images/headerLogo.png";
export default function Header({ msgBody }) {
  return (
    <div className="flex pt-8 pb-8 sm:justify-between pl-8 pr-4 flex-wrap justify-center items-center">
      <div>
        <img
          src={LogoImage}
          alt="AutoCars"
          className="w-[150px]"
          loading="lazy"
        />
      </div>
      {msgBody && (
        <div className="bg-yellow-400 rounded-full py-2 px-4">{msgBody}</div>
      )}
    </div>
  );
}
