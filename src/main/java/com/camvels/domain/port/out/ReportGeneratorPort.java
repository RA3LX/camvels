package com.camvels.domain.port.out;

import com.camvels.domain.model.Movimiento;
import com.camvels.domain.model.Producto;
import com.camvels.domain.model.Proveedor;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface ReportGeneratorPort {

    void generarProductos(List<Producto> productos, OutputStream output) throws IOException;

    void generarStockBajo(List<Producto> productos, OutputStream output) throws IOException;

    void generarMovimientos(List<Movimiento> movimientos, OutputStream output) throws IOException;

    void generarProveedores(List<Proveedor> proveedores, OutputStream output) throws IOException;

    void generarProveedoresProductos(boolean incluirStockBajo, boolean incluirMalEstado,
                                     Map<Integer, List<Object[]>> productosPorProveedor,
                                     OutputStream output) throws IOException;
}
