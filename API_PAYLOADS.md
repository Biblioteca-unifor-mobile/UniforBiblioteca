# Payloads dos Endpoints - Books e Book Copies

## 📚 BOOKS ENDPOINTS

### 1. POST /books - Criar um novo livro (apenas ADMINISTRADOR)

**URL:** `POST http://localhost:3000/books`

**Headers:**
```json
{
  "Authorization": "Bearer {token_jwt}",
  "Content-Type": "application/json"
}
```

**Body (Request):**
```json
{
  "isbn": "978-85-333-0227-3",
  "titulo": "O Senhor dos Anéis",
  "autor": "J.R.R. Tolkien",
  "coAutores": ["Christopher Tolkien"],
  "edicao": "1ª edição",
  "anoEdicao": 2023,
  "idioma": "Português",
  "publicacao": "HarperCollins",
  "resumo": "Uma aventura épica na Terra Média com hobbits, anões e magos em busca de destruir um anel poderoso.",
  "imageUrl": "https://example.com/capa-senhor-aneis.jpg",
  "tipo": "FISICO",
  "numeroExemplares": 5
}
```

**Response (200 OK):**
```json
{
  "id": "clxka1z0q0000abcde1234567",
  "isbn": "978-85-333-0227-3",
  "titulo": "O Senhor dos Anéis",
  "autor": "J.R.R. Tolkien",
  "coAutores": ["Christopher Tolkien"],
  "edicao": "1ª edição",
  "anoEdicao": 2023,
  "idioma": "Português",
  "publicacao": "HarperCollins",
  "resumo": "Uma aventura épica na Terra Média...",
  "imageUrl": "https://example.com/capa-senhor-aneis.jpg",
  "tipo": "FISICO",
  "copies": [
    {
      "id": "copy1",
      "bookId": "clxka1z0q0000abcde1234567",
      "copyNumber": 1,
      "status": "DISPONIVEL",
      "condition": "BOA"
    }
  ]
}
```

---

### 2. GET /books - Listar todos os livros com paginação e filtros

**URL:** `GET http://localhost:3000/books?page=1&limit=10&titulo=Senhor&autor=Tolkien&isbn=978&anoEdicao=2023&edicao=1ª&search=fantasia`

**Headers:**
```json
{
  "Authorization": "Bearer {token_jwt}",
  "Content-Type": "application/json"
}
```

**Query Parameters (Opcionais):**
- `page` (number, default: 1) - Número da página
- `limit` (number, default: 10) - Itens por página
- `titulo` (string) - Filtro por título (busca parcial)
- `autor` (string) - Filtro por autor (busca parcial)
- `isbn` (string) - Filtro por ISBN (busca parcial)
- `anoEdicao` (number) - Filtro por ano de edição exato
- `edicao` (string) - Filtro por edição (busca parcial)
- `search` (string) - Busca geral em múltiplos campos

**Exemplos de URLs:**
```
GET /books
GET /books?page=2&limit=20
GET /books?titulo=Senhor&autor=Tolkien
GET /books?search=fantasia
GET /books?anoEdicao=2023
```

**Response (200 OK):**
```json
{
  "data": [
    {
      "id": "clxka1z0q0000abcde1234567",
      "isbn": "978-85-333-0227-3",
      "titulo": "O Senhor dos Anéis",
      "autor": "J.R.R. Tolkien",
      "coAutores": ["Christopher Tolkien"],
      "edicao": "1ª edição",
      "anoEdicao": 2023,
      "idioma": "Português",
      "publicacao": "HarperCollins",
      "resumo": "Uma aventura épica na Terra Média...",
      "imageUrl": "https://example.com/capa-senhor-aneis.jpg",
      "tipo": "FISICO"
    }
  ],
  "total": 1,
  "page": 1,
  "limit": 10,
  "pages": 1
}
```

---

### 3. GET /books/:id - Obter detalhes de um livro específico

**URL:** `GET http://localhost:3000/books/clxka1z0q0000abcde1234567`

**Headers:**
```json
{
  "Authorization": "Bearer {token_jwt}",
  "Content-Type": "application/json"
}
```

**Response (200 OK):**
```json
{
  "id": "clxka1z0q0000abcde1234567",
  "isbn": "978-85-333-0227-3",
  "titulo": "O Senhor dos Anéis",
  "autor": "J.R.R. Tolkien",
  "coAutores": ["Christopher Tolkien"],
  "edicao": "1ª edição",
  "anoEdicao": 2023,
  "idioma": "Português",
  "publicacao": "HarperCollins",
  "resumo": "Uma aventura épica na Terra Média...",
  "imageUrl": "https://example.com/capa-senhor-aneis.jpg",
  "tipo": "FISICO",
  "copies": [
    {
      "id": "copy1",
      "bookId": "clxka1z0q0000abcde1234567",
      "copyNumber": 1,
      "status": "DISPONIVEL",
      "condition": "BOA"
    }
  ]
}
```

