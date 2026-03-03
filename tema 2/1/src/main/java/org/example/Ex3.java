package org.example;
import org.graalvm.polyglot.*;
import org.graalvm.polyglot.Context;

public class Ex3 {
    public static void main(String[] args){
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();

        //1
        Value pythonFunc=polyglot.eval("python",
                "import random\n" +
                "def generate_num():\n"+
                "   return [random.randint(1,100) for _ in range(20)]\n"+
                "generate_num");

        Value listA = pythonFunc.execute();

        //2

        Value jsFunc = polyglot.eval("js",
                "(list) => { console.log('Lista generata(python) : ' + JSON.stringify(list));}");

        jsFunc.execute(listA);

        //3

        Value pythonFunc2=polyglot.eval("python",
                "def procesare(lista):\n"+
                "   n = len(lista)\n"+
                "   k = int(n * 0.2)\n"+
                "   sorted_list = sorted(lista)\n"+
                "   trim = sorted_list[k:n-k]\n"+
                "   print(f'Lista sortata si filtrata : {trim} ')\n"+
                "   return sum(trim)/len(trim) if len(trim) > 0 else 0\n"+
                "procesare");

        Value medie = pythonFunc2.execute(listA);

        System.out.println("Media aritmetica calculata in python  : "+ medie.asDouble() );





    }
}
