DROP DATABASE IF EXISTS camvels;
CREATE DATABASE camvels CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE camvels;
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    rol ENUM('admin', 'almacen', 'supervisor') NOT NULL DEFAULT 'almacen',
    INDEX idx_usuario (usuario),
    INDEX idx_rol (rol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE categorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    INDEX idx_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE proveedores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ruc VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(200) NOT NULL,
    direccion TEXT,
    telefono VARCHAR(20),
    email VARCHAR(100),
    INDEX idx_ruc (ruc),
    INDEX idx_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    nombre VARCHAR(200) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    stock_buen_estado INT DEFAULT 0,
    stock_mal_estado INT DEFAULT 0,
    minimo INT NOT NULL DEFAULT 0,
    precio DECIMAL(10,2) NOT NULL,
    estado ENUM('buen_estado', 'mal_estado') NOT NULL DEFAULT 'buen_estado',
    proveedor_id INT NULL,
    INDEX idx_codigo (codigo),
    INDEX idx_nombre (nombre),
    INDEX idx_categoria (categoria),
    INDEX idx_stock (stock),
    INDEX idx_minimo (minimo),
    INDEX idx_estado (estado),
    INDEX idx_producto_proveedor (proveedor_id),
    FOREIGN KEY (proveedor_id) REFERENCES proveedores(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE movimientos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo ENUM('ENTRADA', 'SALIDA', 'AJUSTE') NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL,
    usuario_id INT NOT NULL,
    observaciones TEXT,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    INDEX idx_fecha (fecha),
    INDEX idx_tipo (tipo),
    INDEX idx_producto (producto_id),
    INDEX idx_usuario (usuario_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO usuarios (usuario, nombre, password, rol) VALUES 
('admin', 'Carlos Administrador', 'admin123', 'admin'),
('almacen1', 'María Almacén', 'almacen123', 'almacen'),
('almacen2', 'Juan Pérez', 'almacen123', 'almacen'),
('supervisor', 'Ana Supervisor', 'supervisor123', 'supervisor');

INSERT INTO categorias (nombre, descripcion) VALUES 
('Bebidas', 'Bebidas gaseosas, jugos, agua y refrescos'),
('Lácteos', 'Leche, yogurt, queso, mantequilla y derivados'),
('Snacks', 'Galletas, dulces, chips y productos de consumo rápido'),
('Limpieza', 'Detergentes, jabones y productos de limpieza del hogar'),
('Higiene', 'Productos de higiene personal'),
('Abarrotes', 'Productos básicos de despensa: arroz, fideos, azúcar, etc.'),
('Congelados', 'Productos congelados y helados'),
('Carnes', 'Carne de res, pollo, pescado y embutidos'),
('Frutas y Verduras', 'Frutas y verduras frescas'),
('Panadería', 'Pan, bollería y productos de pastelería');

INSERT INTO proveedores (ruc, nombre, direccion, telefono, email) VALUES 
('20123456789', 'Distribuidora Lima Norte S.A.', 'Av. Túpac Amaru 123, Los Olivos', '01-234-5678', 'ventas@dln.com'),
('20123456790', 'Productos Frescos EIRL', 'Jr. Comercial 456, Callao', '01-123-4567', 'pedidos@frescos.com'),
('20123456791', 'Abarrotes Central SAC', 'Av. Industrial 789, San Martín de Porres', '01-987-6543', 'info@abarrotescentral.com'),
('20123456792', 'Bebidas del Norte SAC', 'Calle Principal 321, Comas', '01-555-1234', 'ventas@bebidasnorte.pe'),
('20123456793', 'Lácteos San Fernando SAC', 'Av. Universitaria 654, Los Olivos', '01-777-8888', 'pedidos@sanfernando.com'),
('20123456794', 'Carnes Premium S.A.', 'Av. Venezuela 890, Magdalena', '01-444-5555', 'ventas@carnespremium.com'),
('20123456795', 'Verduras Frescas EIRL', 'Mercado Mayorista 456, Santa Anita', '01-666-7777', 'info@verduritas.com');

INSERT INTO productos (codigo, nombre, categoria, stock, stock_buen_estado, stock_mal_estado, minimo, precio, estado, proveedor_id) VALUES 
('BEB001', 'Coca Cola 500ml', 'Bebidas', 120, 120, 0, 20, 2.50, 'buen_estado', 4),
('BEB002', 'Inca Kola 500ml', 'Bebidas', 95, 95, 0, 20, 2.80, 'buen_estado', 4),
('BEB003', 'Sprite 500ml', 'Bebidas', 80, 75, 5, 15, 2.50, 'buen_estado', 4),
('BEB004', 'Agua San Luis 500ml', 'Bebidas', 150, 150, 0, 30, 1.50, 'buen_estado', 4),
('BEB005', 'Jugo Del Valle 1L', 'Bebidas', 45, 45, 0, 10, 4.50, 'buen_estado', 4),
('BEB006', 'Gatorade 500ml', 'Bebidas', 35, 35, 0, 10, 5.50, 'buen_estado', 4),
('LAC001', 'Leche Gloria 1L', 'Lácteos', 60, 55, 5, 15, 3.80, 'buen_estado', 5),
('LAC002', 'Yogurt Gloria 1kg', 'Lácteos', 40, 40, 0, 10, 4.20, 'buen_estado', 5),
('LAC003', 'Queso Fresco 250g', 'Lácteos', 25, 20, 5, 8, 5.50, 'buen_estado', 5),
('LAC004', 'Mantequilla Laive 200g', 'Lácteos', 30, 30, 0, 8, 4.80, 'buen_estado', 5),
('LAC005', 'Leche Condensada Gloria 400g', 'Lácteos', 20, 18, 2, 6, 4.50, 'buen_estado', 5),
('SNA001', 'Doritos Nacho 150g', 'Snacks', 55, 50, 5, 12, 3.50, 'buen_estado', 1),
('SNA002', 'Galletas Oreo 150g', 'Snacks', 70, 70, 0, 15, 3.80, 'buen_estado', 1),
('SNA003', 'Chizitos Queso 120g', 'Snacks', 45, 45, 0, 10, 2.80, 'buen_estado', 1),
('SNA004', 'Chocman 6 unidades', 'Snacks', 90, 88, 2, 20, 2.50, 'buen_estado', 1),
('SNA005', 'Maní Japonés 100g', 'Snacks', 38, 35, 3, 10, 2.20, 'buen_estado', 1),
('LIM001', 'Detergente Ace 500ml', 'Limpieza', 35, 0, 35, 10, 5.50, 'mal_estado', 1),
('LIM002', 'Jabón Dove 90g', 'Limpieza', 50, 45, 5, 12, 2.90, 'buen_estado', 1),
('LIM003', 'Lavavajillas Sapolio 750ml', 'Limpieza', 28, 28, 0, 8, 4.20, 'buen_estado', 1),
('LIM004', 'Limpiavidrios Mr. Músculo 500ml', 'Limpieza', 22, 20, 2, 6, 6.50, 'buen_estado', 1),
('LIM005', 'Clorox 1L', 'Limpieza', 18, 15, 3, 8, 5.80, 'buen_estado', 1),
('ABA001', 'Arroz Costeño 1kg', 'Abarrotes', 80, 80, 0, 20, 4.50, 'buen_estado', 3),
('ABA002', 'Aceite Primor 1L', 'Abarrotes', 40, 0, 40, 10, 6.50, 'mal_estado', 3),
('ABA003', 'Azúcar Blanca 1kg', 'Abarrotes', 50, 50, 0, 15, 3.80, 'buen_estado', 3),
('ABA004', 'Fideos Don Vittorio 400g', 'Abarrotes', 65, 65, 0, 15, 2.20, 'buen_estado', 3),
('ABA005', 'Atún Real en Lata 160g', 'Abarrotes', 38, 35, 3, 10, 3.50, 'buen_estado', 3),
('ABA006', 'Sal Ilosa 1kg', 'Abarrotes', 25, 25, 0, 8, 2.50, 'buen_estado', 3),
('HIG001', 'Pasta Dental Colgate 90g', 'Higiene', 60, 60, 0, 15, 4.50, 'buen_estado', 1),
('HIG002', 'Shampoo Pantene 400ml', 'Higiene', 42, 42, 0, 10, 8.90, 'buen_estado', 1),
('HIG003', 'Papel Higiénico Scott 4 rollos', 'Higiene', 35, 32, 3, 10, 7.50, 'buen_estado', 1),
('HIG004', 'Desodorante Rexona 90ml', 'Higiene', 48, 48, 0, 12, 6.80, 'buen_estado', 1),
('CON001', 'Helado D''Onofrio 1L', 'Congelados', 20, 18, 2, 8, 12.50, 'buen_estado', 2),
('CON002', 'Nuggets de Pollo 500g', 'Congelados', 15, 15, 0, 5, 15.90, 'buen_estado', 2),
('CON003', 'Hamburguesas 4 unidades', 'Congelados', 22, 22, 0, 8, 18.50, 'buen_estado', 2),
('CAR001', 'Pechuga de Pollo 1kg', 'Carnes', 25, 25, 0, 10, 12.90, 'buen_estado', 6),
('CAR002', 'Carne Molida 500g', 'Carnes', 18, 18, 0, 8, 18.50, 'buen_estado', 6),
('FRU001', 'Plátano de Seda 1kg', 'Frutas y Verduras', 30, 28, 2, 10, 4.50, 'buen_estado', 7),
('FRU002', 'Tomate 1kg', 'Frutas y Verduras', 40, 38, 2, 15, 3.80, 'buen_estado', 7),
('FRU003', 'Cebolla 1kg', 'Frutas y Verduras', 35, 35, 0, 12, 3.20, 'buen_estado', 7),
('BEB007', 'Pepsi 500ml', 'Bebidas', 8, 8, 0, 20, 2.50, 'buen_estado', 4),
('BEB008', 'Fanta Naranja 500ml', 'Bebidas', 5, 5, 0, 15, 2.50, 'buen_estado', 4),
('BEB009', 'Agua Cielo 500ml', 'Bebidas', 12, 12, 0, 30, 1.50, 'buen_estado', 4),
('BEB010', 'Red Bull 250ml', 'Bebidas', 3, 3, 0, 10, 6.50, 'buen_estado', 4),
('BEB011', 'Monster Energy 500ml', 'Bebidas', 0, 0, 0, 10, 7.50, 'buen_estado', 4),
('LAC006', 'Leche Laive 1L', 'Lácteos', 6, 6, 0, 15, 3.90, 'buen_estado', 5),
('LAC007', 'Queso Edam 500g', 'Lácteos', 4, 4, 0, 10, 8.50, 'buen_estado', 5),
('LAC008', 'Yogurt Laive 1kg', 'Lácteos', 7, 7, 0, 12, 4.30, 'buen_estado', 5),
('LAC009', 'Mantequilla La Serenísima 200g', 'Lácteos', 3, 3, 0, 8, 5.20, 'buen_estado', 5),
('SNA006', 'Frituras Lay''s 150g', 'Snacks', 2, 2, 0, 15, 3.60, 'buen_estado', 1),
('SNA007', 'Galletas Soda Field 180g', 'Snacks', 5, 5, 0, 12, 2.80, 'buen_estado', 1),
('SNA008', 'Cheetos 150g', 'Snacks', 8, 8, 0, 15, 3.40, 'buen_estado', 1),
('SNA009', 'Galletas Morochas 150g', 'Snacks', 0, 0, 0, 10, 2.30, 'buen_estado', 1),
('LIM006', 'Detergente OMO 500ml', 'Limpieza', 4, 4, 0, 12, 5.80, 'buen_estado', 1),
('LIM007', 'Jabón en polvo Ariel 1kg', 'Limpieza', 6, 6, 0, 15, 8.50, 'buen_estado', 1),
('LIM008', 'Suavizante Downy 1L', 'Limpieza', 3, 3, 0, 10, 9.90, 'buen_estado', 1),
('LIM009', 'Desinfectante Lysol 750ml', 'Limpieza', 2, 2, 0, 8, 7.50, 'buen_estado', 1),
('ABA007', 'Arroz Superior 1kg', 'Abarrotes', 10, 10, 0, 25, 5.20, 'buen_estado', 3),
('ABA008', 'Aceite Cocinero 1L', 'Abarrotes', 5, 5, 0, 15, 7.20, 'buen_estado', 3),
('ABA009', 'Azúcar Rubia 1kg', 'Abarrotes', 8, 8, 0, 20, 3.90, 'buen_estado', 3),
('ABA010', 'Conservas de Atún Primor 160g', 'Abarrotes', 3, 3, 0, 12, 3.80, 'buen_estado', 3),
('ABA011', 'Leche Evaporada Ideal 400g', 'Abarrotes', 1, 1, 0, 10, 4.60, 'buen_estado', 3),
('ABA012', 'Avena Quaker 500g', 'Abarrotes', 0, 0, 0, 10, 3.50, 'buen_estado', 3),
('HIG005', 'Pasta Dental Signal 90g', 'Higiene', 4, 4, 0, 12, 4.20, 'buen_estado', 1),
('HIG006', 'Shampoo Head & Shoulders 400ml', 'Higiene', 5, 5, 0, 10, 9.50, 'buen_estado', 1),
('HIG007', 'Jabón Protex 90g', 'Higiene', 2, 2, 0, 15, 2.50, 'buen_estado', 1),
('HIG008', 'Cepillo Dental Colgate', 'Higiene', 0, 0, 0, 12, 3.80, 'buen_estado', 1),
('CON004', 'Helado Donofrio 1.5L', 'Congelados', 1, 1, 0, 10, 15.90, 'buen_estado', 2),
('CON005', 'Papas Fritas McCain 1kg', 'Congelados', 4, 4, 0, 8, 12.50, 'buen_estado', 2),
('CON006', 'Pizza Familiar', 'Congelados', 0, 0, 0, 5, 18.90, 'buen_estado', 2),
('CAR003', 'Pollo Entero 1.5kg', 'Carnes', 2, 2, 0, 10, 15.90, 'buen_estado', 6),
('CAR004', 'Chorizo Parrillero 500g', 'Carnes', 5, 5, 0, 12, 12.50, 'buen_estado', 6),
('CAR005', 'Jamón de Pierna 200g', 'Carnes', 3, 3, 0, 8, 8.90, 'buen_estado', 6),
('FRU004', 'Papa 2kg', 'Frutas y Verduras', 7, 7, 0, 20, 5.50, 'buen_estado', 7),
('FRU005', 'Zanahoria 1kg', 'Frutas y Verduras', 5, 5, 0, 15, 4.20, 'buen_estado', 7),
('FRU006', 'Limón 1kg', 'Frutas y Verduras', 2, 2, 0, 10, 4.80, 'buen_estado', 7),
('FRU007', 'Ajo 250g', 'Frutas y Verduras', 0, 0, 0, 8, 6.50, 'buen_estado', 7),
('PAN001', 'Pan de Molde Bimbo', 'Panadería', 4, 4, 0, 12, 4.50, 'buen_estado', 1),
('PAN002', 'Tostadas Laive 200g', 'Panadería', 2, 2, 0, 10, 3.80, 'buen_estado', 1),
('PAN003', 'Galletas de Soda', 'Panadería', 1, 1, 0, 15, 2.90, 'buen_estado', 1);

INSERT INTO movimientos (fecha, tipo, producto_id, cantidad, usuario_id, observaciones) VALUES 
(NOW() - INTERVAL 30 DAY, 'ENTRADA', 1, 100, 1, 'Compra inicial de stock'),
(NOW() - INTERVAL 25 DAY, 'ENTRADA', 2, 80, 1, 'Reposición de stock'),
(NOW() - INTERVAL 20 DAY, 'ENTRADA', 5, 50, 1, 'Nuevo producto'),
(NOW() - INTERVAL 15 DAY, 'ENTRADA', 6, 40, 2, 'Compra a proveedor'),
(NOW() - INTERVAL 10 DAY, 'ENTRADA', 11, 30, 2, 'Reposición'),
(NOW() - INTERVAL 5 DAY, 'ENTRADA', 15, 20, 1, 'Entrada de mercadería'),
(NOW() - INTERVAL 2 DAY, 'ENTRADA', 20, 25, 1, 'Nueva compra'),
(NOW() - INTERVAL 28 DAY, 'SALIDA', 1, 20, 1, 'Venta cliente'),
(NOW() - INTERVAL 22 DAY, 'SALIDA', 2, 15, 1, 'Salida para uso interno'),
(NOW() - INTERVAL 18 DAY, 'SALIDA', 6, 10, 3, 'Venta cliente'),
(NOW() - INTERVAL 12 DAY, 'SALIDA', 11, 25, 2, 'Venta programada'),
(NOW() - INTERVAL 8 DAY, 'SALIDA', 15, 10, 1, 'Salida por requerimiento'),
(NOW() - INTERVAL 3 DAY, 'SALIDA', 1, 40, 2, 'Salida masiva'),
(NOW() - INTERVAL 1 DAY, 'SALIDA', 7, 5, 1, 'Salida diaria'),
(NOW() - INTERVAL 16 DAY, 'AJUSTE', 3, 5, 1, 'Corrección por inventario físico'),
(NOW() - INTERVAL 14 DAY, 'AJUSTE', 8, 10, 1, 'Ajuste por daño encontrado'),
(NOW() - INTERVAL 9 DAY, 'AJUSTE', 1, 20, 1, 'Corrección de conteo'),
(NOW() - INTERVAL 6 DAY, 'AJUSTE', 22, 3, 1, 'Ajuste por merma natural'),
(NOW() - INTERVAL 1 DAY, 'AJUSTE', 3, 10, 1, 'Corrección de inventario'),
(NOW() - INTERVAL 20 DAY, 'SALIDA', 39, 15, 2, 'Venta masiva - stock bajo'),
(NOW() - INTERVAL 18 DAY, 'SALIDA', 40, 12, 1, 'Salida urgente'),
(NOW() - INTERVAL 15 DAY, 'SALIDA', 41, 10, 2, 'Venta cliente - reposición necesaria'),
(NOW() - INTERVAL 12 DAY, 'SALIDA', 42, 7, 1, 'Venta programada'),
(NOW() - INTERVAL 10 DAY, 'SALIDA', 43, 5, 3, 'Salida por requerimiento - agotado'),
(NOW() - INTERVAL 7 DAY, 'SALIDA', 44, 10, 2, 'Venta mayorista - stock crítico'),
(NOW() - INTERVAL 5 DAY, 'SALIDA', 45, 8, 1, 'Venta minorista'),
(NOW() - INTERVAL 3 DAY, 'ENTRADA', 44, 10, 1, 'Reposición parcial - stock sigue bajo'),
(NOW() - INTERVAL 2 DAY, 'SALIDA', 48, 15, 2, 'Venta masiva'),
(NOW() - INTERVAL 1 DAY, 'AJUSTE', 50, 3, 1, 'Ajuste por producto agotado'),
(NOW() - INTERVAL 4 DAY, 'SALIDA', 52, 8, 1, 'Salida urgente'),
(NOW() - INTERVAL 6 DAY, 'SALIDA', 57, 6, 2, 'Venta cliente'),
(NOW() - INTERVAL 8 DAY, 'SALIDA', 59, 5, 1, 'Salida por requerimiento'),
(NOW() - INTERVAL 5 DAY, 'ENTRADA', 57, 3, 1, 'Reposición mínima'),
(NOW() - INTERVAL 1 DAY, 'AJUSTE', 60, 1, 1, 'Ajuste por conteo físico');


-- Vista de productos con stock bajo
CREATE VIEW productos_stock_bajo AS
SELECT p.*, 
       (p.minimo - p.stock) as cantidad_faltante,
       CASE 
           WHEN p.stock = 0 THEN 'CRÍTICO'
           WHEN p.stock <= (p.minimo * 0.5) THEN 'MUY BAJO'
           ELSE 'BAJO'
       END as nivel_alerta
FROM productos p
WHERE p.stock <= p.minimo;

CREATE VIEW productos_mal_estado AS
SELECT p.*,
       ROUND((p.stock_mal_estado / NULLIF(p.stock, 0)) * 100, 2) as porcentaje_mal_estado
FROM productos p
WHERE p.stock_mal_estado > 0 OR p.estado = 'mal_estado';

CREATE VIEW movimientos_recientes AS
SELECT 
    m.id,
    m.fecha,
    m.tipo,
    p.codigo,
    p.nombre as producto,
    m.cantidad,
    u.nombre as usuario,
    m.observaciones
FROM movimientos m
INNER JOIN productos p ON m.producto_id = p.id
INNER JOIN usuarios u ON m.usuario_id = u.id
ORDER BY m.fecha DESC
LIMIT 100;

CREATE VIEW resumen_movimientos AS
SELECT 
    tipo,
    DATE(fecha) as fecha,
    COUNT(*) as total_movimientos,
    SUM(cantidad) as cantidad_total
FROM movimientos
GROUP BY tipo, DATE(fecha)
ORDER BY fecha DESC, tipo;

CREATE VIEW productos_mas_movidos AS
SELECT 
    p.id,
    p.codigo,
    p.nombre,
    p.categoria,
    COUNT(DISTINCT DATE(m.fecha)) as dias_con_movimiento,
    SUM(CASE WHEN m.tipo = 'ENTRADA' THEN m.cantidad ELSE 0 END) as total_entradas,
    SUM(CASE WHEN m.tipo = 'SALIDA' THEN m.cantidad ELSE 0 END) as total_salidas,
    SUM(CASE WHEN m.tipo = 'AJUSTE' THEN ABS(m.cantidad) ELSE 0 END) as total_ajustes,
    COUNT(*) as total_movimientos
FROM productos p
INNER JOIN movimientos m ON p.id = m.producto_id
GROUP BY p.id, p.codigo, p.nombre, p.categoria
ORDER BY total_movimientos DESC;

CREATE VIEW inventario_valorado AS
SELECT 
    p.categoria,
    COUNT(*) as total_productos,
    SUM(p.stock) as stock_total,
    SUM(p.stock_buen_estado) as stock_bueno,
    SUM(p.stock_mal_estado) as stock_malo,
    SUM(p.stock * p.precio) as valor_inventario,
    AVG(p.precio) as precio_promedio
FROM productos p
GROUP BY p.categoria
ORDER BY valor_inventario DESC;

CREATE VIEW productos_sin_movimiento AS
SELECT 
    p.id,
    p.codigo,
    p.nombre,
    p.categoria,
    p.stock,
    MAX(DATE(m.fecha)) as fecha_ultimo_movimiento,
    DATEDIFF(CURRENT_DATE, MAX(DATE(m.fecha))) as dias_sin_movimiento
FROM productos p
LEFT JOIN movimientos m ON p.id = m.producto_id
GROUP BY p.id, p.codigo, p.nombre, p.categoria, p.stock
HAVING fecha_ultimo_movimiento IS NULL 
    OR DATEDIFF(CURRENT_DATE, fecha_ultimo_movimiento) > 90
ORDER BY dias_sin_movimiento DESC;

-- ===================================================================
-- TRIGGERS
-- ===================================================================

-- Trigger para actualizar stock total automáticamente
DELIMITER //
CREATE TRIGGER actualizar_stock_total
BEFORE UPDATE ON productos
FOR EACH ROW
BEGIN
    SET NEW.stock = NEW.stock_buen_estado + NEW.stock_mal_estado;
END//
DELIMITER ;


-- ===================================================================
-- FUNCIONES
-- ===================================================================

DELIMITER //
CREATE FUNCTION dias_desde_ultima_salida(producto_id INT)
RETURNS INT
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE dias INT DEFAULT 0;
    SELECT DATEDIFF(CURRENT_DATE, MAX(DATE(m.fecha))) INTO dias
    FROM movimientos m
    WHERE m.producto_id = producto_id AND m.tipo = 'SALIDA';
    RETURN IFNULL(dias, 999);
END//
DELIMITER ;

DELIMITER //
CREATE FUNCTION dias_desde_ultima_entrada(producto_id INT)
RETURNS INT
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE dias INT DEFAULT 0;
    SELECT DATEDIFF(CURRENT_DATE, MAX(DATE(m.fecha))) INTO dias
    FROM movimientos m
    WHERE m.producto_id = producto_id AND m.tipo = 'ENTRADA';
    RETURN IFNULL(dias, 999);
END//
DELIMITER ;

-- ===================================================================
-- ÍNDICES ADICIONALES
-- ===================================================================

-- Índices compuestos para mejorar consultas frecuentes
CREATE INDEX idx_producto_categoria ON productos(categoria);
CREATE INDEX idx_movimiento_fecha_tipo ON movimientos(fecha, tipo);
CREATE INDEX idx_movimiento_producto_fecha ON movimientos(producto_id, fecha);

-- ===================================================================
-- VERIFICACIÓN DE DATOS
-- ===================================================================

-- Verificar datos insertados
SELECT '=== RESUMEN DE DATOS INSERTADOS ===' as info;

SELECT 'Usuarios:' as info, COUNT(*) as total FROM usuarios;
SELECT 'Categorías:' as info, COUNT(*) as total FROM categorias;
SELECT 'Proveedores:' as info, COUNT(*) as total FROM proveedores;
SELECT 'Productos:' as info, COUNT(*) as total FROM productos;
SELECT 'Movimientos:' as info, COUNT(*) as total FROM movimientos;

SELECT '' as separador;

SELECT 'Productos por categoría:' as info;
SELECT categoria, COUNT(*) as cantidad, SUM(stock) as stock_total 
FROM productos 
GROUP BY categoria 
ORDER BY cantidad DESC;

SELECT 'Productos con stock bajo:' as info;
SELECT codigo, nombre, categoria, stock, minimo, cantidad_faltante, nivel_alerta
FROM productos_stock_bajo
ORDER BY cantidad_faltante DESC
LIMIT 10;

SELECT 'Productos en mal estado:' as info;
SELECT codigo, nombre, stock_mal_estado, stock, porcentaje_mal_estado
FROM productos_mal_estado
ORDER BY stock_mal_estado DESC;

SELECT 'Movimientos por tipo:' as info;
SELECT tipo, COUNT(*) as cantidad, SUM(cantidad) as cantidad_total
FROM movimientos
GROUP BY tipo
ORDER BY cantidad DESC;

SELECT 'Top 5 productos más movidos:' as info;
SELECT nombre, total_movimientos, total_entradas, total_salidas
FROM productos_mas_movidos
LIMIT 5;

SELECT '=== BASE DE DATOS DE INVENTARIO ===' as info;
