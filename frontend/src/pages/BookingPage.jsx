import { useState, useMemo } from 'react';
import { useSearchParams } from 'react-router-dom';
import SeatLayout from '../components/SeatLayout';
import Button from '../components/Button';
import Card from '../components/Card';
import { mockMovies, mockShows, generateMockSeats } from '../utils/mockData';
import './BookingPage.css';

export default function BookingPage() {
  const [searchParams] = useSearchParams();
  const movieId = searchParams.get('movie');

  const [selectedMovie, setSelectedMovie] = useState(
    movieId ? mockMovies.find((m) => m.id === movieId) : null
  );
  const [selectedShow, setSelectedShow] = useState(null);
  const [selectedSeats, setSelectedSeats] = useState([]);
  const [bookingConfirmed, setBookingConfirmed] = useState(false);

  const availableShows = useMemo(
    () => (selectedMovie ? mockShows.filter((s) => s.movieId === selectedMovie.id) : []),
    [selectedMovie]
  );

  const seats = useMemo(
    () => (selectedShow ? generateMockSeats(selectedShow.id) : []),
    [selectedShow]
  );

  const handleConfirmBooking = () => {
    if (selectedSeats.length === 0) return;
    setBookingConfirmed(true);
  };

  if (bookingConfirmed) {
    return (
      <div className="booking-page">
        <div className="booking-success">
          <div className="success-animation">
            <span className="success-icon">🎉</span>
          </div>
          <h2>Booking Confirmed!</h2>
          <p>Your tickets have been booked successfully.</p>
          <Card className="booking-receipt">
            <div className="receipt-row">
              <span className="receipt-label">Movie</span>
              <span className="receipt-value">{selectedMovie.title}</span>
            </div>
            <div className="receipt-row">
              <span className="receipt-label">Show</span>
              <span className="receipt-value">{selectedShow.startTime} — {selectedShow.theatre}</span>
            </div>
            <div className="receipt-row">
              <span className="receipt-label">Seats</span>
              <span className="receipt-value">
                {selectedSeats.map((s) => `${s.row}${s.column}`).join(', ')}
              </span>
            </div>
            <div className="receipt-divider"></div>
            <div className="receipt-row receipt-total">
              <span className="receipt-label">Total</span>
              <span className="receipt-value">
                ₹{selectedSeats.reduce((sum, s) => sum + s.price, 0)}
              </span>
            </div>
          </Card>
          <Button
            variant="primary"
            onClick={() => {
              setBookingConfirmed(false);
              setSelectedSeats([]);
              setSelectedShow(null);
            }}
          >
            Book Another
          </Button>
        </div>
      </div>
    );
  }

  return (
    <div className="booking-page">
      <div className="booking-header">
        <h1 className="booking-title">Book Tickets</h1>
        <p className="booking-subtitle">Select your movie, show, and seats</p>
      </div>

      {/* Step 1: Select Movie */}
      <div className="booking-step">
        <h2 className="step-title">
          <span className="step-number">1</span> Select Movie
        </h2>
        <div className="movie-select-grid">
          {mockMovies.map((movie) => (
            <div
              key={movie.id}
              className={`movie-select-card ${selectedMovie?.id === movie.id ? 'movie-selected' : ''}`}
              onClick={() => { setSelectedMovie(movie); setSelectedShow(null); setSelectedSeats([]); }}
            >
              <img src={movie.posterUrl} alt={movie.title} />
              <div className="movie-select-info">
                <span className="movie-select-title">{movie.title}</span>
                <span className="movie-select-meta">{movie.duration} • {movie.language}</span>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Step 2: Select Show */}
      {selectedMovie && (
        <div className="booking-step">
          <h2 className="step-title">
            <span className="step-number">2</span> Select Show
          </h2>
          {availableShows.length === 0 ? (
            <p className="no-shows">No shows available for this movie.</p>
          ) : (
            <div className="shows-grid">
              {availableShows.map((show) => (
                <Card
                  key={show.id}
                  className={`show-card ${selectedShow?.id === show.id ? 'show-selected' : ''}`}
                  onClick={() => { setSelectedShow(show); setSelectedSeats([]); }}
                >
                  <span className="show-time">{show.startTime}</span>
                  <span className="show-theatre">{show.theatre}</span>
                  <span className="show-end">Ends at {show.endTime}</span>
                </Card>
              ))}
            </div>
          )}
        </div>
      )}

      {/* Step 3: Select Seats */}
      {selectedShow && (
        <div className="booking-step">
          <h2 className="step-title">
            <span className="step-number">3</span> Select Seats
          </h2>
          <SeatLayout seats={seats} onSeatSelect={setSelectedSeats} />
          {selectedSeats.length > 0 && (
            <div className="booking-confirm-bar">
              <div className="confirm-info">
                <span>{selectedSeats.length} seat{selectedSeats.length > 1 ? 's' : ''} selected</span>
                <span className="confirm-total">
                  ₹{selectedSeats.reduce((sum, s) => sum + s.price, 0)}
                </span>
              </div>
              <Button variant="primary" size="lg" onClick={handleConfirmBooking}>
                Confirm Booking
              </Button>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
