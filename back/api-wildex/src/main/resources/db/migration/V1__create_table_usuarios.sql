CREATE SEQUENCE usuarios_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE usuarios (
    id BIGINT NOT NULL DEFAULT nextval('usuarios_id_seq'),
    
    username VARCHAR(30) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,

    telefone VARCHAR(20),
    data_nascimento DATE,
    pais VARCHAR(50),
    foto_perfil VARCHAR(255),
    bio VARCHAR(300),

    email_verified_at TIMESTAMP,
    last_login TIMESTAMP,

    role VARCHAR(20) DEFAULT 'user',
    status VARCHAR(20) DEFAULT 'active',

    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT usuarios_pkey PRIMARY KEY (id),
    CONSTRAINT usuarios_username_key UNIQUE (username),
    CONSTRAINT usuarios_email_key UNIQUE (email)
);
