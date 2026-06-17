import './Input.css';

export default function Input({ label, type = 'text', value, onChange, placeholder, error, name, required }) {
  return (
    <div className="input-group">
      {label && <label className="input-label" htmlFor={name}>{label}</label>}
      <input
        id={name}
        name={name}
        type={type}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        className={`input-field ${error ? 'input-error' : ''}`}
        required={required}
      />
      {error && <span className="input-error-text">{error}</span>}
    </div>
  );
}
