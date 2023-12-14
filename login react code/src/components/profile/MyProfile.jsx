import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const MyProfile = () => {
  const navigate = useNavigate();
  const [user, setUser]  = useState({});
  const jwtToken = localStorage.getItem("jwt");
  const config = {
    headers: {
      Authorization: `Bearer ${jwtToken}`,
    },
  };

  useEffect(() => {
    axios
      .get(`http://localhost:8086/api/users/profile`, config)
      .then((response) => {
        setUser(response.data);
      })
      .catch((error) => {
        console.error(error);
      });
}, []);

  const logout = () => {
    localStorage.clear();
    navigate("/");
    window.location.reload();
  };
  return (
    <div>
      <h1>Your Profile</h1>

      <p className="mt-4 fs-4">
        My Id is:
        <span className="text-primary"> {user.id}</span>
      </p>
      <p className="mt-4 fs-4">
        My Name is:
        <span className="text-primary"> 
          {user.firstName} {user.lastName}
        </span>
      </p>
      <p className="fs-4">
        My Email is: <span className="text-primary"> {user.email}</span>
      </p>
      <button onClick={logout} className="btn btn-primary mt-3">
        logout
      </button>
    </div>
  );
};

export default MyProfile;
