import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { booksAPI } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import './BookDetail.css';
const BookDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const { addToCart } = useCart();
  const [book, setBook] = useState(null);
  const [loading, setLoading] = useState(true);
  const [quantity, setQuantity] = useState(1);
  useEffect(() => {
    booksAPI.getById(id)
      .then(setBook)
      .catch(() => navigate('/books'))
      .finally(() => setLoading(false));
  }, [id, navigate]);
  const handleAddToCart = () => {
    addToCart(book, quantity);
    navigate('/cart');
  };
  if (loading) {
    return <div className="loading">Loading...</div>;
  }
  if (!book) {
    return <div className="error-message">Book not found</div>;
  }
  console.log('quantity:', quantity);
console.log('book:', book);
console.log('stockQuantity:', book?.stockQuantity);
  return (
    <div className="book-detail">
      <button onClick={() => navigate(-1)} className="back-btn">
        ← Back
      </button>
      <div className="book-detail-content">
        <div className="book-detail-cover">
          {book.imageUrl ? (
            <img src={book.imageUrl} alt={book.title} />
          ) : (
            <div className="book-cover-placeholder">📖</div>
          )}
        </div>
        <div className="book-detail-info">
          <span className="book-category-badge">{book.category}</span>
          <h1>{book.title}</h1>
          <p className="book-author">by {book.author}</p>
          
          <p className="book-description">{book.description}</p>
          <div className="book-meta">
            <div className="meta-item">
              <span className="meta-label">ISBN</span>
              <span className="meta-value">{book.isbn}</span>
            </div>
            <div className="meta-item">
              <span className="meta-label">Stock</span>
              <span className="meta-value">{book.stockQuantity} available</span>
            </div>
          </div>
          <div className="book-purchase">
            <span className="book-price">${book.price.toFixed(2)}</span>
            {user && book.stockQuantity > 0 ? (
              <div className="purchase-controls">
                <div className="quantity-selector">
                  <button 
                    onClick={() => setQuantity(q => Math.max(1, q - 1))}
                    disabled={quantity <= 1}
                  >
                    -
                  </button>
                  <span>{quantity}</span>
                  <button 
                    onClick={() => setQuantity(q => Math.min(book.stockQuantity, q + 1))}
                    disabled={quantity >= book.stockQuantity}
                  >
                    +
                  </button>
                </div>
                <button onClick={handleAddToCart} className="btn btn-primary">
                  Add to Cart
                </button>
              </div>
            ) : book.stockQuantity === 0 ? (
              <span className="out-of-stock">Out of Stock</span>
            ) : (
              <p className="login-prompt">
                <a href="/login">Log in</a> to purchase
              </p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};
export default BookDetail;