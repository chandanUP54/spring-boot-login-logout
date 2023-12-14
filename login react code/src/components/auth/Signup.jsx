import React, { useState } from "react";
import "./Signup.css";
import userService from "../backend/user.service";
import { useNavigate } from "react-router-dom";

const Signup = () => {
  const navigate=useNavigate();
  const [user, setUser] = useState({
    firstName: "",
    lastName: "",
    email: "",
    mobile: "",
    password: "",
  });
  const handleChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };
  const handleSubmit = (e) => {
    e.preventDefault();
    userService.saveUser(user).then((res)=>{
      setUser(({
        firstName: "",
        lastName: "",
        email: "",
        mobile: "",
        password: "",
      }))
      navigate("/login")
      
    })
  };
  return (
    <>
      <div className="container-login">
        <form onSubmit={(e) => handleSubmit(e)}>
          <div className="form1">
            <div className="name">
              <label for="name">First Name:</label>
              <br />
              <input
                type="text"
                name="firstName"
                value={user.firstName}
                onChange={(e) => handleChange(e)}
                required
              />
            </div>
            <div className="name">
              <label for="name">Last Name:</label>
              <br />
              <input
                type="text"
                name="lastName"
                value={user.lastName}
                onChange={(e) => handleChange(e)}
                required
              />
            </div>
            <div className="name">
              <label for="name">Email:</label>
              <br />
              <input
                type="email"
                name="email"
                value={user.email}
                onChange={(e) => handleChange(e)}
                required
              />
            </div>
            <div className="name">
              <label for="name">Mobile:</label>
              <br />
              <input
                type="number"
                name="mobile"
                value={user.mobile}
                onChange={(e) => handleChange(e)}
                required
              />
            </div>
            <div className="password">
              <label for="password">Password:</label>
              <br />
              <input
                type="password"
                name="password"
                value={user.password}
                onChange={(e) => handleChange(e)}
                required
              />
            </div>

            <div>
              <button type="submit" className="btn btn-primary">
                Submit
              </button>
            </div>
          </div>
        </form>
      </div>
    </>
  );
};

export default Signup;
