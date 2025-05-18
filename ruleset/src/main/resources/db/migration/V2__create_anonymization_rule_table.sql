CREATE TABLE anonymization_rule (
                                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                    rule_set_id UUID NOT NULL,
                                    name VARCHAR(255) NOT NULL,
                                    pattern TEXT,
                                    replacement TEXT,
                                    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
                                    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
                                    CONSTRAINT fk_rule_set
                                        FOREIGN KEY(rule_set_id)
                                            REFERENCES rule_set(id)
                                            ON DELETE CASCADE
);
