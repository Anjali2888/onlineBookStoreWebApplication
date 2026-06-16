import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { booksAPI } from '../services/api';
import BookCard from '../components/BookCard';
import './Home.css';
const Home = () => {
  const [featuredBooks, setFeaturedBooks] = useState([]);
  const [loading, setLoading] = useState(true);
useEffect(() => {
  booksAPI.getAll()
    .then(books => {
      setFeaturedBooks(books.slice(0, 4));
    })
    .catch(console.error)
    .finally(() => setLoading(false));
}, []);
  return (
    <div className="home">
      <section className="hero">
        <h1>Welcome to BookStore</h1>
        <p>Discover your next favorite book from our curated collection</p>
        <Link to="/books" className="btn btn-primary">
          Browse All Books
        </Link>
      </section>
      <section className="featured-section">
        <h2>Featured Books</h2>
        {loading ? (
          <div className="loading">Loading...</div>
        ) : (
          <div className="books-grid">
            {featuredBooks.map(book => (
              <BookCard key={book.id} book={book} />
            ))}
          </div>
        )}
      </section>
      <section className="categories-section">
        <h2>Browse by Category</h2>
        <div className="categories-grid">
          {['Programming', 'History', 'Fiction', 'Self-Help'].map(category => (
            <Link
              key={category}
              to={`/books?category=${category}`}
              className="category-card"
            >
              {category}
            </Link>
          ))}
        </div>
      </section>
    </div>
  );
};
export default Home;