package com.thanhvh.postgresql.dialect;

import org.hibernate.dialect.PostgresPlusDialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.query.spi.QueryEngine;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;
import org.hibernate.type.BasicTypeRegistry;
import org.hibernate.type.StandardBasicTypes;

/**
 * UnAccentPostgreSQLDialect
 */
public class UnAccentPostgreSQLDialect extends PostgresPlusDialect {
    /**
     * UnAccentPostgreSQLDialect
     */
    public UnAccentPostgreSQLDialect() {
        super();
    }

    @Override
    public void initializeFunctionRegistry(QueryEngine queryEngine) {
        BasicTypeRegistry basicTypeRegistry = queryEngine.getTypeConfiguration().getBasicTypeRegistry();
        SqmFunctionRegistry functionRegistry = queryEngine.getSqmFunctionRegistry();

        functionRegistry.register(
                "unaccent",
                new StandardSQLFunction(
                        "unaccent",
                        StandardBasicTypes.STRING
                )
        );
        functionRegistry.registerPattern(
                "unailike",
                "( unaccent(?1) ilike unaccent( concat('%',?2,'%') ) )",
                basicTypeRegistry.resolve(StandardBasicTypes.BOOLEAN)
        );
        super.initializeFunctionRegistry(queryEngine);
    }
}
