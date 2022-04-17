--
-- ##################################################################################################
-- ##################################################################################################
-- ####
-- #### TODOS
-- ####
-- ##################################################################################################
-- ##################################################################################################

DROP TABLE IF EXISTS todos;
CREATE TABLE IF NOT EXISTS todos
(
    id                              UUID                                    PRIMARY KEY,
    created_at                      TIMESTAMP WITHOUT TIME ZONE             NOT NULL            DEFAULT CURRENT_TIMESTAMP,
    title                           VARCHAR(255)                            NOT NULL,
    content                         VARCHAR                                 NOT NULL,
    data                            JSONB                                   NULL,
    priority                        todo_priority_type                      NOT NULL,
    done                            BOOLEAN                                 NOT NULL            DEFAULT FALSE,

    CONSTRAINT todos_title_UQ UNIQUE (title)
);


