import java.util.Scanner;
import java.util.concurrent.Semaphore;

class Parking {

    private static int numPlazas = 0;
    private static boolean[] arrayBool;// plazas
    public static Semaphore sem;
    private static int contadorLleno = 0;

    public Parking(){
    }

    public static int getContadorLleno() {
        return contadorLleno;
    }

    public static void setContadorLleno() {
        Parking.contadorLleno--;
    }

    public static boolean parkingLleno(){
        if(contadorLleno >= numPlazas){
            return true;
        }
        else return false;
    }

    public void crearParking(){
        for(int i = 0; i < numPlazas; i++){
            arrayBool[i] = false;
        }
    }

    private void crearSemaforo() {
        this.sem = new Semaphore(this.getNumPlazas());
    }

    public static int getNumPlazas() {
        return numPlazas;
    }

    public void on(){
        for(int i = 0; i < (numPlazas)+5; i++){// numPlazas + 5
            Cotxe cotxe = new Cotxe(i);
            cotxe.start();
            if(cotxe.isWaiting()){
                contadorLleno++;
            }
        }
    }

    public static boolean[] getArrayBool() {
        return arrayBool;
    }

    public void setNumPlazas(int numPlazas) {
        this.numPlazas = numPlazas;
        this.arrayBool = new boolean[numPlazas];// plazas
    }

    public static boolean hayPlazasDisponibles(boolean[] arrayBool){
        for(int i = 0; i < arrayBool.length; i++){
            if(arrayBool[i] == false){//false = libre        true = ocupada
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        System.out.println("Numero de plazas:");
        int p = input.nextInt();

        Parking parking = new Parking();
        parking.setNumPlazas(p);
        parking.crearParking();
        parking.crearSemaforo();
        parking.on();

    }


}