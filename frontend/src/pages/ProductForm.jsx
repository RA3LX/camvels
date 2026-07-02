import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import api from '../api/client';

const empty = {
  id: 0, codigo: '', nombre: '', categoria: '', stockBuenEstado: 0, stockMalEstado: 0,
  minimo: 0, precio: 0, estado: 'buen_estado', proveedorId: null,
};

export default function ProductForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form, setForm] = useState(empty);
  const [suppliers, setSuppliers] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    api.get('/products/suppliers').then((r) => setSuppliers(r.data));
    if (id) {
      api.get(`/products/${id}`).then((r) => setForm(r.data));
    }
  }, [id]);

  const submit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const payload = { ...form, proveedorId: form.proveedorId || null };
      if (id) await api.put(`/products/${id}`, payload);
      else await api.post('/products', payload);
      navigate('/products');
    } catch (err) {
      setError(err.response?.data?.error || 'Error al guardar');
    }
  };

  const set = (k, v) => setForm({ ...form, [k]: v });

  return (
    <div className="container">
      <h1>{id ? 'Editar' : 'Nuevo'} producto</h1>
      {error && <div className="alert alert-danger">{error}</div>}
      <form className="card card-body form-grid" onSubmit={submit}>
        <label>Código<input required value={form.codigo} onChange={(e) => set('codigo', e.target.value)} /></label>
        <label>Nombre<input required value={form.nombre} onChange={(e) => set('nombre', e.target.value)} /></label>
        <label>Categoría<input required value={form.categoria} onChange={(e) => set('categoria', e.target.value)} /></label>
        <label>Stock buen estado<input type="number" min="0" value={form.stockBuenEstado} onChange={(e) => set('stockBuenEstado', +e.target.value)} /></label>
        <label>Stock mal estado<input type="number" min="0" value={form.stockMalEstado} onChange={(e) => set('stockMalEstado', +e.target.value)} /></label>
        <label>Mínimo<input type="number" min="0" required value={form.minimo} onChange={(e) => set('minimo', +e.target.value)} /></label>
        <label>Precio<input type="number" step="0.01" min="0" required value={form.precio} onChange={(e) => set('precio', +e.target.value)} /></label>
        <label>Estado
          <select value={form.estado} onChange={(e) => set('estado', e.target.value)}>
            <option value="buen_estado">Buen estado</option>
            <option value="mal_estado">Mal estado</option>
          </select>
        </label>
        <label>Proveedor
          <select value={form.proveedorId || ''} onChange={(e) => set('proveedorId', e.target.value ? +e.target.value : null)}>
            <option value="">Sin proveedor</option>
            {suppliers.map((s) => <option key={s.id} value={s.id}>{s.nombre}</option>)}
          </select>
        </label>
        <div className="form-actions">
          <button className="btn btn-primary" type="submit">Guardar</button>
          <button className="btn btn-secondary" type="button" onClick={() => navigate('/products')}>Cancelar</button>
        </div>
      </form>
    </div>
  );
}
