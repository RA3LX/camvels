import { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import api from '../api/client';

export default function Movements() {
  const location = useLocation();
  const [items, setItems] = useState([]);
  const [products, setProducts] = useState([]);
  const [filters, setFilters] = useState({ tipo: '', producto: '' });

  const load = () => {
    api.get('/movements', { params: filters }).then((r) => setItems(r.data));
    api.get('/movements/products').then((r) => setProducts(r.data));
  };

  useEffect(() => { load(); }, [location.pathname]);

  return (
    <div className="container">
      <div className="page-header"><h1>Movimientos</h1><Link className="btn btn-primary" to="/movements/new">Nuevo</Link></div>
      <div className="filters card card-body">
        <select value={filters.tipo} onChange={(e) => setFilters({ ...filters, tipo: e.target.value })}>
          <option value="">Todos los tipos</option>
          <option value="ENTRADA">Entrada</option>
          <option value="SALIDA">Salida</option>
          <option value="AJUSTE">Ajuste</option>
        </select>
        <select value={filters.producto} onChange={(e) => setFilters({ ...filters, producto: e.target.value })}>
          <option value="">Todos los productos</option>
          {products.map((p) => <option key={p.id} value={p.id}>{p.codigo} - {p.nombre}</option>)}
        </select>
        <button className="btn btn-secondary" onClick={load}>Filtrar</button>
      </div>
      <div className="card">
        <div className="card-body table-responsive">
        <table className="table">
          <thead><tr><th>Fecha</th><th>Tipo</th><th>Producto</th><th>Cantidad</th><th>Usuario</th></tr></thead>
          <tbody>
            {items.map((m) => (
              <tr key={m.id}>
                <td>{m.fecha ? new Date(m.fecha).toLocaleString() : ''}</td>
                <td>{m.tipo}</td>
                <td>{m.productoCodigo} - {m.productoNombre}</td>
                <td>{m.cantidad}</td>
                <td>{m.usuarioNombre}</td>
              </tr>
            ))}
          </tbody>
        </table>
        </div>
      </div>
    </div>
  );
}
