import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/client';
import { useAuth } from '../context/AuthContext';

export default function Products() {
  const { isAdmin } = useAuth();
  const [products, setProducts] = useState([]);
  const [filters, setFilters] = useState({ categoria: '', estado: '', busqueda: '' });

  const load = () => {
    api.get('/products', { params: filters }).then((r) => setProducts(r.data));
  };

  useEffect(() => { load(); }, []);

  const remove = async (id) => {
    if (!window.confirm('¿Eliminar producto?')) return;
    await api.delete(`/products/${id}`);
    load();
  };

  return (
    <div className="container">
      <div className="page-header">
        <h1>Productos</h1>
        <Link className="btn btn-primary" to="/products/new">Nuevo producto</Link>
      </div>
      <div className="filters card card-body">
        <input placeholder="Buscar..." value={filters.busqueda} onChange={(e) => setFilters({ ...filters, busqueda: e.target.value })} />
        <input placeholder="Categoría" value={filters.categoria} onChange={(e) => setFilters({ ...filters, categoria: e.target.value })} />
        <select value={filters.estado} onChange={(e) => setFilters({ ...filters, estado: e.target.value })}>
          <option value="">Todos los estados</option>
          <option value="buen_estado">Buen estado</option>
          <option value="mal_estado">Mal estado</option>
        </select>
        <button className="btn btn-secondary" onClick={load}>Filtrar</button>
      </div>
      <div className="card">
        <div className="card-body table-responsive">
        <table className="table">
          <thead><tr><th>Código</th><th>Nombre</th><th>Categoría</th><th>Stock</th><th>Precio</th><th></th></tr></thead>
          <tbody>
            {products.map((p) => (
              <tr key={p.id}>
                <td>{p.codigo}</td><td>{p.nombre}</td><td>{p.categoria}</td><td>{p.stock}</td><td>S/. {p.precio?.toFixed(2)}</td>
                <td className="actions">
                  <Link to={`/products/${p.id}/edit`}>Editar</Link>
                  {isAdmin && <button className="link-danger" onClick={() => remove(p.id)}>Eliminar</button>}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        </div>
      </div>
    </div>
  );
}
