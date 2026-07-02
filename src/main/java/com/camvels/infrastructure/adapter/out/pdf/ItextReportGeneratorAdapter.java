package com.camvels.infrastructure.adapter.out.pdf;

import com.camvels.domain.model.Movimiento;
import com.camvels.domain.model.Producto;
import com.camvels.domain.model.Proveedor;
import com.camvels.domain.port.out.ReportGeneratorPort;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItextReportGeneratorAdapter implements ReportGeneratorPort {

    @Override
    public void generarProductos(List<Producto> productos, OutputStream output) throws IOException {
        PdfWriter writer = new PdfWriter(output);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        Paragraph titulo = new Paragraph("REPORTE DE PRODUCTOS - CAMVELS")
            .setFontSize(18)
            .setTextAlignment(TextAlignment.CENTER)
            .setBold();
        document.add(titulo);

        Paragraph fecha = new Paragraph("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
            .setFontSize(10)
            .setTextAlignment(TextAlignment.CENTER);
        document.add(fecha);

        document.add(new Paragraph(" "));

        Table table = new Table(UnitValue.createPercentArray(new float[]{10, 30, 20, 15, 15, 10}));
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell(new Cell().add(new Paragraph("ID")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("Nombre")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("Categoría")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("Stock")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("Precio")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("Mínimo")).setBold());

        for (Producto p : productos) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(p.getId()))));
            table.addCell(new Cell().add(new Paragraph(p.getNombre())));
            table.addCell(new Cell().add(new Paragraph(p.getCategoria())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(p.getStock()))));
            table.addCell(new Cell().add(new Paragraph("S/. " + String.format("%.2f", p.getPrecio()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(p.getMinimo()))));
        }

        document.add(table);
        document.close();
    }

    @Override
    public void generarStockBajo(List<Producto> productos, OutputStream output) throws IOException {
        PdfWriter writer = new PdfWriter(output);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        Paragraph titulo = new Paragraph("REPORTE DE STOCK BAJO - CAMVELS")
            .setFontSize(18)
            .setTextAlignment(TextAlignment.CENTER)
            .setBold();
        document.add(titulo);

        Paragraph fecha = new Paragraph("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
            .setFontSize(10)
            .setTextAlignment(TextAlignment.CENTER);
        document.add(fecha);

        document.add(new Paragraph(" "));

        Table table = new Table(UnitValue.createPercentArray(new float[]{15, 35, 20, 15, 15}));
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell(new Cell().add(new Paragraph("Código")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("Producto")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("Categoría")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("Stock")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("Mínimo")).setBold());

        for (Producto p : productos) {
            table.addCell(new Cell().add(new Paragraph(p.getCodigo())));
            table.addCell(new Cell().add(new Paragraph(p.getNombre())));
            table.addCell(new Cell().add(new Paragraph(p.getCategoria())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(p.getStock()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(p.getMinimo()))));
        }

        document.add(table);
        document.close();
    }

    @Override
    public void generarMovimientos(List<Movimiento> movimientos, OutputStream output) throws IOException {
        PdfWriter writer = new PdfWriter(output);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        Paragraph titulo = new Paragraph("REPORTE DE MOVIMIENTOS - CAMVELS")
            .setFontSize(18)
            .setTextAlignment(TextAlignment.CENTER)
            .setBold();
        document.add(titulo);

        Paragraph fecha = new Paragraph("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
            .setFontSize(10)
            .setTextAlignment(TextAlignment.CENTER);
        document.add(fecha);

        document.add(new Paragraph(" "));

        Table table = new Table(UnitValue.createPercentArray(new float[]{12, 12, 25, 15, 15, 21}));
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell(new Cell().add(new Paragraph("Fecha")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("Tipo")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("Producto")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("Cantidad")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("Usuario")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("Observaciones")).setBold());

        for (Movimiento m : movimientos) {
            String fechaStr = m.getFecha() != null ?
                m.getFecha().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "";
            table.addCell(new Cell().add(new Paragraph(fechaStr)));

            String tipoStr;
            switch (m.getTipo()) {
                case "ENTRADA" -> tipoStr = "Entrada";
                case "SALIDA" -> tipoStr = "Salida";
                case "AJUSTE" -> tipoStr = "Ajuste";
                default -> tipoStr = m.getTipo();
            }
            table.addCell(new Cell().add(new Paragraph(tipoStr)));

            String productoStr = (m.getProductoCodigo() != null ? m.getProductoCodigo() : "") + " - " +
                                 (m.getProductoNombre() != null ? m.getProductoNombre() : "");
            table.addCell(new Cell().add(new Paragraph(productoStr)));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(m.getCantidad()))));

            String usuarioStr = m.getUsuarioNombre() != null ? m.getUsuarioNombre() : "";
            table.addCell(new Cell().add(new Paragraph(usuarioStr)));

            String obsStr = m.getObservaciones() != null && !m.getObservaciones().isEmpty() ? m.getObservaciones() : "Sin observaciones";
            table.addCell(new Cell().add(new Paragraph(obsStr.length() > 30 ? obsStr.substring(0, 30) + "..." : obsStr)));
        }

        document.add(table);
        document.close();
    }

    @Override
    public void generarProveedores(List<Proveedor> proveedores, OutputStream output) throws IOException {
        PdfWriter writer = new PdfWriter(output);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        Paragraph titulo = new Paragraph("REPORTE DE PROVEEDORES - CAMVELS")
            .setFontSize(18)
            .setTextAlignment(TextAlignment.CENTER)
            .setBold();
        document.add(titulo);

        Paragraph fecha = new Paragraph("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
            .setFontSize(10)
            .setTextAlignment(TextAlignment.CENTER);
        document.add(fecha);

        document.add(new Paragraph(" "));

        Table table = new Table(UnitValue.createPercentArray(new float[]{10, 15, 30, 25, 20}));
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell(new Cell().add(new Paragraph("ID")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("RUC")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("Nombre")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("Teléfono")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("Email")).setBold());

        for (Proveedor p : proveedores) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(p.getId()))));
            table.addCell(new Cell().add(new Paragraph(p.getRuc())));
            table.addCell(new Cell().add(new Paragraph(p.getNombre())));
            table.addCell(new Cell().add(new Paragraph(p.getTelefono() != null ? p.getTelefono() : "")));
            table.addCell(new Cell().add(new Paragraph(p.getEmail() != null ? p.getEmail() : "")));
        }

        document.add(table);
        document.close();
    }

    @Override
    public void generarProveedoresProductos(boolean incluirStockBajo, boolean incluirMalEstado,
                                            Map<Integer, List<Object[]>> productosPorProveedor,
                                            OutputStream output) throws IOException {
        PdfWriter writer = new PdfWriter(output);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        Paragraph titulo = new Paragraph("REPORTE PARA PROVEEDORES - CAMVELS")
            .setFontSize(18)
            .setTextAlignment(TextAlignment.CENTER)
            .setBold();
        document.add(titulo);

        StringBuilder subtituloText = new StringBuilder("Productos: ");
        if (incluirStockBajo && incluirMalEstado) {
            subtituloText.append("Stock Bajo y Mal Estado");
        } else if (incluirStockBajo) {
            subtituloText.append("Stock Bajo");
        } else {
            subtituloText.append("Mal Estado");
        }
        Paragraph subtitulo = new Paragraph(subtituloText.toString())
            .setFontSize(14)
            .setTextAlignment(TextAlignment.CENTER)
            .setBold();
        document.add(subtitulo);

        Paragraph fecha = new Paragraph("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
            .setFontSize(10)
            .setTextAlignment(TextAlignment.CENTER);
        document.add(fecha);

        document.add(new Paragraph(" "));

        if (productosPorProveedor.isEmpty()) {
            Paragraph sinDatos = new Paragraph("No hay productos con problemas que reportar.")
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER);
            document.add(sinDatos);
        } else {
            for (Map.Entry<Integer, List<Object[]>> entry : productosPorProveedor.entrySet()) {
                List<Object[]> productos = entry.getValue();
                if (productos.isEmpty()) continue;

                Object[] primerProducto = productos.get(0);
                if (primerProducto == null || primerProducto.length < 16) continue;

                String proveedorNombre = primerProducto[11] != null ? (String) primerProducto[11] : "N/A";
                String proveedorRuc = primerProducto[12] != null ? (String) primerProducto[12] : "N/A";
                String proveedorTelefono = primerProducto[13] != null ? (String) primerProducto[13] : "N/A";
                String proveedorEmail = primerProducto[14] != null ? (String) primerProducto[14] : "N/A";

                document.add(new Paragraph(" "));

                Paragraph provTitulo = new Paragraph("PROVEEDOR: " + proveedorNombre)
                    .setFontSize(14)
                    .setBold()
                    .setMarginTop(10);
                document.add(provTitulo);

                Paragraph provInfo = new Paragraph("RUC: " + (proveedorRuc != null ? proveedorRuc : "N/A") +
                    " | Tel: " + (proveedorTelefono != null ? proveedorTelefono : "N/A") +
                    " | Email: " + (proveedorEmail != null ? proveedorEmail : "N/A"))
                    .setFontSize(10)
                    .setMarginBottom(5);
                document.add(provInfo);

                List<Object[]> productosStockBajo = new ArrayList<>();
                List<Object[]> productosMalEstado = new ArrayList<>();

                for (Object[] producto : productos) {
                    if (producto == null || producto.length < 10) continue;

                    int stock = producto[4] != null ? (Integer) producto[4] : 0;
                    int minimo = producto[5] != null ? (Integer) producto[5] : 0;
                    int stockMalEstado = producto[7] != null ? (Integer) producto[7] : 0;
                    String estado = producto[9] != null ? (String) producto[9] : "";

                    boolean tieneStockBajo = stock <= minimo;
                    boolean estaMalEstado = stockMalEstado > 0 || "mal_estado".equals(estado);

                    if (incluirStockBajo && tieneStockBajo) {
                        productosStockBajo.add(producto);
                    }
                    if (incluirMalEstado && estaMalEstado) {
                        productosMalEstado.add(producto);
                    }
                }

                if (!productosStockBajo.isEmpty() && incluirStockBajo) {
                    Paragraph stockTitulo = new Paragraph("PRODUCTOS CON STOCK BAJO:")
                        .setFontSize(12)
                        .setBold()
                        .setMarginTop(8);
                    document.add(stockTitulo);

                    Table tablaStock = new Table(new float[]{80, 180, 100, 70, 70, 80});
                    tablaStock.setWidth(UnitValue.createPercentValue(100));
                    tablaStock.setMarginBottom(10);

                    tablaStock.addHeaderCell(new Cell().add(new Paragraph("Código"))
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setPadding(5));
                    tablaStock.addHeaderCell(new Cell().add(new Paragraph("Producto"))
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setPadding(5));
                    tablaStock.addHeaderCell(new Cell().add(new Paragraph("Categoría"))
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setPadding(5));
                    tablaStock.addHeaderCell(new Cell().add(new Paragraph("Stock\nActual"))
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setPadding(5));
                    tablaStock.addHeaderCell(new Cell().add(new Paragraph("Stock\nMínimo"))
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setPadding(5));
                    tablaStock.addHeaderCell(new Cell().add(new Paragraph("Cantidad\nFaltante"))
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setPadding(5));

                    for (Object[] producto : productosStockBajo) {
                        if (producto == null || producto.length < 10) continue;

                        int stock = producto[4] != null ? (Integer) producto[4] : 0;
                        int minimo = producto[5] != null ? (Integer) producto[5] : 0;
                        int cantidadFaltante = Math.max(0, minimo - stock);

                        String codigo = producto[1] != null ? ((String) producto[1]).trim() : "";
                        String nombre = producto[2] != null ? ((String) producto[2]).trim() : "";
                        String categoria = producto[3] != null ? ((String) producto[3]).trim() : "";

                        if (nombre.length() > 40) {
                            nombre = nombre.substring(0, 37) + "...";
                        }
                        if (categoria.length() > 20) {
                            categoria = categoria.substring(0, 17) + "...";
                        }

                        tablaStock.addCell(new Cell().add(new Paragraph(codigo))
                            .setPadding(5)
                            .setTextAlignment(TextAlignment.LEFT));
                        tablaStock.addCell(new Cell().add(new Paragraph(nombre))
                            .setPadding(5)
                            .setTextAlignment(TextAlignment.LEFT));
                        tablaStock.addCell(new Cell().add(new Paragraph(categoria))
                            .setPadding(5)
                            .setTextAlignment(TextAlignment.LEFT));
                        tablaStock.addCell(new Cell().add(new Paragraph(String.valueOf(stock)))
                            .setPadding(5)
                            .setTextAlignment(TextAlignment.CENTER));
                        tablaStock.addCell(new Cell().add(new Paragraph(String.valueOf(minimo)))
                            .setPadding(5)
                            .setTextAlignment(TextAlignment.CENTER));
                        tablaStock.addCell(new Cell().add(new Paragraph(String.valueOf(cantidadFaltante)))
                            .setBold()
                            .setPadding(5)
                            .setTextAlignment(TextAlignment.CENTER));
                    }

                    document.add(tablaStock);
                }

                if (!productosMalEstado.isEmpty() && incluirMalEstado) {
                    Paragraph estadoTitulo = new Paragraph("PRODUCTOS EN MAL ESTADO:")
                        .setFontSize(12)
                        .setBold()
                        .setMarginTop(8);
                    document.add(estadoTitulo);

                    Table tablaEstado = new Table(new float[]{80, 200, 120, 90, 90});
                    tablaEstado.setWidth(UnitValue.createPercentValue(100));
                    tablaEstado.setMarginBottom(10);

                    tablaEstado.addHeaderCell(new Cell().add(new Paragraph("Código"))
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setPadding(5));
                    tablaEstado.addHeaderCell(new Cell().add(new Paragraph("Producto"))
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setPadding(5));
                    tablaEstado.addHeaderCell(new Cell().add(new Paragraph("Categoría"))
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setPadding(5));
                    tablaEstado.addHeaderCell(new Cell().add(new Paragraph("Stock\nMal Estado"))
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setPadding(5));
                    tablaEstado.addHeaderCell(new Cell().add(new Paragraph("Stock\nTotal"))
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setPadding(5));

                    for (Object[] producto : productosMalEstado) {
                        if (producto == null || producto.length < 10) continue;

                        int stockMalEstado = producto[7] != null ? (Integer) producto[7] : 0;
                        int stock = producto[4] != null ? (Integer) producto[4] : 0;

                        String codigo = producto[1] != null ? ((String) producto[1]).trim() : "";
                        String nombre = producto[2] != null ? ((String) producto[2]).trim() : "";
                        String categoria = producto[3] != null ? ((String) producto[3]).trim() : "";

                        if (nombre.length() > 50) {
                            nombre = nombre.substring(0, 47) + "...";
                        }
                        if (categoria.length() > 25) {
                            categoria = categoria.substring(0, 22) + "...";
                        }

                        tablaEstado.addCell(new Cell().add(new Paragraph(codigo))
                            .setPadding(5)
                            .setTextAlignment(TextAlignment.LEFT));
                        tablaEstado.addCell(new Cell().add(new Paragraph(nombre))
                            .setPadding(5)
                            .setTextAlignment(TextAlignment.LEFT));
                        tablaEstado.addCell(new Cell().add(new Paragraph(categoria))
                            .setPadding(5)
                            .setTextAlignment(TextAlignment.LEFT));
                        tablaEstado.addCell(new Cell().add(new Paragraph(String.valueOf(stockMalEstado)))
                            .setBold()
                            .setPadding(5)
                            .setTextAlignment(TextAlignment.CENTER));
                        tablaEstado.addCell(new Cell().add(new Paragraph(String.valueOf(stock)))
                            .setPadding(5)
                            .setTextAlignment(TextAlignment.CENTER));
                    }

                    document.add(tablaEstado);
                }

                document.add(new Paragraph(" "));
            }
        }

        document.add(new Paragraph(" "));
        StringBuilder notaText = new StringBuilder("Este reporte contiene productos que requieren atención: ");
        if (incluirStockBajo && incluirMalEstado) {
            notaText.append("reposición de stock (cantidad faltante calculada según stock mínimo) y/o revisión por mal estado.");
        } else if (incluirStockBajo) {
            notaText.append("reposición de stock. La cantidad faltante se calcula restando el stock actual del stock mínimo requerido.");
        } else {
            notaText.append("revisión por mal estado.");
        }
        Paragraph nota = new Paragraph(notaText.toString())
            .setFontSize(9)
            .setItalic()
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginTop(15);
        document.add(nota);

        document.close();
    }
}
