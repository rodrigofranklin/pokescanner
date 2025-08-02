# agent.md

## 1. **Objetivo do agente**

Este documento descreve, em detalhes operacionais e técnicos, como um agente autônomo (IA, Codex, ChatGPT ou similar) deve operar para apoiar o desenvolvimento, manutenção e melhoria do aplicativo **PokéScanner AR**, que realiza reconhecimento e precificação automática de cartas Pokémon TCG via realidade aumentada em dispositivos Android.

---

## 2. **Escopo das atividades do agente**

O agente deve ser capaz de:

* Interpretar e implementar especificações funcionais do app
* Propor e gerar código (preferencialmente em Kotlin, podendo sugerir Java)
* Sugerir, integrar e ajustar bibliotecas de OCR, AR e scraping
* Fazer engenharia reversa básica a partir de exemplos e prints de tela
* Criar e atualizar testes automatizados
* Sugerir e documentar melhores práticas de arquitetura Android
* Identificar possíveis problemas de privacidade/dados sensíveis
* Gerar documentação para usuários e desenvolvedores
* Interagir com APIs externas e/ou simular scraping conforme necessário
* Adaptar a experiência de uso para diferentes perfis de usuários (leigo/avançado)

---

## 3. **Fluxo macro de funcionamento do app**

### 3.1. **Reconhecimento**

* **Entrada:** Imagem capturada pela câmera do dispositivo
* **Processamento:**

  * Aplicar OCR (recomenda-se Google ML Kit ou Tesseract)
  * Identificar na área inferior da carta um texto no padrão `[número]/[total da coleção]` (exemplo: `151/182`)
  * Validar se o texto é plausível (checar se está em uma das coleções oficiais do TCG)
  * (Opcional) Identificar se é foil, reverse ou outra variante por análise visual (diferença de brilho, posição dos efeitos)

### 3.2. **Busca de preço**

* **Entrada:** Código da carta identificado (exemplo: `Kangaskhan 151/182`)
* **Processamento:**

  * Realizar requisição ao site Liga Pokémon ou APIs públicas usando o número e nome da carta
  * Caso não exista API: executar scraping controlado, parseando HTML das páginas de resultado de busca, extraindo nome, condições e preços da carta
  * (Opcional) Buscar em múltiplas fontes para comparar preços
  * Validar formato e consistência dos dados (ex: descartar anúncios de sleeves/lotes)

### 3.3. **Sobreposição de informação (AR)**

* **Entrada:** Imagem da carta (em tempo real)
* **Processamento:**

  * Exibir sobre a carta, usando ARCore ou overlay 2D/3D, as informações de preço, condições, loja, data da consulta, etc.
  * Permitir interação (toque para mais detalhes, favoritos, histórico)

### 3.4. **Persistência e histórico**

* Armazenar consultas recentes e favoritas em banco local (Room ou SQLite)
* (Opcional) Sincronizar com backend em nuvem (ex: Firebase)

---

## 4. **Detalhamento técnico**

### 4.1. **OCR e Reconhecimento Visual**

