package io.github.noahshen.nagrand.builders.hsqldb
/**
 *
 */
@Singleton
class HSQLDBTypeMapper {

    public static final String DEFAULT_TYPE = "VARCHAR(255)"

    public static final Map<Class, String> DEFAULT_MAPPINGS = Collections.unmodifiableMap([
            (int)                 : "NUMERIC",
            (java.lang.Integer)   : "NUMERIC",
            (long)                : "NUMERIC",
            (java.lang.Long)      : "NUMERIC",
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

    HSQLDBTypeMapper setType(Class className, String sqlType) {
        mappings.put(className, sqlType)
        return this
    }

    void reset() {
        mappings = new HashMap<>(DEFAULT_MAPPINGS)
        defaultType = DEFAULT_TYPE
    }

}


