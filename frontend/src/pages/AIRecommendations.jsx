import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';

const RequestDetail = () => {
  const [requests, setRequests] = useState([]);
  const [selectedReq, setSelectedReq] = useState('');
  const [recommendations, setRecommendations] = useState([]);
  const [loading, setLoading] = useState(false);
  const [creatingOrder, setCreatingOrder] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    // Fetch all SUBMITTED requests for recommendations
    const fetchRequests = async () => {
      try {
        const response = await api.get('/requests');
        setRequests(response.data.filter(r => r.status === 'SUBMITTED'));
      } catch (error) {
        console.error('Error fetching requests:', error);
      }
    };
    fetchRequests();
  }, []);

  const handleGetRecommendations = async (e) => {
    e.preventDefault();
    if (!selectedReq) return;
    
    setLoading(true);
    try {
      const response = await api.get(`/ai/recommend/${selectedReq}`);
      setRecommendations(response.data);
    } catch (error) {
      console.error('Error fetching recommendations:', error);
      alert('Failed to get AI recommendations');
    } finally {
      setLoading(false);
    }
  };

  const handleApproveAndOrder = async (supplierId) => {
    if (!window.confirm("Are you sure you want to approve this request and create an order with this supplier?")) return;
    
    setCreatingOrder(true);
    try {
      await api.post('/orders', {
        requestId: parseInt(selectedReq),
        supplierId: supplierId
      });
      alert('Order created successfully!');
      navigate('/orders');
    } catch (error) {
      console.error('Error creating order:', error);
      alert('Failed to create order');
    } finally {
      setCreatingOrder(false);
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">AI Supplier Recommendations</h1>
          <p className="text-gray-500 mt-1">Smart matching based on price, delivery, rating, and reliability</p>
        </div>
      </div>

      <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
        <form onSubmit={handleGetRecommendations} className="flex gap-4 items-end">
          <div className="flex-1">
            <label className="block text-sm font-medium text-gray-700 mb-1">Select Purchase Request pending approval</label>
            <select 
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 bg-white"
              value={selectedReq}
              onChange={(e) => setSelectedReq(e.target.value)}
              required
            >
              <option value="">-- Select Request --</option>
              {requests.map(req => (
                <option key={req.id} value={req.id}>
                  REQ-{req.id.toString().padStart(4, '0')} : {req.product} (Budget: ${req.budget})
                </option>
              ))}
            </select>
          </div>
          <button 
            type="submit" 
            disabled={loading || !selectedReq}
            className="px-6 py-2 bg-indigo-600 text-white font-medium rounded-lg hover:bg-indigo-700 transition disabled:opacity-50 flex items-center"
          >
            {loading ? 'Analyzing...' : (
              <>
                <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path></svg>
                Get AI Matches
              </>
            )}
          </button>
        </form>
      </div>

      {recommendations.length > 0 && (
        <div className="space-y-4">
          <h2 className="text-lg font-semibold text-gray-800">Top Recommended Suppliers</h2>
          
          <div className="grid grid-cols-1 gap-6">
            {recommendations.map((rec, index) => (
              <div key={rec.supplierId} className={`bg-white rounded-xl shadow-sm border ${index === 0 ? 'border-indigo-400 ring-1 ring-indigo-400' : 'border-gray-100'} p-6 overflow-hidden relative`}>
                {index === 0 && (
                  <div className="absolute top-0 right-0 bg-indigo-500 text-white px-4 py-1 rounded-bl-lg text-sm font-medium flex items-center">
                    <svg className="w-4 h-4 mr-1" fill="currentColor" viewBox="0 0 20 20"><path fillRule="evenodd" d="M11.3 1.046A1 1 0 0112 2v5h4a1 1 0 01.82 1.573l-7 10A1 1 0 018 18v-5H4a1 1 0 01-.82-1.573l7-10a1 1 0 011.12-.381z" clipRule="evenodd"></path></svg>
                    Best Match
                  </div>
                )}
                
                <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-6">
                  <div className="flex-1">
                    <div className="flex items-center gap-3 mb-2">
                      <h3 className="text-xl font-bold text-gray-900">{rec.supplierName}</h3>
                      <span className="bg-indigo-50 text-indigo-700 font-bold px-2.5 py-0.5 rounded-md text-sm">
                        AI Score: {rec.aiScore}
                      </span>
                    </div>
                    
                    <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mt-4">
                      <div>
                        <p className="text-sm text-gray-500">Price</p>
                        <p className="font-semibold text-gray-900">${rec.price.toLocaleString()}</p>
                      </div>
                      <div>
                        <p className="text-sm text-gray-500">Delivery</p>
                        <p className="font-semibold text-gray-900">{rec.deliveryTime} days</p>
                      </div>
                      <div>
                        <p className="text-sm text-gray-500">Rating</p>
                        <div className="flex items-center text-yellow-500 font-semibold gap-1">
                          {rec.rating}
                          <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 20 20"><path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"></path></svg>
                        </div>
                      </div>
                      <div>
                        <p className="text-sm text-gray-500">Reliability</p>
                        <p className="font-semibold text-green-600">{rec.reliabilityScore}%</p>
                      </div>
                    </div>
                    
                    <div className="mt-4 bg-gray-50 p-3 rounded-lg border border-gray-100 flex items-start">
                      <svg className="w-5 h-5 text-gray-400 mr-2 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                      <p className="text-sm text-gray-600"><strong>AI Insight:</strong> {rec.reason}</p>
                    </div>
                  </div>
                  
                  <div className="w-full md:w-auto mt-4 md:mt-0 pt-4 md:pt-0 border-t md:border-t-0 border-gray-100">
                    <button
                      onClick={() => handleApproveAndOrder(rec.supplierId)}
                      disabled={creatingOrder}
                      className="w-full bg-green-600 text-white px-6 py-3 rounded-lg hover:bg-green-700 transition shadow-sm font-medium flex justify-center items-center"
                    >
                      {creatingOrder ? 'Processing...' : 'Approve & Order'}
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default RequestDetail;
