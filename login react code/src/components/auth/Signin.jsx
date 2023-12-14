import React, { useState } from "react";
import "./Signin.css";
import userService from "../backend/user.service";
import { useNavigate } from "react-router-dom";

const Signin = () => {
  const navigate = useNavigate();
  const [user, setUser] = useState({
    email: "",
    password: "",
  });
  const handleChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };
  const handleSubmit = (e) => {
    e.preventDefault();
    userService.loginUser(user).then((response) => {
      navigate("/");
      localStorage.setItem("login", true);
      
      const { jwt } = response.data; //getting token after signin
      console.log(response.data);
      localStorage.setItem("jwt", jwt); // set token in local storage
      window.location.reload();
      setUser({
        email: "",
        password: "",
      });
    }).catch((error)=>{
      console.log(error);
    });
  };
  return (
    <>
      <div className="container-login">
        <form onSubmit={(e) => handleSubmit(e)}>
          <div className="form2">
            <div className="name">
              <label for="name">Username:</label>
              <br />
              <input
                type="email"
                name="email"
                value={user.email}
                onChange={(e) => handleChange(e)}
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
              />
            </div>

            <div>
              <button type="submit" className="btn btn-primary">
                Submit
              </button>
            </div>
            <p style={{ padding: "0px 20px" }}>
              {" "}
              New User! to create new account <a href="/signup">Click Here</a>
            </p>
          </div>
        </form>
      </div>
    </>
  );
};

export default Signin;
