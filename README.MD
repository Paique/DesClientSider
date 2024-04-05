# DesClientSider (DCS)

DesClientSider (ou DCS) é uma ferramenta em Java projetada para ajudar  a detectar possíveis mods client-side de forma eficiente, desativando automaticamente aqueles que não são compatíveis com servidores.

O DCS compara os nomes dos arquivos de mods com uma lista de palavras-chave e move os arquivos correspondentes para uma pasta designada, desativando-os efetivamente.

## Funcionalidades

-   **Desativação Automática de Mods**: O DCS detecta mods client-side incompatíveis com servidores específicos e os desativa automaticamente, movendo-os para uma pasta separada.
-   **Compatibilidade com SFTP**: Os usuários têm a opção de se conectar ao servidor de destino usando SFTP para facilitar o gerenciamento remoto de mods.
-   **Utilização Local**: O DCS também pode ser utilizado localmente, permitindo aos usuários definir manualmente o caminho para a pasta de mods.

## Como Usar

1.  **Configuração**: Antes de usar o DCS, é necessário configurar o arquivo de palavras-chave com os termos que identificam mods incompatíveis. Este arquivo deve estar localizado no mesmo diretório que o executável do DCS e deve ser nomeado `keywords.txt` (Também é gerado automaticamente na primeira inicialização).
    
2.  **Execução**:
    
    -   **Localmente**: Se estiver sendo utilizado localmente, basta executar o arquivo JAR do DCS e fornecer o caminho para a pasta de mods quando solicitado.
![](https://i.imgur.com/rHdsAV8.png)

    -   **SFTP**: Para utilizar SFTP, forneça as credenciais de conexão.
![](https://i.imgur.com/wv39V3u.png)
Adicionalmente é possível utilizar a senha definindo o arg PASSWORD no console:
![](https://i.imgur.com/HFP8QfK.png)

## Requisitos do Sistema

-   Java 17 ou superior.

## Instalação

1.  Baixe o arquivo JAR do DCS nas tags.
2.  Tenha o java instalado
3.  Execute o arquivo JAR do DCS.

## Exemplo de Uso

`java -jar DesClientSider.jar` 

Após selecionar as opções desejadas o DCS analisará os arquivos de mods, desativará aqueles que correspondem às palavras-chave especificadas e os moverá para a nova pasta criada "client".

## Contribuição

Contribuições são bem-vindas! Se você encontrar bugs ou tiver sugestões de melhorias, sinta-se à vontade para abrir uma issue ou enviar um pull request para o repositório oficial do DCS.

## Licença

Este projeto é licenciado sob a MIT License.
