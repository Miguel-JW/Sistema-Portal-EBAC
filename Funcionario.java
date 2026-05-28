import java.util.ArrayList;
import java.util.List;

public abstract class Funcionario {
    private final int    id;
    private final String nome;
    private final List<RegistroHoras> registros = new ArrayList<>();

    public Funcionario(int id, String nome) {
        this.id   = id;
        this.nome = nome;
    }

    public int    getId()   { return id; }
    public String getNome() { return nome; }

    public List<RegistroHoras> getRegistros() { return registros; }

    public void adicionarRegistro(RegistroHoras r) { registros.add(r); }

    // Cada tipo define se bate ponto e o limite de horas extras
    public abstract boolean batePonto();
    public abstract int     limiteHorasExtras();
    public abstract String  getTipo();

    @Override
    public String toString() {
        return String.format("  [%d] %-15s | Tipo: %s", id, nome, getTipo());
    }
}
