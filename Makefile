SRC=src
OUT=out
ALL=uk/ac/gla/atanaspam/auction
INTERFACES=uk/ac/gla/atanaspam/auction/interfaces
SERVER=uk/ac/gla/atanaspam/auction/server
CLIENT=uk/ac/gla/atanaspam/auction/client

all: begin compileinterface compileclient compileserver

begin:
	if test -d out;	then echo "'out' exists"; else mkdir out ; fi

compileinterface: $(SRC)/$(INTERFACES)/*.java
	javac $(SRC)/$(INTERFACES)/*.java -d $(OUT)
compileserver: $(SRC)/$(SERVER)/*.java
	javac -cp $(OUT) $(SRC)/$(SERVER)/*.java -d $(OUT)
compileclient: $(SRC)/$(CLIENT)/*.java
	javac -cp $(OUT) $(SRC)/$(CLIENT)/*.java -d $(OUT)

clean:
	rm -rf $(OUT)/$(ALL)