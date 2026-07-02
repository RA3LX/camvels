import { useEffect, useState } from 'react';
import { Link, NavLink, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Layout() {
  const { user, logout, isAdmin, canManageCategories, canManageMovements, canViewReports } = useAuth();
  const [menuOpen, setMenuOpen] = useState(false);
  const location = useLocation();

  useEffect(() => {
    setMenuOpen(false);
  }, [location.pathname]);

  useEffect(() => {
    document.body.style.overflow = menuOpen ? 'hidden' : '';
    return () => { document.body.style.overflow = ''; };
  }, [menuOpen]);

  const navLinks = (
    <>
      <NavLink to="/" end onClick={() => setMenuOpen(false)}>
        <i className="fas fa-chart-pie" aria-hidden="true" /> Dashboard
      </NavLink>
      <NavLink to="/products" onClick={() => setMenuOpen(false)}>
        <i className="fas fa-box" aria-hidden="true" /> Productos
      </NavLink>
      <NavLink to="/suppliers" onClick={() => setMenuOpen(false)}>
        <i className="fas fa-truck" aria-hidden="true" /> Proveedores
      </NavLink>
      {canManageCategories && (
        <NavLink to="/categories" onClick={() => setMenuOpen(false)}>
          <i className="fas fa-tags" aria-hidden="true" /> Categorías
        </NavLink>
      )}
      {canManageMovements && (
        <NavLink to="/movements" onClick={() => setMenuOpen(false)}>
          <i className="fas fa-exchange-alt" aria-hidden="true" /> Movimientos
        </NavLink>
      )}
      {canViewReports && (
        <NavLink to="/reports" onClick={() => setMenuOpen(false)}>
          <i className="fas fa-file-pdf" aria-hidden="true" /> Reportes
        </NavLink>
      )}
      {isAdmin && (
        <NavLink to="/users" onClick={() => setMenuOpen(false)}>
          <i className="fas fa-users" aria-hidden="true" /> Usuarios
        </NavLink>
      )}
    </>
  );

  return (
    <div className={`app-shell${menuOpen ? ' app-shell--menu-open' : ''}`}>
      <header className="mobile-topbar">
        <button
          type="button"
          className="mobile-menu-btn"
          onClick={() => setMenuOpen((v) => !v)}
          aria-label={menuOpen ? 'Cerrar menú' : 'Abrir menú'}
          aria-expanded={menuOpen}
        >
          <i className={`fas ${menuOpen ? 'fa-times' : 'fa-bars'}`} aria-hidden="true" />
        </button>
        <Link to="/" className="mobile-brand" onClick={() => setMenuOpen(false)}>
          <i className="fas fa-cube" aria-hidden="true" /> Camvels
        </Link>
      </header>

      <div
        className="sidebar-backdrop"
        onClick={() => setMenuOpen(false)}
        aria-hidden={!menuOpen}
      />

      <aside className="sidebar">
        <div className="sidebar-header">
          <Link to="/" className="sidebar-brand" onClick={() => setMenuOpen(false)}>
            <i className="fas fa-cube" aria-hidden="true" /> Camvels
          </Link>
        </div>
        <nav className="sidebar-nav">{navLinks}</nav>
        <div className="sidebar-footer">
          <div className="sidebar-user">
            <i className="fas fa-user-circle" aria-hidden="true" />
            <span>{user?.nombre}</span>
          </div>
          <button type="button" className="btn btn-secondary btn-sm btn-block" onClick={logout}>
            Salir
          </button>
        </div>
      </aside>

      <main className="main-content">
        <Outlet />
      </main>
    </div>
  );
}
