@Service
public class StockService {

    @Autowired
    private IngredienteRepository ingredienteRepo;

    @Autowired
    private RecetaIngredienteRepository recetaIngredienteRepo;

    public boolean hayStockSuficiente(Receta receta, int raciones) {
        List<RecetaIngrediente> ingredientes = recetaIngredienteRepo.findByReceta(receta);
        for (RecetaIngrediente ri : ingredientes) {
            Ingrediente ing = ri.getIngrediente();
            double requerido = ri.getCantidad() * raciones;
            if (ing.getStock() < requerido) {
                return false;
            }
        }
        return true;
    }

    public void descontarIngredientes(Receta receta, int raciones) {
        List<RecetaIngrediente> ingredientes = recetaIngredienteRepo.findByReceta(receta);
        for (RecetaIngrediente ri : ingredientes) {
            Ingrediente ing = ri.getIngrediente();
            double requerido = ri.getCantidad() * raciones;
            ing.setStock(ing.getStock() - requerido);
            ingredienteRepo.save(ing);
        }
    }
}

