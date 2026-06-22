import { Outlet } from 'react-router-dom';
import Sidebar from './Sidebar';
import UserProfile from './UserProfile';

const Layout = () => {
  return (
    <div className="min-h-screen flex">
      <Sidebar />
      <div className="flex-1 ml-64">
        <div className="bg-white px-6 py-4 flex justify-end border-b border-gray-200">
          <UserProfile />
        </div>
        <main className="bg-castello-light min-h-[calc(100vh-73px)] p-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default Layout;

