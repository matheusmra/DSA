# TP2 — Relacionamento N:N
**Trabalho Prático — Algoritmos e Estruturas de Dados III**

---

## Participantes

- Felipe Henrique Oliveira Diniz
- Matheus de Almeida Moreira

---

## Descrição do Sistema

O sistema, chamado **EntrePares 1.0**, é uma aplicação de linha de comando para gerenciamento de usuários e cursos com persistência em arquivos binários. Foi desenvolvido em Java seguindo o padrão arquitetural **MVC (Model-View-Controller)** e implementa as estruturas de dados de indexação **Tabela Hash Extensível** e **Árvore B+** para garantir acesso eficiente aos registros em disco.

### Funcionalidades Principais

- **Cadastro e autenticação de usuários**: o usuário se registra informando nome, e-mail, senha, pergunta e resposta secreta. A senha e a resposta secreta são armazenadas como hashes MD5.
- **Gerenciamento do perfil**: após o login, o usuário pode alterar seus dados (nome, e-mail, pergunta/resposta secreta) ou deletar a conta.
- **CRUD de cursos**: cada usuário autenticado pode criar, visualizar, editar, encerrar, concluir ou cancelar seus cursos.
- **Relacionamento 1:N**: um usuário possui zero ou mais cursos; o vínculo é mantido por uma Árvore B+ de relacionamento.
- **Código compartilhável**: ao criar um curso, um código único de 10 caracteres (NanoID) é gerado automaticamente.
- **Estado do curso**: cada curso pode estar em um dos quatro estados: *Aberto* (0), *Inscrições encerradas* (1), *Concluído* (2) ou *Cancelado* (3).
- **Proteção na exclusão de conta**: a conta só pode ser deletada se o usuário não possuir cursos ativos (estado 0 ou 1). Cursos inativos (estado 2 ou 3) são removidos automaticamente antes da exclusão.
- **Inscrições em cursos (N:N)**: usuários podem se inscrever em cursos de outros usuários (mas não nos próprios). O relacionamento N:N é mantido por duas Árvores B+ independentes.
- **Menu "Minhas Inscrições"**: lista os cursos em que o usuário está inscrito, com indicação de status (inscrições encerradas, concluído, cancelado).
- **Busca de cursos por código NanoID**: qualquer curso pode ser localizado pelo seu código compartilhável de 10 caracteres.
- **Listagem paginada de cursos**: todos os cursos do sistema são listados em ordem cronológica de início, com paginação de 10 itens por página.
- **Gestão de inscritos (proponente)**: o autor do curso pode visualizar dados dos inscritos, cancelar inscrições individualmente e exportar a lista para CSV.
- **Integridade referencial N:N**: inscrições são removidas automaticamente quando um curso ou usuário é excluído.

---

## Fluxo de Navegação

```
Tela Inicial
├── (A) Realizar Login
│     └── Menu Logado
│           ├── (A) Meus Dados
│           │     ├── (A) Alterar dados
│           │     └── (B) Deletar conta
│           ├── (B) Meus Cursos
│           │     ├── (A) Criar novo curso
│           │     └── (N) Detalhe do curso selecionado
│           │           ├── (A) Gerenciar inscritos
│           │           │     ├── (N) Detalhe do inscrito (nome, e-mail, data)
│           │           │     │     └── (A) Cancelar inscrição do usuário
│           │           │     └── (A) Exportar lista para CSV
│           │           ├── (B) Editar curso
│           │           ├── (C) Encerrar inscrições
│           │           ├── (D) Concluir curso
│           │           └── (E) Cancelar curso
│           └── (C) Minhas Inscrições
│                 ├── (A) Buscar curso por código (NanoID)
│                 │     └── Detalhe do curso → (A) Inscrever-se
│                 ├── (B) Buscar por palavras-chave [TP3]
│                 ├── (C) Listar todos os cursos (paginado, 10/pág.)
│                 │     └── Detalhe do curso → (A) Inscrever-se
│                 └── (N) Detalhe de curso inscrito
│                       └── (A) Cancelar minha inscrição
└── (B) Cadastrar novo usuário
```

