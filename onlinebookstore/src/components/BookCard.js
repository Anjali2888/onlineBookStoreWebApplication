import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import './BookCard.css';
const BookCard = ({ book }) => {

  const { user } = useAuth();
  const { addToCart } = useCart();

  const handleAddToCart = (e) => {
    e.preventDefault();
    addToCart(book);
  };
  return (
    <Link to={`/books/${book.id}`} className="book-card">
      <div className="book-cover">
        {book.imageUrl ? (
          <img src={book.imageUrl} alt={book.title} />
        ) : (
          <div className="book-cover-placeholder">📖</div>
        )}
      </div>
      <div className="book-info">
        <h3 className="book-title">{book.title}</h3>
        <p className="book-author">by {book.author}</p>
        <p className="book-category">{book.category}</p>
        <div className="book-footer">
          <span className="book-price">${book.price.toFixed(2)}</span>
          {user && book.stockQuantity > 0 && (
            <button onClick={handleAddToCart} className="btn btn-primary btn-sm">
              Add to Cart
            </button>
          )}
          {book.stockQuantity === 0 && (
            <span className="out-of-stock">Out of Stock</span>
          )}
        </div>
      </div>
    </Link>
  );
};
export default BookCard;