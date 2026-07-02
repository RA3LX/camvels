import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/client';

export default function MovementForm() {
  const navigate = useNavigate();
  const [products, setProducts] = useState([]);
  const [form, setForm] = useState({ tipo: 'ENTRADA', productoId: '', cantidad: 1, observaciones: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    api.get('/movements/products').then((r) => setProducts(r.data));
  }, []);

  const submit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const payload = { ...form, productoId: +form.productoId, cantidad: +form.cantidad };
      console.log('Enviando movimiento:', payload);
      await api.post('/movements', payload);
      navigate('/movements');
    } catch (err) {
      const errorMsg = err.response?.data?.error || err.response?.data?.message || err.message || 'Error al registrar movimiento';
      console.error('Error al guardar movimiento:', err.response?.data || err.message);
      setError(errorMsg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <h1>Nuevo movimiento</h1>
      {error && <div className="alert alert-danger"><strong>Error:</strong> {error}</div>}
      <form className="card card-body form-grid" onSubmit={submit}>
        <label>Tipo
          <select value={form.tipo} onChange={(e) => setForm({ ...form, tipo: e.target.value })}>
            <option value="ENTRADA">Entrada</option>
            <option value="SALIDA">Salida</option>
            <option value="AJUSTE">Ajuste</option>
          </select>
        </label>
        <label>Producto
          <select required value={form.productoId} onChange={(e) => setForm({ ...form, productoId: e.target.value })}>
            <option value="">Seleccionar...</option>
            {products.map((p) => <option key={p.id} value={p.id}>{p.codigo} - {p.nombre}</option>)}
          </select>
        </label>
        <label>Cantidad<input type="number" min="1" required value={form.cantidad} onChange={(e) => setForm({ ...form, cantidad: e.target.value })} /></label>
        <label>Observaciones<textarea value={form.observaciones} onChange={(e) => setForm({ ...form, observaciones: e.target.value })} /></label>
        <div className="form-actions">
          <button className="btn btn-primary" type="submit" disabled={loading}>{loading ? 'Registrando...' : 'Registrar'}</button>
          <button className="btn btn-secondary" type="button" onClick={() => navigate('/movements')} disabled={loading}>Cancelar</button>
        </div>
      </form>
    </div>
  );
}
