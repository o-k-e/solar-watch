import {useNavigate} from "react-router-dom";
import {checkLoginStatus, logout} from "../Service/AuthService.js";

const Navbar = () => {

    const navigate = useNavigate();
    const isLoggedIn = checkLoginStatus();

    const handleLogout = () => {
        logout();
        navigate("/");
    }

    return (
    <div className='text-left text-[#ff8c00] text-3xl p-10 bg-[#272121] flex justify-between items-center'>
        <span>SolarWatch</span>
        {isLoggedIn && (
            <button
                onClick={handleLogout}
                className="text-[#ff8c00]"
            >
                Logout
            </button>
        )}

    </div>
  )
}

export default Navbar;