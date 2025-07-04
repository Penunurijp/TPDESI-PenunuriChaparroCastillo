@Entity
public class Preparacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate fecha;

    @ManyToOne(optional = false)
    private Receta receta;

    @Min(1)
    private int cantidadRaciones;

    private boolean eliminado = false;

    // Getters y setters
}