---

## Telas do Sistema

### Tela inicial — Login, Cadastro e Recuperação de senha

<img width="430" height="322" alt="{06A390BE-ACEC-4D4D-BFFE-F45D92378C68}" src="https://github.com/user-attachments/assets/ad49f99b-de8e-464b-b2fa-018acaaa3c71" />



### Tela de cadastro de usuário

<img width="1296" height="625" alt="{561545EA-76C1-44EC-A74E-D9969DFBD836}" src="https://github.com/user-attachments/assets/f1181064-e6b1-409f-83ff-bba7628b5745" />


### Tela de login

<img width="1112" height="534" alt="{5F6EE82F-F9BD-4ACF-A378-F5CA6B6B43DB}" src="https://github.com/user-attachments/assets/f7514727-fce4-4b36-856b-d429a4ae29b9" />

### Tela redefinir senha

<img width="784" height="644" alt="{FCCFF724-9CB6-4874-AF6E-1F2793F12170}" src="https://github.com/user-attachments/assets/52adf48f-8dd9-44bb-89fd-1a57b7d97656" />

### Menu principal (pós-login)

<img width="1203" height="448" alt="{9CE1DB99-82DB-4DF9-A976-2525AA8CC099}" src="https://github.com/user-attachments/assets/9757093f-75a3-4971-ac1b-c760043b0d29" />


### Meus Dados

<img width="701" height="509" alt="{994AAAB9-0E32-4589-8B87-190EC36ED360}" src="https://github.com/user-attachments/assets/2df9a9dd-12a4-4724-bbfa-9181c865c707" />


### Meus Cursos — lista

<img width="573" height="377" alt="{F5111B8D-B83E-4CCC-9368-F23463543135}" src="https://github.com/user-attachments/assets/da980dca-7e6d-4cf6-b38b-c1627990c816" />


### Meus Cursos — criar novo curso

<img width="671" height="313" alt="{E194C4CC-E062-4B7E-A1A6-31EA8864AF11}" src="https://github.com/user-attachments/assets/d984a919-f4e6-406c-bbfe-943ffa2f14da" />


### Detalhe do curso

<img width="810" height="650" alt="{02A4F57C-EF8A-434C-9612-1890346C8C86}" src="https://github.com/user-attachments/assets/2c5b4105-f9b8-41d1-9ae3-58dfcf280f4b" />

### Gerenciar inscritos no curso

<img width="742" height="388" alt="{3EC37E3F-384C-46A2-87B7-2F61F64FC111}" src="https://github.com/user-attachments/assets/686c71d7-0a88-4b5b-9301-edb681273c31" />


### Detalhe de um inscrito

<img width="530" height="458" alt="{9014407A-2E82-43A9-BFD8-2220AED5B129}" src="https://github.com/user-attachments/assets/6584e12a-7f6d-4443-9bd7-eaf1b47dcb3c" />


### Exportação para CSV

<img width="741" height="484" alt="{93A4F7A2-C64C-40BA-883B-6E956A5EB7FB}" src="https://github.com/user-attachments/assets/1250a7f1-f172-4672-8e1a-4c80c4d29cd2" />


### Menu Minhas Inscrições

<img width="582" height="517" alt="{02E04220-ABA4-4630-AFEF-C6DFD7BC2473}" src="https://github.com/user-attachments/assets/37d39873-286a-413f-9058-f1ab5bfee1d7" />


### Busca de curso por código NanoID

<img width="723" height="585" alt="{9C8A96B0-EDF6-4B74-BBAA-1E1DEF7D8855}" src="https://github.com/user-attachments/assets/e9d6414e-0f45-41c7-8c5f-6618b4f281d2" />


### Listagem paginada de todos os cursos

<img width="747" height="712" alt="{96E0AECA-BE66-480D-A2B4-1A15E6FA1608}" src="https://github.com/user-attachments/assets/f8bcd954-3c32-41d4-b52f-da4e59d1fb9d" />


### Detalhe do curso inscrito — cancelamento

