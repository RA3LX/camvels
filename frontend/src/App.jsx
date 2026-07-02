import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Layout from './components/Layout';
import ProtectedRoute from './components/ProtectedRoute';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Products from './pages/Products';
import ProductForm from './pages/ProductForm';
import Categories from './pages/Categories';
import CategoryForm from './pages/CategoryForm';
import Suppliers from './pages/Suppliers';
import SupplierForm from './pages/SupplierForm';
import Movements from './pages/Movements';
import MovementForm from './pages/MovementForm';
import Users from './pages/Users';
import UserForm from './pages/UserForm';
import Reports from './pages/Reports';

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route element={<ProtectedRoute><Layout /></ProtectedRoute>}>
            <Route index element={<Dashboard />} />
            <Route path="products" element={<Products />} />
            <Route path="products/new" element={<ProductForm />} />
            <Route path="products/:id/edit" element={<ProductForm />} />
            <Route path="categories" element={<ProtectedRoute roles={['admin', 'supervisor']}><Categories /></ProtectedRoute>} />
            <Route path="categories/new" element={<ProtectedRoute roles={['admin', 'supervisor']}><CategoryForm /></ProtectedRoute>} />
            <Route path="categories/:id/edit" element={<ProtectedRoute roles={['admin', 'supervisor']}><CategoryForm /></ProtectedRoute>} />
            <Route path="suppliers" element={<Suppliers />} />
            <Route path="suppliers/new" element={<SupplierForm />} />
            <Route path="suppliers/:id/edit" element={<SupplierForm />} />
            <Route path="movements" element={<ProtectedRoute roles={['admin', 'supervisor']}><Movements /></ProtectedRoute>} />
            <Route path="movements/new" element={<ProtectedRoute roles={['admin', 'supervisor']}><MovementForm /></ProtectedRoute>} />
            <Route path="users" element={<ProtectedRoute roles={['admin']}><Users /></ProtectedRoute>} />
            <Route path="users/new" element={<ProtectedRoute roles={['admin']}><UserForm /></ProtectedRoute>} />
            <Route path="users/:id/edit" element={<ProtectedRoute roles={['admin']}><UserForm /></ProtectedRoute>} />
            <Route path="reports" element={<ProtectedRoute roles={['admin', 'supervisor', 'almacen']}><Reports /></ProtectedRoute>} />
          </Route>
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
