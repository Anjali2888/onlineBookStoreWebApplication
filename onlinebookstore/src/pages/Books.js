import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { booksAPI } from '../services/api';
import BookCard from '../components/BookCard';
import './Books.css';

const Books = () => {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [searchParams, setSearchParams] = useSearchParams();
  const categoryFilter = searchParams.get('category') || '';
  useEffect(() => {
    setLoading(true);
    const fetchBooks = async () => {
      try {
        let data;
        if (categoryFilter) {
          data = await booksAPI.getByCategory(categoryFilter);
        } else if (searchTerm) {
          data = await booksAPI.search(searchTerm);
        } else {
          data = await booksAPI.getAll();
        }
        setBooks(data);
      } catch (error) {
        console.error('Failed to fetch books:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchBooks();
  }, [categoryFilter, searchTerm]);
  const handleSearch = (e) => {
    e.preventDefault();
    setSearchParams({});
  };
  const clearFilters = () => {
    setSearchTerm('');
    setSearchParams({});
  };
  return (
    <div className="books-page">
      <h1 className="page-title">
        {categoryFilter ? `${categoryFilter} Books` : 'All Books'}
      </h1>
      <div className="books-toolbar">
        <form onSubmit={handleSearch} className="search-form">
          <input
            type="text"
            placeholder="Search books..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <button type="submit" className="btn btn-primary">Search</button>
        </form>
        {(categoryFilter || searchTerm) && (
          <button onClick={clearFilters} className="btn btn-secondary">
            Clear Filters
          </button>
        )}
      </div>
      {loading ? (
        <div className="loading">Loading books...</div>
      ) : books.length === 0 ? (
        <div className="no-results">
          <p>No books found.</p>
        </div>
      ) : (
        <div className="books-grid">
          {books.map(book => (
            <BookCard key={book.id} book={book} />
          ))}
        </div>
      )}
    </div>
  );
};
export default Books;