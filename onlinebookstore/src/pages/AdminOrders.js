import React, { useState, useEffect } from 'react';
import { ordersAPI } from '../services/api';
import './Admin.css';
const AdminOrders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  useEffect(() => {
    loadOrders();
  }, []);
  const loadOrders = async () => {
    try {
      const data = await ordersAPI.getAllOrders();
      setOrders(data);
    } catch (err) {
      setError('Failed to load orders');
    } finally {
      setLoading(false);
    }
  };
  const handleStatusChange = async (orderId, newStatus) => {
    try {
      await ordersAPI.updateStatus(orderId, newStatus);
      loadOrders();
    } catch (err) {
      setError('Failed to update order status');
    }
  };
  const getStatusColor = (status) => {
    const colors = {
      PENDING: '#f39c12',
      CONFIRMED: '#3498db',
      SHIPPED: '#9b59b6',
      DELIVERED: '#27ae60',
      CANCELLED: '#e74c3c'
    };
    return colors[status] || '#7f8c8d';
  };
  if (loading) {
    return <div className="loading">Loading...</div>;
  }
 console.log("myorder:"+orders);
  return (
    <div className="admin-page">
      <h1 className="page-title">All Orders</h1>
      {error && <div className="error-message">{error}</div>}
      {orders.length === 0 ? (
        <p>No orders found.</p>
      ) : (
        <div className="orders-admin-list">
          {orders.map(order => (
            
            <div key={order.id} className="order-admin-card card">
              <div className="order-admin-header">
                <div className="order-meta">
                  <span className="order-id">Order #{order.id}</span>
                  <span className="order-user">by {order.username}</span>
                  <span className="order-date">{new Date(order.createdAt).toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' })}</span>
                </div>
                <div className="order-status-control">
                  <select
                    value={order.status}
                    onChange={(e) => handleStatusChange(order.id, e.target.value)}
                    style={{ borderColor: getStatusColor(order.status) }}
                  >
                    <option value="PENDING">Pending</option>
                    <option value="CONFIRMED">Confirmed</option>
                    <option value="SHIPPED">Shipped</option>
                    <option value="DELIVERED">Delivered</option>
                    <option value="CANCELLED">Cancelled</option>
                  </select>
                </div>
              </div>
              <div className="order-items-list">
                {order.items.map(item => (
                  <div key={item.bookId} className="order-item-row">
                    <span>{item.bookTitle}</span>
                    <span>×{item.quantity}</span>
                    <span>${item.priceAtPurchase.toFixed(2)}</span>
                  </div>
                ))}
              </div>
              <div className="order-admin-footer">
                <span className="order-total">
                  Total: ${order.totalAmount.toFixed(2)}
                </span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
export default AdminOrders;