---

### 4. PATCH /books/:id - Atualizar um livro (apenas ADMINISTRADOR)

**URL:** `PATCH http://localhost:3000/books/clxka1z0q0000abcde1234567`

**Headers:**
```json
{
  "Authorization": "Bearer {token_jwt}",
  "Content-Type": "application/json"
}
```

**Body (Request - Todos os campos opcionais):**
```json
{
  "isbn": "978-85-333-0227-3",
  "titulo": "O Senhor dos Anéis - Edição Especial",
  "autor": "J.R.R. Tolkien",
  "coAutores": ["Christopher Tolkien", "Ed. Smith"],
  "edicao": "2ª edição",
  "anoEdicao": 2024,
  "idioma": "Português",
  "publicacao": "HarperCollins Brasil",
  "resumo": "Uma aventura épica revisada e ampliada...",
  "imageUrl": "https://example.com/capa-senhor-aneis-v2.jpg",
  "tipo": "FISICO"
}
```

**Response (200 OK):**
```json
{
  "id": "clxka1z0q0000abcde1234567",
  "isbn": "978-85-333-0227-3",
  "titulo": "O Senhor dos Anéis - Edição Especial",
  "autor": "J.R.R. Tolkien",
  "coAutores": ["Christopher Tolkien", "Ed. Smith"],
  "edicao": "2ª edição",
  "anoEdicao": 2024,
  "idioma": "Português",
  "publicacao": "HarperCollins Brasil",
  "resumo": "Uma aventura épica revisada e ampliada...",
  "imageUrl": "https://example.com/capa-senhor-aneis-v2.jpg",
  "tipo": "FISICO"
}
```

---

### 5. DELETE /books/:id - Deletar um livro (apenas ADMINISTRADOR)

**URL:** `DELETE http://localhost:3000/books/clxka1z0q0000abcde1234567`

**Headers:**
```json
{
  "Authorization": "Bearer {token_jwt}",
  "Content-Type": "application/json"
}
```

**Response (200 OK):**
```json
{
  "message": "Livro deletado com sucesso"
}
```

---

## 📖 BOOK COPIES ENDPOINTS

### 1. POST /book-copies - Criar um novo exemplar (apenas ADMINISTRADOR)

**URL:** `POST http://localhost:3000/book-copies`

**Headers:**
```json
{
  "Authorization": "Bearer {token_jwt}",
  "Content-Type": "application/json"
}
```

**Body (Request):**
```json
{
  "bookId": "clxka1z0q0000abcde1234567",
  "copyNumber": 1,
  "status": "DISPONIVEL",
  "condition": "BOA"
}
```

**Valores possíveis para `status`:**
- `DISPONIVEL`
- `ALUGADO`
- `RESERVADO`
- `INDISPONIVEL`

**Valores possíveis para `condition`:**
- `MUITO_BOA`
- `BOA`
- `CONSERVADO`
- `RUIM`
- `MUITO_RUIM`

**Response (201 Created):**
```json
{
  "id": "copyclxka1z0q0000xyz",
  "bookId": "clxka1z0q0000abcde1234567",
  "copyNumber": 1,
  "status": "DISPONIVEL",
  "condition": "BOA"
}
```

---

### 2. GET /book-copies - Listar todos os exemplares

**URL:** `GET http://localhost:3000/book-copies`

**Headers:**
```json
{
  "Authorization": "Bearer {token_jwt}",
  "Content-Type": "application/json"
}
```

**Response (200 OK):**
```json
[
  {
    "id": "copyclxka1z0q0000xyz",
    "bookId": "clxka1z0q0000abcde1234567",
    "copyNumber": 1,
    "status": "DISPONIVEL",
    "condition": "BOA"
  },
  {
    "id": "copyclxka1z0q0001xyz",
    "bookId": "clxka1z0q0000abcde1234567",
    "copyNumber": 2,
    "status": "ALUGADO",
    "condition": "CONSERVADO"
  }
]
```

---

### 3. GET /book-copies/book/:bookId - Listar exemplares de um livro específico

**URL:** `GET http://localhost:3000/book-copies/book/clxka1z0q0000abcde1234567`

**Headers:**
```json
{
  "Authorization": "Bearer {token_jwt}",
  "Content-Type": "application/json"
}
```

**Response (200 OK):**
```json
[
  {
    "id": "copyclxka1z0q0000xyz",
    "bookId": "clxka1z0q0000abcde1234567",
    "copyNumber": 1,
    "status": "DISPONIVEL",
    "condition": "BOA"
  },
  {
    "id": "copyclxka1z0q0001xyz",
    "bookId": "clxka1z0q0000abcde1234567",
    "copyNumber": 2,
    "status": "ALUGADO",
    "condition": "CONSERVADO"
  }
]
```

---

