import { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import api from '../api/client';

const emptyForm = { tipo: 'ENTRADA', productoId: '', cantidad: 1, observaciones: '' };

export default function MovementForm() {
  const navigate = useNavigate();
  const location = useLocation();
  const [products, setProducts] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [loadingProducts, setLoadingProducts] = useState(true);

  useEffect(() => {
    setForm(emptyForm);
    setError('');
    setLoadingProducts(true);
    api.get('/movements/products')
      .then((r) => setProducts(r.data))
      .catch((err) => setError(err.response?.data?.error || 'No se pudieron cargar los productos'))
      .finally(() => setLoadingProducts(false));
  }, [location.key]);

  const update = (field, value) => setForm((prev) => ({ ...prev, [field]: value }));

  const submit = async (e) => {
    e.preventDefault();
    setError('');

    if (!form.productoId) {
      setError('Seleccione un producto');
      return;
    }
    const cantidad = Number(form.cantidad);
    if (!Number.isFinite(cantidad) || cantidad < 1) {
      setError('La cantidad debe ser mayor a cero');
      return;
    }

    setLoading(true);
    try {
      const payload = {
        tipo: form.tipo,
        productoId: Number(form.productoId),
        cantidad,
        observaciones: form.observaciones,
      };
      await api.post('/movements', payload);
      navigate('/movements');
    } catch (err) {
      const errorMsg = err.response?.data?.error
        || err.response?.data?.message
        || err.message
        || 'Error al registrar movimiento';
      setError(errorMsg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <h1>Nuevo movimiento</h1>
      <form className="card card-body form-grid" onSubmit={submit} noValidate>
        {error && <div className="alert alert-danger"><strong>Error:</strong> {error}</div>}
        {loadingProducts && <div className="alert alert-info">Cargando productos...</div>}
        {!loadingProducts && products.length === 0 && (
          <div className="alert alert-warning">
            No hay productos registrados. Cree un producto antes de registrar un movimiento.
          </div>
        )}
        <label>Tipo
          <select value={form.tipo} onChange={(e) => update('tipo', e.target.value)}>
            <option value="ENTRADA">Entrada</option>
            <option value="SALIDA">Salida</option>
            <option value="AJUSTE">Ajuste</option>
          </select>
        </label>
        <label>Producto
          <select
            value={form.productoId}
            onChange={(e) => update('productoId', e.target.value)}
            disabled={loadingProducts || products.length === 0}
          >
            <option value="">Seleccionar...</option>
            {products.map((p) => (
              <option key={p.id} value={p.id}>{p.codigo} - {p.nombre}</option>
            ))}
          </select>
        </label>
        <label>
          Cantidad
          <input
            type="number"
            min="1"
            value={form.cantidad}
            onChange={(e) => update('cantidad', e.target.value)}
          />
        </label>
        <label>
          Observaciones
          <textarea
            value={form.observaciones}
            onChange={(e) => update('observaciones', e.target.value)}
          />
        </label>
        <div className="form-actions">
          <button
            className="btn btn-primary"
            type="submit"
            disabled={loading || loadingProducts || products.length === 0}
          >
            {loading ? 'Registrando...' : 'Registrar'}
          </button>
          <button
            className="btn btn-secondary"
            type="button"
            onClick={() => navigate('/movements')}
            disabled={loading}
          >
            Cancelar
          </button>
        </div>
      </form>
    </div>
  );
}
