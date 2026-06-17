import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Input from '../components/Input';
import Button from '../components/Button';
import { mockUsers, mockToken } from '../utils/mockData';
import './LoginPage.css';

export default function LoginPage() {
  const [isLogin, setIsLogin] = useState(true);
  const [form, setForm] = useState({ name: '', email: '', password: '', role: 'USER' });
  const [error, setError] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');

    if (!form.email || !form.password) {
      setError('Please fill in all fields');
      return;
    }

    // Mock login — simulate backend response
    const mockUser = form.email.includes('admin')
      ? mockUsers.admin
      : form.email.includes('owner')
      ? mockUsers.owner
      : { ...mockUsers.user, name: form.name || mockUsers.user.name, email: form.email };

    login(mockUser, mockToken);
    navigate('/dashboard');
  };

  return (
    <div className="login-page">
      <div className="login-bg">
        <div className="bg-gradient-1"></div>
        <div className="bg-gradient-2"></div>
        <div className="bg-gradient-3"></div>
      </div>

      <div className="login-container">
        <div className="login-card">
          <div className="login-header">
            <span className="login-icon">🎬</span>
            <h1 className="login-title">CineVerse</h1>
            <p className="login-subtitle">
              {isLogin ? 'Welcome back! Sign in to continue.' : 'Create your account to get started.'}
            </p>
          </div>

          <form className="login-form" onSubmit={handleSubmit}>
            {!isLogin && (
              <Input
                label="Full Name"
                name="name"
                value={form.name}
                onChange={handleChange}
                placeholder="Enter your name"
                required
              />
            )}
            <Input
              label="Email"
              name="email"
              type="email"
              value={form.email}
              onChange={handleChange}
              placeholder="you@example.com"
              required
            />
            <Input
              label="Password"
              name="password"
              type="password"
              value={form.password}
              onChange={handleChange}
              placeholder="••••••••"
              required
            />
            {!isLogin && (
              <div className="input-group">
                <label className="input-label">Role</label>
                <select
                  name="role"
                  value={form.role}
                  onChange={handleChange}
                  className="input-field"
                >
                  <option value="USER">User</option>
                  <option value="THEATRE_OWNER">Theatre Owner</option>
                  <option value="ADMIN">Admin</option>
                </select>
              </div>
            )}

            {error && <div className="login-error">{error}</div>}

            <Button type="submit" variant="primary" fullWidth size="lg">
              {isLogin ? 'Sign In' : 'Create Account'}
            </Button>
          </form>

          <div className="login-footer">
            <span className="login-switch-text">
              {isLogin ? "Don't have an account?" : 'Already have an account?'}
            </span>
            <button
              className="login-switch-btn"
              onClick={() => { setIsLogin(!isLogin); setError(''); }}
            >
              {isLogin ? 'Sign Up' : 'Sign In'}
            </button>
          </div>

          <div className="login-demo">
            <p className="demo-title">Quick Demo Login</p>
            <div className="demo-buttons">
              <button className="demo-btn" onClick={() => { login(mockUsers.user, mockToken); navigate('/dashboard'); }}>
                👤 User
              </button>
              <button className="demo-btn" onClick={() => { login(mockUsers.admin, mockToken); navigate('/dashboard'); }}>
                🛡️ Admin
              </button>
              <button className="demo-btn" onClick={() => { login(mockUsers.owner, mockToken); navigate('/dashboard'); }}>
                🎭 Owner
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
