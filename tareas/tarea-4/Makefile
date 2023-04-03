JC = javac
JFLAGS = -g
SRC = $(wildcard *.java)
CLASSES = $(SRC:.java=.class)

default: compile

compile: $(CLASSES)

%.class: %.java
	$(JC) $(JFLAGS) $<

runServer: compile
	@echo "Ejecutando el servidor en el puerto $(PORT)..."
	@java Servidor $(PORT)

runClienteGET: compile
	@echo "Ejecutando el cliente GET..."
	@java ClienteGET $(IP) $(PORT) $(FILE)

runClientePUT: compile
	@echo "Ejecutando el cliente PUT..."
	@java ClientePUT $(IP) $(PORT) $(FILE)

clean:
	$(RM) *.class
