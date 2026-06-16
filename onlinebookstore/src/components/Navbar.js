
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import './Navbar.css';
const Navbar = () => {
  const { user, logout, isAdmin } = useAuth();
  const { itemCount } = useCart();
  const navigate = useNavigate();
  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-brand">
          📚 BookStore
        </Link>
        <div className="navbar-links">
          <Link to="/books">Books</Link>
          
          {user && (
            <>
              <Link to="/cart" className="cart-link">
                🛒 Cart
                {itemCount > 0 && <span className="cart-badge">{itemCount}</span>}
              </Link>
              <Link to="/orders">Your Orders</Link>
            </>
          )}
          {isAdmin && (
            <div className="admin-dropdown">
              <span className="admin-trigger">Admin ▾</span>
              <div className="admin-menu">
                <Link to="/admin/books">Manage Books</Link>
                <Link to="/admin/orders">All Orders</Link>
              </div>
            </div>
          )}
        </div>
        <div className="navbar-auth">
          {user ? (
            <>
              <span className="username">👤 {user.username}</span>
              <button onClick={handleLogout} className="btn-logout">
                Logout
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="btn-login">Login</Link>
              <Link to="/register" className="btn-register">Register</Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};
export default Navbar;