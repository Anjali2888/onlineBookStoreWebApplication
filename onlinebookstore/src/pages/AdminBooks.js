import React, { useState, useEffect } from 'react';
import { booksAPI } from '../services/api';
import './Admin.css';
const AdminBooks = () => {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    title: '',
    author: '',
    isbn: '',
    description: '',
    price: '',
    stockQuantity: '',
    category: '',
    imageUrl: ''
  });
  const [error, setError] = useState('');
  useEffect(() => {
    loadBooks();
  }, []);
  const loadBooks = async () => {
    try {
      const data = await booksAPI.getAll();
      setBooks(data);
    } catch (err) {
      setError('Failed to load books');
    } finally {
      setLoading(false);
    }
  };
  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };
  const resetForm = () => {
    setFormData({
      title: '',
      author: '',
      isbn: '',
      description: '',
      price: '',
      stockQuantity: '',
      category: '',
      imageUrl: ''
    });
    setEditing(null);
    setShowForm(false);
  };
  const handleEdit = (book) => {
    setFormData({
      title: book.title,
      author: book.author,
      isbn: book.isbn,
      description: book.description || '',
      price: book.price.toString(),
      stockQuantity: book.stockQuantity.toString(),
      category: book.category,
      imageUrl: book.imageUrl || ''
    });
    setEditing(book.id);
    setShowForm(true);
  };
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    const bookData = {
      ...formData,
      price: parseFloat(formData.price),
      stockQuantity: parseInt(formData.stockQuantity)
    };
    try {
      if (editing) {
        await booksAPI.update(editing, bookData);
      } else {
        await booksAPI.create(bookData);
      }
      loadBooks();
      resetForm();
    } catch (err) {
      setError(err.response?.data.data?.message || 'Failed to save book');
    }
  };
  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this book?')) return;
    try {
      await booksAPI.delete(id);
      loadBooks();
    } catch (err) {
      setError('Failed to delete book');
    }
  };
  if (loading) {
    return <div className="loading">Loading...</div>;
  }
 
  return (
    <div className="admin-page">
      <div className="admin-header">
        <h1 className="page-title">Manage Books</h1>
        <button 
          onClick={() => setShowForm(!showForm)} 
          className="btn btn-primary"
        >
          {showForm ? 'Cancel' : '+ Add Book'}
        </button>
      </div>
      {error && <div className="error-message">{error}</div>}
      {showForm && (
        <form onSubmit={handleSubmit} className="admin-form card">
          <h2>{editing ? 'Edit Book' : 'Add New Book'}</h2>
          
          <div className="form-row">
            <div className="form-group">
              <label><span style={{ color: 'red' }}>*</span>
                Title</label>
              <input name="title" value={formData.title} onChange={handleChange} required />
            </div>
            <div className="form-group">
              <label><span style={{ color: 'red' }}>*</span>
                Author</label>
              <input name="author" value={formData.author} onChange={handleChange} required />
            </div>
          </div>
          <div className="form-row">
            <div className="form-group">
              <label><span style={{ color: 'red' }}>*</span>
                ISBN</label>
              <input name="isbn" value={formData.isbn} onChange={handleChange} required />
            </div>
            <div className="form-group">
              <label><span style={{ color: 'red' }}>*</span>
                Category</label>
              <select name="category" value={formData.category} onChange={handleChange} required>
                <option value="">Select category</option>
                <option value="Programming">Programming</option>
                <option value="Self-Help">Self-Help</option>
                <option value="History">History</option>
                <option value="Fiction">Fiction</option>
              </select>
            </div>
          </div>
          <div className="form-row">
            <div className="form-group">
                <label><span style={{ color: 'red' }}>*</span>Price ($)</label>
              <input 
                type="number" 
                name="price" 
                value={formData.price} 
                onChange={handleChange} 
                step="0.01"
                min="0"
                required 
              />
            </div>
            <div className="form-group">
              <label><span style={{ color: 'red' }}>*</span>
                Stock</label>
              <input 
                type="number" 
                name="stockQuantity" 
                value={formData.stockQuantity} 
                onChange={handleChange}
                min="0"
                required 
              />
            </div>
          </div>
          <div className="form-group">
            <label>Image URL</label>
            <input name="imageUrl" value={formData.imageUrl} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label>Description</label>
            <textarea 
              name="description" 
              value={formData.description} 
              onChange={handleChange}
              rows={4}
            />
          </div>
          <div className="form-actions">
            <button type="button" onClick={resetForm} className="btn btn-secondary">
              Cancel
            </button>
            <button type="submit" className="btn btn-success">
              {editing ? 'Update Book' : 'Add Book'}
            </button>
          </div>
        </form>
      )}
      <div className="admin-table-container">
        <table className="admin-table">
          <thead>
            <tr>
              <th>Title</th>
              <th>Author</th>
              <th>Category</th>
              <th>Price</th>
              <th>Stock</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {books.map(book => (
              <tr key={book.id}>
                <td>{book.title}</td>
                <td>{book.author}</td>
                <td>{book.category}</td>
                <td>${book.price.toFixed(2)}</td>
                <td>{book.stockQuantity}</td>
                <td>
                  <div className="action-buttons">
                    <button onClick={() => handleEdit(book)} className="btn btn-secondary btn-sm">
                      Edit
                    </button>
                    <button onClick={() => handleDelete(book.id)} className="btn btn-danger btn-sm">
                      Delete
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};
export default AdminBooks;