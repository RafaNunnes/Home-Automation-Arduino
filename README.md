# Automação Residencial
### Descrição do Projeto
<p align="justify">
Este trabalho tem como objetivo analisar um sistema de Controle Residencial proposto pelo autor, que busca o melhor custo-benefício possível no contexto domiciliar brasileiro, tentando encontrar uma solução na área da Automação Residencial. Para isso, foi feita uma análise de viabilidade do sistema proposto por meio de pesquisa de mercado, indicadores de renda familiar, e outros trabalhos de pesquisa científica elaborados no meio acadêmico, para que, ao final, se pudesse fazer um mapeamento da parcela da população brasileira que está em uma situação favorável à implantação do sistema. Com a conclusão dessa análise, a modularização do sistema de Controle Residencial se mostrou uma solução apropriada para contribuir com a inclusão das classes menos favorecidas da sociedade na faixa de viabilidade de implantação. A partir disso, foi elaborado um protótipo para demonstração do sistema proposto, que inclui três tipos de módulos de controle de aparelhos domésticos, e uma aplicação Android para controle desses módulos através de um smartphone. Este protótipo contém módulos de controle Chaveado (Liga/Desliga), módulo de controle Dimmer, e módulo de controle Infravermelho. Estes módulos podem ser usados em conjunto com aparelhos suportados.
</p>

**Obs:.** Para melhor entendimento do sistema proposto bem como o estudo de viabilidade do modelo no contexto financeiro da população brasileira, recomenda-se a leitura da documentação do trabalho anexada ao repositório.

### Requisitos para Execução
 - Arduino Uno
 - Smartphone com Sistema Android
 - Módulo de conexão Bluetooth (HC-06)
 - Componentes eletrônicos para confecção dos Módulos de Controle propostos
 - IDE do Arduino e Android Studio 

<p align="justify">

