
import React, { useState, useEffect } from "react";
import { FaBars } from "react-icons/fa";
import { NavLink, useNavigate } from "react-router-dom";
import Sidebar from "./Sidebar";
import "./Navbar.css";

const Navigation = () => {
  const [display1, setDisplay1] = useState("");
  const [display2, setDisplay2] = useState("display:none");

  useEffect(() => {
    let login1 = localStorage.getItem("login");
    if (login1 === "true") {
      setDisplay1("display:none");
      setDisplay2("");
    }
  }, []);

  const [showSidebar, setShowSidebar] = useState(false);
  const navigate = useNavigate();

  const toggleSidebar = () => {
    setShowSidebar(!showSidebar);
  };

  const scrollToTop = () => {
    window.scrollTo(0, 0);
    navigate("/");
  };

  return (
    <nav className="navbar">
      <div className="navbar-container ">
        <div className="menu-icon" onClick={toggleSidebar}>
          <FaBars />
        </div>
        <div className="navbar-logo ">
          <NavLink
            to="/"
            className="text-decoration-none sell"
            onClick={scrollToTop}
          >
            SellX
          </NavLink>
        </div>
        <div className="navbar-items">
          <NavLink to="/">Shop</NavLink>
          <NavLink to="/men">Men</NavLink>
          <NavLink to="/women">Women</NavLink>
        </div>
        <div className="login">
        
          <NavLink to={localStorage.getItem("login") ? "/user/profile" : "/login"} style={{ display: display2 }}>
            <button>{localStorage.getItem("login") ? "My Profile" : "Login"}</button>
          </NavLink>
        </div>

        {showSidebar && (
          <Sidebar isOpen={showSidebar} toggleSidebar={toggleSidebar} />
        )}
      </div>
    </nav>
  );
};

export default Navigation;
