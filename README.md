# Executando o projeto
O JAR Executável da aplicação fica dentro do diretório <code>target/</code> com o nome <code>apirest-1.0.jar</code>.

Os comandos para execução do projeto, desde a compilação, são os seguintes:
  - acessar a pasta onde está o arquivo <code>pom.xml</code>, execute: <code>mvn clean compile</code>
  - depois de compilado, executar o comando: <code>mvn package -DskipTests</code> para gerar o arquivo JAR. O parâmetro <code>-DskipTests</code> foi adicionado, pois existem alguns testes que validam o retorno da api, e como até aqui ainda não estamos com o projeto em execução, pulamos o teste.
  - com o arquivo JAR criado dentro da pasta <code>target/</code>, acessar o diretório e executar o comando: <code>java -jar apirest-1.0.jar</code>. Este comando irá iniciar o Tomcat configurado dentro da aplicação, e executar o projeto, deixando acessível através do Endpoint: <code>http://localhost:8080/api/products/</code>.

# Executando os testes

Para a execução dos testes automatizados da aplicação, basta acessar o diretório onde está o arquivo <code>pom.xml</code> e executar o comando: <code>mvn test</code>. Para testar diferentes retornos da api, existe um arquivo JSON no diretório <code>/files/file.json</code> que contém o array de produtos de entrada, e este é usado nos testes automatizados.
