import axios from "axios";

const AUTH_REST_API_BASE_URL = "http://localhost:8080/user";

export const registerAPICall = async (registerObj) => {
  try {
    const response = await axios.post(`http://localhost:8080/user/register`, registerObj, {
      headers: {
        "Content-Type": "application/json",
      },
    });

    return response.data;
  } catch (error) {
    console.error("Registration failed:", error);
    throw error;
  }
};


export const loginAPICall = async (loginObj) => {
    try {
      const response = await axios.post(`${AUTH_REST_API_BASE_URL}/login`, loginObj, {
        headers: {
          "Content-Type": "application/json",
        },
      });
  
      return response.data;
    } catch (error) {
      console.error("Login failed:", error);
      throw error;
    }
  
  };

  export const fetchUserProfile = async () => {
    try {
      const response = await axios.get(`${AUTH_REST_API_BASE_URL}/me`, {
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + localStorage.getItem("jwt")
        },
        withCredentials: true,
      });

      return response.data;
    } catch (error) {
      console.error("Login failed:", error);
      throw error;
    }
  };


export const logout = async () => {
  localStorage.removeItem("jwt");
}

export const checkLoginStatus = () => Boolean(localStorage.getItem("jwt"));