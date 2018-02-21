-----------------------------------
---			ACCOUNTS			---
-----------------------------------
CREATE TABLE users (
    id bigserial NOT NULL,
    created timestamp without time zone NOT NULL DEFAULT now()::timestamp,
    last_updated timestamp without time zone NOT NULL DEFAULT now()::timestamp,
    username character varying(64) NOT NULL,
    email character varying(128) NOT NULL,
    password character(60) NOT NULL,
	is_enabled boolean NOT NULL DEFAULT FALSE,
    is_blocked boolean NOT NULL DEFAULT FALSE
);

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

-----------------------------------
---			PRIVILEGES			---
-----------------------------------
CREATE TABLE user_roles (
    id bigserial NOT NULL,
    created timestamp without time zone NOT NULL DEFAULT now()::timestamp,
    last_updated timestamp without time zone NOT NULL DEFAULT now()::timestamp,
    name character varying(16) NOT NULL
);

ALTER TABLE ONLY user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (id);
	
-- add default roles
INSERT INTO user_roles(created, last_updated, name)
	VALUES	(now(), now(), 'GANDALF'),
			(now(), now(), 'SERVER_ADMIN'),
			(now(), now(), 'SERVER_SUPPORT'),
			(now(), now(), 'CONTENT_ADMIN'),
			(now(), now(), 'CONTENT_SUPPORT'),
			(now(), now(), 'PREMIUM_USER'),
			(now(), now(), 'USER'),
			(now(), now(), 'GUEST');

-----------------------------------
---		  USER HAS ROLES		---
-----------------------------------
CREATE TABLE user_has_roles (
    created timestamp without time zone NOT NULL DEFAULT now()::timestamp,
    last_updated timestamp without time zone NOT NULL DEFAULT now()::timestamp,
    user_id bigint NOT NULL,
    user_role_id bigint NOT NULL
);

ALTER TABLE ONLY user_has_roles
    ADD CONSTRAINT user_has_roles_pkey PRIMARY KEY (user_id, user_role_id);

ALTER TABLE ONLY user_has_roles
    ADD CONSTRAINT user_has_roles_user FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE ONLY user_has_roles
    ADD CONSTRAINT user_has_roles_role FOREIGN KEY (user_role_id) REFERENCES user_roles(id);
	
-----------------------------------
---			  CLIENTS			---
-----------------------------------
CREATE TABLE clients (
	id bigserial NOT NULL,
	created timestamp without time zone NOT NULL DEFAULT now()::timestamp,
	last_updated timestamp without time zone NOT NULL DEFAULT now()::timestamp,
	client_id character(36) NOT NULL,
	secret character(36),
	secret_required boolean NOT NULL DEFAULT TRUE,
	auto_approve boolean NOT NULL DEFAULT FALSE,
	-- TODO: what is a good default value?
	access_token_validity integer NOT NULL,
	-- TODO: what is a good default value?
	refresh_token_validity integer NOT NULL,
	redirect_uri character varying(256)
);

ALTER TABLE ONLY clients
    ADD CONSTRAINT clients_pkey PRIMARY KEY (id);

-----------------------------------
---			AUTHORITIES			---
-----------------------------------
CREATE TABLE client_authorities (
    id bigserial NOT NULL,
    created timestamp without time zone NOT NULL DEFAULT now()::timestamp,
    last_updated timestamp without time zone NOT NULL DEFAULT now()::timestamp,
    name character varying(16) NOT NULL
);

ALTER TABLE ONLY client_authorities
    ADD CONSTRAINT client_authorities_pkey PRIMARY KEY (id);
		
-----------------------------------
---	  CLIENT HAS AUTHORITIES  	---
-----------------------------------
CREATE TABLE client_has_authorities (
    created timestamp without time zone NOT NULL DEFAULT now()::timestamp,
    last_updated timestamp without time zone NOT NULL DEFAULT now()::timestamp,
    client_id bigint NOT NULL,
    client_authority_id bigint NOT NULL
);

ALTER TABLE ONLY client_has_authorities
    ADD CONSTRAINT client_has_authorities_pkey PRIMARY KEY (client_id, client_authority_id);

ALTER TABLE ONLY client_has_authorities
    ADD CONSTRAINT client_has_authorities_client FOREIGN KEY (client_id) REFERENCES clients(id);

ALTER TABLE ONLY client_has_authorities
    ADD CONSTRAINT client_has_authorities_authority FOREIGN KEY (client_authority_id) REFERENCES client_authorities(id);
	
