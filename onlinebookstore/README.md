# 📚 Folio Bookstore — React Frontend

A full React 16 frontend for the Spring Boot Online Bookstore API.

---
## 🚀 Quick Start

### Prerequisites
- Node.js 16+
- Spring Boot backend running on `http://localhost:8080`

### Install & Run

```bash
npm install
npm start
```

Opens at: `http://localhost:3000`

---

## 📁 Project Structure

```
src/
├── App.js                    # Root: routing + all providers
├── index.js                  # React entry point
├── index.css                 # Global design system (CSS vars, utilities)
│
├── services/
│   └── api.js                # All Axios calls to Spring Boot
│
├── context/
│   ├── AuthContext.js        # Global user state + login/logout/register
│   └── CartContext.js        # Shopping cart state
│
├── components/
│   ├── Navbar.js/css         # Sticky nav with cart badge + user menu
│   ├── BookCard.js/css       # Book tile for the grid
│   └── ProtectedRoute.js     # For user redirect to login or register
│
└── pages/
    ├── Home.js/css           # Browse + search + category filter
    ├── BookDetail.js/css     # Single book page
    ├── Login.js              # Sign in with demo credentials hint
    ├── Register.js           # Register + auto-login
    ├── Cart.js/css           # Cart with quantity controls + checkout
    ├── Orders.js/css         # Order history list
    ├── AdminBooks.js        # Single order with item table
    └── AdminOrders.js         # Dashboard: stats + order management + book inventory
```

---

## 🔑 Authentication

Uses **HTTP Basic Auth** (matching the Spring Boot backend). Credentials are stored as `btoa(username:password)` in `localStorage` and sent as the `Authorization` header on every request.

**Demo accounts (seeded by Spring Boot):**

| Role  | Username | Password   |
|-------|----------|------------|
| ADMIN | `admin`  | `admin123` |
| USER  | `ram`    | `user123`  |

---

## 📡 Pages & Features

| Page | Route | Access | Description |
|------|-------|--------|-------------|
| Browse | `/` | Public | Book grid with search + category chips |
| Book Detail | `/books/:id` | Public | Full book info + add to cart |
| Login | `/login` | Guest | Basic auth sign in |
| Register | `/register` | Guest | Create account + auto-login |
| Cart | `/cart` | User | Quantity control + place order |
| My Orders | `/orders` | User | Order history list |
| Order Detail | `/orders/:id` | User | Items table + status badge |
| Admin | `/admin` | Admin | Stats dashboard + order status updates |

---

## ⚙️ Configuration

Edit `.env` to change the backend URL:

```env
REACT_APP_API_URL=http://localhost:8080
```

For production builds:
```bash
npm run build
```

---

## 🛠 Tech Stack

- **React 18** — functional components + hooks
- **React Router v7** — client-side routing
- **Axios** — HTTP client with request/response interceptors
- **Context API** — Auth + Cart global state
- **CSS Variables** — design system with no external UI library
