import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/client';

export default function Users() {
  const [items, setItems] = useState([]);

  const load = () => api.get('/users').then((r) => setItems(r.data));
  useEffect(() => { load(); }, []);

  const remove = async (id) => {
    if (!window.confirm('¿Eliminar usuario?')) return;
    await api.delete(`/users/${id}`);
    load();
  };

  return (
    <div className="container">
      <div className="page-header"><h1>Usuarios</h1><Link className="btn btn-primary" to="/users/new">Nuevo</Link></div>
      <div className="card">
        <div className="card-body table-responsive">
        <table className="table">
          <thead><tr><th>Usuario</th><th>Nombre</th><th>Rol</th><th></th></tr></thead>
          <tbody>
            {items.map((u) => (
              <tr key={u.id}>
                <td>{u.usuario}</td><td>{u.nombre}</td><td>{u.rol}</td>
                <td className="actions">
                  <Link to={`/users/${u.id}/edit`}>Editar</Link>
                  <button className="link-danger" onClick={() => remove(u.id)}>Eliminar</button>
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
