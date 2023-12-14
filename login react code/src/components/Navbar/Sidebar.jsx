import React from "react";
import { Link } from "react-router-dom";

const Sidebar = ({ isOpen, toggleSidebar }) => {
  return (
    // addig open class when isOpen=true
    <div className={`sidebar  ${isOpen ? "open" : ""}`}>
      <div className="close-icon fs-semibold" onClick={toggleSidebar}>
        &times;
      </div>
      <div className="wrap ">
        <ul className="sidebar-menu ">
          <li>
            <Link to="/">About</Link>
          </li>
          <li>
            <Link to="/about">Discover</Link>
          </li>
          <li>
            <Link to="/services">Services</Link>
          </li>
          <li>
            <Link to="/services">Sign Up</Link>
          </li>
        </ul>
      </div>
    </div>
  );
};

export default Sidebar;
