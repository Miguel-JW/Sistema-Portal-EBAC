public class Analista extends Funcionario {
    public Analista(int id, String nome) { super(id, nome); }

    @Override public boolean batePonto()         { return true; }
    @Override public int     limiteHorasExtras() { return 3; }
    @Override public String  getTipo()           { return "Analista"; }
}
