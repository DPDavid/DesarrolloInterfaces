package restaurante;

import javax.persistence.*;

@Entity
@Table(name = "pedidodetalles")
public class PedidoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Productos producto;

    private int cantidad;

    private double precio;

    private double subtotal;


    public PedidoDetalle() {

    }


    public PedidoDetalle(Pedido pedido, Productos producto, int cantidad) {
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = producto.getPrecio();
        this.subtotal = calcularSubtotal();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Productos getProducto() { return producto; }
    public void setProducto(Productos producto) {
        this.producto = producto;
        this.precio = producto.getPrecio();
        calcularSubtotal();
    }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public double getPrecio() { return precio; }
    public double getSubtotal() { return subtotal; }


    private double calcularSubtotal() {
        this.subtotal = this.cantidad * this.precio;
        return this.subtotal;
    }



    @Override
    public String toString() {
        return "PedidoDetalle{" +
                "id=" + id +
                ", producto=" + producto.getNombre() +
                ", cantidad=" + cantidad +
                ", precio=" + precio +
                ", subtotal=" + subtotal +
                '}';
    }
}
