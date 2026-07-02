import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/client';
import { useAuth } from '../context/AuthContext';

export default function Suppliers() {
  const { isAdmin } = useAuth();
  const [items, setItems] = useState([]);
  const [emailForm, setEmailForm] = useState(null);
  const [message, setMessage] = useState('');

  const load = () => api.get('/suppliers').then((r) => setItems(r.data));
  useEffect(() => { load(); }, []);

  const remove = async (id) => {
    if (!window.confirm('¿Eliminar proveedor?')) return;
    await api.delete(`/suppliers/${id}`);
    load();
  };

  const sendEmail = async (e) => {
    e.preventDefault();
    setMessage('');
    const data = new FormData(e.target);
    try {
      const res = await api.post('/suppliers/email', data, { headers: { 'Content-Type': 'multipart/form-data' } });
      setMessage(res.data.message);
      setEmailForm(null);
    } catch (err) {
      setMessage(err.response?.data?.error || 'Error al enviar correo');
    }
  };

  return (
    <div className="container">
      <div className="page-header"><h1>Proveedores</h1><Link className="btn btn-primary" to="/suppliers/new">Nuevo</Link></div>
      {message && <div className="alert alert-info">{message}</div>}
      <div className="card">
        <div className="card-body table-responsive">
        <table className="table">
          <thead><tr><th>RUC</th><th>Nombre</th><th>Email</th><th></th></tr></thead>
          <tbody>
            {items.map((p) => (
              <tr key={p.id}>
                <td>{p.ruc}</td><td>{p.nombre}</td><td>{p.email}</td>
                <td className="actions">
                  <Link to={`/suppliers/${p.id}/edit`}>Editar</Link>
                  <button onClick={() => setEmailForm(p)}>Email</button>
                  {isAdmin && <button className="link-danger" onClick={() => remove(p.id)}>Eliminar</button>}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        </div>
      </div>

      {emailForm && (
        <div className="modal-backdrop">
          <form className="modal card card-body" onSubmit={sendEmail}>
            <h3>Enviar correo a {emailForm.nombre}</h3>
            <input type="hidden" name="proveedorId" value={emailForm.id} />
            <label>Email<input name="proveedorEmail" defaultValue={emailForm.email} required /></label>
            <label>Asunto<input name="asunto" required /></label>
            <label>Mensaje<textarea name="mensaje" required rows="4" /></label>
            <label>Adjunto<input type="file" name="archivoAdjunto" /></label>
            <div className="form-actions">
              <button className="btn btn-primary" type="submit">Enviar</button>
              <button className="btn btn-secondary" type="button" onClick={() => setEmailForm(null)}>Cancelar</button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
}
