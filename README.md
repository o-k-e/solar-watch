# SolarWatch

<details>
<summary><h2><strong>Table of Contents</strong><h2></summary>
  
- [About the Project](#about-the-project)
- [Built With](#built-with)
- [Contact](#contact)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation Steps](#installation-steps)
- [Usage](#usage)
- [Acknowledgments](#acknowledgments)
  
</details>

## About The Project

SolarWatch is a full-stack web application that allows users to search for the sunrise and sunset times of any city on a specific date. It integrates external APIs ([OpenWeather Geocoding API](https://openweathermap.org/api/geocoding-api) and [Sunrise and Sunset Times API](https://sunrise-sunset.org/api)) and presents the results in a clean and modern desktop layout.

Built with a `React`, `Vite`, `Tailwind` frontend and a `Spring Boot`, `PostgreSQL` backend, the app is fully containerized using Docker for smooth local setup and deployment.

### Features

The project features role-based access control:
- Regular users can search and view solar data of the selected location.
- Admin users have additional access to view the city data stored in the database.

**API Providers**

- 🌍 [OpenWeather Geocoding API](https://openweathermap.org/api/geocoding-api)
  Used to convert city names into geographic coordinates (latitude & longitude).
- 🌅 [Sunrise and Sunset Times API](https://sunrise-sunset.org/api)
  Provides accurate sunrise and sunset times for a given location and date.

<img width="865" alt="SolarWatch-landingpage" src="https://github.com/user-attachments/assets/503a67ad-0f97-4ee2-beea-5a3455225a5a" />

<img width="865" alt="SolarWatch-homepage" src="https://github.com/user-attachments/assets/6006f37e-bb6a-468b-98cc-b87709907718" />

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

## Contact

- Erika Oláhné Klár: [![GitHub](https://img.shields.io/badge/GitHub-%2312100E.svg?style=for-the-badge&logo=github&logoColor=white)](https://github.com/o-k-e)  [![LinkedIn](https://img.shields.io/badge/LinkedIn-%230077B5.svg?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/erika-olahne-klar/)
  
## Getting Started
To get a local copy up and running, follow these steps:

### Prerequisites
  
  This project uses **Docker** for containerized development.
  
  To install everything you need, just download `Docker Desktop`:
  
  - [Docker Desktop for Windows/macOS](https://www.docker.com/products/docker-desktop)
    
  Tip: After installation, **start Docker Desktop** to ensure `docker` commands work.

### Installation Steps

1. Open a **terminal** and navigate to the directory where you would like to save the repository.
   
2. **Clone the repository** to your machine by executing the command `git clone https://github.com/o-k-e/solar-watch.git` in your **terminal**.
   
3. **Configure Environment Variables**
      - Before starting the app, **create** a `.env` file **inside the backend folder** containing the following environment variables:
      
      ```env
      DATABASE_URL=jdbc:postgresql://db:5432/solarwatch
      DATABASE_USERNAME=your_username
      DATABASE_PASSWORD=your_password
      JWT_SECRET=uH38!v4zP#cE1sM@9rT$wX2qL*kbZ7oN
      JWT_EXPIRATION=8640000
      ```
      
      Replace `your_username`, `your_password` with your actual credentials.

      ⚠️ **Important:** The `JWT_SECRET` value should **never be exposed** in a real production environment.  
      This setup is acceptable **only for local development** or learning purposes.
      In a production setup, use secure environment variable management or secret vaults.

5. **Ensure Docker is Running**
     - Start `Docker Desktop`
     
6. **Build and run the containers**
     - Execute the command in the `backend` folder:
       ```bash
       docker compose up --build
       ```

7. **Access the Application**
     - Open your browser and visit: [http://localhost:4173](http://localhost:4173)
  
8. **Stopping the application**
    - To stop the containers, you can either press `CTRL+C` in the terminal (if running in the foreground), or run: 
      ```bash
      docker-compose down
      ```

## Usage
Once the services are up and running, you can access the frontend to explore the application.

On the website, you can either:
- **Log in** with the default admin user credentials:
  ```bash
  Username: admin
  Password: admin
  ```
or
- **Register** a new user, then **Log in** with your own credentials.
- **Search for sunrise and sunset times** by entering a city and a specific date.
- **View solar information** displayed in a clear and easy-to-read format.
- **Admin users** can view the list of cities stored in the database.
 
<img width="863" alt="SolarWatch-login" src="https://github.com/user-attachments/assets/03005ec8-a5e1-4d75-bab4-39bd6266876b" />

<img width="865" alt="SolarWatch-homepage" src="https://github.com/user-attachments/assets/cb8a0ee5-1657-4169-900b-fda22a3c2365" />

<img width="865" alt="SolarWatch-homepage-user" src="https://github.com/user-attachments/assets/f3df7739-de01-43d7-9787-a90dbc2e7cad" />

## Acknowledgments

- [Best-README-Template](https://github.com/othneildrew/Best-README-Template) for inspiration
- 🛡️ [Shields.io](https://shields.io) for the badges
- ⚛️ [React Icons](https://react-icons.github.io/react-icons) for popular icon packs as React components 
- 🌍 [OpenWeather Geocoding API](https://openweathermap.org/api/geocoding-api)  
- 🌅 [Sunrise and Sunset Times API](https://sunrise-sunset.org/api)  




 
