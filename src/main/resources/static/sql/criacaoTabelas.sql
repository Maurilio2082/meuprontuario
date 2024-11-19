use meuprontuario;

DROP TABLE IF EXISTS livros;
DROP TABLE IF EXISTS autor;

CREATE TABLE autor (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE livros (
    id BIGINT NOT NULL AUTO_INCREMENT,
    autor_id BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (autor_id) REFERENCES autor(id) ON DELETE CASCADE ON UPDATE CASCADE
);



CREATE TABLE ESPECIALIDADE (
    ID_ESPECIALIDADE INT AUTO_INCREMENT NOT NULL,
    NOME_ESPECIALIDADE VARCHAR(100) NOT NULL,
    PRIMARY KEY (ID_ESPECIALIDADE)
);