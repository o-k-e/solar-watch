import {useNavigate} from "react-router-dom";
import {checkLoginStatus, logout} from "../Service/AuthService.js";
import { FiLogOut } from "react-icons/fi";

const Navbar = () => {

    const navigate = useNavigate();
    const isLoggedIn = checkLoginStatus();

    const handleLogout = () => {
        logout();
        navigate("/");
    }

    return (
    <div className='text-left text-[#ffd369] text-3xl px-30 bg-[#091930]  flex justify-between items-center'>
        <img src="/logo.png" alt="SolarWatch Logo" className="h-40" />
        {isLoggedIn && (
            <button
                onClick={handleLogout}
                className="text-[#ffd369]"
            >
                <FiLogOut />
            </button>
        )}

    </div>
  )
}

export default Navbar;