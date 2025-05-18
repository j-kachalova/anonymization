CREATE TABLE rule_set (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
                          updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);
