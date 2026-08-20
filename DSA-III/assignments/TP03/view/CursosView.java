package view;

import controller.InscricaoController;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import model.Curso;
import model.Usuario;

public class CursosView {

    private final Scanner console;

    public static class DadosNovoCurso {
        public String nome;
        public String dataInicioCurso;
        public String descricao;
    }

    public CursosView(Scanner console) {
        this.console = console;
    }

    public void mostrarMensagem(String msg) {
        System.out.println(msg);
    }

    public String lerOpcaoMenuCursos(Usuario usuario, List<Curso> cursos) {

        System.out.println("\nEntrePares 1.0");
        System.out.println("--------------");
        System.out.println("> Início > Meus cursos");

        System.out.println("\nCURSOS");

        int index = 1;
        for (Curso curso : cursos) {
            System.out.println("(" + index + ") " + curso.getNome() + " - " + curso.getDataInicioCurso());
            index++;
        }

        System.out.println("\n(A) Novo curso");
        System.out.println("(R) Retornar ao menu anterior");
        System.out.print("\nOpção: ");

        if (!console.hasNextLine()) {
            return "R";
        }

        String opcao = console.nextLine().trim().toUpperCase();

        if (opcao.matches("\\d+")) {
            int num = Integer.parseInt(opcao);
            if (num >= 1 && num <= cursos.size()) {
                return opcao;
            } else {
                System.out.println("Opção inválida.");
                return "";
            }
        }

        if (opcao.equals("A") || opcao.equals("R")) {
            return opcao;
        }

        System.out.println("Opção inválida.");
        return "";
    }

    public DadosNovoCurso lerDadosNovoCurso() {
        DadosNovoCurso dados = new DadosNovoCurso();

        System.out.println("\nEntrePares 1.0");
        System.out.println("--------------");
        System.out.println("> Início > Meus cursos > Novo curso");

        System.out.print("\nNome do curso: ");
        dados.nome = console.nextLine().trim();

        System.out.print("Data de início (dd/mm/aaaa): ");
        dados.dataInicioCurso = console.nextLine().trim();

        System.out.print("Descrição: ");
        dados.descricao = console.nextLine().trim();

        return dados;
    }

    public String lerOpcaoListaInscritos(Curso curso, List<InscricaoController.InscritoDados> inscritos) {
        System.out.println("\nEntrePares 1.0");
        System.out.println("--------------");
        System.out.println("> Início > Meus cursos > " + curso.getNome() + " > Inscrições");

        if (inscritos.isEmpty()) {
            System.out.println("\nNenhum inscrito neste curso.");
        } else {
            System.out.println("\nINSCRITOS");
            int index = 1;
            for (InscricaoController.InscritoDados inscrito : inscritos) {
                System.out.println("(" + index + ") " + inscrito.usuario.getNome() + " (" + inscrito.dataInscricao + ")");
                index++;
            }
        }

        System.out.println("\n(A) Exportar lista");
        System.out.println("(R) Retornar ao menu anterior");
        System.out.print("\nOpção: ");

        if (!console.hasNextLine()) {
            return "R";
        }

        String opcao = console.nextLine().trim().toUpperCase();

        if (opcao.matches("\\d+")) {
            int num = Integer.parseInt(opcao);
            if (num >= 1 && num <= inscritos.size()) {
                return opcao;
            } else {
                System.out.println("Opção inválida.");
                return "";
            }
        }

        if (opcao.equals("A") || opcao.equals("R")) {
            return opcao;
        }

        System.out.println("Opção inválida.");
        return "";
    }

    public String mostrarDetalheInscrito(Usuario usuario, String dataInscricao) {
        System.out.println("\nEntrePares 1.0");
        System.out.println("--------------");
        System.out.println("> Inscrição");

        System.out.println("\nDADOS DO INSCRITO");
        System.out.println("Nome: " + usuario.getNome());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Data de Inscrição: " + dataInscricao);

        System.out.println("\n(A) Cancelar inscrição");
        System.out.println("(R) Retornar ao menu anterior");
        System.out.print("\nOpção: ");

        if (!console.hasNextLine()) {
            return "R";
        }

        String opcao = console.nextLine().trim().toUpperCase();

        if (opcao.equals("A") || opcao.equals("R")) {
            return opcao;
        }

        System.out.println("Opção inválida.");
        return "";
    }

    public String lerNomeArquivoExportacao() {
        System.out.print("Nome do arquivo CSV (sem extensão): ");
        if (!console.hasNextLine()) {
            return "";
        }
        return console.nextLine().trim();
    }

    public boolean exportarListaParaCSV(List<InscricaoController.InscritoDados> inscritos, String nomeArquivo) {
        if (nomeArquivo.isEmpty()) {
            System.out.println("Nome de arquivo inválido.");
            return false;
        }

        String caminhoArquivo = nomeArquivo + ".csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            // Escrever cabeçalho
            writer.write("nome,email");
            writer.newLine();

            // Escrever dados dos inscritos
            for (InscricaoController.InscritoDados inscrito : inscritos) {
                String linha = escaparCSV(inscrito.usuario.getNome()) + "," + 
                               escaparCSV(inscrito.usuario.getEmail());
                writer.write(linha);
                writer.newLine();
            }

            System.out.println("Arquivo exportado com sucesso: " + caminhoArquivo);
            return true;

        } catch (IOException e) {
            System.out.println("Erro ao exportar arquivo: " + e.getMessage());
            return false;
        }
    }

    private String escaparCSV(String valor) {
        if (valor == null) {
            return "";
        }
        // Se contém vírgula, aspas ou quebra de linha, envolver em aspas
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}