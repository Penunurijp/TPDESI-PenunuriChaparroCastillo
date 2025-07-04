@Service
public class PreparacionService {

    @Autowired
    private IPreparacionRepository repo;

    @Autowired
    private StockService stockService;

    public void registrar(Preparacion p) {
        if (p.getFecha().isAfter(LocalDate.now())) {
            throw new RuntimeException("La fecha no puede ser futura");
        }
        if (repo.existsByRecetaAndFechaAndEliminadoFalse(p.getReceta(), p.getFecha())) {
            throw new RuntimeException("Ya existe una preparación para esa receta y fecha");
        }
        if (!stockService.hayStockSuficiente(p.getReceta(), p.getCantidadRaciones())) {
            throw new RuntimeException("Stock insuficiente para los ingredientes");
        }
        repo.save(p);
        stockService.descontarIngredientes(p.getReceta(), p.getCantidadRaciones());
    }

    public void eliminar(Long id) {
        Preparacion p = repo.findById(id).orElseThrow();
        p.setEliminado(true);
        repo.save(p);
    }

    public void modificarFecha(Long id, LocalDate nuevaFecha) {
        Preparacion p = repo.findById(id).orElseThrow();
        p.setFecha(nuevaFecha);
        repo.save(p);
    }

    public List<Preparacion> listar() {
        return repo.findByEliminadoFalse();
    }

    public Preparacion buscarPorId(Long id) {
        return repo.findById(id).orElseThrow();
    }
}