<img width="890" height="619" alt="{61B2E350-9BDD-49BB-9F87-7E6B99136ED0}" src="https://github.com/user-attachments/assets/667057da-bc06-480c-95f2-3d1622c98487" />

---

## Classes Criadas

### `model/`

| Classe | Descrição |
|--------|-----------|
| `Usuario` | Entidade usuário com campos `id`, `nome`, `email`, `hashSenha`, `PerguntaSecreta`, `RespostaSecreta`. Implementa serialização binária via `toByteArray()` / `fromByteArray()`. |
| `Curso` | Entidade curso com campos `id`, `nome`, `dataInicioCurso`, `descricao`, `codigoCompartilhavel`, `estado` e `usuarioId` (chave estrangeira). O código compartilhável é gerado automaticamente na construção. |
| `NanoID` | Utilitário que gera strings alfanuméricas seguras usando `SecureRandom`. |
| `CursoUsuario` | Entidade de associação N:N com campos `id`, `idCurso`, `idUsuario` e `dataInscricao`. Implementa serialização binária. |

### `repository/`

| Classe | Descrição |
|--------|-----------|
| `Registro` | Interface contrato para entidades persistíveis: `setId`, `getId`, `toByteArray`, `fromByteArray`. |
| `RegistroHashExtensivel<T>` | Interface contrato para elementos armazenáveis na Hash Extensível. |
| `RegistroArvoreBMais<T>` | Interface contrato para elementos armazenáveis na Árvore B+. |
| `Arquivo<T>` | Motor de armazenamento binário genérico. Arquivos com cabeçalho de 12 bytes (último ID + cabeça da lista de espaços livres). Cada registro tem lápide (1 byte) + comprimento (2 bytes) + payload. Mantém um `HashExtensivel<ParIDEndereco>` como índice direto ID → endereço em disco. |
| `HashExtensivel<T>` | Implementação completa de **Hashing Extensível** persistida em dois arquivos: diretório (`.d.db`) e baldes (`.c.db`). Suporta split de baldes e duplicação de diretório. |
| `ArvoreBMais<T>` | Implementação completa de **Árvore B+** em disco. Folhas encadeadas para varredura sequencial. Suporta chaves duplicadas distinguidas por um segundo campo (`num2`). |
| `ParIDEndereco` | Par `(int id, long endereço)` — entrada do índice direto de todos os arquivos. |

### `repository/Usuario/`

| Classe | Descrição |
|--------|-----------|
| `ArquivoUsuario` | Estende `Arquivo<Usuario>`. Adiciona índice indireto `HashExtensivel<ParEmailID>` para busca por e-mail. Sobrescreve `create`, `delete` e `update` para manter o índice de e-mail sincronizado. |
| `ParEmailID` | Par `(String email, int id)` — entrada do índice indireto de e-mail. Tamanho fixo de 44 bytes. |

### `repository/Curso/`

| Classe | Descrição |
|--------|-----------|
| `ArquivoCurso` | Estende `Arquivo<Curso>`. Adiciona índice de relacionamento `ArvoreBMais<ParIntInt>` para o vínculo 1:N usuário-curso e `HashExtensivel<ParCodigoID>` para busca por NanoID. Sobrescreve `create` e `delete` para manter os índices sincronizados. Implementa `listarPorUsuario`, `buscarPorCodigo`, `temCursosAtivos`, `deletarCursosInativos` e `listarTodos`. |
| `ParIntInt` | Par `(int num1, int num2)` representando `(idX, idY)` — entrada genérica das Árvores B+. Tamanho fixo de 8 bytes. Reutilizado em `ArquivoCurso` e `ArquivoCursoUsuario`. |
| `ParCodigoID` | Par `(String codigo, int id)` — entrada do índice indireto NanoID → ID do curso. |

### `repository/Inscricao/`

