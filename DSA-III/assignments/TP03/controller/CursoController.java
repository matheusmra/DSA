package controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import model.Curso;
import repository.Busca;
import repository.Curso.ArquivoCurso;
import repository.ElementoLista;
import repository.ListaInvertida;
import repository.Inscricao.ArquivoCursoUsuario;

public class CursoController {

    private ArquivoCurso repository;
    private ArquivoCursoUsuario arqInscricoes;
    private ListaInvertida listaInvertida;
    private Busca busca;

    public CursoController() throws Exception {
        repository = new ArquivoCurso();
        arqInscricoes = new ArquivoCursoUsuario();
        listaInvertida = new ListaInvertida(
            4,
            "./dados/cursos/indiceInvertido.d.db",
            "./dados/cursos/indiceInvertido.b.db"
        );
        busca = new Busca(listaInvertida);
    }

    public void close() throws Exception {
        repository.close();
        arqInscricoes.close();
        if (listaInvertida != null) listaInvertida.close();
    }

    // CREATE
    public int cadastrar(Curso c) {
        try {
            int id = repository.create(c);
            if (id >= 0) {
                c.setId(id);
                indexarCurso(c);
            }
            return id;
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

    // BUSCA POR PALAVRAS-CHAVE (TFxIDF)
    public List<Curso> buscarPorPalavrasChave(String query) {
        try {
            List<Integer> ids = busca.pesquisar(query);
            List<Curso> cursos = new java.util.ArrayList<>();
            for (Integer id : ids) {
                Curso c = repository.read(id);
                if (c != null) {
                    cursos.add(c);
                }
            }
            return cursos;
        } catch (Exception e) {
            System.out.println("Erro ao buscar cursos por palavras-chave: " + e.getMessage());
            return new java.util.ArrayList<>();
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
            Curso existente = repository.read(c.getId());
            if (existente == null) {
                System.out.println("Erro: Curso não encontrado para atualizar.");
                return false;
            }

            if (existente.getNome().equals(c.getNome())) {
                return repository.update(c);
            }

            // Atualização de nome: remove os termos antigos e reindexa os novos
            desindexarCurso(existente);
            boolean atualizado = repository.update(c);
            if (atualizado) {
                indexarCurso(c);
                return true;
            } else {
                // Restaura a indexação antiga caso a atualização falhe
                indexarCurso(existente);
                return false;
            }
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

            // PASSO 2: Atualizar índice invertido antes de apagar o curso
            desindexarCurso(curso);

            // PASSO 3: Deletar o curso
            if (repository.delete(id)) {
                System.out.println("✓ Curso deletado com sucesso");
                return true;
            } else {
                // Restaura indexação caso o delete falhe
                indexarCurso(curso);
                System.out.println("✗ Erro ao deletar curso (após remover inscrições)");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Erro ao excluir curso: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void indexarCurso(Curso curso) throws Exception {
        List<String> termos = busca.removerStopWords(curso.getNome());
        HashMap<String, Integer> frequencias = new HashMap<String, Integer>();
        for (String termo : termos) {
            frequencias.put(termo, frequencias.getOrDefault(termo, 0) + 1);
        }

        int totalTermos = termos.isEmpty() ? 1 : termos.size();
        for (String termo : frequencias.keySet()) {
            listaInvertida.create(termo, new ElementoLista(curso.getId(), ((float) frequencias.get(termo)) / totalTermos));
        }
        listaInvertida.incrementaEntidades();
    }

    private void desindexarCurso(Curso curso) throws Exception {
        List<String> termos = busca.removerStopWords(curso.getNome());
        Set<String> termosUnicos = new HashSet<String>(termos);
        for (String termo : termosUnicos) {
            listaInvertida.delete(termo, curso.getId());
        }
        listaInvertida.decrementaEntidades();
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
            System.err.println("Erro ao listar todos os cursos: " + e.getMessage());
            e.printStackTrace();
            return new java.util.ArrayList<>();
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