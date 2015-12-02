import java.util.Random;

/**
 * Representa un vehiculo: camion
 */
public class Camio extends Thread{

    private int id;
    private int plaza1Ocupada;
    private int plaza2Ocupada;
    private boolean waiting = false;

    public Camio(int numCamio) {
        this.id = numCamio;

    }
    /**
     * Funcion que indica si el camion esta
     * en cola esperando su turno
     * @return
     */
    public boolean isWaiting() {
        return waiting;
    }

    /**
     * Funcion que facilita 2 plazas del
     * parking vacias a un camion
     * @param arrayBool todas las plazas del parking
     * @return plaza de parking en cuestion
     *          (999 si no la hay)
     */
    public int darPlazaLibre(boolean[] arrayBool){
        for(int i = 0; i < arrayBool.length; i++){
            if(!arrayBool[i] && !arrayBool[i+1]){//si las plazas estan libres
                plaza1Ocupada = i;
                plaza2Ocupada = i+1;
                arrayBool[plaza1Ocupada] = true;
                arrayBool[plaza2Ocupada] = true;
                return i;
            }
        }
        return 999;
    }

    /**
     * Procedimiento que pone la plaza libre
     * una vez el coche la ha dejado
     * @param arrayBool
     */
    public void desocuparPlaza(boolean[] arrayBool){
        arrayBool[plaza1Ocupada] = false;
        arrayBool[plaza2Ocupada] = false;
    }


    /**
     * Tarea del camion:
     * entrar en el parking y ocupar 2 plazas
     * durante un tiempo. Despues las abandona, sale del
     * parking y vuelve a repetir el proceso
     */
    @Override
    public void run() {
        Random rnd = new Random();

        while(true){
            try {
                Thread.sleep(rnd.nextInt(100));
                //Parking.sem.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(Parking.hayPlazasDisponiblesCamiones(Parking.getArrayBool())){
                if(waiting == true) {
                    waiting = false;
                }

                try {
                    this.darPlazaLibre(Parking.getArrayBool());//ocupar plazas
                    Parking.sem.acquire();
                    Parking.sem.acquire();
                    System.out.println("ENTRADA CAMION-->Plaza::" + plaza1Ocupada + " y " + plaza2Ocupada + ":: OCUPADA por: camion" + id +
                            " Plazas libres >> " + Parking.sem.availablePermits());


                    Thread.sleep(rnd.nextInt(1000));//tiempo de camion aparcado

                    if(Parking.parkingLleno()){
                        if(Parking.getContadorLleno() == Parking.getNumPlazas()){//condicion para que pueda entrar otro camion
                            notify();
                        }
                        Parking.setContadorLleno();
                        Parking.setContadorLleno();
                    }
                    Parking.sem.release();
                    Parking.sem.release();
                    this.desocuparPlaza(Parking.getArrayBool());//desocupar plaza
                    System.out.println("SALIDA CAMION-->Plaza::" + plaza1Ocupada + " y " + plaza2Ocupada + ":: LIBRES" +
                            " Plazas disponibles >> " + Parking.sem.availablePermits());


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    System.out.println("...PARKING LLENO PARA CAMIONES -- camion" + id + " en cola. Plazas libres: "+
                            Parking.sem.availablePermits());
                    waiting = true;

                    while(Parking.parkingLleno()){
                        wait();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }//run()

}
