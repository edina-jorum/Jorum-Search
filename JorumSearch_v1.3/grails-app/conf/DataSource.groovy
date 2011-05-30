dataSource {
	pooled = true
	driverClassName = "org.hsqldb.jdbcDriver"
	username = "sa"
	password = ""
}
hibernate {
    cache.use_second_level_cache=true
    cache.use_query_cache=true
    cache.provider_class='net.sf.ehcache.hibernate.EhCacheProvider'
    hibernate.order_inserts=true
	hibernate.order_updates=true
	hibernate.jdbc.batch_size=50
}
// environment specific settings
environments {
	development {
		dataSource {
			dbCreate = "update" // one of 'create', 'create-drop','update'
			pooled = true
			url = "jdbc:mysql://localhost/JorumSearch"
			driverClassName = "com.mysql.jdbc.Driver"
			dialect = 'org.hibernate.dialect.MySQL5InnoDBDialect'
			username = "jorum"
			password = "changeme"
		}
	}
	test {
		dataSource {
			dbCreate = "update" // one of 'create', 'create-drop','update'
			pooled = true
			url = "jdbc:mysql://localhost/JorumSearch"
			driverClassName = "com.mysql.jdbc.Driver"
			dialect = 'org.hibernate.dialect.MySQL5InnoDBDialect'
			username = "jorum"
			password = "changeme"
		}
	}
	production {
		dataSource {
			dbCreate = "update" // one of 'create', 'create-drop','update'
			pooled = true
			url = "jdbc:mysql://localhost/JorumSearch"
			driverClassName = "com.mysql.jdbc.Driver"
			dialect = 'org.hibernate.dialect.MySQL5InnoDBDialect'
			username = "jorum"
			password = "changeme"
		}
	}
}