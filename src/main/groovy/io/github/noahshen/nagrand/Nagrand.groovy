package io.github.noahshen.nagrand
import groovy.sql.Sql
import groovy.util.logging.Log
import io.github.noahshen.nagrand.builders.SQLBuilderFactory
import io.github.noahshen.nagrand.builders.SQLDialect
import io.github.noahshen.nagrand.enhance.ModelClassEnhancer
import io.github.noahshen.nagrand.helpers.SqlObjectFactory
import io.github.noahshen.nagrand.metadata.ClassMetaData

import java.sql.Connection
import java.util.logging.Level

@Log
class Nagrand {

    Sql sql

    SQLDialect dialect

    /**
     * Constructs Nagrand using in-memory hsqldb database
     */
    Nagrand(){
        this(SqlObjectFactory.memoryDB())
    }

    /**
     * Constructs Nagrand using disk based (persistent) hsqldb database
     *
     * @param dbPath the path of the database
     */
    Nagrand(String dbPath){
        this(SqlObjectFactory.fileDB(dbPath))
    }

    /**
     * Constructs Nagrand using provided Connection
     *
     * @param connection instance of java.sql.Connection
     */
    Nagrand(Connection connection){
        this(new Sql(connection))
    }

    /**
     * Constructs Nagrand using provided Sql instance
     *
     * @param connection instance of groovy.sql.Sql
     */
    Nagrand(Sql sql, SQLDialect dialect = SQLDialect.HSQLDB) {
        this.sql = sql
        this.dialect = dialect
    }

    /**
     * Adds CRUD methods to the modelClass. Also creates table for class if does not exist already.
     *
     * @param modelClass
     */
    def register(Class modelClass, Boolean createTable = false) {
        ClassMetaData classMetaData = new ClassMetaData(modelClass)
        if (createTable) {
            createTableFor(classMetaData)
        }
        new ModelClassEnhancer(classMetaData, sql, dialect).enhance()
        return this
    }

    private def createTableFor(ClassMetaData metaData) {
        SQLBuilderFactory sqlBuilderFactory = SQLBuilderFactory.getInstance()
        def buildResult = sqlBuilderFactory.createCreateTableSqlBuilder(dialect, metaData).buildSqlAndValues()
        sql.execute(buildResult.sql)
    }

    def enableQueryLogging(level = Level.FINE) {
        def sqlMetaClass = Sql.class.metaClass

        sqlMetaClass.invokeMethod = { String name, args ->
            if (args) log.log(level, args.first()) // so far the first arg has been the query.
            sqlMetaClass.getMetaMethod(name, args).invoke(delegate, args)
        }
    }
}
