import axios, {options} from "axios";

export const fetchSolarData = async (cityName, date) => {
    const jwt = localStorage.getItem("jwt");

    const options = {
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${jwt}`,
        }
    }

    try {
        const response = await axios.get(`http://localhost:8080/solarwatch?city=${cityName}&date=${date}`, options);
        return response.data;
    } catch (error) {
        console.error(`Could not fetch solar data for ${cityName}`, error);
        throw error;
    }
};


export const fetchAllCities = async () => {
    const jwt = localStorage.getItem("jwt");

    const options = {
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${jwt}`,
        }
    }

    try {
        const response = await axios.get(`http://localhost:8080/city/find-all`, options);
        return response.data;
    } catch (error) {
        console.error("Error in fetching all cities", error);
        throw error;
    }
}