package restaurante;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<PedidoDetalle> detalles = new HashSet<>();

    private LocalDateTime fecha;

    @Column(name = "total")
    private double total;

    private String estado;

    public Pedido(Cliente clienteSeleccionado, LocalDateTime fecha, Double totalPedido,String estado) {
        this.cliente = clienteSeleccionado;
        this.fecha = fecha;
        this.total = totalPedido;
        this.estado = estado;
    }

    public Pedido() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fechaHora) { this.fecha = fechaHora; }

    public double getTotal() { return calcularTotal(); } // Total se calcula automáticamente

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Set<PedidoDetalle> getDetalles() { return detalles; }

    public void setDetalles(Set<PedidoDetalle> detalles) {
        this.detalles = detalles;
        calcularTotal();
    }


    public void addDetalle(PedidoDetalle detalle) {
        detalles.add(detalle);
        this.total = calcularTotal();
    }


    public void removeDetalle(PedidoDetalle detalle) {
        this.detalles.remove(detalle);
    }


    public double calcularTotal() {
        return detalles.stream().mapToDouble(detalle -> detalle.getProducto().getPrecio() * detalle.getCantidad()).sum();
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", cliente=" + (cliente != null ? cliente.getNombre() : "N/A") +
                ", fecha=" + fecha +
                ", total=" + getTotal() +
                ", estado='" + estado + '\'' +
                '}';
    }
}