| Classe | Descrição |
|--------|-----------|
| `ArquivoCursoUsuario` | Estende `Arquivo<CursoUsuario>`. Implementa o relacionamento N:N com dois índices `ArvoreBMais<ParIntInt>`: `indiceCursoUsuario` `(idCurso, idInscricao)` e `indiceUsuarioCurso` `(idUsuario, idInscricao)`. Todos os métodos CRUD mantêm os dois índices sincronizados. Expõe `listarPorCurso`, `listarPorUsuario`, `buscar`, `deletarTodasInscricoesCurso` e `deletarTodasInscricoesUsuario`. |

### `controller/`

| Classe | Descrição |
|--------|-----------|
| `UsuarioController` | Camada de serviço para usuários. Encapsula o repositório, aplica hashing MD5 na senha e na resposta secreta, e expõe operações de login, cadastro, atualização, exclusão e busca por ID (`buscarPorId`). Ao deletar um usuário, remove automaticamente todas as suas inscrições. |
| `CursoController` | Camada de serviço para cursos. Delega ao repositório e expõe operações CRUD mais `listarTodos`, `buscarPorCodigo`, `temCursosAtivos`, `excluirCursosInativos` e `cancelarInscricoesCurso` (integridade referencial). |
| `InscricaoController` | Camada de serviço para inscrições. Aplica regras de negócio: estado do curso deve ser *Aberto*, usuário não pode se inscrever no próprio curso, duplicatas são rejeitadas. Expõe `inscrever`, `cancelarPorUsuario`, `cancelarPorProponente`, `listarCursosDoUsuario` e `listarInscritosComDados`. Usa as Árvores B+ via `ArquivoCursoUsuario`. |

### `view/`

| Classe | Descrição |
|--------|-----------|
| `LoginView` | Tela inicial: menu de login e cadastro. Contém inner class `DadosNovoUsuario` e `CredenciaisLogin`. |
| `InicioView` | Menu pós-login: Meus Dados / Meus Cursos / Sair. |
| `DadosView` | Tela de perfil do usuário: exibe dados e permite atualização ou exclusão. Contém inner class `DadosAtualizados`. |
| `CursosView` | Lista de cursos do usuário e formulário de criação. Contém inner class `DadosNovoCurso`. |
| `CursoDetalheView` | Exibe detalhes de um curso e opções de gerenciamento (editar, encerrar, concluir, cancelar). Contém inner class `DadosAtualizados`. |
| `InscricoesView` | Menu "Minhas Inscrições": lista cursos inscritos com indicação de status, detalhe do curso para inscrição (com campo AUTOR), listagem paginada de todos os cursos (10/pág.) e detalhe do curso inscrito com opção de cancelamento. |

### Ponto de entrada

| Classe | Descrição |
|--------|-----------|
| `Principal` | Classe `main`. Instancia todos os controllers e views e gerencia o fluxo de navegação. |

---

## Estrutura de Arquivos de Dados

```
dados/
├── usuarios/
│   ├── usuarios.db             # Registros binários de usuários
│   ├── usuarios.d.db           # Diretório do HashExtensivel (índice direto ID→addr)
│   ├── usuarios.c.db           # Baldes do HashExtensivel (índice direto)
│   ├── indiceEMAIL.d.db        # Diretório do HashExtensivel (índice indireto email→ID)
│   └── indiceEMAIL.c.db        # Baldes do HashExtensivel (índice indireto)
├── cursos/
│   ├── cursos.db               # Registros binários de cursos
│   ├── cursos.d.db             # Diretório do HashExtensivel (índice direto ID→addr)
│   ├── cursos.c.db             # Baldes do HashExtensivel (índice direto)
│   ├── indiceUsuarioCurso.db   # Árvore B+ do relacionamento 1:N (usuarioId, cursoId)
│   ├── indiceCodigo.d.db       # Diretório do HashExtensivel (NanoID→ID)
│   └── indiceCodigo.c.db       # Baldes do HashExtensivel (NanoID→ID)
└── inscricoes/
    ├── inscricoes.db               # Registros binários de inscrições (CursoUsuario)
    ├── inscricoes.d.db             # Diretório do HashExtensivel (índice direto ID→addr)
    ├── inscricoes.c.db             # Baldes do HashExtensivel (índice direto)
    ├── indiceCursoUsuario.btree    # Árvore B+ N:N (idCurso, idInscricao)
    └── indiceUsuarioCurso.btree    # Árvore B+ N:N (idUsuario, idInscricao)
```

