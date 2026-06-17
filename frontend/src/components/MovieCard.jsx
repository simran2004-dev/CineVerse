import './MovieCard.css';

export default function MovieCard({ movie, onClick }) {
  return (
    <div className="movie-card" onClick={() => onClick?.(movie)}>
      <div className="movie-poster">
        <img src={movie.posterUrl} alt={movie.title} />
        <div className="movie-rating">
          <span className="rating-star">★</span>
          <span>{movie.rating}</span>
        </div>
        <div className="movie-overlay">
          <button className="movie-book-btn">Book Now</button>
        </div>
      </div>
      <div className="movie-info">
        <h3 className="movie-title">{movie.title}</h3>
        <div className="movie-meta">
          <span className="movie-genre">{movie.genre?.join(', ')}</span>
          <span className="movie-duration">{movie.duration}</span>
        </div>
        <div className="movie-language">{movie.language}</div>
      </div>
    </div>
  );
}
