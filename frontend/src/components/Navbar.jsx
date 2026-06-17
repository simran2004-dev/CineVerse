import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Navbar.css';

export default function Navbar() {
  const { user, logout, hasRole } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar-inner">
        <Link to="/dashboard" className="navbar-brand">
          <span className="brand-icon">🎬</span>
          <span className="brand-text">CineVerse</span>
        </Link>

        <div className="navbar-links">
          <Link to="/dashboard" className="nav-link">Dashboard</Link>
          <Link to="/movies" className="nav-link">Movies</Link>
          <Link to="/booking" className="nav-link">Book Tickets</Link>
          {hasRole('ADMIN') && (
            <Link to="/admin" className="nav-link nav-link-admin">Admin</Link>
          )}
          {hasRole('THEATRE_OWNER') && (
            <Link to="/admin" className="nav-link nav-link-admin">Manage</Link>
          )}
        </div>

        <div className="navbar-actions">
          {user && (
            <>
              <div className="navbar-user">
                <span className="user-avatar">{user.name?.charAt(0).toUpperCase()}</span>
                <span className="user-name">{user.name}</span>
                <span className="user-role">{user.role}</span>
              </div>
              <button className="nav-logout" onClick={handleLogout}>
                Logout
              </button>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}
