package view;

import java.util.List;
import java.util.Scanner;
import model.Curso;

public class InscricoesView {

    private final Scanner console;
    private int paginaAtual = 1;
    private int totalPaginas = 1;
    private List<Curso> todosCursos;

    public InscricoesView(Scanner console) {
        this.console = console;
    }

    public String lerOpcaoMenuInscricoes(List<Curso> inscricoes) {
        System.out.println("\nEntrePares 1.0");
        System.out.println("--------------");
        System.out.println("> Início > Minhas inscrições\n");
        System.out.println("INSCRIÇÕES");

        if (inscricoes == null || inscricoes.isEmpty()) {
            System.out.println("Você ainda não possui inscrições.");
        } else {
            for (int i = 0; i < inscricoes.size(); i++) {
                Curso c = inscricoes.get(i);
                // estado 0 = aberto, 1 = inscrições encerradas, 2 = concluído, 3 = cancelado
                String status;
                switch (c.getEstado()) {
                    case 1:  status = " (INSCRIÇÕES ENCERRADAS)"; break;
                    case 2:  status = " (CONCLUÍDO)";            break;
                    case 3:  status = " (CANCELADO)";            break;
                    default: status = "";                        break;
                }
                System.out.printf("(%d) %s - %s%s\n", (i + 1), c.getNome(), c.getDataInicioCurso(), status);
            }
        }

        System.out.println("\n(A) Buscar curso por código");
        System.out.println("(B) Buscar curso por palavras-chave");
        System.out.println("(C) Listar todos os cursos\n");
        System.out.println("(R) Retornar ao menu anterior\n");
        System.out.print("Opção: ");

        return console.nextLine().trim().toUpperCase();
    }

    public void mostrarMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public String lerCodigo() {
        System.out.print("\nDigite o código do curso: ");
        return console.nextLine().trim();
    }

    public String lerCodigoCurso() {
        System.out.print("\nDigite o código do curso: ");
        return console.nextLine().trim();
    }

    public boolean confirmarCancelamento() {
        System.out.print("Cancelar minha inscrição neste curso? Confirmar (S/N): ");
        return console.nextLine().trim().equalsIgnoreCase("S");
    }

    public String mostrarDetalheCursoParaInscricao(Curso curso, String nomeAutor) {
        System.out.println("\nEntrePares 1.0");
        System.out.println("--------------");
        System.out.println("> Início > Minhas inscrições > " + curso.getNome());

        System.out.println("\nCÓDIGO........: " + curso.getCodigoCompartilhavel());
        System.out.println("CURSO.........: " + curso.getNome());
        System.out.println("AUTOR.........: " + nomeAutor);
        System.out.println("DESCRIÇÃO.....: " + curso.getDescricao());
        System.out.println("DATA DE INÍCIO: " + curso.getDataInicioCurso());

        System.out.println();
        System.out.println("(A) Fazer minha inscrição no curso");
        System.out.println("\n(R) Retornar ao menu anterior");
        System.out.print("\nOpção: ");

        return console.nextLine().trim().toUpperCase();
    }

    public String mostrarDetalheCursoInscrito(Curso curso, String nomeAutor) {
        System.out.println("\nEntrePares 1.0");
        System.out.println("--------------");
        System.out.println("> Início > Minhas inscrições > " + curso.getNome());

        System.out.println("\nCÓDIGO........: " + curso.getCodigoCompartilhavel());
        System.out.println("CURSO.........: " + curso.getNome());
        System.out.println("AUTOR.........: " + nomeAutor);
        System.out.println("DESCRIÇÃO.....: " + curso.getDescricao());
        System.out.println("DATA DE INÍCIO: " + curso.getDataInicioCurso());

        // Exibe o estado do curso
        String status;
        switch (curso.getEstado()) {
            case 1:  status = "Este curso está com inscrições encerradas."; break;
            case 2:  status = "Este curso foi concluído."; break;
            case 3:  status = "Este curso foi cancelado."; break;
            default: status = "Este curso está aberto para inscrições."; break;
        }
        System.out.println("\n" + status);

        System.out.println("\n(A) Cancelar minha inscrição no curso");
        System.out.println("\n(R) Retornar ao menu anterior");
        System.out.print("\nOpção: ");

        return console.nextLine().trim().toUpperCase();
    }

