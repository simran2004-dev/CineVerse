import { useState } from 'react';
import MovieCard from '../components/MovieCard';
import Input from '../components/Input';
import { mockMovies } from '../utils/mockData';
import { useNavigate } from 'react-router-dom';
import './MovieCatalog.css';

export default function MovieCatalog() {
  const [search, setSearch] = useState('');
  const [selectedGenre, setSelectedGenre] = useState('All');
  const navigate = useNavigate();

  const genres = ['All', ...new Set(mockMovies.flatMap((m) => m.genre))];

  const filtered = mockMovies.filter((movie) => {
    const matchesSearch = movie.title.toLowerCase().includes(search.toLowerCase());
    const matchesGenre = selectedGenre === 'All' || movie.genre.includes(selectedGenre);
    return matchesSearch && matchesGenre;
  });

  const handleMovieClick = (movie) => {
    navigate(`/booking?movie=${movie.id}`);
  };

  return (
    <div className="catalog">
      <div className="catalog-header">
        <div className="catalog-title-section">
          <h1 className="catalog-title">Movies</h1>
          <p className="catalog-subtitle">Discover and book your next cinematic experience</p>
        </div>

        <div className="catalog-filters">
          <div className="search-wrapper">
            <span className="search-icon">🔍</span>
            <input
              type="text"
              className="search-input"
              placeholder="Search movies..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
          </div>

          <div className="genre-filters">
            {genres.map((genre) => (
              <button
                key={genre}
                className={`genre-chip ${selectedGenre === genre ? 'genre-active' : ''}`}
                onClick={() => setSelectedGenre(genre)}
              >
                {genre}
              </button>
            ))}
          </div>
        </div>
      </div>

      <div className="catalog-results">
        <span className="results-count">{filtered.length} movies found</span>
      </div>

      {filtered.length === 0 ? (
        <div className="catalog-empty">
          <span className="empty-icon">🎬</span>
          <h3>No movies found</h3>
          <p>Try a different search or filter</p>
        </div>
      ) : (
        <div className="catalog-grid">
          {filtered.map((movie) => (
            <MovieCard key={movie.id} movie={movie} onClick={handleMovieClick} />
          ))}
        </div>
      )}
    </div>
  );
}
