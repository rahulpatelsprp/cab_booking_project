import React from "react";

/**
 * A reusable SVG icon component.
 * @param {object} props - The component props.
 * @param {string} props.path - The SVG path data.
 * @param {string} [props.className] - Optional CSS class name.
 */
const Icon = ({ path, className = "icon" }) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    fill="none"
    viewBox="0 0 24 24"
    strokeWidth={1.5}
    stroke="currentColor"
    className={className}
  >
    <path strokeLinecap="round" strokeLinejoin="round" d={path} />
  </svg>
);

export default Icon;
