public interface IPreparacionRepository extends JpaRepository<Preparacion, Long> {
    boolean existsByRecetaAndFechaAndEliminadoFalse(Receta receta, LocalDate fecha);
    List<Preparacion> findByEliminadoFalse();
    List<Preparacion> findByEliminadoFalseAndFechaBetween(LocalDate desde, LocalDate hasta);
}