-----------------------------------
---			GRANT TYPES			---
-----------------------------------
CREATE TABLE client_grant_types (
	id bigserial NOT NULL,
	created timestamp without time zone NOT NULL DEFAULT now()::timestamp,
	last_updated timestamp without time zone NOT NULL DEFAULT now()::timestamp,
	name character varying(32) NOT NULL
);

ALTER TABLE ONLY client_grant_types
    ADD CONSTRAINT client_grant_types_pkey PRIMARY KEY (id);

-- Add default values
INSERT INTO client_grant_types (created, last_updated, name)
	VALUES (now(), now(), 'authorization_code'),
	(now(), now(), 'refresh_token'),
	(now(), now(), 'implicit'),
	(now(), now(), 'password');

-----------------------------------
---	  CLIENT HAS GRANT TYPES	---
-----------------------------------
CREATE TABLE client_has_grant_types (
    created timestamp without time zone NOT NULL DEFAULT now()::timestamp,
    last_updated timestamp without time zone NOT NULL DEFAULT now()::timestamp,
    client_id bigint NOT NULL,
    client_grant_type_id bigint NOT NULL
);

ALTER TABLE ONLY client_has_grant_types
    ADD CONSTRAINT client_has_grant_types_pkey PRIMARY KEY (client_id, client_grant_type_id);

ALTER TABLE ONLY client_has_grant_types
    ADD CONSTRAINT client_has_grant_types_client FOREIGN KEY (client_id) REFERENCES clients(id);

ALTER TABLE ONLY client_has_grant_types
    ADD CONSTRAINT client_has_grant_types_grant_type FOREIGN KEY (client_grant_type_id) REFERENCES client_grant_types(id);

-----------------------------------
---			  SCOPES			---
-----------------------------------
CREATE TABLE client_scopes (
	id bigserial NOT NULL,
	created timestamp without time zone NOT NULL DEFAULT now()::timestamp,
	last_updated timestamp without time zone NOT NULL DEFAULT now()::timestamp,
	name character varying(8) NOT NULL
);

ALTER TABLE ONLY client_scopes
    ADD CONSTRAINT client_scopes_pkey PRIMARY KEY (id);

-- Add default values
INSERT INTO client_scopes (created, last_updated, name)
	VALUES (now(), now(), 'read'),
		(now(), now(), 'write'),
		(now(), now(), 'trust');

-----------------------------------
---			  SCOPES			---
-----------------------------------
CREATE TABLE client_has_scopes (
	id bigserial NOT NULL,
	created timestamp without time zone NOT NULL DEFAULT now()::timestamp,
	last_updated timestamp without time zone NOT NULL DEFAULT now()::timestamp,
	client_id bigint NOT NULL,
    client_scope_id bigint NOT NULL
);

ALTER TABLE ONLY client_has_scopes
    ADD CONSTRAINT client_has_scopes_pkey PRIMARY KEY (client_id, client_scope_id);

ALTER TABLE ONLY client_has_scopes
    ADD CONSTRAINT client_has_scopes_client FOREIGN KEY (client_id) REFERENCES clients(id);

ALTER TABLE ONLY client_has_scopes
    ADD CONSTRAINT cclient_has_scopes_scope FOREIGN KEY (client_scope_id) REFERENCES client_scopes(id);

-----------------------------------
---	  AUTHENTICATION LOGGING  	---
-----------------------------------
CREATE TABLE authentication_log (
	id bigserial NOT NULL,
	created timestamp without time zone NOT NULL DEFAULT now()::timestamp,
	last_updated timestamp without time zone NOT NULL DEFAULT now()::timestamp,
	state character varying(3) NOT NULL,
	ip character(45) NOT NULL,
	message character varying(256),
	user_id bigint NOT NULL
);

-- TODO: Caused by: org.postgresql.util.PSQLException: FEHLER: keine Berechtigung für Sequenz authentication_log_id_seq
ALTER TABLE ONLY authentication_log
    ADD CONSTRAINT authentication_log_pkey PRIMARY KEY (id);

ALTER TABLE ONLY authentication_log
	ADD CONSTRAINT authentication_for_user FOREIGN KEY (user_id) REFERENCES users(id);

-----------------------------------
---			IP BLACKLIST		---
-----------------------------------
CREATE TABLE blacklisted_ips (
	id bigserial NOT NULL,
	created timestamp without time zone NOT NULL DEFAULT now()::timestamp,
	last_updated timestamp without time zone NOT NULL DEFAULT now()::timestamp,
	ip character varying(45) NOT NULL
);

ALTER TABLE ONLY blacklisted_ips
    ADD CONSTRAINT blacklisted_ips_pkey PRIMARY KEY (id);