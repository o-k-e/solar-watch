# SolarWatch

<details>
<summary><h2><strong>Table of Contents</strong><h2></summary>
  
- [About the Project](#about-the-project)
- [Built With](#built-with)
- [Contributors](#contributors)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation Steps](#installation-steps)
- [Usage](#usage)
- [Acknowledgments](#acknowledgments)
  
</details>

## About The Project

SolarWatch is a full-stack web application that allows users to search for the sunrise and sunset times of any city on a specific date. It integrates external APIs ([OpenWeather Geocoding API](https://openweathermap.org/api/geocoding-api) and [Sunrise and Sunset Times API](https://sunrise-sunset.org/api)) and presents the results in a clean and modern desktop layout.

Built with a React / Vite + Tailwind frontend and a Spring Boot + PostgreSQL backend, the app is fully containerized using Docker for smooth local setup and deployment.

### Features

The project features role-based access control:
- Regular users can search and view solar data of the selected location.
- Admin users have additional access to view the city data stored in the database.

**API Providers**

- 🌍 [OpenWeather Geocoding API](https://openweathermap.org/api/geocoding-api)
  Used to convert city names into geographic coordinates (latitude & longitude).
- 🌅 [Sunrise and Sunset Times API](https://sunrise-sunset.org/api)
  Provides accurate sunrise and sunset times for a given location and date.

<img width="860" alt="SolarWatch-landingpage" src="https://github.com/user-attachments/assets/f1f53d91-c8f1-438b-afa9-8b935bc8514c" />

<img width="861" alt="SolarWatch-homepage" src="https://github.com/user-attachments/assets/a59365f0-1464-449a-af98-686311c5b2e9" />

## Built With

- **Backend:**  
  [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)

- **Frontend:**  
  [![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)](https://reactjs.org/)  
  [![Vite](https://img.shields.io/badge/Vite-646CFF?style=for-the-badge&logo=vite&logoColor=white)](https://vitejs.dev/)  
  [![TailwindCSS](https://img.shields.io/badge/TailwindCSS-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white)](https://tailwindcss.com/)

- **Database:**  
  [![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)

- **Containerization:**  
  [![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)  

## Getting Started

To get a local copy up and running, follow these steps:

### Prerequisites

Make sure you have the following installed:

- **Java 23**
- **Node.js 18+**
- **PostgreSQL 14+**
- **Maven**
- **Docker** (*optional*)

### Installation Steps

1. Open a **terminal** and navigate to the directory where you would like to save the repository.
   
2. **Clone the repository** to your machine by executing the command git clone https://github.com/o-k-e/solar-watch.git in your **terminal**, then start the application locally.
  
4. **Backend (Spring Boot)**
```bash
cd backend
./mvnw spring-boot:run
```
5. **Frontend (React + Vite)**
```bash
cd frontend
npm install
npm run dev
```

- Frontend runs at: [http://localhost:5173](http://localhost:5173)
- Backend runs at: [http://localhost:8080](http://localhost:8080)

## Usage
Once the services are up and running, you can access the frontend to explore the application.

On the website, you can:

- **Log in** to your account for a personalized experience.
- **Search for sunrise and sunset times** by entering a city and a specific date.
- **View solar information** displayed in a clear and easy-to-read format.
- **Admin users** can view the list of cities stored in the database.
 
<img width="862" alt="SolarWatch-login" src="https://github.com/user-attachments/assets/4a0feb15-8305-42e1-8c46-537efd92968f" />

<img width="861" alt="SolarWatch-homepage" src="https://github.com/user-attachments/assets/622b8b2a-1dcf-4d95-be2f-12d61821755c" />

<img width="859" alt="SolarWatch-homepage-user" src="https://github.com/user-attachments/assets/60cff95c-8b28-48af-b9d3-c701c0a44f0c" />

## Acknowledgments

- [Best-README-Template](https://github.com/othneildrew/Best-README-Template) for inspiration
- 🛡️ [Shields.io](https://shields.io) for the badges
- ⚛️ [React Icons](https://react-icons.github.io/react-icons) for popular icon packs as React components 
- 🌍 [OpenWeather Geocoding API](https://openweathermap.org/api/geocoding-api)  
- 🌅 [Sunrise and Sunset Times API](https://sunrise-sunset.org/api)  




 
