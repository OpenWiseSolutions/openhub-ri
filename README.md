# OpenHub - Reference implementation

Sample project to show how to use/integrate OpenHub framework with specific integration project.

Look at [wiki] for more details.

### Maven modules
* openhub-ext: routes and business logic implementation
* openhub-war: WAR build

### Public services
* Latest exchange rate (REST)
	* GET /http/exchange/v1/latest
	* input route: org.openhubframework.openhub.ri.in.exchange.ExchangeRestInRoute
* Translate (Web Services)
	* WSDL /ws/translate.wsdl 
	* input routes: org.openhubframework.openhub.ri.in.translate.SyncTranslateWsRoute, org.openhubframework.openhub.ri.in.translate.AsyncTranslateWsRoute

### Running

#### Standalone
1. Build: `mvn clean package`
2. Run: `java $JAVA_OPTS -Dserver.port=8080 -Dspring.profiles.active=example-module,h2 -jar openhub-war/target/openhub-ri.war`

#### Servlet container
1. Build: `mvn clean package -Dserver`
2. Deploy on servlet container such as Tomcat

#### Docker
1. Run: `docker-compose up`

Open web browser: `http://localhost:8080/web/admin/console/`

### License

[Apache License 2.0]

[Apache License 2.0]: http://www.apache.org/licenses/LICENSE-2.0.txt
[wiki]: https://openhubframework.atlassian.net/wiki/spaces/OHF/pages/23363588/Reference+implementation