### 4. GET /book-copies/:id - Obter detalhes de um exemplar específico

**URL:** `GET http://localhost:3000/book-copies/copyclxka1z0q0000xyz`

**Headers:**
```json
{
  "Authorization": "Bearer {token_jwt}",
  "Content-Type": "application/json"
}
```

**Response (200 OK):**
```json
{
  "id": "copyclxka1z0q0000xyz",
  "bookId": "clxka1z0q0000abcde1234567",
  "copyNumber": 1,
  "status": "DISPONIVEL",
  "condition": "BOA",
  "book": {
    "id": "clxka1z0q0000abcde1234567",
    "isbn": "978-85-333-0227-3",
    "titulo": "O Senhor dos Anéis",
    "autor": "J.R.R. Tolkien"
  }
}
```

---

### 5. PATCH /book-copies/:id - Atualizar um exemplar (apenas ADMINISTRADOR)

**URL:** `PATCH http://localhost:3000/book-copies/copyclxka1z0q0000xyz`

**Headers:**
```json
{
  "Authorization": "Bearer {token_jwt}",
  "Content-Type": "application/json"
}
```

**Body (Request - Todos os campos opcionais):**
```json
{
  "status": "RESERVADO",
  "condition": "CONSERVADO"
}
```

**Response (200 OK):**
```json
{
  "id": "copyclxka1z0q0000xyz",
  "bookId": "clxka1z0q0000abcde1234567",
  "copyNumber": 1,
  "status": "RESERVADO",
  "condition": "CONSERVADO"
}
```

---

### 6. DELETE /book-copies/:id - Deletar um exemplar (apenas ADMINISTRADOR)

**URL:** `DELETE http://localhost:3000/book-copies/copyclxka1z0q0000xyz`

**Headers:**
```json
{
  "Authorization": "Bearer {token_jwt}",
  "Content-Type": "application/json"
}
```

**Response (200 OK):**
```json
{
  "message": "Exemplar deletado com sucesso"
}
```

---

## 🔐 Notas importantes sobre autenticação

1. **Token JWT obrigatório** em todos os endpoints (exceto login/registro)
2. **Apenas ADMINISTRADOR** pode:
   - POST /books (criar livros)
   - PATCH /books/:id (atualizar livros)
   - DELETE /books/:id (deletar livros)
   - POST /book-copies (criar exemplares)
   - PATCH /book-copies/:id (atualizar exemplares)
   - DELETE /book-copies/:id (deletar exemplares)

3. **Todos os usuários autenticados** podem:
   - GET /books (listar livros)
   - GET /books/:id (ver detalhes de livro)
   - GET /book-copies (listar exemplares)
   - GET /book-copies/:id (ver detalhes de exemplar)
   - GET /book-copies/book/:bookId (listar exemplares de um livro)

---

## 📝 Exemplo usando cURL

```bash
# Criar um livro
curl -X POST http://localhost:3000/books \
  -H "Authorization: Bearer seu_token_jwt" \
  -H "Content-Type: application/json" \
  -d '{
    "isbn": "978-85-333-0227-3",
    "titulo": "O Senhor dos Anéis",
    "autor": "J.R.R. Tolkien",
    "coAutores": ["Christopher Tolkien"],
    "edicao": "1ª edição",
    "anoEdicao": 2023,
    "idioma": "Português",
    "publicacao": "HarperCollins",
    "tipo": "FISICO",
    "numeroExemplares": 5
  }'

# Listar livros com filtro
curl -X GET "http://localhost:3000/books?page=1&limit=10&search=Tolkien" \
  -H "Authorization: Bearer seu_token_jwt"

# Obter um livro específico
curl -X GET http://localhost:3000/books/clxka1z0q0000abcde1234567 \
  -H "Authorization: Bearer seu_token_jwt"

# Criar um exemplar
curl -X POST http://localhost:3000/book-copies \
  -H "Authorization: Bearer seu_token_jwt" \
  -H "Content-Type: application/json" \
  -d '{
    "bookId": "clxka1z0q0000abcde1234567",
    "copyNumber": 1,
    "status": "DISPONIVEL",
    "condition": "BOA"
  }'

# Listar exemplares de um livro
curl -X GET http://localhost:3000/book-copies/book/clxka1z0q0000abcde1234567 \
  -H "Authorization: Bearer seu_token_jwt"
```

---

## ✨ Dicas para testes

1. **Use Postman ou Insomnia** para testar os endpoints com interface visual
2. **Copie o token JWT** do login/register e use nos headers de Authorization
3. **IDs dinâmicos**: Substitua os IDs nos exemplos pelos IDs reais retornados pela API
4. **Status do exemplar**: DISPONIVEL, ALUGADO, RESERVADO, INDISPONIVEL
5. **Condição do exemplar**: MUITO_BOA, BOA, CONSERVADO, RUIM, MUITO_RUIM
