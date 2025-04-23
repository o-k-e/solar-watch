import React from 'react';
import { Link } from 'react-router-dom';

const LandingPage = () => {
    return (
        <div className="flex flex-col items-center justify-center min-h-screen text-center bg-[#1e3163] text-white px-4">
            <h1 className="text-4xl md:text-6xl font-bold mb-4">Welcome to SolarWatch</h1>
            <p className="text-lg md:text-xl mb-8">Track sunrise and sunset times by city 🌅 </p>
            <div className="flex gap-4">
                <Link to="/login" className="bg-white text-[#272121] px-6 py-2 rounded shadow hover:bg-gray-200 transition">Login</Link>
                <Link to="/register" className="border border-white px-6 py-2 rounded hover:bg-white hover:text-[#272121] transition">Register</Link>
            </div>
        </div>
    );
};

export default LandingPage;