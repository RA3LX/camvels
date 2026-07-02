import { useEffect, useState } from 'react';
import api from '../api/client';

export default function Reports() {
  const [suppliers, setSuppliers] = useState([]);
  const [form, setForm] = useState({
    tipo: 'productos', proveedorId: '0', incluirStockBajo: true, incluirMalEstado: false,
  });

  useEffect(() => {
    api.get('/reports/suppliers').then((r) => setSuppliers(r.data));
  }, []);

  const download = async () => {
    const params = { tipo: form.tipo };
    if (form.tipo === 'proveedores_productos') {
      if (form.proveedorId !== '0') params.proveedorId = form.proveedorId;
      params.incluirStockBajo = form.incluirStockBajo;
      params.incluirMalEstado = form.incluirMalEstado;
    }
    const response = await api.get('/reports/pdf', { params, responseType: 'blob' });
    const url = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));
    const link = document.createElement('a');
    link.href = url;
    link.download = `reporte_${form.tipo}.pdf`;
    link.click();
    window.URL.revokeObjectURL(url);
  };

  return (
    <div className="container">
      <h1>Reportes PDF</h1>
      <div className="card card-body form-grid">
        <label>Tipo de reporte
          <select value={form.tipo} onChange={(e) => setForm({ ...form, tipo: e.target.value })}>
            <option value="productos">Productos</option>
            <option value="stock_bajo">Stock bajo</option>
            <option value="movimientos">Movimientos</option>
            <option value="proveedores">Proveedores</option>
            <option value="proveedores_productos">Proveedores y productos</option>
          </select>
        </label>

        {form.tipo === 'proveedores_productos' && (
          <>
            <label>Proveedor
              <select value={form.proveedorId} onChange={(e) => setForm({ ...form, proveedorId: e.target.value })}>
                <option value="0">Todos</option>
                {suppliers.map((s) => <option key={s.id} value={s.id}>{s.nombre}</option>)}
              </select>
            </label>
            <label><input type="checkbox" checked={form.incluirStockBajo} onChange={(e) => setForm({ ...form, incluirStockBajo: e.target.checked })} /> Incluir stock bajo</label>
            <label><input type="checkbox" checked={form.incluirMalEstado} onChange={(e) => setForm({ ...form, incluirMalEstado: e.target.checked })} /> Incluir mal estado</label>
          </>
        )}

        <button className="btn btn-primary" type="button" onClick={download}>Descargar PDF</button>
      </div>
    </div>
  );
}
