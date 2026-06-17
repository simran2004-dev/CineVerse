import { useAuth } from '../context/AuthContext';
import Card from '../components/Card';
import { mockMovies, mockTheatres } from '../utils/mockData';
import './AdminPage.css';

export default function AdminPage() {
  const { hasRole } = useAuth();

  if (!hasRole('ADMIN') && !hasRole('THEATRE_OWNER')) {
    return (
      <div className="admin-page">
        <div className="admin-denied">
          <span>🚫</span>
          <h2>Access Denied</h2>
          <p>You don't have permission to view this page.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="admin-page">
      <div className="admin-header">
        <h1 className="admin-title">
          {hasRole('ADMIN') ? 'Admin Dashboard' : 'Theatre Management'}
        </h1>
        <p className="admin-subtitle">
          {hasRole('ADMIN')
            ? 'Manage movies, theatres, users, and system settings'
            : 'Manage your theatres and show schedules'}
        </p>
      </div>

      {/* Movies Management */}
      <section className="admin-section">
        <div className="section-header">
          <h2>Movies</h2>
          <button className="admin-add-btn">+ Add Movie</button>
        </div>
        <div className="admin-table-wrapper">
          <table className="admin-table">
            <thead>
              <tr>
                <th>Title</th>
                <th>Genre</th>
                <th>Rating</th>
                <th>Language</th>
                <th>Duration</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {mockMovies.map((movie) => (
                <tr key={movie.id}>
                  <td>
                    <div className="table-movie">
                      <img src={movie.posterUrl} alt={movie.title} className="table-poster" />
                      <span>{movie.title}</span>
                    </div>
                  </td>
                  <td>{movie.genre.join(', ')}</td>
                  <td><span className="table-rating">★ {movie.rating}</span></td>
                  <td>{movie.language}</td>
                  <td>{movie.duration}</td>
                  <td>
                    <div className="table-actions">
                      <button className="action-edit">Edit</button>
                      <button className="action-delete">Delete</button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>

      {/* Theatres Management */}
      <section className="admin-section">
        <div className="section-header">
          <h2>Theatres</h2>
          <button className="admin-add-btn">+ Add Theatre</button>
        </div>
        <div className="theatres-grid">
          {mockTheatres.map((theatre) => (
            <Card key={theatre.id} className="theatre-card">
              <div className="theatre-header">
                <span className="theatre-icon">🏛️</span>
                <div>
                  <h3 className="theatre-name">{theatre.name}</h3>
                  <span className="theatre-location">📍 {theatre.location}</span>
                </div>
              </div>
              <div className="theatre-screens">
                {theatre.screens.map((screen) => (
                  <div key={screen.id} className="screen-item">
                    <span className="screen-name">{screen.name}</span>
                    <span className="screen-cap">{screen.capacity} seats</span>
                  </div>
                ))}
              </div>
            </Card>
          ))}
        </div>
      </section>
    </div>
  );
}
