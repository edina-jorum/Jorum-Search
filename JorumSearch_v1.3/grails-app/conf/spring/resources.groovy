// Place your Spring DSL code here
import org.springframework.jmx.support.MBeanServerFactoryBean
import org.springframework.jmx.export.MBeanExporter
import org.apache.log4j.jmx.HierarchyDynamicMBean
import org.hibernate.jmx.StatisticsService


beans = {
  hibernateStats(StatisticsService) {
	  sessionFactory=ref("sessionFactory")
	  statisticsEnabled=true
  }
  IndexMetadataService(uk.ac.jorum.search.service.IndexMetadataService) {
  }
  log4jBean(HierarchyDynamicMBean)
  
  mbeanServer(MBeanServerFactoryBean) {
    locateExistingServerIfPossible=true
  }
  
  exporter(MBeanExporter) {
    server = mbeanServer
  	beans = ["log4j:hierarchy=default":log4jBean, "org.hibernate:name=statistics":hibernateStats, "uk.ac.jorum.search.service:name=IndexMetadata":IndexMetadataService]
  }    
}
