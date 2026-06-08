import controller.CursoController;
import controller.UsuarioController;
import model.Curso;
import model.Usuario;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourseSeeder {
    public static void main(String[] args) {
        try {
            UsuarioController usuarioController = new UsuarioController();
            CursoController cursoController = new CursoController();

            String autorEmail = "teste@teste.com";
            Usuario autor = usuarioController.buscarPorEmail(autorEmail);
            if (autor == null) {
                autor = new Usuario(-1, "Teste Autor", autorEmail, "senha", "pergunta", "resposta");
                int usuarioId = usuarioController.cadastrar(autor);
                if (usuarioId < 0) {
                    System.err.println("Erro ao cadastrar autor de teste.");
                    return;
                }
                autor.setId(usuarioId);
                System.out.println("Usuário autor criado: " + autorEmail + " (ID=" + usuarioId + ")");
            } else {
                System.out.println("Usuário autor já existe: " + autorEmail + " (ID=" + autor.getId() + ")");
            }

            List<Curso> cursos = Arrays.asList(
                new Curso(-1, "Introdução ao marketing digital", "14/02/2026", "somente um teste", 0, autor.getId()),
                new Curso(-1, "Formação básica em Excel", "18/02/2026", "somente um teste", 0, autor.getId()),
                new Curso(-1, "DevOps", "02/03/2026", "somente um teste", 0, autor.getId()),
                new Curso(-1, "Oratória", "10/03/2026", "somente um teste", 0, autor.getId()),
                new Curso(-1, "Inglês para negócios", "14/03/2026", "somente um teste", 0, autor.getId()),
                new Curso(-1, "Power BI", "17/03/2026", "somente um teste", 0, autor.getId())
            );

            List<String> existentes = new ArrayList<>();
            for (Curso c : cursoController.listarTodos()) {
                existentes.add(c.getNome() + "|" + c.getDataInicioCurso());
            }

            int criados = 0;
            for (Curso curso : cursos) {
                String chave = curso.getNome() + "|" + curso.getDataInicioCurso();
                if (existentes.contains(chave)) {
                    System.out.println("Curso já cadastrado, pulando: " + curso.getNome() + " (" + curso.getDataInicioCurso() + ")");
                    continue;
                }
                int id = cursoController.cadastrar(curso);
                if (id >= 0) {
                    System.out.println("Curso cadastrado: " + curso.getNome() + " (ID=" + id + ")");
                    criados++;
                } else {
                    System.err.println("Falha ao cadastrar curso: " + curso.getNome());
                }
            }

            System.out.println("Cursos criados: " + criados);

            cursoController.close();
            usuarioController.close();
        } catch (Exception e) {
            System.err.println("Erro ao inserir cursos de teste: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
