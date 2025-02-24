plugins {
	kotlin("jvm") version "2.1.10"
	kotlin("plugin.spring") version "2.1.10"
	kotlin("plugin.jpa") version "2.1.10"
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.sonarqube") version "6.0.1.5171"
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("javax.persistence.Embeddable")
}

group = "site.gachontable"
version = "1.0.5-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

sonar {
	properties {
		property("sonar.projectKey", "Gachon-Table_GachonTable-BE")
		property("sonar.organization", "lupg")
		property("sonar.host.url", "https://sonarcloud.io")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")
	implementation("io.jsonwebtoken:jjwt:0.12.5")
	implementation("com.google.code.gson:gson:2.11.0")

	implementation("org.springframework.boot:spring-boot-starter-aop")

	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.redisson:redisson-spring-boot-starter:3.34.1")

	implementation("org.springframework.boot:spring-boot-starter-actuator")

	implementation("net.nurigo:sdk:4.3.0")

	runtimeOnly("com.mysql:mysql-connector-j")
	runtimeOnly("io.micrometer:micrometer-registry-prometheus")

	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.projectlombok:lombok")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")

	runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.115.Final")
}

tasks.jar {
	enabled = false
}

tasks.named<Test>("test") {
	useJUnitPlatform()
}

tasks.withType<JavaCompile> {
	options.compilerArgs.add("-parameters")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.register("installGitHooks") {
	group = "gitHooks"
	description = "Install or update Git Hooks."

	doLast {
		val hooksDir = file(".git/hooks")
		val scriptsDir = file("scripts")

		scriptsDir.listFiles()?.forEach { script ->
			val hookFile = file("${hooksDir}/${script.name}")

			script.copyTo(hookFile, overwrite = true)
			hookFile.setExecutable(true)

			println("Installed Git Hooks: ${hookFile.name}")
		}
	}
}
