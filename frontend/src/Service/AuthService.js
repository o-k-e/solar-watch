const AUTH_REST_API_BASE_URL = "http://localhost:8080/user";

export const registerAPICall = async (registerObj) => {
  try {
    const response = await fetch(`${AUTH_REST_API_BASE_URL}/register`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(registerObj),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }

    return await response.json();
  } catch (error) {
    console.error("Registration failed:", error);
    throw error;
  }

};


export const loginAPICall = async (loginObj) => {
    try {
      const response = await fetch(`${AUTH_REST_API_BASE_URL}/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(loginObj),
      });
  
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
  
      return await response.json();
    } catch (error) {
      console.error("Login failed:", error);
      throw error;
    }
  
  };

  export const fetchUserProfile = async () => {
    try {
      const response = await fetch(`${AUTH_REST_API_BASE_URL}/me`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + localStorage.getItem("jwt")
        },
        credentials: "include"
      });
  
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
  
      return await response.json();
    } catch (error) {
      console.error("Login failed:", error);
      throw error;
    }
  
  };