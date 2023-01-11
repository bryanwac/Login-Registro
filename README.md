
<h2>Sobre o sistema de login</h2>

<ul>
    <li>Utiliza autenticação via token JWT;</li>
    <li>Verifica autorização de acesso via Permissões;</li>
    <li>Atualizações periódicas acompanhando versão Java/SPRING - Atualmente segue arquitetura do SPRING 3.0</li>
</ul>

<h2>Como utilizar ? </h2>

<h5>Antes da execução:</h5>
    <ul>
        <li> Criar um Scheema no banco de dados selecionado (neste caso MySQL) com o nome: backgenerico (ou o nome desejado)</li>
        <li>Executar o seguinte script na query da tabela de permissoes:</li>
                <p>INSERT INTO permissoes(nome) VALUES('PERM_USER');</p>
                <p>INSERT INTO permissoes(nome) VALUES('PERM_ADMIN');</p>
        <p>Obs: Seu app pode possuir a quantidade de permissões que você quiser, para dar estar permissões para seu usuario <br> no momento da criação de conta, basta mudar na função create ou utilizar um painel no front que altere essas permissões</p>
    </ul>

<h5>Rotas criadas até o momento :</h5>
    <ul>
        <li>/registro = espera um body<JSON> com os dados de registro:</li>
            <ul>
                <li>username : nome completo do usuario;</li>
                <li>cpf : 14 digitos max.;</li>
                <li>telefone;</li>
                <li>isWhatsapp : Para marcar no BD o telefone estar vinculado a um whatsapp ou não, por padrão é false;</li>
                <li>aceitouTermos : usuario precisa aceitar os termos de uso do seu app;</li>
                <li>email : passa por validação na inserção, mas a validação principal de formato é recomendada ser feita no front;</li>
                <li>password : senha até o momento sem limitação de qtd. de caracteres</li>
            </ul>
        <br>
        <li>/login = espera um body<JSON> com os dados de registro:</li>
            <ul>
                <li>email: chave unica no BD, pode ser alterado, mas nunca repetido</li>
                <li>password: usada para autenticação</li>
            </ul>
        <li>/usuario = rota autenticada, para testes, libera o painel do usuario</li>
        <li>/usuario/perfil/alterar-dados = rota autenticada que permite alterar dados do usuario, <br> espera um body<JSON> com 3 campos OPICIONAIS:</li>
            <ul>
                <li>nome</li>
                <li>email: usada para autenticação, chave unica</li>
                <li>telefone</li>
                <p>Obs: Estes dados vem para um DTO, todos são opicionais, caso exista no DTO, será alterado.</p>
            </ul>
    </ul>