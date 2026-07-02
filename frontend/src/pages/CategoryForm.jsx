import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import api from '../api/client';

export default function CategoryForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form, setForm] = useState({ id: 0, nombre: '', descripcion: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (id) api.get(`/categories/${id}`).then((r) => setForm(r.data));
  }, [id]);

  const submit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      if (id) await api.put(`/categories/${id}`, form);
      else await api.post('/categories', form);
      navigate('/categories');
    } catch (err) {
      setError(err.response?.data?.error || 'Error al guardar la categoría');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <h1>{id ? 'Editar' : 'Nueva'} categoría</h1>
      <form className="card card-body form-grid" onSubmit={submit}>
        {error && <div className="alert alert-danger">{error}</div>}
        <label>Nombre<input required value={form.nombre} onChange={(e) => setForm({ ...form, nombre: e.target.value })} /></label>
        <label>Descripción<textarea value={form.descripcion || ''} onChange={(e) => setForm({ ...form, descripcion: e.target.value })} /></label>
        <div className="form-actions">
          <button className="btn btn-primary" type="submit" disabled={loading}>{loading ? 'Guardando...' : 'Guardar'}</button>
          <button className="btn btn-secondary" type="button" onClick={() => navigate('/categories')} disabled={loading}>Cancelar</button>
        </div>
      </form>
    </div>
  );
}
