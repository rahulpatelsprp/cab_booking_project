import React, { useState } from "react";
import { NavLink, useNavigate } from "react-router-dom";

export default function NavBarRider() {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const navigator = useNavigate();
  const isLoggedIn = sessionStorage.getItem("token") ? true : false;
  const role = sessionStorage.getItem("role");

  function logout() {
    // Clear all session storage items
    sessionStorage.clear();
    navigator("/");
  }

  const objList =
    role == "USER"
      ? [
          {
            name: "home",
            path: "/user/homepage",
            icon: "M341.8 72.6C329.5 61.2 310.5 61.2 298.3 72.6L74.3 280.6C64.7 289.6 61.5 303.5 66.3 315.7C71.1 327.9 82.8 336 96 336L112 336L112 512C112 547.3 140.7 576 176 576L464 576C499.3 576 528 547.3 528 512L528 336L544 336C557.2 336 569 327.9 573.8 315.7C578.6 303.5 575.4 289.5 565.8 280.6L341.8 72.6zM304 384L336 384C362.5 384 384 405.5 384 432L384 528L256 528L256 432C256 405.5 277.5 384 304 384z",
          },
          {
            name: "time",
            path: "/user/ridehistory",
            icon: "M320 64C461.4 64 576 178.6 576 320C576 461.4 461.4 576 320 576C178.6 576 64 461.4 64 320C64 178.6 178.6 64 320 64zM296 184L296 320C296 328 300 335.5 306.7 340L402.7 404C413.7 411.4 428.6 408.4 436 397.3C443.4 386.2 440.4 371.4 429.3 364L344 307.2L344 184C344 170.7 333.3 160 320 160C306.7 160 296 170.7 296 184z",
          },
          {
            name: "user",
            path: "/user/profile",
            icon: "M320 312C386.3 312 440 258.3 440 192C440 125.7 386.3 72 320 72C253.7 72 200 125.7 200 192C200 258.3 253.7 312 320 312zM290.3 368C191.8 368 112 447.8 112 546.3C112 562.7 125.3 576 141.7 576L498.3 576C514.7 576 528 562.7 528 546.3C528 447.8 448.2 368 349.7 368L290.3 368z",
          },
        ]
      : [
          {
            name: "home",
            path: "/driver/rides",
            icon: "M341.8 72.6C329.5 61.2 310.5 61.2 298.3 72.6L74.3 280.6C64.7 289.6 61.5 303.5 66.3 315.7C71.1 327.9 82.8 336 96 336L112 336L112 512C112 547.3 140.7 576 176 576L464 576C499.3 576 528 547.3 528 512L528 336L544 336C557.2 336 569 327.9 573.8 315.7C578.6 303.5 575.4 289.5 565.8 280.6L341.8 72.6zM304 384L336 384C362.5 384 384 405.5 384 432L384 528L256 528L256 432C256 405.5 277.5 384 304 384z",
          },
          {
            name: "time",
            path: "/driver/ridehistory",
            icon: "M320 64C461.4 64 576 178.6 576 320C576 461.4 461.4 576 320 576C178.6 576 64 461.4 64 320C64 178.6 178.6 64 320 64zM296 184L296 320C296 328 300 335.5 306.7 340L402.7 404C413.7 411.4 428.6 408.4 436 397.3C443.4 386.2 440.4 371.4 429.3 364L344 307.2L344 184C344 170.7 333.3 160 320 160C306.7 160 296 170.7 296 184z",
          },
          {
            name: "user",
            path: "/driver/profile",
            icon: "M320 312C386.3 312 440 258.3 440 192C440 125.7 386.3 72 320 72C253.7 72 200 125.7 200 192C200 258.3 253.7 312 320 312zM290.3 368C191.8 368 112 447.8 112 546.3C112 562.7 125.3 576 141.7 576L498.3 576C514.7 576 528 562.7 528 546.3C528 447.8 448.2 368 349.7 368L290.3 368z",
          },
        ];

  // Not logged in state
  if (!isLoggedIn) {
    return (
      <nav className="fixed top-4 right-4 md:top-0 md:right-0 md:w-[60px] lg:w-[100px] md:h-[100vh] lg:bg-amber-400 z-50">
        <div className="md:h-full md:flex md:flex-col md:justify-center md:items-center md:gap-10">
          <NavLink
            to="/"
            className="w-10 h-10 rounded-full bg-amber-950 flex items-center justify-center hover:bg-black transition-colors duration-200"
          >
            <svg
              className="fill-white w-5 h-5"
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 640 640"
            >
              <path d={objList[0].icon} />
            </svg>
          </NavLink>
        </div>
      </nav>
    );
  }

  return (
    <>
      <button
        onClick={() => setIsMenuOpen(!isMenuOpen)}
        className="fixed top-4 right-4 z-50 w-10 h-10 flex items-center justify-center rounded-full bg-amber-400 shadow-lg sm:hidden hover:bg-amber-500 transition-colors border"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          className="h-6 w-6"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          {isMenuOpen ? (
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M6 18L18 6M6 6l12 12"
            />
          ) : (
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M4 6h16M4 12h16M4 18h16"
            />
          )}
        </svg>
      </button>

      <nav
        className={`
        fixed sm:top-0 sm:right-0 sm:h-[100vh]
        sm:w-[60px] lg:w-[100px] bg-amber-400
        sm:flex sm:flex-col sm:justify-center sm:items-center sm:gap-10
        ${
          isMenuOpen
            ? "top-0 right-0 h-auto w-[200px] py-6 px-4 rounded-bl-2xl shadow-xl animate-slideIn sm:min-w-0 sm:p-0 sm:rounded-none sm:shadow-none sm:animate-none"
            : "hidden"
        }
        sm:block z-40
      `}
      >
        <div className="flex flex-col gap-6 items-stretch sm:items-center sm:mt-0">
          {objList.map((item) => (
            <NavLink
              key={item.name}
              to={item.path}
              onClick={() => setIsMenuOpen(false)}
              className={({ isActive }) =>
                `group flex items-center gap-3 p-2 rounded-full hover:bg-black transition-all duration-200 ${
                  isActive ? "bg-black fill-white text-white" : ""
                }`
              }
            >
              <div className="w-8 h-8 flex items-center justify-center">
                <svg
                  className={`w-5 h-5 ${({ isActive }) =>
                    isActive
                      ? "fill-white"
                      : "fill-black group-hover:fill-white hover:bg-white"}`}
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 640 640"
                >
                  <path d={item.icon} />
                </svg>
              </div>
              <span className="text-sm font-medium group-hover:text-white sm:hidden">
                {item.name.charAt(0).toUpperCase() + item.name.slice(1)}
              </span>
            </NavLink>
          ))}

          <div
            onClick={() => {
              logout();
              setIsMenuOpen(false);
            }}
            className="group flex items-center gap-3 p-2 rounded-full hover:bg-red-600 cursor-pointer transition-all duration-200"
          >
            <div className="w-8 h-8 flex items-center justify-center">
              <svg
                className="w-5 h-5 group-hover:fill-white"
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 640 640"
              >
                <path d="M569 337C578.4 327.6 578.4 312.4 569 303.1L425 159C418.1 152.1 407.8 150.1 398.8 153.8C389.8 157.5 384 166.3 384 176L384 256L272 256C245.5 256 224 277.5 224 304L224 336C224 362.5 245.5 384 272 384L384 384L384 464C384 473.7 389.8 482.5 398.8 486.2C407.8 489.9 418.1 487.9 425 481L569 337zM224 160C241.7 160 256 145.7 256 128C256 110.3 241.7 96 224 96L160 96C107 96 64 139 64 192L64 448C64 501 107 544 160 544L224 544C241.7 544 256 529.7 256 512C256 494.3 241.7 480 224 480L160 480C142.3 480 128 465.7 128 448L128 192C128 174.3 142.3 160 160 160L224 160z" />
              </svg>
            </div>
            <span className="text-sm font-medium group-hover:text-white sm:hidden">
              Logout
            </span>
          </div>
        </div>
      </nav>
    </>
  );
}
