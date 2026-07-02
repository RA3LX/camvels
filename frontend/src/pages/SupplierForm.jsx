import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import api from '../api/client';

export default function SupplierForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form, setForm] = useState({ id: 0, ruc: '', nombre: '', direccion: '', telefono: '', email: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (id) api.get(`/suppliers/${id}`).then((r) => setForm(r.data));
  }, [id]);

  const submit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      if (id) await api.put(`/suppliers/${id}`, form);
      else await api.post('/suppliers', form);
      navigate('/suppliers');
    } catch (err) {
      setError(err.response?.data?.error || 'Error al guardar el proveedor');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <h1>{id ? 'Editar' : 'Nuevo'} proveedor</h1>
      <form className="card card-body form-grid" onSubmit={submit}>
        {error && <div className="alert alert-danger">{error}</div>}
        <label>RUC<input required value={form.ruc} onChange={(e) => setForm({ ...form, ruc: e.target.value })} /></label>
        <label>Nombre<input required value={form.nombre} onChange={(e) => setForm({ ...form, nombre: e.target.value })} /></label>
        <label>Dirección<input value={form.direccion || ''} onChange={(e) => setForm({ ...form, direccion: e.target.value })} /></label>
        <label>Teléfono<input value={form.telefono || ''} onChange={(e) => setForm({ ...form, telefono: e.target.value })} /></label>
        <label>Email<input type="email" value={form.email || ''} onChange={(e) => setForm({ ...form, email: e.target.value })} /></label>
        <div className="form-actions">
          <button className="btn btn-primary" type="submit" disabled={loading}>{loading ? 'Guardando...' : 'Guardar'}</button>
          <button className="btn btn-secondary" type="button" onClick={() => navigate('/suppliers')} disabled={loading}>Cancelar</button>
        </div>
      </form>
    </div>
  );
}
