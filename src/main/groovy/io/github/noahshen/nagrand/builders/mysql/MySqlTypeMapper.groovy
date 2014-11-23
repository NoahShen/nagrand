package io.github.noahshen.nagrand.builders.mysql
/**
 * Maintains global Java/Groovy to Sql type mappings. Mappings can be altered here.
 *
 */
@Singleton
class MySqlTypeMapper {

    public static final String DEFAULT_TYPE = "VARCHAR(255)"

    public static final Map<Class, String> DEFAULT_MAPPINGS = Collections.unmodifiableMap([
            (int)                 : "INT(11)",
            (java.lang.Integer)   : "INT(11)",
            (long)                : "BIGINT(20)",
            (java.lang.Long)      : "BIGINT(20)",
            (float)               : "DECIMAL(10,6)",
            (java.lang.Float)     : "DECIMAL(10,6)",
            (double)              : "DECIMAL(10,6)",
            (java.lang.Double)    : "DECIMAL(10,6)",
            (java.math.BigDecimal): "DECIMAL(10,6)",
            (java.util.Date)      : "TIMESTAMP",
            (java.sql.Timestamp)  : "TIMESTAMP",
            (java.sql.Date)       : "DATE",
            (java.sql.Time)       : "TIME"
    ])

    private Map<Class, String> mappings = new HashMap<>(DEFAULT_MAPPINGS)
    def defaultType = DEFAULT_TYPE

    String getSqlType(Class fieldClass) {
        mappings.containsKey(fieldClass) ? mappings.get(fieldClass) : defaultType
    }

    MySqlTypeMapper setType(Class className, String sqlType) {
        mappings.put(className, sqlType)
        return this
    }

    void reset() {
        mappings = new HashMap<>(DEFAULT_MAPPINGS)
        defaultType = DEFAULT_TYPE
    }

}


