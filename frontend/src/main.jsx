import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from 'react-router-dom';

import './index.css'
// import App from './App.jsx'
import Layout from './Components/Layout.jsx'
import Login from './Pages/Login.jsx'
import Register from './Pages/Register.jsx';
import UserProfile from './Pages/UserProfile.jsx';
import LandingPage from "./Pages/LandingPage.jsx";

const router = createBrowserRouter([
  {
    path: '/',
    element: <Layout />,
    children: [
      {
        index: true,
        element: <LandingPage />,
      },
      {
        path: '/login',
        element: <Login />,
      },
      {
        path: '/register',
        element: <Register />,
      },
      {
        path: '/user-profile',
        element: <UserProfile />
      }
    ],
  },
    
]);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>,
)
