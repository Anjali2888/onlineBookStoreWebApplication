import React, { useState, useEffect } from 'react';
import { ordersAPI } from '../services/api';
import './Orders.css';


const Orders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError]=useState(null);
useEffect(() => {
  ordersAPI.getMyOrders()
    .then(orders => {
      setOrders(orders);
    })
    .catch(err => {
      console.error('Order error:', err.response?.data);
      setError(err.response?.data?.message || 'Failed to load orders');
    })
    .finally(() => {
      setLoading(false);
    });
}, []);
 
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
    return <div className="loading">Loading orders...</div>;
  }
 if (error) {
  return <div className="error-message">{error}</div>;
}
  return (
    
    <div className="orders-page">
      <h1 className="page-title">My Orders</h1>
      {orders.length === 0 ? (
        <div className="no-orders">
          <p>You haven't placed any orders yet.</p>
        </div>
      ) : (
        <div className="orders-list">
          {orders.map(order => (
            <div key={order.id} className="order-card card">
              <div className="order-header">
                <div>
                  <span className="order-id">Order #{order.id}</span>
                   <span className="order-date">{new Date(order.createdAt).toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' })}</span>
                </div>
                <span 
                  className="order-status"
                  style={{ background: getStatusColor(order.status) }}
                >
                  {order.status}
                </span>
              </div>
              <div className="order-items">
                {order.items.map(item => (
                  <div key={item.bookId} className="order-item">
                    <span className="item-title">{item.bookTitle}</span>
                    <span className="item-qty">×{item.quantity}</span>
                    <span className="item-price">${item.priceAtPurchase.toFixed(2)}</span>
                  </div>
                ))}
              </div>
              <div className="order-footer">
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
export default Orders;