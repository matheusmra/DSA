package controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import model.Usuario;
import repository.Inscricao.ArquivoCursoUsuario;
import repository.Usuario.ArquivoUsuario;

public class UsuarioController {

    private final ArquivoUsuario repository;
    private final ArquivoCursoUsuario arqInscricoes;

    public UsuarioController() throws Exception {
        this.repository = new ArquivoUsuario();
        this.arqInscricoes = new ArquivoCursoUsuario();
    }

    public void close() throws Exception {
        repository.close();
        arqInscricoes.close();
    }

    public Usuario buscarPorEmail(String email) {
        return this.repository.buscarPorEmail(email);
    }

    public Usuario buscarPorId(int id) {
        try {
            return this.repository.read(id);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean login(String email, String senha){
        Usuario usuario = this.repository.buscarPorEmail(email);
        if (usuario == null) {
            return false;
        }

        if (!usuario.hashSenha.equals(toMd5(senha))) {
            return false;
        }

        return true;
    }

    public int cadastrar(Usuario u){
        int id = -1;
        try{
            u.setHashSenha(toMd5(u.getHashSenha()));
            u.RespostaSecreta = toMd5(u.getRespostaSecreta().toLowerCase().trim());
            id = this.repository.create(u);
        }
        catch(Exception e){
            e.printStackTrace();;
        }
        return id;
    }

    public boolean atualizarDados(String emailOriginal, String novoNome, String novoEmail, String novaPergunta, String novaResposta) {
        try {
            Usuario usuario = repository.buscarPorEmail(emailOriginal);
            if (usuario == null) {
                return false;
            }

            // Se estiver alterando o email, verifica se já existe outro usuário com o novo email
            if (!novoEmail.equals(emailOriginal)) {
                Usuario existente = repository.buscarPorEmail(novoEmail);
                if (existente != null && existente.getId() != usuario.getId()) {
                    return false;
                }
            }

            usuario.nome = novoNome;
            usuario.email = novoEmail;
            // mantem o hash da senha
            usuario.PerguntaSecreta = novaPergunta;
            if (novaResposta != null && !novaResposta.trim().isEmpty()) {
                usuario.RespostaSecreta = toMd5(novaResposta.toLowerCase().trim());
            }

            return repository.update(usuario);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean redefinirSenha(String email, String novaSenha) {
        try {
            Usuario usuario = repository.buscarPorEmail(email);
            if (usuario == null) {
                return false;
            }

            usuario.setHashSenha(toMd5(novaSenha));
            return repository.update(usuario);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletarPorEmail(String email) {
        try {
            Usuario usuario = repository.buscarPorEmail(email);
            if (usuario == null) {
                System.out.println("Erro: Usuário não encontrado.");
                return false;
            }

            // PASSO 1: Remover TODAS as inscrições associadas ao usuário
            int inscricoesRemovidas = arqInscricoes.deletarTodasInscricoesUsuario(usuario.getId());
            System.out.println("✓ Integridade referencial: " + inscricoesRemovidas + " inscrição(ões) removida(s)");

            // PASSO 2: Deletar o usuário
            if (repository.delete(email)) {
                System.out.println("✓ Usuário deletado com sucesso");
                return true;
            } else {
                System.out.println("✗ Erro ao deletar usuário (após remover inscrições)");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Erro ao deletar usuário: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String toMd5(final String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(senha.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash MD5", e);
        }
    }

    /**
     * Deleta um usuário por ID com integridade referencial.
     * Usado internamente quando precisar deletar por ID em vez de email.
     * @param idUsuario ID do usuário a deletar
     * @return true se deletado com sucesso, false caso contrário
     */
    public boolean deletarPorId(int idUsuario) {
        try {
            Usuario usuario = repository.read(idUsuario);
            if (usuario == null) {
                System.out.println("Erro: Usuário não encontrado.");
                return false;
            }

            // PASSO 1: Remover TODAS as inscrições associadas ao usuário
            int inscricoesRemovidas = arqInscricoes.deletarTodasInscricoesUsuario(idUsuario);
            System.out.println("✓ Integridade referencial: " + inscricoesRemovidas + " inscrição(ões) removida(s)");

            // PASSO 2: Deletar o usuário
            if (repository.delete(idUsuario)) {
                System.out.println("✓ Usuário deletado com sucesso");
                return true;
            } else {
                System.out.println("✗ Erro ao deletar usuário (após remover inscrições)");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Erro ao deletar usuário: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
