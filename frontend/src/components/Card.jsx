import './Card.css';

export default function Card({ children, className = '', hover = true, onClick }) {
  return (
    <div
      className={`card ${hover ? 'card-hover' : ''} ${className}`}
      onClick={onClick}
    >
      {children}
    </div>
  );
}
