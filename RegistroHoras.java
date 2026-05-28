import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class RegistroHoras {

    private static final int JORNADA_PADRAO  = 8; // horas
    private static final int ALMOCO          = 1; // hora descontada

    private final LocalDate data;
    private final LocalTime entrada;
    private final LocalTime saida;

    public RegistroHoras(LocalDate data, LocalTime entrada, LocalTime saida) {
        this.data    = data;
        this.entrada = entrada;
        this.saida   = saida;
    }

    public LocalTime getEntrada() { return entrada; }
    public LocalTime getSaida()   { return saida; }

    // Horas trabalhadas descontando 1h de almoço
    public double getHorasTrabalhadas() {
        double total = saida.getHour() - entrada.getHour()
                     + (saida.getMinute() - entrada.getMinute()) / 60.0;
        return total - ALMOCO;
    }

    // Horas extras além da jornada padrão
    public double getHorasExtras() {
        double extras = getHorasTrabalhadas() - JORNADA_PADRAO;
        return Math.max(0, extras);
    }

    @Override
    public String toString() {
        DateTimeFormatter fData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter fHora = DateTimeFormatter.ofPattern("HH:mm");
        return String.format(
            "  Data: %s | Entrada: %s | Saída: %s | Trabalhadas: %.1fh | Extras: %.1fh",
            data.format(fData), entrada.format(fHora),
            saida.format(fHora), getHorasTrabalhadas(), getHorasExtras()
        );
    }
}
