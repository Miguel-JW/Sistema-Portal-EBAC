public class Coordenador extends Funcionario {
    public Coordenador(int id, String nome) { super(id, nome); }

    @Override public boolean batePonto()         { return true; }
    @Override public int     limiteHorasExtras() { return 5; }
    @Override public String  getTipo()           { return "Coordenador"; }
}
