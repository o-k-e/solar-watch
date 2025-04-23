import React, {useEffect, useState} from "react";
import {fetchUserProfile} from "../Service/AuthService.js";
import {fetchSolarData} from "../Service/ApiService.js";

const HomePage = () => {

    const [ user, setUser ] = useState({});
    const [ result, setResult] = useState({});
    const [ city, setCity ] = useState("");
    const [ date, setDate ] = useState("");

    useEffect(() => {
        fetchUserProfile()
        .then(res => setUser(res))
            .catch(err => console.error(`Could not fetch user profile:`, err));
    }, []);

    const handleSearch = (e) => {
        e.preventDefault();
        fetchSolarData(city, date)
        .then(res => setResult(res))
        .catch(err => console.error(`Could not fetch solar data for:`, city, err));
    }

    console.log("Rendered result:", result);

    return (
        <div className="min-h-screen bg-[#d0e0ed] text-gray-800 p-6">
            <h1 className="text-3xl font-bold mb-4">Welcome back, {user.username} 👋</h1>
            <p><strong>Your role:</strong> {user.roles?.map(role => role.replace('ROLE_', '')).join(', ')}</p>

            <form onSubmit={handleSearch} className="mt-8 flex gap-4">
                <input
                    type="text"
                    placeholder="Enter city name..."
                    value={city}
                    onChange={(e) => setCity(e.target.value)}
                    className="px-4 py-2 border border-gray-400 rounded w-full max-w-sm"
                />

                <input
                    type="date"
                    value={date}
                    onChange={(e) => setDate(e.target.value)}
                    className="px-4 py-2 border border-gray-400 rounded w-full max-w-sm"
                />

                <button
                    type="submit"
                    className="bg-[#272121] text-white px-6 py-2 rounded hover:bg-gray-700 transition"
                >
                    Search
                </button>
            </form>

            {result?.sunrise && result?.sunset && (
                <div className="mt-8 border border-[#ffd369] bg-[#1e3163] text-[#ffd369] p-6 rounded shadow max-w-md">
                    <h2 className="text-xl font-semibold mb-4">Solar Data</h2>
                    <p><strong>City:</strong> {result.city}</p>
                    <p><strong>Date:</strong> {result.date}</p>
                    <p><strong>Sunrise:</strong> {result.sunrise}</p>
                    <p><strong>Sunset:</strong> {result.sunset}</p>
                    </div>
            )}

            {user.roles?.includes("ROLE_ADMIN") && (
                <div className="mt-10 p-4 border border-[#ffd369] rounded bg-[#1e3163] text-[#ffd369] max-w-md">
                    <h2 className="text-lg font-semibold mb-2">Admin Panel</h2>
                </div>
            )}
        </div>
    );
}

export default HomePage;