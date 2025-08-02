# README.md

# PokéScanner AR
*App Android para identificação e precificação de cartas Pokémon TCG usando realidade aumentada*

## Descrição

O PokéScanner AR é um aplicativo Android desenvolvido para facilitar a vida de colecionadores e jogadores de Pokémon TCG. Utilizando a câmera do celular e técnicas de reconhecimento de imagem (OCR), o app identifica a carta física, extrai seu número de coleção e busca automaticamente o preço atualizado em plataformas brasileiras, como o site Liga Pokémon. O preço é exibido em tempo real por cima da imagem da carta, utilizando realidade aumentada.

## Funcionalidades principais

* **Reconhecimento de cartas via câmera:** Identificação automática do número da carta (ex: 151/182) usando OCR.
* **Busca automática de preços:** Consulta em tempo real ao site Liga Pokémon ou APIs públicas disponíveis.
* **Sobreposição em AR:** Exibição do preço atual e outros dados relevantes sobre a imagem da carta.
* **Interface intuitiva:** Foco em usabilidade, permitindo consulta rápida e prática para qualquer usuário.
* **(Futuro)** Histórico de pesquisas, favoritos e comparação de preços em diferentes estados de conservação (normal, foil, reverse, etc.).

## Tecnologias envolvidas

* **Android Studio (Kotlin ou Java)**
* **Google ML Kit / Tesseract OCR** para reconhecimento de texto em imagens.
* **ARCore** (opcional, para overlay de realidade aumentada).
* **Bibliotecas HTTP (Retrofit, Volley, etc.)** para consultas web.
* **Web Scraping** (caso não exista API pública para preços).
* **(Opcional)** Firebase para histórico e login.

## Como executar o projeto

1. **Clone este repositório:**

   ```bash
   git clone https://github.com/seu-usuario/pokescanner-ar.git
   ```
2. **Abra o projeto no Android Studio.**
3. **Configure as dependências** (veja o arquivo `build.gradle` para detalhes de bibliotecas necessárias).
4. **Compile e execute no seu dispositivo ou emulador.**
5. (Opcional) Configure variáveis de ambiente para chaves de API se houver integração com serviços externos.

## Como contribuir

1. Faça um fork do projeto.
2. Crie um branch para sua feature ou correção:
   `git checkout -b feature/nome-da-feature`
3. Commit suas alterações:
   `git commit -am 'Adiciona nova feature'`
4. Faça push para o branch:
   `git push origin feature/nome-da-feature`
5. Crie um Pull Request.

## Roadmap (próximos passos)

* [ ] Finalizar protótipo de OCR para numeração da carta
* [ ] Integrar busca de preços automatizada
* [ ] Implementar sobreposição AR
* [ ] Criar sistema de favoritos e histórico
* [ ] Testes de usabilidade

## Licença

[MIT](LICENSE)

