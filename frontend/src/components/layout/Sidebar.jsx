import React from 'react';
import { NavLink } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const Sidebar = () => {
  const { user } = useAuth();

  const links = [
    { name: 'Dashboard', path: '/dashboard', icon: '📊', roles: ['EMPLOYEE', 'MANAGER', 'ADMIN', 'VENDOR'] },
    { name: 'Purchase Requests', path: '/requests', icon: '📝', roles: ['EMPLOYEE', 'MANAGER', 'ADMIN'] },
    { name: 'Orders', path: '/orders', icon: '📦', roles: ['MANAGER', 'ADMIN', 'VENDOR'] },
    { name: 'Suppliers', path: '/suppliers', icon: '🏢', roles: ['ADMIN'] },
    { name: 'AI Recommendations', path: '/ai-recommendations', icon: '🤖', roles: ['MANAGER', 'ADMIN'] },
  ];

  const filteredLinks = links.filter(link => link.roles.includes(user?.role));

  return (
    <aside className="fixed inset-y-0 left-0 bg-white w-64 shadow-lg border-r border-gray-200 z-20 hidden md:block">
      <div className="flex items-center justify-center h-16 border-b border-gray-200">
        <span className="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-600 to-indigo-600">
          AI Procurement
        </span>
      </div>
      <div className="overflow-y-auto overflow-x-hidden flex-grow px-4 py-6">
        <ul className="flex flex-col space-y-2">
          {filteredLinks.map((link) => (
            <li key={link.name}>
              <NavLink
                to={link.path}
                className={({ isActive }) =>
                  `flex items-center px-4 py-3 rounded-lg transition-all duration-200 ${
                    isActive
                      ? 'bg-blue-50 text-blue-700 shadow-sm'
                      : 'text-gray-600 hover:bg-gray-50 hover:text-blue-600'
                  }`
                }
              >
                <span className="mr-3 text-lg">{link.icon}</span>
                <span className="font-medium">{link.name}</span>
              </NavLink>
            </li>
          ))}
        </ul>
      </div>
      <div className="absolute bottom-0 w-full p-4 border-t border-gray-200 bg-gray-50">
        <div className="flex items-center">
          <div className="w-8 h-8 rounded-full bg-blue-500 flex items-center justify-center text-white font-bold">
            {user?.name?.charAt(0)}
          </div>
          <div className="ml-3">
            <p className="text-sm font-medium text-gray-700 truncate">{user?.name}</p>
            <p className="text-xs text-gray-500 capitalize">{user?.role?.toLowerCase()}</p>
          </div>
        </div>
      </div>
    </aside>
  );
};

export default Sidebar;
