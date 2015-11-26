SRC=src
OUT=out
ALL=uk/ac/gla/atanaspam/auction
INTERFACES=uk/ac/gla/atanaspam/auction/interfaces
SERVER=uk/ac/gla/atanaspam/auction/server
CLIENT=uk/ac/gla/atanaspam/auction/client

all: compileinterface compileclient compileserver

compileinterface: $(SRC)/$(INTERFACES)/*.java
	javac $(SRC)/$(INTERFACES)/*.java -d $(OUT)
compileserver: $(SRC)/$(SERVER)/*.java
	javac -cp $(OUT) $(SRC)/$(SERVER)/*.java -d $(OUT)
compileclient: $(SRC)/$(CLIENT)/*.java
	javac -cp $(OUT) $(SRC)/$(CLIENT)/*.java -d $(OUT)

clean:
	rm -rf $(OUT)/$(ALL)