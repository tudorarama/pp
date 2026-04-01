import os


class GenericFile():
    def get_path(self):
        raise NotImplementedError("Nu ai implementat metoda")
    def get_freq(self):
        raise NotImplementedError("Nu ai implementat metoda")


class TextASCII(GenericFile):
    def __init__(self):
        self.path_absolut = ""
        self.frecvente = 0

    def get_path(self):
        return self.path_absolut

    def get_freq(self):
        return self.frecvente


class TextUNICODE(GenericFile):
    def __init__(self):
        self.path_absolut = ""
        self.frecvente = 0

    def get_path(self):
        return self.path_absolut

    def get_freq(self):
        return self.frecvente


class Binary(GenericFile):
    def __init__(self):
        self.path_absolut = ""
        self.frecvente = 0

    def get_path(self):
        return self.path_absolut

    def get_freq(self):
        return self.frecvente


class XMLFile(TextASCII):
    def __init__(self):
        super().__init__()
        self.first_tag = ""

    def get_first_tag(self):
        return self.first_tag


class BMP(Binary):
    def __init__(self):
        super().__init__()
        self.width = 0
        self.height = 0
        self.bpp = 0

    def show_info(self):
        print("Informatii despre fisierul binar: Width: " + str(self.width) + " Height: " + str(
            self.height) + " Bpp: " + str(self.bpp))


def main():
    Root_Dir = os.path.dirname(os.path.abspath(__file__))

    # Parcurgem recursiv fisierele
    for root, subdirs, files in os.walk(Root_Dir):
        for file in files:
            file_path = os.path.join(root, file)

            if os.path.isfile(file_path):
                f = open(file_path, "rb")
                try:
                    content = f.read()
                    if not content:
                        continue

                    if content.startswith(b'BM'):
                        fisier_bmp = BMP()
                        fisier_bmp.path_absolut = file_path

                        fisier_bmp.width = int.from_bytes(content[18:22])
                        fisier_bmp.height = int.from_bytes(content[22:26])
                        fisier_bmp.bpp = int.from_bytes(content[28:30])

                        print("Fisierul BMP: " + fisier_bmp.get_path())
                        fisier_bmp.show_info()
                        continue

                    ok_ascii = True
                    for octet in content:
                        if octet >= 128:
                            ok_ascii = False
                            break

                    if ok_ascii:
                        if content.lstrip().startswith(b'<'):
                            fisier_xml = XMLFile()
                            fisier_xml.path_absolut = file_path
                            octeti = content.lstrip().split(b'>')[0] + b'>'
                            fisier_xml.first_tag = octeti.decode('ascii')
                            print("Fisier XML Ascii: " + fisier_xml.get_path())
                            print("Primul tag: " + fisier_xml.get_first_tag() + "\n")
                    else:
                        fisier_uni = TextUNICODE()
                        fisier_uni.path_absolut = file_path
                        print("Fisier Unicode: " + fisier_uni.get_path() + "\n")

                finally:
                    f.close()


if __name__ == '__main__':
    main()