---

## Checklist

**Há um CRUD da entidade de associação `CursoUsuario` (que estende a classe `ArquivoIndexado`, acrescentando Tabelas Hash Extensíveis e Árvores B+ como índices diretos e indiretos conforme necessidade) que funciona corretamente?**

Sim. A classe `ArquivoCursoUsuario` estende `Arquivo<CursoUsuario>`. Possui índice direto via `HashExtensivel<ParIDEndereco>` (herdado da superclasse) e dois índices indiretos via `ArvoreBMais<ParIntInt>`: `indiceCursoUsuario` com pares `(idCurso, idInscricao)` e `indiceUsuarioCurso` com pares `(idUsuario, idInscricao)`. Todos os métodos CRUD (`create`, `update`, `delete`) mantêm os dois índices sincronizados. Além do CRUD básico, estão implementados: `buscar(idUsuario, idCurso)`, `listarPorUsuario`, `listarPorCurso`, `deletarTodasInscricoesUsuario` e `deletarTodasInscricoesCurso`.

**A visão de inscrições está corretamente implementada e permite consultas aos cursos em que um usuário está inscrito?**

Sim. A classe `InscricoesView` exibe o menu "Minhas Inscrições" listando todos os cursos em que o usuário logado está inscrito, com indicação do status de cada um (inscrições encerradas, concluído, cancelado). A partir desse menu é possível: buscar um curso pelo código NanoID (opção A), listar todos os cursos do sistema com paginação de 10 itens por página (opção C), selecionar um curso inscrito para ver seus detalhes completos (incluindo AUTOR) e cancelar a inscrição.

**A visão de cursos funciona corretamente e permite a gestão dos usuários inscritos em um curso?**

Sim. Em "Meus Cursos", ao selecionar um curso, a opção (A) "Gerenciar inscritos" exibe a lista de inscritos com nome e data de inscrição. O proponente pode selecionar um inscrito para ver seus dados (nome, e-mail, data), cancelar sua inscrição individualmente (opção A) e exportar toda a lista para um arquivo CSV com nome e e-mail (opção A no menu de inscritos).

**Há uma visualização dos cursos de outras pessoas por meio de um código NanoID?**

Sim. No menu "Minhas Inscrições", a opção (A) "Buscar curso por código" solicita o NanoID de 10 caracteres e exibe os dados completos do curso encontrado (código, nome, autor, descrição, data de início), permitindo que o usuário se inscreva.

**A integridade do relacionamento entre cursos e usuários está mantida em todas as operações?**

Sim. As seguintes regras de integridade referencial são aplicadas em todas as operações:
- Ao **deletar um usuário**, todas as suas inscrições são removidas automaticamente (`UsuarioController.deletarPorEmail` → `deletarTodasInscricoesUsuario`).
- Ao **deletar um curso**, todas as inscrições nele são removidas automaticamente (`CursoController.excluir` → `deletarTodasInscricoesCurso`).
- Um usuário **não pode se inscrever no próprio curso** (verificação de `curso.usuarioId == idUsuario`).
- **Inscrições duplicadas são rejeitadas** (verificação via Árvore B+ em `ArquivoCursoUsuario.buscar`).
- Somente cursos com estado **Aberto (0)** aceitam novas inscrições.

**O trabalho compila corretamente?**

Sim. 

**O trabalho está completo e funcionando sem erros de execução?**

Sim. O sistema executa o fluxo completo: cadastro/login de usuários, CRUD de cursos, busca por código NanoID, listagem paginada de todos os cursos, inscrição em cursos, cancelamento de inscrições (pelo usuário e pelo proponente), gestão de inscritos e exportação para CSV — sem lançar exceções não tratadas durante o uso normal.

**O trabalho é original e não a cópia de um trabalho de outro grupo?**

Sim. O código foi inteiramente desenvolvido pelos integrantes do grupo listados neste relatório.

