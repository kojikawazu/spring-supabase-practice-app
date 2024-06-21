# ベースイメージとしてOpenJDKを使用
FROM openjdk:17-jdk-alpine

# 作業ディレクトリを作成
WORKDIR /app

# WARファイルをコンテナにコピー
COPY target/spring-supabase-practice-app.war /app/spring-supabase-practice-app.war

# アプリケーションを実行
ENTRYPOINT ["java", "-jar", "/app/spring-supabase-practice-app.war"]
