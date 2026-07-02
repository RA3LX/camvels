import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import api from '../api/client';

export default function UserForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form, setForm] = useState({ id: 0, usuario: '', password: '', nombre: '', rol: 'almacen' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (id) api.get(`/users/${id}`).then((r) => setForm({ ...r.data, password: '' }));
  }, [id]);

  const submit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      if (id) await api.put(`/users/${id}`, form);
      else await api.post('/users', form);
      navigate('/users');
    } catch (err) {
      setError(err.response?.data?.error || 'Error al guardar el usuario');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <h1>{id ? 'Editar' : 'Nuevo'} usuario</h1>
      <form className="card card-body form-grid" onSubmit={submit}>
        {error && <div className="alert alert-danger">{error}</div>}
        <label>Usuario<input required value={form.usuario} onChange={(e) => setForm({ ...form, usuario: e.target.value })} /></label>
        <label>Contraseña<input type="password" required={!id} value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} /></label>
        <label>Nombre<input required value={form.nombre} onChange={(e) => setForm({ ...form, nombre: e.target.value })} /></label>
        <label>Rol
          <select value={form.rol} onChange={(e) => setForm({ ...form, rol: e.target.value })}>
            <option value="admin">Admin</option>
            <option value="supervisor">Supervisor</option>
            <option value="almacen">Almacén</option>
          </select>
        </label>
        <div className="form-actions">
          <button className="btn btn-primary" type="submit" disabled={loading}>{loading ? 'Guardando...' : 'Guardar'}</button>
          <button className="btn btn-secondary" type="button" onClick={() => navigate('/users')} disabled={loading}>Cancelar</button>
        </div>
      </form>
    </div>
  );
}
