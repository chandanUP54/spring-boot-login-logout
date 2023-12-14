import React from "react";
import Navbar from "../Navbar/Navbar";
import { Route, Routes } from "react-router-dom";
import Signin from "../auth/Signin";
import Signup from "../auth/Signup";
import MyProfile from "../profile/MyProfile";
import Protected from "../protected/Protected";

const Home = () => {
  return (
    <>
      <Navbar />  
      <Routes>
        <Route path="/login" element={<Signin />}></Route>
        <Route path="/signup" element={<Signup/>}></Route>
      
        <Route
         path="/user/profile" 
        element={<Protected Component={MyProfile} />}
      />
      </Routes>
    </>
  );
};

export default Home;
