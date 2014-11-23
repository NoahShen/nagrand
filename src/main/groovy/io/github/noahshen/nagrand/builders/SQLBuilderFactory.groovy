package io.github.noahshen.nagrand.builders

import io.github.noahshen.nagrand.builders.hsqldb.*
import io.github.noahshen.nagrand.builders.mysql.*
import io.github.noahshen.nagrand.metadata.ClassMetaData

/**
 * sql builder factory
 */
class SQLBuilderFactory {

    private static volatile SQLBuilderFactory instance

    private SQLBuilderFactory() {
    }

    static SQLBuilderFactory getInstance() {
        if (!instance) {
            synchronized (SQLBuilderFactory) {
                if (!instance) {
                    instance = new SQLBuilderFactory()
                }
            }
        }
        instance
    }

    BaseCreateTableSqlBuilder createCreateTableSqlBuilder(SQLDialect dialect, ClassMetaData classMetaData) {
        if (dialect == SQLDialect.HSQLDB) {
            new HSQLDBCreateTableSqlBuilder(classMetaData)
        } else if (dialect == SQLDialect.MYSQL) {
            new MySqlCreateTableSqlBuilder(classMetaData);
        }
    }

    BaseInsertSqlBuilder createInsertSqlBuilder(SQLDialect dialect, ClassMetaData classMetaData, entity) {
        if (dialect == SQLDialect.HSQLDB) {
            new HSQLDBInsertSqlBuilder(classMetaData, entity)
        } else if (dialect == SQLDialect.MYSQL) {
            new MySqlInsertSqlBuilder(classMetaData, entity);
        }
    }

    BaseUpdateSqlBuilder createUpdateSqlBuilder(SQLDialect dialect, ClassMetaData classMetaData, entity) {
        if (dialect == SQLDialect.HSQLDB) {
            new HSQLDBUpdateSqlBuilder(classMetaData, entity)
        } else if (dialect == SQLDialect.MYSQL) {
            new MySqlUpdateSqlBuilder(classMetaData, entity);
        }
    }

    BaseDeleteSqlBuilder createDeleteSqlBuilder(SQLDialect dialect, ClassMetaData classMetaData) {
        if (dialect == SQLDialect.HSQLDB) {
            new HSQLDBDeleteSqlBuilder(classMetaData)
        } else if (dialect == SQLDialect.MYSQL) {
            new MySqlDeleteSqlBuilder(classMetaData);
        }
    }

    BaseCountSqlBuilder createCountSqlBuilder(SQLDialect dialect, ClassMetaData classMetaData) {
        if (dialect == SQLDialect.HSQLDB) {
            new HSQLDBCountSqlBuilder(classMetaData)
        } else if (dialect == SQLDialect.MYSQL) {
            new MySqlCountSqlBuilder(classMetaData);
        }
    }

    BaseSelectSqlBuilder createSelectSqlBuilder(SQLDialect dialect, ClassMetaData classMetaData) {
        if (dialect == SQLDialect.HSQLDB) {
            new HSQLDBSelectSqlBuilder(classMetaData)
        } else if (dialect == SQLDialect.MYSQL) {
            new MySqlSelectSqlBuilder(classMetaData);
        }
    }
}
