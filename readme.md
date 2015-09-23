#Jersey + Spring Boot + Swagger Example

## Setup Swagger

###Add properties to application.properties

```
swagger.apiVersion=1.0
swagger.basePath=http://localhost:8080/api
swagger.resourcePackage=demo.api.rest
```

###Define configuration beans

* SwaggerConfiguration.java
* AccessHiddenSpecFilter.java
* AccessHiddenModelConverter.java
* SwaggerConfig.java


###Define Jersey configuration
```java
import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {	
	
	public JerseyConfig() {
		
		final String restEndpointsPackge = "demo.api.rest";
		final String jacksonPackage = "org.codehaus.jackson.jaxrs";
		final String swaggerJaxrsJsonPackage = "com.wordnik.swagger.jaxrs.json";
		final String swaggerJaxrsListingPackage = "com.wordnik.swagger.jaxrs.listing";
		
		packages(restEndpointsPackge, swaggerJaxrsJsonPackage, swaggerJaxrsListingPackage, jacksonPackage);		                
	}
}
```

### Update application endpoint (/api/api-docs) in index.html
```javascript
$(function () {
      var url = window.location.search.match(/url=([^&]+)/);
      if (url && url.length > 1) {
        url = decodeURIComponent(url[1]);
      } else {
        url = "/api/api-docs";
      }
```    



