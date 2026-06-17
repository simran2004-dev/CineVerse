import { useAuth } from '../context/AuthContext';
import { Link } from 'react-router-dom';
import Card from '../components/Card';
import { mockMovies } from '../utils/mockData';
import './Dashboard.css';

export default function Dashboard() {
  const { user, hasRole } = useAuth();

  const stats = [
    { label: 'Movies Available', value: mockMovies.length, icon: '🎬', color: '#6366f1' },
    { label: 'Upcoming Shows', value: 12, icon: '🎭', color: '#8b5cf6' },
    { label: 'My Bookings', value: 3, icon: '🎟️', color: '#ec4899' },
    { label: 'Reviews Written', value: 7, icon: '⭐', color: '#f59e0b' },
  ];

  const adminStats = [
    { label: 'Total Users', value: 1248, icon: '👥', color: '#6366f1' },
    { label: 'Total Bookings', value: 4521, icon: '🎟️', color: '#10b981' },
    { label: 'Revenue', value: '₹8.5L', icon: '💰', color: '#f59e0b' },
    { label: 'Active Shows', value: 38, icon: '🎭', color: '#ec4899' },
  ];

  const displayStats = hasRole('ADMIN') ? adminStats : stats;

  return (
    <div className="dashboard">
      <div className="dashboard-hero">
        <div className="hero-content">
          <h1 className="hero-title">
            Welcome back, <span className="hero-name">{user?.name}</span>
          </h1>
          <p className="hero-subtitle">
            {hasRole('ADMIN')
              ? 'Manage your platform from the admin dashboard.'
              : hasRole('THEATRE_OWNER')
              ? 'Manage your theatres and shows.'
              : 'Discover, explore, and book your next movie experience.'}
          </p>
          <div className="hero-actions">
            <Link to="/movies" className="hero-btn hero-btn-primary">
              Browse Movies
            </Link>
            <Link to="/booking" className="hero-btn hero-btn-secondary">
              Book Tickets
            </Link>
          </div>
        </div>
      </div>

      <div className="dashboard-stats">
        {displayStats.map((stat, i) => (
          <Card key={i} className="stat-card">
            <div className="stat-icon" style={{ background: `${stat.color}15`, color: stat.color }}>
              {stat.icon}
            </div>
            <div className="stat-info">
              <span className="stat-value">{stat.value}</span>
              <span className="stat-label">{stat.label}</span>
            </div>
          </Card>
        ))}
      </div>

      <div className="dashboard-section">
        <div className="section-header">
          <h2 className="section-title">Trending Now</h2>
          <Link to="/movies" className="section-link">View All →</Link>
        </div>
        <div className="trending-grid">
          {mockMovies.slice(0, 3).map((movie) => (
            <Link to={`/booking?movie=${movie.id}`} key={movie.id} className="trending-item">
              <div className="trending-poster">
                <img src={movie.posterUrl} alt={movie.title} />
                <div className="trending-badge">★ {movie.rating}</div>
              </div>
              <div className="trending-info">
                <h3>{movie.title}</h3>
                <span>{movie.genre.join(' • ')}</span>
              </div>
            </Link>
          ))}
        </div>
      </div>

      {hasRole('ADMIN') && (
        <div className="dashboard-section">
          <div className="section-header">
            <h2 className="section-title">Quick Actions</h2>
          </div>
          <div className="admin-actions">
            <Card className="admin-card" onClick={() => {}}>
              <span className="admin-card-icon">➕</span>
              <span className="admin-card-label">Add Movie</span>
            </Card>
            <Card className="admin-card" onClick={() => {}}>
              <span className="admin-card-icon">🏛️</span>
              <span className="admin-card-label">Manage Theatres</span>
            </Card>
            <Card className="admin-card" onClick={() => {}}>
              <span className="admin-card-icon">📊</span>
              <span className="admin-card-label">View Reports</span>
            </Card>
            <Card className="admin-card" onClick={() => {}}>
              <span className="admin-card-icon">👥</span>
              <span className="admin-card-label">Manage Users</span>
            </Card>
          </div>
        </div>
      )}
    </div>
  );
}
