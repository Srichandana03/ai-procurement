import React from 'react';
import ReactDOM from 'react-router-dom';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { ProtectedRoute } from './components/ProtectedRoute';

// Layout
import MainLayout from './components/layout/MainLayout';

// Pages
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import PurchaseRequests from './pages/PurchaseRequests';
import CreateRequest from './pages/CreateRequest';
import Orders from './pages/Orders';
import Suppliers from './pages/Suppliers';
import AIRecommendations from './pages/AIRecommendations';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          
          <Route path="/" element={
            <ProtectedRoute>
              <MainLayout />
            </ProtectedRoute>
          }>
            <Route index element={<Navigate to="/dashboard" replace />} />
            <Route path="dashboard" element={<Dashboard />} />
            
            <Route path="requests" element={<PurchaseRequests />} />
            <Route path="requests/create" element={
              <ProtectedRoute roles={['EMPLOYEE', 'ADMIN']}>
                <CreateRequest />
              </ProtectedRoute>
            } />
            
            <Route path="suppliers" element={
              <ProtectedRoute roles={['ADMIN']}>
                <Suppliers />
              </ProtectedRoute>
            } />

            <Route path="orders" element={
              <ProtectedRoute roles={['MANAGER', 'ADMIN', 'VENDOR']}>
                <Orders />
              </ProtectedRoute>
            } />
            
            <Route path="ai-recommendations" element={
              <ProtectedRoute roles={['MANAGER', 'ADMIN']}>
                <AIRecommendations />
              </ProtectedRoute>
            } />
          </Route>
          
          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
