plugins {
    `java-library`
}

group = "com.github.jhinslog"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // 기상청 서버와 HTTP 통신을 하기 위한 부품
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // 기상청이 보내준 JSON 데이터를 Java 객체로 변환하기 위한 부품
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")

    // 코드 작성을 편리하게 해주는 Lombok
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    // 우리가 만든 코드가 잘 작동하는지 테스트하기 위한 부품
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

java {
    // 이 라이브러리는 Java 21 문법으로 만들어졌습니다.
    sourceCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
    useJUnitPlatform()
}