import { useState } from 'react';
import './SeatLayout.css';

export default function SeatLayout({ seats, onSeatSelect }) {
  const [selectedSeats, setSelectedSeats] = useState([]);

  const toggleSeat = (seat) => {
    if (seat.status === 'BOOKED' || seat.status === 'LOCKED') return;

    setSelectedSeats((prev) => {
      const isSelected = prev.find((s) => s.id === seat.id);
      const updated = isSelected
        ? prev.filter((s) => s.id !== seat.id)
        : [...prev, seat];
      onSeatSelect?.(updated);
      return updated;
    });
  };

  const getSeatClass = (seat) => {
    if (seat.status === 'BOOKED') return 'seat seat-booked';
    if (seat.status === 'LOCKED') return 'seat seat-locked';
    if (selectedSeats.find((s) => s.id === seat.id)) return 'seat seat-selected';
    if (seat.type === 'VIP') return 'seat seat-vip';
    if (seat.type === 'PREMIUM') return 'seat seat-premium';
    return 'seat seat-available';
  };

  // Group seats by row
  const rows = seats.reduce((acc, seat) => {
    if (!acc[seat.row]) acc[seat.row] = [];
    acc[seat.row].push(seat);
    return acc;
  }, {});

  return (
    <div className="seat-layout">
      <div className="screen-indicator">
        <div className="screen-curve"></div>
        <span>SCREEN</span>
      </div>

      <div className="seats-grid">
        {Object.entries(rows).map(([row, rowSeats]) => (
          <div key={row} className="seat-row">
            <span className="row-label">{row}</span>
            <div className="row-seats">
              {rowSeats
                .sort((a, b) => a.column - b.column)
                .map((seat) => (
                  <button
                    key={seat.id}
                    className={getSeatClass(seat)}
                    onClick={() => toggleSeat(seat)}
                    disabled={seat.status === 'BOOKED' || seat.status === 'LOCKED'}
                    title={`${seat.row}${seat.column} - ${seat.type} - ₹${seat.price}`}
                  >
                    {seat.column}
                  </button>
                ))}
            </div>
          </div>
        ))}
      </div>

      <div className="seat-legend">
        <div className="legend-item">
          <div className="legend-box seat-available"></div>
          <span>Available</span>
        </div>
        <div className="legend-item">
          <div className="legend-box seat-selected"></div>
          <span>Selected</span>
        </div>
        <div className="legend-item">
          <div className="legend-box seat-booked"></div>
          <span>Booked</span>
        </div>
        <div className="legend-item">
          <div className="legend-box seat-premium"></div>
          <span>Premium</span>
        </div>
        <div className="legend-item">
          <div className="legend-box seat-vip"></div>
          <span>VIP</span>
        </div>
      </div>

      {selectedSeats.length > 0 && (
        <div className="selection-summary">
          <span>Selected: {selectedSeats.map((s) => `${s.row}${s.column}`).join(', ')}</span>
          <span className="selection-total">
            Total: ₹{selectedSeats.reduce((sum, s) => sum + s.price, 0)}
          </span>
        </div>
      )}
    </div>
  );
}
