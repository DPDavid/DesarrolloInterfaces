package restaurante;

import javax.persistence.*;


@Entity
@Table(name = "productos")
public class Productos {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String nombre;
        private String categoria;
        private double precio;
        private String disponible;

        public Productos() {
        }

        public Productos(String nombre, String categoria, double precio, String disponible) {
            this.nombre = nombre;
            this.categoria = categoria;
            this.precio = precio;
            this.disponible = disponible;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }

        public double getPrecio() {return precio;}

        public void setPrecio(float precio) {this.precio = precio;}

        public String getDisponible() {
            return disponible.equals("1") ? "Sí" : "No";
        }

        public void setDisponible(String disponible) {
            this.disponible = disponible;
        }

        @Override
        public String toString() {
            return "Producto{" +
                    "id=" + id +
                    ", nombre='" + nombre + '\'' +
                    ", categoria='" + categoria + '\'' +
                    ", precio='" + precio + '\'' +
                    ", disponibilidad='" + disponible + '\'' +
                    '}';
        }
}
