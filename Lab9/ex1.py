import subprocess

class ExecuteFile:
    def __init__(self,command_list):
        self.command_list = command_list
    def execute(self):
        try:
            result = subprocess.run(self.command_list)
            return result.stdout
        except subprocess.CalledProcessError as e:
            return e.stderr


def bash_decorator(link):
    def wrapper(file_path,file_content):
        if file_content.strip().startswith("#!/bin/bash"):
            return ExecuteFile(["bash",file_path])
        return link(file_path,file_content)
    return wrapper

def kotlin_decorator(link):
    def wrapper(file_path,file_content):
        keywords=["fun","var","val"]
        if any(kw in file_content for kw in keywords):
            return ExecuteFile(["kotlin",file_path])
        return link(file_path,file_content)
    return wrapper

def java_decorator(link):
    def wrapper(file_path,file_content):
        if "public static void main" in file_content:
            return ExecuteFile(['java',file_path])
        return link(file_path,file_content)
    return wrapper

def python_decorator(link):
    def wrapper(file_path,file_content):
        keywords=["import", "def" , "if __name__ == '__main__':"]
        if any(kw in file_content for kw in keywords):
            return ExecuteFile(['python',file_content])
        return link(file_path,file_content)
    return wrapper


@bash_decorator
@kotlin_decorator
@java_decorator
@python_decorator
def proceseaza_fisier(file_path, file_content):
    return None

def main():
    fisier = input("Fisier: ")
    try:
        with open(fisier,"r") as f:
            continut = f.read()
    except FileNotFoundError:
        print("Nu ai acest fisier")
        return

    comanda = proceseaza_fisier(fisier,continut)
    if comanda is None:
        print("Nu s a putut determina tipul fisierului")
    else:
        output = comanda.execute()
        if output:
            print(output)

if __name__ == '__main__':
    main()