package org.example;

import java.sql.*;

/**
 * Clase principal de la aplicación.
 * Esta clase se conecta a una base de datos MySQL y ejecuta varios informes.
 */
public class Main {
    private static final String URL = "jdbc:mysql://localhost:3306/parcial3";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    /**
     * Método principal.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Reporte de Inventario:");
            reporteInventario(conn);

            System.out.println("\nReporte de Ventas Mensuales:");
            reporteVentasMensuales(conn, 6, 2024);  // Ejemplo para junio 2024

            System.out.println("\nReporte de Clientes Activos:");
            reporteClientesActivos(conn);

            System.out.println("\nReporte de Pedidos Pendientes:");
            reportePedidosPendientes(conn);

            System.out.println("\nReporte de Proveedores y Pedidos:");
            reporteProveedoresPedidos(conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Establece una conexión con la base de datos MySQL.
     *
     * @return Un objeto Connection.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Ejecuta el procedimiento almacenado "sp_reporte_inventario" e imprime el informe.
     *
     * @param conn Un objeto Connection.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    private static void reporteInventario(Connection conn) throws SQLException {
        String query = "{CALL sp_reporte_inventario()}";
        try (CallableStatement stmt = conn.prepareCall(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("producto_id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                double precio = rs.getDouble("precio");
                int cantidad = rs.getInt("cantidad");
                System.out.println(id + ", " + nombre + ", " + descripcion + ", " + precio + ", " + cantidad);
            }
        }
    }

    /**
     * Ejecuta el procedimiento almacenado "sp_reporte_ventas_mensuales" e imprime el informe.
     *
     * @param conn Un objeto Connection.
     * @param mes El mes del informe.
     * @param anio El año del informe.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    private static void reporteVentasMensuales(Connection conn, int mes, int anio) throws SQLException {
        String query = "{CALL sp_reporte_ventas_mensuales(?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(query)) {
            stmt.setInt(1, mes);
            stmt.setInt(2, anio);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("venta_id");
                    String fecha = rs.getString("fecha_venta");
                    int clienteId = rs.getInt("cliente_id");
                    int productoId = rs.getInt("producto_id");
                    int cantidad = rs.getInt("cantidad_vendida");
                    double precioTotal = rs.getDouble("precio_total");
                    System.out.println(id + ", " + fecha + ", " + clienteId + ", " + productoId + ", " + cantidad + ", " + precioTotal);
                }
            }
        }
    }

    /**
     * Ejecuta el procedimiento almacenado "sp_reporte_clientes_activos" e imprime el informe.
     *
     * @param conn Un objeto Connection.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    private static void reporteClientesActivos(Connection conn) throws SQLException {
        String query = "{CALL sp_reporte_clientes_activos()}";
        try (CallableStatement stmt = conn.prepareCall(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("cliente_id");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String telefono = rs.getString("telefono");
                System.out.println(id + ", " + nombre + ", " + email + ", " + telefono);
            }
        }
    }

    /**
     * Ejecuta el procedimiento almacenado "sp_reporte_pedidos_pendientes" e imprime el informe.
     *
     * @param conn Un objeto Connection.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    private static void reportePedidosPendientes(Connection conn) throws SQLException {
        String query = "{CALL sp_reporte_pedidos_pendientes()}";
        try (CallableStatement stmt = conn.prepareCall(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("pedido_id");
                String fecha = rs.getString("fecha_pedido");
                int proveedorId = rs.getInt("proveedor_id");
                int productoId = rs.getInt("producto_id");
                int cantidad = rs.getInt("cantidad");
                String estado = rs.getString("estado");
                System.out.println(id + ", " + fecha + ", " + proveedorId + ", " + productoId + ", " + cantidad + ", " + estado);
            }
        }
    }

    /**
     * Ejecuta el procedimiento almacenado "sp_reporte_proveedores_pedidos" e imprime el informe.
     *
     * @param conn Un objeto Connection.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    private static void reporteProveedoresPedidos(Connection conn) throws SQLException {
        String query = "{CALL sp_reporte_proveedores_pedidos()}";
        try (CallableStatement stmt = conn.prepareCall(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int proveedorId = rs.getInt("proveedor_id");
                String proveedorNombre = rs.getString("proveedor_nombre");
                int pedidoId = rs.getInt("pedido_id");
                String fecha = rs.getString("fecha_pedido");
                int productoId = rs.getInt("producto_id");
                int cantidad = rs.getInt("cantidad");
                String estado = rs.getString("estado");
                System.out.println(proveedorId + ", " + proveedorNombre + ", " + pedidoId + ", " + fecha + ", " + productoId + ", " + cantidad + ", " + estado);
            }
        }
    }
}
