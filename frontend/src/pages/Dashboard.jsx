import { useEffect, useState } from 'react';
import {
  Chart as ChartJS,
  ArcElement,
  BarElement,
  CategoryScale,
  Legend,
  LinearScale,
  Tooltip,
} from 'chart.js';
import { Bar, Doughnut, Pie } from 'react-chartjs-2';
import api from '../api/client';

ChartJS.register(CategoryScale, LinearScale, BarElement, ArcElement, Tooltip, Legend);

const CHART_COLORS = [
  '#6366f1', '#3b82f6', '#14b8a6', '#10b981', '#f59e0b',
  '#ef4444', '#ec4899', '#8b5cf6', '#06b6d4', '#eab308',
];

const legendTop = {
  position: 'top',
  align: 'center',
  labels: {
    boxWidth: 12,
    boxHeight: 12,
    padding: 10,
    font: { size: 11, weight: '500' },
    color: '#64748b',
    usePointStyle: false,
  },
};

const pieOptions = {
  responsive: true,
  maintainAspectRatio: true,
  plugins: {
    legend: {
      ...legendTop,
      labels: {
        ...legendTop.labels,
        generateLabels: (chart) => {
          const data = chart.data;
          if (!data.labels?.length) return [];
          return data.labels.map((label, i) => ({
            text: label,
            fillStyle: data.datasets[0].backgroundColor[i],
            strokeStyle: '#fff',
            lineWidth: 1,
            hidden: false,
            index: i,
          }));
        },
      },
    },
    tooltip: {
      backgroundColor: '#1e293b',
      padding: 10,
      cornerRadius: 6,
    },
  },
};

const doughnutOptions = {
  ...pieOptions,
  cutout: '55%',
};

const barOptions = {
  responsive: true,
  maintainAspectRatio: true,
  plugins: {
    legend: {
      position: 'top',
      align: 'center',
      labels: {
        boxWidth: 12,
        boxHeight: 12,
        font: { size: 11, weight: '500' },
        color: '#64748b',
      },
    },
    tooltip: {
      backgroundColor: '#1e293b',
      padding: 10,
      cornerRadius: 6,
    },
  },
  scales: {
    x: {
      grid: { display: false },
      ticks: {
        color: '#64748b',
        font: { size: 10 },
        maxRotation: 45,
        minRotation: 45,
      },
    },
    y: {
      beginAtZero: true,
      grid: { color: '#f1f5f9' },
      ticks: { color: '#94a3b8', font: { size: 11 } },
    },
  },
};

export default function Dashboard() {
  const [summary, setSummary] = useState({ totalStock: 0, stockBajo: [] });
  const [stats, setStats] = useState(null);

  useEffect(() => {
    api.get('/dashboard/summary').then((r) => setSummary(r.data));
    api.get('/dashboard/stats').then((r) => setStats(r.data));
  }, []);

  const stockBajoCount = summary.stockBajo?.length || 0;

  return (
    <div className="dashboard-page">
      <header className="dashboard-header">
        <h1>Dashboard</h1>
        <p className="dashboard-subtitle">Resumen general del inventario</p>
      </header>

      <div className="kpi-row">
        <div className="kpi-card">
          <div className="kpi-icon kpi-icon--primary">
            <i className="fas fa-box" aria-hidden="true" />
          </div>
          <div className="kpi-content">
            <span className="kpi-label">Productos en stock</span>
            <strong className="kpi-value">{summary.totalStock.toLocaleString()}</strong>
          </div>
        </div>
        <div className="kpi-card">
          <div className="kpi-icon kpi-icon--danger">
            <i className="fas fa-exclamation-triangle" aria-hidden="true" />
          </div>
          <div className="kpi-content">
            <span className="kpi-label">Stock bajo</span>
            <strong className="kpi-value">{stockBajoCount}</strong>
          </div>
        </div>
      </div>

      {stats && (
        <div className="charts-row">
          <div className="chart-card">
            <h3 className="chart-title">Productos por categoría</h3>
            <div className="chart-wrap">
              <Pie data={buildPieData(stats.productosPorCategoria)} options={pieOptions} />
            </div>
          </div>
          <div className="chart-card">
            <h3 className="chart-title">Stock por categoría</h3>
            <div className="chart-wrap">
              <Bar data={buildBarData(stats.stockPorCategoria)} options={barOptions} />
            </div>
          </div>
          <div className="chart-card">
            <h3 className="chart-title">Movimientos por tipo</h3>
            <div className="chart-wrap">
              <Doughnut data={buildPieData(stats.movimientosPorTipo)} options={doughnutOptions} />
            </div>
          </div>
          <div className="chart-card">
            <h3 className="chart-title">Stock por estado</h3>
            <div className="chart-wrap">
              <Pie data={buildPieData(stats.estadisticasStock)} options={pieOptions} />
            </div>
          </div>
        </div>
      )}

      <div className="table-card">
        <div className="table-card-header">
          <h3>Productos con stock bajo</h3>
          <span className="badge badge--warning">{stockBajoCount} productos</span>
        </div>
        <div className="table-responsive">
          <table className="table table--dashboard">
            <thead>
              <tr>
                <th>Código</th>
                <th>Producto</th>
                <th>Categoría</th>
                <th>Stock</th>
                <th>Mínimo</th>
                <th>Faltante</th>
              </tr>
            </thead>
            <tbody>
              {(summary.stockBajo || []).length === 0 ? (
                <tr>
                  <td colSpan={6} className="table-empty">No hay productos con stock bajo</td>
                </tr>
              ) : (
                summary.stockBajo.map((p) => {
                  const faltante = Math.max(0, (p.minimo || 0) - (p.stock || 0));
                  return (
                    <tr key={p.id}>
                      <td><code className="code-tag">{p.codigo}</code></td>
                      <td>{p.nombre}</td>
                      <td>{p.categoria}</td>
                      <td><span className="stock-low">{p.stock}</span></td>
                      <td>{p.minimo}</td>
                      <td><span className="badge badge--danger">{faltante}</span></td>
                    </tr>
                  );
                })
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

function buildPieData(map) {
  const labels = Object.keys(map || {});
  return {
    labels,
    datasets: [{
      data: labels.map((k) => map[k]),
      backgroundColor: labels.map((_, i) => CHART_COLORS[i % CHART_COLORS.length]),
      borderWidth: 2,
      borderColor: '#ffffff',
    }],
  };
}

function buildBarData(map) {
  const labels = Object.keys(map || {});
  return {
    labels,
    datasets: [{
      label: 'Cantidad',
      data: labels.map((k) => map[k]),
      backgroundColor: '#818cf8',
      hoverBackgroundColor: '#6366f1',
      borderRadius: { topLeft: 6, topRight: 6 },
      borderSkipped: false,
      maxBarThickness: 48,
    }],
  };
}
