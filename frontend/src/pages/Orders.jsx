import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';

const Orders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();
  const isManager = user?.role === 'MANAGER' || user?.role === 'ADMIN';

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const response = await api.get('/orders');
      setOrders(response.data);
    } catch (error) {
      console.error('Error fetching orders:', error);
    } finally {
      setLoading(false);
    }
  };

  const updateStatus = async (id, status) => {
    try {
      await api.put(`/orders/${id}/status`, { status });
      fetchOrders(); // refresh table
    } catch (error) {
      console.error('Error updating order:', error);
      alert('Failed to update status');
    }
  };

  const getStatusBadge = (status) => {
    const styles = {
      'PENDING': 'bg-yellow-100 text-yellow-800 border border-yellow-200',
      'SHIPPED': 'bg-blue-100 text-blue-800 border border-blue-200',
      'DELIVERED': 'bg-green-100 text-green-800 border border-green-200'
    };
    return `px-3 py-1 inline-flex text-xs leading-5 font-bold rounded-full uppercase tracking-wide ${styles[status] || styles['PENDING']}`;
  };

  if (loading) return <div>Loading orders...</div>;

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-gray-900">Order Management</h1>
      </div>

      <div className="bg-white shadow-sm rounded-xl border border-gray-100 overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Order ID</th>
              <th className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Product</th>
              <th className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Supplier</th>
              <th className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Amount</th>
              <th className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Status</th>
              <th className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Date</th>
              {isManager && <th className="px-6 py-4 text-right text-xs font-semibold text-gray-500 uppercase tracking-wider">Actions</th>}
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {orders.length === 0 ? (
              <tr><td colSpan={isManager ? "7" : "6"} className="px-6 py-10 text-center text-gray-500">No orders found</td></tr>
            ) : (
              orders.map((order) => (
                <tr key={order.id} className="hover:bg-gray-50/50 transition-colors">
                  <td className="px-6 py-5 font-medium text-gray-900">ORD-{order.id.toString().padStart(4, '0')}</td>
                  <td className="px-6 py-5 text-gray-700">{order.productName}</td>
                  <td className="px-6 py-5 text-gray-500 text-sm">
                    <div className="flex items-center">
                      <div className="w-6 h-6 rounded bg-indigo-100 flex items-center justify-center text-indigo-700 text-xs font-bold mr-2">
                        {order.supplierName.charAt(0)}
                      </div>
                      {order.supplierName}
                    </div>
                  </td>
                  <td className="px-6 py-5 font-semibold text-gray-900">${order.totalAmount.toLocaleString()}</td>
                  <td className="px-6 py-5">
                    <span className={getStatusBadge(order.status)}>{order.status}</span>
                  </td>
                  <td className="px-6 py-5 text-sm text-gray-500">
                    {new Date(order.orderDate).toLocaleDateString()}
                  </td>
                  {isManager && (
                    <td className="px-6 py-5 text-right whitespace-nowrap">
                      {order.status === 'PENDING' && (
                        <button onClick={() => updateStatus(order.id, 'SHIPPED')} className="text-sm text-blue-600 hover:text-blue-900 font-medium mr-4">Mark Shipped</button>
                      )}
                      {order.status === 'SHIPPED' && (
                        <button onClick={() => updateStatus(order.id, 'DELIVERED')} className="text-sm text-green-600 hover:text-green-900 font-medium">Mark Delivered</button>
                      )}
                    </td>
                  )}
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Orders;
