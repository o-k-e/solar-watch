import React, { useEffect, useState } from 'react'
import { fetchUserProfile } from '../Service/AuthService';

const UserProfile = () => {
    
    const [user, setUser] = useState({});

    useEffect(() => {
        fetchUserProfile()
            .then(res => setUser(res))
    }, []);

  return (

    <>
        <div>UserProfile</div>
        <div>
        <p><strong>Username:</strong> {user.username}</p>
        <p><strong>Roles:</strong> {user.roles?.map(role => role.replace("ROLE_", "")).join(", ")}</p>
        </div>

    </>
  )
}

export default UserProfile