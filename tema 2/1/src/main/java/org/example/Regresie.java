package org.example;

import org.graalvm.polyglot.*;

import java.util.*;
import java.io.*;
import java.nio.file.*;



class Main{
    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);

        //citire din dataset.txt

        List<Double> xValues = new ArrayList<>();
        List<Double> yvalues = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get("dataset.txt"));


        for(String line : lines){
            String[] parts = line.split("\\s+");
            xValues.add(Double.parseDouble(parts[0]));
            yvalues.add(Double.parseDouble(parts[1]));
        }


        //citire de la tastatura

        System.out.println("Nume fisier output: (ex: plot.png)");
        String fileName = sc.nextLine();
        System.out.println("Cale salvare(ex: .): ");
        String path = sc.nextLine();
        System.out.println("Culoare punte: ");
        String pColor = sc.nextLine();
        System.out.println("Culoare linie: ");
        String lColor = sc.nextLine();


        //apelare functie

        System.out.println("Se apeleaza functia din python");

        String fileLocation = Regression(xValues,yvalues,fileName,path,pColor,lColor);

        System.out.println("succes ! imaginea a fost salvata si deschisa din : "+fileLocation);








    }

    private static String Regression(List<Double> x,List<Double> y,String fileName,String path,String dotColor,String lineColor){
        Context polyglot = Context.newBuilder("python").allowAllAccess(true).build();



        String script =
                "import numpy as np\n"+
                        "import matlplotlib.pyplot as plt\n"+
                        "from scipy import stats\n"+
                        "import os\n"+
                        "import subprocess\n"+
                        "\n"+
                        "def perform(x_data,y_data,f_name,f_path,dColor,lColor):\n"+
                        "   x = np.array(x_data)\n"+
                        "   y = np.array(y_data)\n"+
                        "   \n"+
                        "   #Calcul regresie : panta ,intersectie ,r ,p ,eroare \n"+
                        "   slope, intersectie, r, p, std_err = stats.linegress(x, y)\n"+
                        "   model_line = slope * x + intersectie\n"+
                        "   \n"+
                        "   #Cream plot ul"+
                        "   plt.figure(figsize=(10,6))\n"+
                        "   plt.scatter(x, y, color=dColor, label='Date Intrare')\n"+
                        "   plt.plot(x, model_line, color=lColor, label=f'Regresie : y={slope:.2f}x+{intersectie:.2f}')\n"+
                        "   plt.title('Regresie Liniara')\n"+
                        "   plt.legend()\n"+
                        "   #Salvare pe disk\n"+
                        "   full_path = os.path.join(f_path, f_name)\n"+
                        "   plt.savefig(full_path)\n"+
                        "   \n"+
                        "   if os.name == 'nt':\n"+
                        "       os.startfile(full_path)\n"+
                        "   else:\n"+
                        "       subprocess.call(['xdg-open',full_path])\n"+
                        "   \n"+
                        "   return full_path\n";

        polyglot.eval("python",script);

        Value pythonFunc = polyglot.getBindings("python").getMember("perform");


        Value result = pythonFunc.execute(x, y, fileName,path,dotColor,lineColor);

        String savedPath = result.asString();

        polyglot.close();

        return savedPath;
    }
}