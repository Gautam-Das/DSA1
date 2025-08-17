all: compile

compile:
	@echo "Compiling with javac..."
	javac -d out -cp "out:src/main/java" src/main/java/*.java