public class Estagiario extends Funcionario {
    public Estagiario(int id, String nome) { super(id, nome); }

    @Override public boolean batePonto()         { return false; }
    @Override public int     limiteHorasExtras() { return 0; }
    @Override public String  getTipo()           { return "Estagiário"; }
}
