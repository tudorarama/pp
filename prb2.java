package lab1;
import java.io.*;

public class Main
{
static void main(String[] args) throws IOException
{
BufferedReader br = new BufferedReader(new FileReader("text.txt"));
String txt = "";

try{
StringBuilder sb = new StringBuilder();
String line = br.readLine();

while(line != null)
{
sb.append(line);
sb.append(System.lineSeparator());
line = br.readLine();
}
txt = sb.toString();
}
finally {
br.close();
}
System.out.println("Textul inainte de a fi modificat:");
System.out.println(txt);

for(int i = 0; i < txt.length(); i++)
{
txt = txt.toLowerCase();
}

System.out.println("Textul dupa prima modificare:");
System.out.println(txt);

String result = txt.replaceAll("\\p{Punct}", "");

System.out.println("Textul dupa a 2 a modificare:");
System.out.println(result);

String result1 = result.replaceAll(" ", "");
System.out.println("Textul dupa a 3 a modificare:");
System.out.println(result1);
}
}
