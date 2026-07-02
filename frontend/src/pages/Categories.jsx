import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/client';
import { useAuth } from '../context/AuthContext';

export default function Categories() {
  const { isAdmin } = useAuth();
  const [items, setItems] = useState([]);

  const load = () => api.get('/categories').then((r) => setItems(r.data));
  useEffect(() => { load(); }, []);

  const remove = async (id) => {
    if (!window.confirm('¿Eliminar categoría?')) return;
    await api.delete(`/categories/${id}`);
    load();
  };

  return (
    <div className="container">
      <div className="page-header"><h1>Categorías</h1><Link className="btn btn-primary" to="/categories/new">Nueva</Link></div>
      <div className="card">
        <div className="card-body table-responsive">
        <table className="table">
          <thead><tr><th>Nombre</th><th>Descripción</th><th></th></tr></thead>
          <tbody>
            {items.map((c) => (
              <tr key={c.id}>
                <td>{c.nombre}</td><td>{c.descripcion}</td>
                <td className="actions">
                  <Link to={`/categories/${c.id}/edit`}>Editar</Link>
                  {isAdmin && <button className="link-danger" onClick={() => remove(c.id)}>Eliminar</button>}
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
