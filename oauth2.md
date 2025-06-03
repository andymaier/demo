# OAuth2 Authentifizierung mit Keycloak - Setup Anleitung

## Projektübersicht
Spring Boot Projekt mit OAuth2-Authentifizierung über Keycloak. Alle Controller außer dem HelloWorld Controller werden authentifiziert.

## 1. Maven Dependencies aktualisieren

Fügen Sie folgende Dependencies zu Ihrer `pom.xml` hinzu:


Vollständige `pom.xml`:

	<!-- Spring Security -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-security</artifactId>
	</dependency>

## 2. Security Configuration erstellen

Erstellen Sie eine Security-Konfiguration unter `src/main/java/de/gdvdl/demo/config/SecurityConfig.java`:

```java package de.gdvdl.demo.config;
import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration; import org.springframework.security.config.annotation.web.builders.HttpSecurity; import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; import org.springframework.security.web.SecurityFilterChain;
@Configuration @EnableWebSecurity public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/hello-world/**").permitAll()  // HelloWorld Controller öffentlich
                        .requestMatchers("/actuator/**").permitAll()     // Actuator endpoints öffentlich
                        .anyRequest().authenticated()                    // Alle anderen Endpoints authentifiziert
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwkSetUri("http://localhost:8080/realms/demo-realm/protocol/openid-connect/certs")
                        )
                );

        return http.build();
    }
}
```

## 3. Application Properties konfigurieren

Fügen Sie folgende Konfiguration zu Ihrer `application.properties` hinzu:

```# Server Port (falls Keycloak auf 8080 läuft)
server.port=8081
# OAuth2 Resource Server Configuration
spring.security.oauth2.resourceserver.jwt.issuer-uri=[http://localhost:8080/realms/demo-realm](http://localhost:8080/realms/demo-realm) spring.security.oauth2.resourceserver.jwt.jwk-set-uri=[http://localhost:8080/realms/demo-realm/protocol/openid-connect/certs](http://localhost:8080/realms/demo-realm/protocol/openid-connect/certs)
# Optional: OAuth2 Client Configuration (für Frontend-Integration)
spring.security.oauth2.client.registration.keycloak.client-id=demo-app spring.security.oauth2.client.registration.keycloak.client-secret=YOUR_CLIENT_SECRET_HERE spring.security.oauth2.client.registration.keycloak.scope=openid,profile,email spring.security.oauth2.client.registration.keycloak.authorization-grant-type=authorization_code spring.security.oauth2.client.registration.keycloak.redirect-uri={baseUrl}/login/oauth2/code/{registrationId}
spring.security.oauth2.client.provider.keycloak.issuer-uri=[http://localhost:8080/realms/demo-realm](http://localhost:8080/realms/demo-realm) spring.security.oauth2.client.provider.keycloak.user-name-attribute=preferred_username
# Database Configuration (falls benötigt)
spring.datasource.url=jdbc:postgresql://localhost:5432/demo spring.datasource.username=demo spring.datasource.password=demo
# Logging für Debugging
logging.level.org.springframework.security=DEBUG
```

## Keycloak Einrichtung - Schritt für Schritt Anleitung

### Schritt 1: Keycloak mit Docker starten

# Keycloak Container starten
docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:latest start-dev

**Alternative mit Docker Compose (`docker-compose.yml`):**
```
yaml version: '3.8' services: keycloak: image: quay.io/keycloak/keycloak:latest environment: KEYCLOAK_ADMIN: admin KEYCLOAK_ADMIN_PASSWORD: admin ports: - "8080:8080" command: start-dev
```bash

# Mit Docker Compose starten
docker-compose up -d keycloak
```

### Schritt 2: Keycloak Admin Console öffnen

1. Öffnen Sie den Browser und navigieren Sie zu `http://localhost:8080`
2. Melden Sie sich mit den Credentials an:
    - **Username:** `admin`
    - **Password:** `admin`

### Schritt 3: Realm erstellen

1. Klicken Sie auf das Dropdown **"master"** (oben links)
2. Klicken Sie auf **"Create Realm"**
3. Geben Sie als Realm name ein: `demo-realm`
4. Klicken Sie auf **"Create"**

### Schritt 4: Client erstellen

1. Navigieren Sie zu **"Clients"** im linken Menü
2. Klicken Sie auf **"Create client"**
3. Füllen Sie folgende Felder aus:
    - **Client type:** `OpenID Connect`
    - **Client ID:** `demo-app`
    - **Name:** `Demo Application`
