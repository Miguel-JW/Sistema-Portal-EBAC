import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PortalRH {

    private final List<Funcionario> funcionarios = new ArrayList<>();
    private int proximoId = 1;
    private final Scanner scanner = new Scanner(System.in);

    // ── Menu principal ─────────────────────────────────────
    public void iniciar() {
        boasVindas();
        int opcao;
        do {
            exibirMenu();
            opcao = lerInt();
            switch (opcao) {
                case 1: criarFuncionario();      break;
                case 2: registrarHoras();        break;
                case 3: listarFuncionarios();    break;
                case 4: verRegistros();          break;
                case 5: removerFuncionario();    break;
                case 6: mensagemSaida();         break;
                default: System.out.println("\n⚠  Opção inválida!");
            }
        } while (opcao != 6);
        scanner.close();
    }

    // ── Boas vindas ────────────────────────────────────────
    void boasVindas() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("       🏢  Portal de RH - Registro         ");
        System.out.println("            de Horas de Trabalho           ");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println("  Bem-vindo ao sistema de ponto eletrônico!");
    }

    void exibirMenu() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("  1 - Criar funcionário                    ");
        System.out.println("  2 - Registrar horários                   ");
        System.out.println("  3 - Listar funcionários                  ");
        System.out.println("  4 - Ver registros de um funcionário      ");
        System.out.println("  5 - Remover funcionário                  ");
        System.out.println("  6 - Sair                                 ");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.print("  Opção: ");
    }

    // ── Criar funcionário ──────────────────────────────────
    void criarFuncionario() {
        scanner.nextLine();
        System.out.println("\n── Novo Funcionário ──────────────────────");
        System.out.print("  Nome: ");
        String nome = scanner.nextLine().trim();

        System.out.println("  Tipos: 1-Gerente  2-Coordenador  3-Analista  4-Assistente  5-Estagiário");
        System.out.print("  Tipo: ");
        int tipo = lerInt();

        Funcionario f = criarTipo(tipo, proximoId, nome);
        if (f == null) { System.out.println("⚠  Tipo inválido!"); return; }

        funcionarios.add(f);
        System.out.println("✔  " + f.getTipo() + " " + nome + " criado com ID " + proximoId + "!");
        proximoId++;
    }

    Funcionario criarTipo(int tipo, int id, String nome) {
        switch (tipo) {
            case 1: return new Gerente(id, nome);
            case 2: return new Coordenador(id, nome);
            case 3: return new Analista(id, nome);
            case 4: return new Assistente(id, nome);
            case 5: return new Estagiario(id, nome);
            default: return null;
        }
    }

    // ── Registrar horários ─────────────────────────────────
    void registrarHoras() {
        if (funcionarios.isEmpty()) {
            System.out.println("\n⚠  Nenhum funcionário cadastrado!"); return;
        }

        System.out.print("\n  ID do funcionário: ");
        int id = lerInt();
        Funcionario f = buscarPorId(id);

        if (f == null) { System.out.println("⚠  Funcionário não encontrado!"); return; }

        if (!f.batePonto()) {
            System.out.println("⚠  " + f.getTipo() + " não bate ponto!"); return;
        }

        scanner.nextLine();

        // Data
        LocalDate data = lerData();
        if (data == null) return;

        // Entrada
        System.out.print("  Horário de entrada (HH:mm): ");
        LocalTime entrada = lerHora();
        if (entrada == null) return;

        if (entrada.isBefore(LocalTime.of(6, 0))) {
            System.out.println("⚠  Entrada não pode ser antes das 06:00!"); return;
        }

        // Saída
        System.out.print("  Horário de saída  (HH:mm): ");
        LocalTime saida = lerHora();
        if (saida == null) return;

        if (!saida.isAfter(entrada)) {
            System.out.println("⚠  Saída deve ser após a entrada!"); return;
        }
        if (saida.isAfter(LocalTime.of(22, 0))) {
            System.out.println("⚠  Saída não pode ser após as 22:00!"); return;
        }

        // Valida horas extras
        RegistroHoras reg = new RegistroHoras(data, entrada, saida);
        if (reg.getHorasExtras() > f.limiteHorasExtras()) {
            System.out.printf("⚠  Limite de horas extras excedido! Máximo: %dh | Registrado: %.1fh%n",
                f.limiteHorasExtras(), reg.getHorasExtras());
            return;
        }

        f.adicionarRegistro(reg);
        System.out.println("✔  Registro realizado com sucesso!");
        System.out.println(reg);
    }

    // ── Listar funcionários ────────────────────────────────
    void listarFuncionarios() {
        System.out.println("\n── Funcionários Cadastrados ──────────────");
        if (funcionarios.isEmpty()) {
            System.out.println("  Nenhum funcionário cadastrado."); return;
        }
        for (Funcionario f : funcionarios) System.out.println(f);
    }

    // ── Ver registros de um funcionário ───────────────────
    void verRegistros() {
        System.out.print("\n  ID do funcionário: ");
        int id = lerInt();
        Funcionario f = buscarPorId(id);

        if (f == null) { System.out.println("⚠  Funcionário não encontrado!"); return; }

        System.out.println("\n── Registros de " + f.getNome() + " ─────────────────");
        if (f.getRegistros().isEmpty()) {
            System.out.println("  Nenhum registro encontrado."); return;
        }
        for (RegistroHoras r : f.getRegistros()) System.out.println(r);
    }

    // ── Remover funcionário ────────────────────────────────
    void removerFuncionario() {
        System.out.print("\n  ID do funcionário para remover: ");
        int id = lerInt();
        Funcionario f = buscarPorId(id);

        if (f == null) { System.out.println("⚠  Funcionário não encontrado!"); return; }

        funcionarios.remove(f);
        System.out.println("✔  Funcionário " + f.getNome() + " removido!");
    }

    void mensagemSaida() {
        System.out.println("\n  Obrigado por usar o Portal de RH! Até logo! 👋");
    }

    // ── Helpers ────────────────────────────────────────────
    Funcionario buscarPorId(int id) {
        for (Funcionario f : funcionarios)
            if (f.getId() == id) return f;
        return null;
    }

    LocalDate lerData() {
        System.out.print("  Data do registro (dd/MM/yyyy): ");
        try {
            return LocalDate.parse(scanner.nextLine().trim(),
                                   DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            System.out.println("⚠  Data inválida! Use o formato dd/MM/yyyy.");
            return null;
        }
    }

    LocalTime lerHora() {
        try {
            return LocalTime.parse(scanner.nextLine().trim(),
                                   DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            System.out.println("⚠  Hora inválida! Use o formato HH:mm.");
            return null;
        }
    }

    int lerInt() {
        while (!scanner.hasNextInt()) {
            System.out.print("⚠  Digite um número válido: ");
            scanner.next();
        }
        return scanner.nextInt();
    }
}
