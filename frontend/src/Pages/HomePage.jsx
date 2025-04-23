import React, {useEffect, useState} from "react";
import {fetchUserProfile} from "../Service/AuthService.js";
import {fetchSolarData} from "../Service/ApiService.js";
import AdminCityTable from "../Components/AdminCityTable.jsx";

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
        <div className="flex flex-col items-center justify-center min-h-screen bg-[#273f79] text-[#d0e0ed] p-6">
            <h1 className="text-3xl font-bold mb-4">Welcome back, {user.username} 👋</h1>
            <p><strong>Your role:</strong> {user.roles?.map(role => role.replace('ROLE_', '')).join(', ')}</p>

            <form onSubmit={handleSearch} className="mt-8 flex gap-4">
                <input
                    type="text"
                    placeholder="Enter city name..."
                    value={city}
                    onChange={(e) => setCity(e.target.value)}
                    className="border  rounded-md px-4 py-2 shadow-sm focus:outline-none focus:ring-2 "
                />

                <input
                    type="date"
                    value={date}
                    onChange={(e) => setDate(e.target.value)}
                    className="border rounded-md px-4 py-2 shadow-sm focus:outline-none focus:ring-2"
                />

                <button
                    type="submit"
                    className="bg-[#273f79] text-white px-6 py-2 rounded-md border  hover:bg-[#1e3163] hover:text-[#ffd369] focus:outline-none focus:ring-2 focus:ring-[#ffd369]"
                >
                    Search
                </button>
            </form>

            {result?.sunrise && result?.sunset && (
                <div className="w-full flex justify-center mt-8 text-white px-4">
                    <div className="bg-[#0d1e45] rounded-2xl shadow-lg p-6 w-full max-w-xl text-center space-y-4">

                        <h2 className="text-2xl font-semibold flex items-center justify-center gap-2">
                            {result.city}
                            <span>📍</span>
                        </h2>

                        <p className="text-sm text-gray-300">{result.date}</p>

                        <div className="w-32 h-32 mx-auto bg-[url(/public/sun.png)] rounded-full"></div>

                        <div className="flex justify-between text-lg mt-4 px-4">
                            <div className="text-left">
                                <p className="text-sm text-gray-300">Sunrise</p>
                                <p className="text-xl font-bold">{result.sunrise}</p>
                            </div>
                            <div className="text-right">
                                <p className="text-sm text-gray-300">Sunset</p>
                                <p className="text-xl font-bold">{result.sunset}</p>
                            </div>
                        </div>
                    </div>
                </div>
            )}

            {user.roles?.includes("ROLE_ADMIN") && <AdminCityTable />}
        </div>
    );
}

export default HomePage;