4. Klicken Sie auf **"Next"**
5. Aktivieren Sie:
    - **Client authentication:** `ON`
    - **Authorization:** `ON` (optional)
    - **Standard flow:** `ON`
    - **Direct access grants:** `ON`
6. Klicken Sie auf **"Next"**
7. **Valid redirect URIs:** `http://localhost:8081/*`
8. **Web origins:** `http://localhost:8081`
9. Klicken Sie auf **"Save"**

### Schritt 5: Client Secret notieren

1. Gehen Sie zum **"Credentials"** Tab des erstellten Clients
2. Kopieren Sie das **Client Secret** (wird später benötigt)
3. Tragen Sie dieses Secret in die `application.properties` unter `spring.security.oauth2.client.registration.keycloak.client-secret` ein

### Schritt 6: Benutzer erstellen

1. Navigieren Sie zu **"Users"** im linken Menü
2. Klicken Sie auf **"Create new user"**
3. Füllen Sie aus:
    - **Username:** `testuser`
    - **Email:** `test@example.com`
    - **First name:** `Test`
    - **Last name:** `User`
    - **Email verified:** `ON`
    - **Enabled:** `ON`
4. Klicken Sie auf **"Create"**
5. Gehen Sie zum **"Credentials"** Tab
6. Klicken Sie auf **"Set password"**
7. Setzen Sie ein Passwort (z.B. `password`)
8. Deaktivieren Sie **"Temporary"**
9. Klicken Sie auf **"Save"**

## 4. Optional: Controller für geschützte Bereiche

Erstellen Sie einen Controller unter `src/main/java/de/gdvdl/demo/controller/ProtectedController.java`:

```java
package de.gdvdl.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProtectedController {

    @GetMapping("/protected")
    public String protectedEndpoint(Authentication authentication) {
        return "Hello " + authentication.getName() + "! This is a protected endpoint.";
    }
    
    @GetMapping("/user-info")
    public Object userInfo(Authentication authentication) {
        return authentication.getPrincipal();
    }
    
    @GetMapping("/admin")
    public String adminEndpoint(Authentication authentication) {
        return "Admin area accessed by: " + authentication.getName();
    }
}
```

## 5. HelloWorld Controller (öffentlich zugänglich)

Beispiel für einen öffentlichen Controller:

```java
package de.gdvdl.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello-world")
public class HelloWorldController {

    @GetMapping
    public String hello() {
        return "Hello World! This endpoint is public.";
    }
    
    @GetMapping("/info")
    public String info() {
        return "This is a public information endpoint.";
    }
}
```

## 6. Testen der Integration

### Token abrufen (über Postman oder curl):

```bash
curl -X POST http://localhost:8080/realms/demo-realm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=demo-app" \
  -d "client_secret=YOUR_CLIENT_SECRET" \
  -d "username=testuser" \
  -d "password=password"
```
### Geschützten Endpoint aufrufen:

bash curl -H "Authorization: Bearer YOUR_ACCESS_TOKEN" [http://localhost:8081/](http://localhost:8081/protected-endpoint)

### Öffentlichen HelloWorld Endpoint testen:

bash curl [http://localhost:8081/hello-world](http://localhost:8081/hello-world)

## 6. Troubleshooting

### Häufige Probleme:

1. **Port-Konflikt:** Stellen Sie sicher, dass Keycloak auf Port 8080 und Ihre App auf Port 8081 läuft
2. **Client Secret:** Vergessen Sie nicht, das Client Secret aus Keycloak in die `application.properties` zu kopieren
3. **Realm Name:** Achten Sie darauf, dass der Realm Name in allen Konfigurationen identisch ist (`demo-realm`)
4. **JWT Validation:** Bei Problemen mit JWT-Validation prüfen Sie die Issuer-URI

### Logs prüfen:

# Spring Boot App Logs
tail -f logs/spring.log
# Keycloak Logs
docker logs -f CONTAINER_ID

## Zusammenfassung

Mit dieser Konfiguration haben Sie:

- ✅ OAuth2-Authentifizierung mit Keycloak eingerichtet
- ✅ HelloWorld Controller öffentlich zugänglich gelassen
- ✅ Alle anderen Controller geschützt
- ✅ JWT-Token-Validation konfiguriert
- ✅ Keycloak komplett eingerichtet
- ✅ Test-User erstellt

Die Anwendung läuft nun auf Port 8081, während Keycloak auf Port 8080 läuft. Alle Endpoints außer `/hello-world/**` und `/actuator/**` erfordern eine gültige JWT-Authentifizierung.


