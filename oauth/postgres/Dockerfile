FROM postgres:13

ENV POSTGRES_USER devwikiuser
# (실 배포시 수정됩니다.)
ENV POSTGRES_PASSWORD SECURED
# (실 배포시 수정됩니다.)
ENV POSTGRES_DB common_oauth

COPY *.sql /docker-entrypoint-initdb.d/
