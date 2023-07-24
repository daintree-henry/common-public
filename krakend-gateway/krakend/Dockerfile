FROM devopsfaith/krakend:2.4.1

COPY krakend.json /etc/krakend/krakend.json

ENTRYPOINT [ "/usr/bin/krakend" ]

CMD [ "run", "-c", "/etc/krakend/krakend.json", "-d" ]