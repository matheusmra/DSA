package controller;

import java.util.List;
import model.Curso;
import repository.Curso.ArquivoCurso;
import repository.Inscricao.ArquivoCursoUsuario;

public class CursoController {

    private ArquivoCurso repository;
    private ArquivoCursoUsuario arqInscricoes;

    public CursoController() throws Exception {
        repository = new ArquivoCurso();
        arqInscricoes = new ArquivoCursoUsuario();
    }

    public void close() throws Exception {
        repository.close();
        arqInscricoes.close();
    }

    // CREATE
    public int cadastrar(Curso c) {
        try {
            return repository.create(c);
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar curso");
            return -1;
        }
    }

    // READ (por ID)
    public Curso buscar(int id) {
        try {
            return repository.read(id);
        } catch (Exception e) {
            System.out.println("Erro ao buscar curso");
            return null;
        }
    }

    // READ (por codigoCompartilhavel)
    public Curso buscarPorCodigo(String codigo) {
        try {
            return repository.buscarPorCodigo(codigo);
        } catch (Exception e) {
            System.out.println("Erro ao buscar curso por código");
            return null;
        }
    }

    // ✅ LISTAR POR USUÁRIO (COM ÁRVORE B+)
    public List<Curso> listarPorUsuario(int usuarioId) {
        try {
            return repository.listarPorUsuario(usuarioId);
        } catch (Exception e) {
            System.out.println("Erro ao listar cursos do usuário");
            return null;
        }
    }

    // UPDATE
    public boolean atualizar(Curso c) {
        try {
            return repository.update(c);
        } catch (Exception e) {
            System.out.println("Erro ao atualizar curso");
            return false;
        }
    }

    // DELETE - COM INTEGRIDADE REFERENCIAL
    public boolean excluir(int id) {
        try {
            Curso curso = repository.read(id);
            if (curso == null) {
                System.out.println("Erro: Curso não encontrado.");
                return false;
            }

            // PASSO 1: Remover TODAS as inscrições associadas ao curso
            int inscricoesRemovidas = arqInscricoes.deletarTodasInscricoesCurso(id);
            System.out.println("✓ Integridade referencial: " + inscricoesRemovidas + " inscrição(ões) removida(s)");

            // PASSO 2: Deletar o curso
            if (repository.delete(id)) {
                System.out.println("✓ Curso deletado com sucesso");
                return true;
            } else {
                System.out.println("✗ Erro ao deletar curso (após remover inscrições)");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Erro ao excluir curso: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Retorna true se o usuário tiver cursos ativos (estado 0 ou 1)
    public boolean temCursosAtivos(int usuarioId) {
        try {
            return repository.temCursosAtivos(usuarioId);
        } catch (Exception e) {
            System.out.println("Erro ao verificar cursos ativos");
            return false;
        }
    }

    // Exclui todos os cursos inativos do usuário (estado 2 ou 3)
    public void excluirCursosInativos(int usuarioId) {
        try {
            repository.deletarCursosInativos(usuarioId);
        } catch (Exception e) {
            System.out.println("Erro ao excluir cursos inativos");
        }
    }

    // ✅ LISTAR TODOS OS CURSOS (COM ORDENAÇÃO POR DATA)
    public List<Curso> listarTodos() {
        try {
            return repository.listarTodos();
        } catch (Exception e) {
            System.out.println("Erro ao listar todos os cursos");
            return null;
        }
    }

    /**
     * Cancela todas as inscrições de um curso quando seu estado muda para inativo.
     * Operação de integridade referencial para quando um curso é cancelado (estado 2 ou 3).
     * @param idCurso ID do curso
     * @return Número de inscrições canceladas
     */
    public int cancelarInscricoesCurso(int idCurso) {
        try {
            int removidas = arqInscricoes.deletarTodasInscricoesCurso(idCurso);
            System.out.println("✓ Cancelamento de inscrições: " + removidas + " inscrição(ões) removida(s)");
            return removidas;
        } catch (Exception e) {
            System.err.println("✗ Erro ao cancelar inscrições do curso: " + e.getMessage());
            return -1;
        }
    }
}