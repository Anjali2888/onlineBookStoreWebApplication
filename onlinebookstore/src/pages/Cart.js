import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import { ordersAPI } from '../services/api';
import './Cart.css';
const Cart = () => {
  const { items, updateQuantity, removeFromCart, clearCart, total } = useCart();
  const navigate = useNavigate();
  const [loading, setLoading] = React.useState(false);
  const [error, setError] = React.useState('');
  const handleCheckout = async () => {
    setLoading(true);
    setError('');
    try {
      const orderData = {
        items: items.map(item => ({
          bookId: item.book.id,
          quantity: item.quantity
        }))
      };
      await ordersAPI.create(orderData);
      clearCart();
      navigate('/orders');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to place order');
    } finally {
      setLoading(false);
    }
  };
  if (items.length === 0) {
    return (
      <div className="cart-empty">
        <h1>Your Cart is Empty</h1>
        <p>Add some books to get started!</p>
        <button onClick={() => navigate('/books')} className="btn btn-primary">
          Browse Books
        </button>
      </div>
    );
  }
  return (
    <div className="cart-page">
      <h1 className="page-title">Shopping Cart</h1>
      {error && <div className="error-message">{error}</div>}
      <div className="cart-content">
        <div className="cart-items">
          {items.map(({ book, quantity }) => (
            <div key={book.id} className="cart-item">
              <div className="cart-item-cover">
                {book.imageUrl ? (
                  <img src={book.imageUrl} alt={book.title} />
                ) : (
                  <div className="placeholder">📖</div>
                )}
              </div>
              <div className="cart-item-info">
                <h3>{book.title}</h3>
                <p className="author">by {book.author}</p>
                <p className="price">${book.price.toFixed(2)}</p>
              </div>
              <div className="cart-item-quantity">
                <button onClick={() => updateQuantity(book.id, quantity - 1)}>-</button>
                <span>{quantity}</span>
                <button onClick={() => updateQuantity(book.id, quantity + 1)}>+</button>
              </div>
              <div className="cart-item-subtotal">
                ${(book.price * quantity).toFixed(2)}
              </div>
              <button 
                onClick={() => removeFromCart(book.id)}
                className="remove-btn"
              >
                ✕
              </button>
            </div>
          ))}
        </div>
        <div className="cart-summary card">
          <h2>Order Summary</h2>
          
          <div className="summary-row">
            <span>Subtotal ({items.length} items)</span>
            <span>${total.toFixed(2)}</span>
          </div>
          
          <div className="summary-row">
            <span>Shipping</span>
            <span>Free</span>
          </div>
          
          <div className="summary-total">
            <span>Total</span>
            <span>${total.toFixed(2)}</span>
          </div>
          <button 
            onClick={handleCheckout} 
            className="btn btn-success checkout-btn"
            disabled={loading}
          >
            {loading ? 'Processing...' : 'Place Order'}
          </button>
        </div>
      </div>
    </div>
  );
};
export default Cart;