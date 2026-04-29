class Observable:
    def __init__(self):
        self.observers = []

    def attach(self,observer):
        if observer not in self.observers:
            self.observers.append(observer)

    def detach(self,observer):
        if observer in self.observers:
            self.observers.remove(observer)

    def Notify_all(self,*args):
        for observer in self.observers:
            observer.update(*args)

class DisplayObserver:
    def update(self,money):
        print(f"Suma actualizată: {money} LEI")


class SelectProductSTM(Observable):
    def __init__(self):
        super().__init__()
        self.select_product_state = SelectProduct(self,0)
        self.coca_cola_state = CocaCola(self,5.0)
        self.pepsi_state = Pepsi(self,4.5)
        self.sprite_state = Sprite(self,6.7)
        self.current_state = self.select_product_state

    def set_state(self, state):
        self.current_state = state
        if state != self.select_product_state:
            self.Notify_all(state)

    def choose_another_product(self):
        self.set_state(self.select_product_state)
        self.current_state.choose()

class State:
    def choose(self,state):
        pass

class SelectProduct(State):
    def __init__(self,state_machine,price):
        self.state_machine = state_machine
        self.price = price

    def choose(self):
        print("\n--- Meniu Produse ---")
        print("1. Coca Cola (5.0 LEI)\n")
        print("2. Pepsi (4.5 LEI)\n")
        print("3. Sprite (6.7 LEI)\n")
        opt = input("Alege produsul: \n")

        if opt == '1':
            self.state_machine.set_state(self.state_machine.coca_cola_state)
        elif opt == '2':
            self.state_machine.set_state(self.state_machine.pepsi_state)
        elif opt == '3':
            self.state_machine.set_state(self.state_machine.sprite_state)
        else:
            self.choose()

class CocaCola():
    def __init__(self,state_machine,price):
        self.state_machine = state_machine
        self.price = price
        self.name = "CocaCola"

class Pepsi():
    def __init__(self,state_machine,price):
        self.state_machine = state_machine
        self.price = price
        self.name = "Pepsi"

class Sprite():
    def __init__(self,state_machine,price):
        self.state_machine = state_machine
        self.price = price
        self.name = "Sprite"

class TakeMoneySTM(Observable):
    def __init__(self):
        super().__init__()
        self.wait_state = WaitingForClient(self)
        self.insert_money_state = InsertMoney(self)
        self.current_state = self.wait_state
        self.money = 0

    def set_state(self, state):
        self.current_state = state

    def add_money(self, value):
        self.money += value
        self.Notify_all(self.money)

    def update_amount_of_money(self, value):
        self.money = value
        self.Notify_all(self.money)

class WaitingForClient():
    def __init__(self,state_machine):
        self.state_machine = state_machine

    def client_arrived(self):
        print("\nBine ai venit! Introdu bani:\n")
        self.state_machine.set_state(self.state_machine.insert_money_state)
        self.state_machine.current_state.process_insertion()

class InsertMoney:
    def __init__(self,state_machine):
        self.state_machine = state_machine

    def process_insertion(self):
        print("\nSe accepta doar bancnote/monede de: 0.1, 0.5, 1, 5, 10, 20, 50, 100 LEI")
        while True:
            val = input("Introduceti valoare: (gata pt oprire) ")
            if val == 'gata':
                break
            try:
                val_float = float(val)
                if val_float == 0.1: self.insert_10bani()
                elif val_float == 0.5: self.insert_50bani()
                elif val_float == 1.0: self.insert_1leu()
                elif val_float == 5.0: self.insert_5lei()
                elif val_float == 10.0: self.insert_10lei()
                elif val_float == 50.0: self.insert_50lei()
                elif val_float == 100.0: self.insert100lei()
                else: print("Nu acceptam!")
            except ValueError:
                print("Valoare numerica neacceptata")

    def insert_10bani(self):
        self.state_machine.add_money(0.1)

    def insert_50bani(self):
        self.state_machine.add_money(0.5)

    def insert_1leu(self):
        self.state_machine.add_money(1.0)

    def insert_5lei(self):
        self.state_machine.add_money(5.0)

    def insert_10lei(self):
        self.state_machine.add_money(10.0)

    def insert_50lei(self):
        self.state_machine.add_money(50.0)

    def insert100lei(self):
        self.state_machine.add_money(100.0)

class VendingMachineSTM:
    def __init__(self):
        self.take_money_stm = TakeMoneySTM()
        self.select_product_stm = SelectProductSTM()

        display_obs = DisplayObserver()
        self.take_money_stm.attach(display_obs)

        self.select_product_stm.attach(self)

    def update(self, product_state):
        self.proceed_to_checkout(product_state)

    def proceed_to_checkout(self, product_state):
        print("\nCHECKOUT:\n")

        if self.take_money_stm.money >= product_state.price:
            self.take_money_stm.money -= product_state.price
            print(f"Produsul {product_state.name} a fost eliberat!")
            print(f"Credit ramas: {self.take_money_stm.money} LEI")

            opt = input("Doriti returnarea restului (1) sau selectarea altui produs (2)? ")
            if opt == '1':
                print(f"Ati primit restul de {self.take_money_stm.money} LEI. O zi misto sa ai!")
                self.take_money_stm.update_amount_of_money(0)
                self.take_money_stm.set_state(self.take_money_stm.wait_state)
            else:
                self.select_product_stm.choose_another_product()
        else:
            lipsa = product_state.price - self.take_money_stm.money
            print(f"Fonduri insuficiente. Mai ai nevoie de {lipsa} LEI.")

            self.take_money_stm.set_state(self.take_money_stm.insert_money_state)
            self.take_money_stm.current_state.process_insertion()

            self.proceed_to_checkout(product_state)

def main():
    vending_machine = VendingMachineSTM()

    vending_machine.take_money_stm.current_state.client_arrived()

    vending_machine.select_product_stm.current_state.choose()

if __name__=="__main__":
    main()
