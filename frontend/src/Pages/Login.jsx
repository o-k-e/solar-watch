import React, { useState } from "react";
import {Link, useNavigate} from "react-router-dom";
import { FaUser, FaEye, FaEyeSlash } from "react-icons/fa";
import { loginAPICall } from "../Service/AuthService";

const Login = () => {
  const [showPassword, setShowPassword] = useState(false);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLoginForm = (e) => {
    e.preventDefault();

    const loginObj = {
        username: username,
        password: password
    };

    console.log(loginObj);

    loginAPICall(loginObj)
        .then(res => {
            console.log(res)
            localStorage.setItem("jwt", res.jwt);
            navigate("/home");
        }).catch(error => {
            console.log(error)
        })
  };

  return (
    <div className="relative">
      <div className="flex justify-center items-center font-[sans-serif] h-full min-h-screen p-4 bg-[url(/public/sunrise.jpg)] bg-no-repeat bg-cover bg-center">
        
        <div className="max-w-md w-full mx-auto">
          <form onSubmit={handleLoginForm} className="bg-opacity-25 shadow-xl rounded-lg p-6">
            <div className="mb-12">
              <h3 className="text-gray-800 text-center text-3xl font-bold">Login</h3>
            </div>

            {/* Username Input */}
            <div className="relative flex items-center">
              <input
                name="username"
                type="text"
                required
                className="bg-transparent w-full text-sm text-gray-800 border-b border-gray-400 focus:border-gray-800 pl-2 pr-8 py-3 outline-none placeholder:text-gray-800"
                placeholder="Enter username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
              <FaUser className="absolute right-2 text-gray-500" />
            </div>

            {/* Password Input */}
            <div className="mt-6 relative flex items-center">
              <input
                name="password"
                type={showPassword ? "text" : "password"}
                required
                className="bg-transparent w-full text-sm text-gray-800 border-b border-gray-400 focus:border-gray-800 pl-2 pr-8 py-3 outline-none placeholder:text-gray-800"
                placeholder="Enter password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
              {showPassword ? (
                <FaEyeSlash
                  className="absolute right-2 text-gray-500 cursor-pointer"
                  onClick={() => setShowPassword(false)}
                />
              ) : (
                <FaEye
                  className="absolute right-2 text-gray-500 cursor-pointer"
                  onClick={() => setShowPassword(true)}
                />
              )}
            </div>

            {/* Remember Me & Forgot Password */}
            <div className="flex flex-wrap items-center justify-between gap-4 mt-6">
              <div className="flex items-center">
                <input
                  id="remember-me"
                  name="remember-me"
                  type="checkbox"
                  className="h-4 w-4 shrink-0 border-gray-300 rounded"
                />
                <label htmlFor="remember-me" className="ml-3 block text-sm text-gray-800">
                  Remember me
                </label>
              </div>
              <div>
                <Link className="text-gray-800 text-sm font-semibold hover:underline">
                  Forgot Password?
                </Link>
              </div>
            </div>

            {/* Login Button */}
            <div className="mt-12">
              <button
                type="submit"
                className="cursor-pointer w-full py-2.5 px-4 text-sm font-semibold tracking-wider rounded text-white bg-gray-800 hover:bg-gray-500 focus:outline-none"
              >
                Login
              </button>
              <p className="text-gray-800 text-sm text-center mt-6">
                Don't have an account
                <Link to="/register" className="text-[#394a51] font-semibold hover:underline ml-1 whitespace-nowrap">
                  Register here
                </Link>
              </p>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Login;