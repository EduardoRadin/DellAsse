import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import { ConfigProvider } from './contexts/ConfigContext'; 
import ProtectedRoute from './components/ProtectedRoute';
import Layout from './components/Layout';

// Pages principais
import Home from './pages/Home';
import Gallery from './pages/Gallery';
import Contact from './pages/Contact';
import Cart from './pages/Cart';
import Login from './pages/Login';
import Register from './pages/Register';
import CreateCustomParty from './pages/CreateCustomParty';

// Admin Pages
import AdminPanel from './pages/AdminPanel';
import ViewRequests from './pages/admin/ViewRequests';
import ViewDatabase from './pages/admin/ViewDatabase';
import AddParty from './pages/admin/AddParty';
import DeleteReview from './pages/admin/DeleteReview';
import UploadLogo from './pages/admin/UploadLogo';
import ManageProducts from './pages/admin/ManageProducts';

function App() {
  return (
    <AuthProvider>
      <ConfigProvider>
        <Router>
          <Routes>
            <Route path="/" element={<Layout />}>
              <Route index element={<Home />} />
              <Route path="galeria" element={<Gallery />} />
              <Route path="contato" element={<Contact />} />
              <Route path="carrinho" element={<Cart />} />
              <Route path="login" element={<Login />} />
              <Route path="register" element={<Register />} />
              
              <Route 
                path="criar-festa" 
                element={
                  <ProtectedRoute>
                    <CreateCustomParty />
                  </ProtectedRoute>
                } 
              />
              
              <Route
                path="painel-adm"
                element={
                  <ProtectedRoute requiredRole="ADMIN">
                    <AdminPanel />
                  </ProtectedRoute>
                }
              >
                <Route path="upload-logo" element={<UploadLogo />} />
                <Route path="base-dados" element={<ViewDatabase />} />
                <Route path="solicitacoes" element={<ViewRequests />} />
                <Route path="apagar-avaliacao" element={<DeleteReview />} />
                <Route path="adicionar-festa" element={<AddParty />} />
                <Route path="gerenciar-produtos" element={<ManageProducts />} />
              </Route>

              <Route path="*" element={<Navigate to="/" replace />} />
            </Route>
          </Routes>
        </Router>
      </ConfigProvider>
    </AuthProvider>
  );
}

export default App;