* **Sugestão de bibliotecas:**

  * [Google ML Kit OCR](https://developers.google.com/ml-kit/vision/text-recognition/android)
  * [Tesseract OCR](https://github.com/tesseract-ocr/tesseract)

* **Prompt para Codex/IA:**

  > Implemente um módulo Kotlin que usa ML Kit para extrair texto da região inferior de uma imagem, focando em padrões numéricos do tipo `\d{1,3}/\d{1,3}`.

* **Desafios:**

  * Cartas desgastadas, brilho excessivo, imagem tremida
  * Necessidade de recortar automaticamente apenas a área do número

### 4.2. **Web Scraping/Integração de Preço**

* **Sugestão de fluxo:**

  1. Executar busca pelo número e coleção no [Liga Pokémon](https://ligapokemon.com.br)
  2. Identificar elemento HTML (ex: `<span class="card-price">`)
  3. Parsear preço e condição (Novo, Usado, Foil, Reverse)
  4. Expor função que recebe `nome + número` e retorna os preços

* **Prompt para Codex/IA:**

  > Gere um parser HTML em Kotlin que acesse a página de uma carta do site Liga Pokémon e extraia todos os preços listados, com suas condições (Novo, Usado, Foil, etc).

* **Cuidado:**
  Respeitar políticas de uso do site e não sobrecarregar o servidor. Preferir APIs oficiais se existirem.

### 4.3. **Realidade aumentada/Overlay**

* **Sugestão de bibliotecas:**

  * [ARCore](https://developers.google.com/ar)
  * Alternativamente, overlay 2D com View personalizada sobre `CameraX`

* **Prompt para Codex/IA:**

  > Implemente uma overlay sobre o preview da câmera que mostre um card com preço e link para compra, alinhado sobre a região inferior direita da carta reconhecida.

* **Desafio extra:**
  Reconhecer e alinhar corretamente a overlay em cartas inclinadas ou parcialmente cobertas.

### 4.4. **Persistência e Histórico**

* **Banco sugerido:** Room Database (ORM oficial Android)
* **Prompt para Codex/IA:**

  > Implemente um DAO para armazenar consultas recentes, salvando nome, número, preço e timestamp.

### 4.5. **Testes Automatizados**

* **Prompt para Codex/IA:**

  > Gere um teste instrumental para validar a identificação correta do número da carta a partir de uma imagem de exemplo.

---

## 5. **Fluxo detalhado para o agente**

### 5.1. **Passos para desenvolvimento de nova feature**

1. **Receber/Interpretar demanda** (ex: "Preciso extrair também a raridade da carta")
2. **Pesquisar requisitos e exemplos**
3. **Gerar código sugerido**
4. **Testar localmente com mocks/dados reais**
5. **Gerar documentação Markdown para a feature**
6. **Atualizar testes automatizados**
7. **Submeter pull request com changelog detalhado**

### 5.2. **Como lidar com ambiguidade/erro**

* Sugerir diferentes abordagens caso OCR falhe (ex: permitir digitação manual)
* Se o scraping não retornar preço, sugerir fontes alternativas
* Reportar logs e sugestões de ajuste

---

## 6. **Sugestão de prompts para o agente Codex/ChatGPT**

* **Geração de código OCR:**

  > Gere um trecho de código Kotlin para capturar uma imagem da câmera, aplicar OCR na área inferior, e retornar um padrão de número de carta Pokémon TCG.

* **Web scraping:**

  > Gere um script que acesse a página de busca da Liga Pokémon, busque pelo termo "Kangaskhan 151/182", e retorne todos os preços encontrados, agrupados por condição da carta.

* **Teste automatizado:**

  > Implemente um teste de UI que simule a captura de uma carta e valide se o preço correto foi exibido no overlay.

* **Expansão de funcionalidades:**

  > Implemente uma função que diferencie automaticamente cartas foil e reverse foil usando análise de cor/brilho.

* **Documentação:**

  > Atualize o README.md com uma explicação da integração entre OCR e módulo de consulta de preço.

---

## 7. **Checklist de entregáveis para cada tarefa**

* [ ] Código-fonte funcional e comentado
* [ ] Teste automatizado cobrindo casos normais e de erro
* [ ] Documentação da feature no repositório
* [ ] Sugestão de melhoria ou refatoração futura
* [ ] (Opcional) Demonstração em vídeo ou GIF animado

---

## 8. **Considerações éticas e de privacidade**

* Não armazenar nem transmitir imagens de cartas sem consentimento do usuário
* Informar claramente se dados são salvos em nuvem ou local
* Respeitar as políticas de scraping e uso de dados dos sites externos consultados

---

## 9. **Requisitos de integração e deploy**

* O agente deve garantir que qualquer código novo seja compatível com as versões mínimas de Android suportadas (sugestão: Android 8+)
* Todo novo módulo precisa ser testado em dispositivos reais e emuladores
* Código de scraping deve ser facilmente desativável ou substituível por API oficial caso o site alvo mude

---

## 10. **Glossário de termos**

* **OCR:** Reconhecimento Óptico de Caracteres
* **AR:** Realidade Aumentada
* **Foil/Reverse Foil:** Tipos de acabamento de carta colecionável
* **Overlay:** Elemento visual sobreposto à imagem real
* **Pull Request:** Solicitação de integração de novo código ao repositório
* **Mock:** Dado ou função simulada para testes

