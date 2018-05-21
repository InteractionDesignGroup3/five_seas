CONFIG_TARGET=src/main/
CONFIG_LOCATION=config/
METEOMATICS_CONFIG=config.json
HERE_MAPS_CONFIG=here.json

PROJECT=five_seas
VERSION=1.0-SNAPSHOT
EXECUTABLE=$(PROJECT)-$(VERSION).jar
OUT=target/$(EXECUTABLE)

run: all
	java -jar $(PROJECT).jar 

all: jar
	cp $(OUT) $(PROJECT).jar
	
jar: 
	cp $(CONFIG_LOCATION)$(METEOMATICS_CONFIG) $(CONFIG_TARGET) 
	cp $(CONFIG_LOCATION)$(HERE_MAPS_CONFIG) $(CONFIG_TARGET)
	mvn -Dmaven.test.skip=true compile assembly:single
	rm $(CONFIG_TARGET)$(METEOMATICS_CONFIG)
	rm $(CONFIG_TARGET)$(HERE_MAPS_CONFIG)

clean:
	mvn clean
	rm $(PROJECT).jar
	rm -r *.json 
