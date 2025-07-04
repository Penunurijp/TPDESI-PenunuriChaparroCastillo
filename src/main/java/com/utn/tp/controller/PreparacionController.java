@Controller
@RequestMapping("/preparaciones")
public class PreparacionController {

    @Autowired
    private PreparacionService servicio;

    @Autowired
    private RecetaService recetaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("preparaciones", servicio.listar());
        return "preparaciones/listado";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("preparacion", new Preparacion());
        model.addAttribute("recetas", recetaService.listar());
        return "preparaciones/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Preparacion p, RedirectAttributes redir) {
        try {
            servicio.registrar(p);
            redir.addFlashAttribute("exito", "Preparación guardada correctamente");
        } catch (Exception e) {
            redir.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/preparaciones";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("preparacion", servicio.buscarPorId(id));
        return "preparaciones/editar";
    }

    @PostMapping("/modificar")
    public String modificar(@ModelAttribute Preparacion p) {
        servicio.modificarFecha(p.getId(), p.getFecha());
        return "redirect:/preparaciones";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return "redirect:/preparaciones";
    }
}
