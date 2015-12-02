import java.util.Scanner;
import java.util.concurrent.Semaphore;

/**
 * Parking con un semaforo para controlar entradas
 * y salidas de vehiculos
 */
class Parking {

    private static int numPlazas = 0;
    private static int numCoches = 0;
    private static boolean[] arrayBool;// plazas
    public static Semaphore sem;
    private static int contadorLleno = 0;
    private static int numCamiones = 0;

    public Parking(){
    }

    //getters -- setters
    public static int getContadorLleno() {
        return contadorLleno;
    }

    public static void setContadorLleno() {
        Parking.contadorLleno--;
    }

    public static int getNumPlazas() {
        return numPlazas;
    }

    public void setNumCamiones(int numCamiones) {
        this.numCamiones = numCamiones;
    }

    public void setNumCoches(int numCoches) {
        this.numCoches = numCoches;
    }

    public static boolean[] getArrayBool() {
        return arrayBool;
    }

    public void setNumPlazas(int numPlazas) {
        this.numPlazas = numPlazas;
        this.arrayBool = new boolean[numPlazas];// plazas
    }

    /**
     * Se crea el array que representa
     * las plazas del parking
     */
    public void crearParking(){
        for(int i = 0; i < numPlazas; i++){
            arrayBool[i] = false;
        }
    }

    /**
     * Funcion que indica si el parking esta lleno
     * o si esta vacio
     * @return valor booleano: NOEstaLLeno=false; lleno=true
     */
    public static boolean parkingLleno(){
        if(contadorLleno >= numPlazas){
            return true;
        }
        else return false;
    }

    /**
     * Se crea el semaforo que llevara la cuenta de
     * los vehiculos que usaran el parking
     */
    private void crearSemaforo() {
        this.sem = new Semaphore(this.getNumPlazas());
    }

    /**
     * Procedimiento donde se crean los vehiculos
     * y se ejecutan
     */
    public void on(){
        int contVehiculos = 0;
        for(int i = 0; i < numCoches+numCamiones; i++){
            if(contVehiculos < numCoches){
                contVehiculos++;
                Cotxe cotxe = new Cotxe(i);
                cotxe.start();
                if(cotxe.isWaiting()){
                    contadorLleno++;
                }
            }
            else{
                Camio camio = new Camio(i);
                camio.start();
            }

        }
    }

    /**
     * Funcion que indica si hay plazas disponibles
     * para coches (1 plaza)
     * @param arrayBool las plazas del parking
     * @return si o no
     */
    public static boolean hayPlazasDisponiblesCoches(boolean[] arrayBool){
        for(int i = 0; i < arrayBool.length; i++){
            if(arrayBool[i] == false){//false = libre        true = ocupada
                return true;
            }
        }
        return false;
    }

    /**
     * Funcion que indica si hay plazas disponibles
     * para camiones (2 plazas)
     * @param arrayBool las plazas del parking
     * @return si o no
     */
    public static boolean hayPlazasDisponiblesCamiones(boolean[] arrayBool){
        for(int i = 0; i < arrayBool.length; i++){
            if(arrayBool[i] == false && ((i+1) < arrayBool.length) && arrayBool[i+1] == false){//false = libre      true = ocupada
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        System.out.println("Numero de plazas:");
        int p = input.nextInt();
        System.out.println("Numero de coches:");
        int c = input.nextInt();
        System.out.println("Numero de camiones:");
        int t = input.nextInt();

        input.close();

        Parking parking = new Parking();
        parking.setNumPlazas(p);
        parking.setNumCoches(c);
        parking.setNumCamiones(t);
        parking.crearParking();
        parking.crearSemaforo();
        parking.on();

    }


}