### Instruções para Execução
- Clone o projeto
- Integre a biblioteca [IRremote](https://www.pjrc.com/teensy/td_libs_IRremote.html) ao Ambiente de Desenvolvimento Integrado do Arduino
- Compile o arquivo *main_controller_bt.ino* disponível na pasta [Arduino_Code](https://github.com/RafaNunnes/Home-Automation-Arduino/tree/master/Arduino_Code/main_controller_bt) utilizando a IDE do próprio Arduino
- Gere o apk do projeto e instale no smartphone, ou rode o projeto através do atalho "*run*" do Android Studio

### Recursos de Software Utilizados
- **API Bluetooth** disponibilizada pela plataforma Android
- **SQLite** para a persistência de dados
- **Biblioteca IRremote** para o tratamento da troca de informações através do infravermelho integrado ao sistema embarcado

</p>

## Funcionamento do Projeto

<p align="justify">
Com a finalidade de fornecer um sistema que favorece a viabilidade de implantação para a sociedade em geral, o mesmo foi projetado com o propósito de ser bastante modular, ou seja, o usuário possuirá a vantagem de agregar mais tipos de controles a medida que possuir recursos financeiros disponíveis, com isso, tal usuário não precisa arcar com o custo de implantação total de sua residência de uma única vez, mas com o passar do tempo novos módulos podem ser adicionados ao domicílio de acordo  com a sua necessidade. Além disso, dependendo da classe de renda do usuário, controles mais sofisticados podem ser agregados além dos três básicos tratados e implementados no decorrer deste trabalho, à medida que novos módulos de controle forem desenvolvidos para o sistema em questão.
</p>

### Diagrama Arquitetural
![Diagrama Arquitetural](https://i.imgur.com/LOtJweE.png)

Como este trabalho possui o objetivo de desenvolver um protótipo de baixo
custo, então foram escolhidos três tipos de controle fundamentais para implementação
através de Módulos de Controle Residencial. São eles:
- **Módulo Chaveado (Liga/Desliga):** composto pelo Circuito Chaveado;
- **Módulo Dimmer:** composto pelo Circuito Dimmer;
- **Módulo Infravermelho:** composto pelo Circuito Infravermelho.

### Módulo Chaveado (Liga/Desliga)
![Módulo Chaveado](https://i.imgur.com/R1PNkuv.png)

### Módulo Dimmer
![Módulo Dimmer](https://i.imgur.com/bPy7TJ3.png)

### Módulo Infravermelho
![Módulo Infravermelho](https://i.imgur.com/Rd6YwbN.png)

**Obs:.** Para o entendimento mais profundo da composição e funcionamento de cada Módulo de Controle é necessário ler a documentação elaborada pelo autor anteriormente citada, mais especificamente na seção **4.3 (Módulos de Controle)**.

## Funcionamento da Aplicação Android

<p align="justify">
Ao iniciar a aplicação, será apresentado ao usuário a sua Tela a Principal, que representa uma abstração de uma residência. A Tela Principal possui visualização semelhante à figura ilustrada abaixo, e nela contém toda a lista de botões de controle criados pelo usuário. Ao iniciar a aplicação pela primeira vez, esta tela estará vazia com exceção dos botões “Conectar”, utilizado para estabelecer a conexão bluetooth, e “Adicionar”, utilizado para fazer a adição de um novo botão de controle.
</p>

<p align="center">
<img src="https://i.imgur.com/U4R6dXv.jpg" alt="Tela Principal" width="350" height="550">
</p>

<p align="justify">
Para adicionar um novo botão de controle o usuário apenas precisa selecionar o botão “Adicionar”, sendo assim, redirecionado para a tela de criação de botões de controle ilustrada na figura abaixo. Nessa tela pode-se escolher entre os diferentes tipos de controle.
</p>

<p align="center">
<img src="https://i.imgur.com/IY0nSdT.jpg" alt="Tela de Criação de Botões" width="350" height="550">
</p>

<p align="justify">
Na criação dos botões de controle Chaveado e Dimmer, o usuário deverá informar o nome que deseja dar ao controle e a porta lógica escolhida para o acionamento do módulo no microcontrolador do Arduino. Para finalizar a criação do botão basta selecionar o botão “Confirmar” para que o novo botão gerado seja apresentado na Tela Principal da aplicação. Já para o caso da adição de um controle Infravermelho, o sistema embarcado se preparará para a recepção de um sinal infravermelho fornecido pelo usuário através de um controle remoto. Este controle será captado pelo Arduino por meio do seu sensor infravermelho e repassará a informação para o aplicativo Android que, de posse do código infravermelho, armazenará essa informação no novo botão de controle criado. Desta forma, o sistema ganha o seu atributo de modularidade, garantindo que o modelo seja adaptável e flexível ao seu utilizador.
</p>

<p align="justify">
Ao concluir a etapa de criação de botões, a Tela Principal irá conter toda a lista de controles criados, aparentando um visual semelhante ao demonstrado na figura abaixo.
</p>

<p align="center">
<img src="https://i.imgur.com/79nJsqh.jpg" alt="Tela Principal Modificada" width="350" height="550">
</p>

<p align="justify">
Ao pressionar qualquer botão de controle na Tela Principal, a aplicação abrirá uma janela de edição de botão como exibido na figura abaixo. Nesta janela é possível efetuar tanto a operação de exclusão de botão, ao selecionar o botão “Excluir”, quanto a operação de edição, onde pode-se alterar o nome e porta lógica atribuídos durante a etapa de criação do botão de controle, caso se trate de um controle do tipo Dimmer ou Chaveado, ou receber uma nova configuração de controle infravermelho, caso se trate de um controle do tipo Infravermelho.
</p>

<p align="center">
<img src="https://i.imgur.com/ro9MQaE.jpg" alt="Tela Edição de Botão de Controle" width="350" height="550">
</p>

<p align="justify">
Terminado o processo de criação dos botões de controle, para que de fato seja possível utilizar o controle dos módulos cadastrados, é necessário estabelecer uma conexão bluetooth através da seleção do botão “Conectar”, direcionando o usuário para a uma tela semelhante à demonstrada na figura abaixo. Nessa tela, será exibido a lista de todos os dispositivos bluetooth pareados no smartphone em questão, e, ao selecionar o dispositivo correspondente ao módulo bluetooth conectado ao Arduino, já será possível utilizar os botões de controle da Tela Principal.
</p>

<p align="center">
<img src="https://i.imgur.com/01Acwlk.jpg" alt="Tela Conexão Bluetooth" width="350" height="550">
</p>

## Propostas de Melhorias

<p align="justify">
Como proposta de melhorias para o presente trabalho, deve-se considerar a implantação de sensores e programação para que, de fato, o projeto descrito por meio deste trabalho atue de forma automática na residência implantada, com isso, alcançando completamente o patamar de Automação Residencial. Não descartando as futuras pesquisas de mercado, incluindo tais sensores no cálculo, para uma nova análise de viabilidade.
</p>

Já para a proposta de implantação, pode-se considerar as seguintes melhorias:
- Estudar a viabilidade de implantação de novos tipos de módulos de controle de baixo, médio e até alto custo
- Utilizar de sensores como suporte automático para que o sistema possa de fato se configurar como Automação Residencial completamente, bem como estudar a possibilidade de aplicações de inteligência artificial, aprendizagem de máquina e/ou programação de usuário no controle automático do sistema
- Adaptar o sistema para operar também através da internet, possivelmente até permitindo a conexão simultânea entre a internet e o bluetooth, dependendo de como o usuário desejar no momento
- Estudar a possibilidade de substituição do módulo de conexão Bluetooth por um ESP8266, por possuir um custo financeiro menor e suportar a forma de conexão através da internet pelo Wi-Fi
- Melhorar a interface de interação com o usuário, permitindo customizações variadas e melhor interatividade.

## Estágio atual do protótipo

![Protótipo Atual](https://i.imgur.com/KhZypAY.jpg)