    public String mostrarListaCursos(List<Curso> cursos, int pagina, int totalPaginas) {
        System.out.println("\nEntrePares 1.0");
        System.out.println("--------------");
        System.out.println("> Início > Minhas inscrições > Lista de cursos\n");
        System.out.printf("Página %d de %d\n\n", pagina, totalPaginas);

        // Calcula os índices de início e fim da página
        int inicio = (pagina - 1) * 10;
        int fim = Math.min(inicio + 10, cursos.size());

        // Exibe os cursos da página atual
        for (int i = inicio; i < fim; i++) {
            Curso c = cursos.get(i);
            int indiceExibicao = (i - inicio + 1) % 10; // 1-9 e 0 para o 10º item
            System.out.printf("(%d) %s - %s\n", indiceExibicao, c.getNome(), c.getDataInicioCurso());
        }

        System.out.println();

        // Opções de navegação
        if (pagina > 1) {
            System.out.println("(A) Página anterior");
        }
        if (pagina < totalPaginas) {
            System.out.println("(B) Próxima página");
        }

        System.out.println("\n(R) Retornar ao menu anterior");
        System.out.print("\nOpção: ");

        return console.nextLine().trim().toUpperCase();
    }

    // Exibe a lista de todos os cursos com paginação (10 itens por página)
    public String lerOpcaoMenuListaTodosCursos(List<Curso> cursos) {
        this.todosCursos = cursos;
        this.totalPaginas = (cursos.size() + 9) / 10; // Arredonda para cima
        if (this.totalPaginas == 0) this.totalPaginas = 1;

        System.out.println("\nEntrePares 1.0");
        System.out.println("--------------");
        System.out.println("> Início > Minhas inscrições > Lista de cursos\n");
        System.out.printf("Página %d de %d\n\n", paginaAtual, totalPaginas);

        // Calcula os índices de início e fim da página
        int inicio = (paginaAtual - 1) * 10;
        int fim = Math.min(inicio + 10, cursos.size());

        // Exibe os cursos da página atual
        for (int i = inicio; i < fim; i++) {
            Curso c = cursos.get(i);
            int indiceExibicao = (i - inicio + 1) % 10; // 1-9 e 0 para o 10º item
            System.out.printf("(%d) %s - %s\n", indiceExibicao, c.getNome(), c.getDataInicioCurso());
        }

        System.out.println();

        // Opções de navegação
        if (paginaAtual > 1) {
            System.out.println("(A) Página anterior");
        }
        if (paginaAtual < totalPaginas) {
            System.out.println("(B) Próxima página");
        }

        System.out.println("\n(R) Retornar ao menu anterior");
        System.out.print("\nOpção: ");

        return console.nextLine().trim().toUpperCase();
    }

    // Retorna o curso selecionado pela opção (1-9 e 0 para o 10º)
    public Curso obterCursoSelecionado(String opcao) {
        try {
            int indice = Integer.parseInt(opcao);
            int posicaoGlobal = (paginaAtual - 1) * 10 + (indice == 0 ? 9 : indice - 1);
            
            if (posicaoGlobal >= 0 && posicaoGlobal < todosCursos.size()) {
                return todosCursos.get(posicaoGlobal);
            }
        } catch (NumberFormatException e) {
            // Ignorado
        }
        return null;
    }

    // Valida se a opção de seleção é válida
    public boolean opcaoValida(String opcao) {
        try {
            int indice = Integer.parseInt(opcao);
            if (indice < 0 || indice > 9) return false;
            
            int posicaoGlobal = (paginaAtual - 1) * 10 + (indice == 0 ? 9 : indice - 1);
            return posicaoGlobal < todosCursos.size();
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Avança para a próxima página
    public void irProxima() {
        if (paginaAtual < totalPaginas) {
            paginaAtual++;
        }
    }

    // Volta para a página anterior
    public void irAnterior() {
        if (paginaAtual > 1) {
            paginaAtual--;
        }
    }

    // Reseta a paginação para a primeira página
    public void resetarPaginacao() {
        paginaAtual = 1;
    }
}