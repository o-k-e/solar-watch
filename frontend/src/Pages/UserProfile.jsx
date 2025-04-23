import React, { useEffect, useState } from 'react'
import { fetchUserProfile } from '../Service/AuthService';
import {Link} from "react-router-dom";

const UserProfile = () => {
    
    const [user, setUser] = useState({});

    useEffect(() => {
        fetchUserProfile()
            .then(res => setUser(res))
            .catch(err => console.log("Could not fetch profile", err));
    }, []);

  return (

    <>
        <div className="min-h-screen bg-[#273f79] text-gray-800 p-6 flex flex-col items-center justify-center">
            <div className="border border-[#ffd369] bg-[#1e3163] text-[#ffd369] shadow-md rounded p-6 mx-w-md w-full text-center">
                <h1 className="text-2xl font-bold mb-4">User Profile</h1>
                <p><strong>Username:</strong> {user.username}</p>
                <p><strong>Roles:</strong> {user.roles?.map(role => role.replace("ROLE_", "")).join(", ")}</p>

                <Link to="/home" className="mt-6 inline-block bg-[#ffd369] text-[#1e3163] px-4 py-2 rounded hover:bg-gray-700 transition">
                    Back to Home
                </Link>
            </div>
        </div>
    </>
  )
}

export default UserProfile