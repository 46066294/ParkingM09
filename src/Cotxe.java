import java.util.Random;

public class Cotxe extends Thread{

    private int id;
    private int plazaOcupada;
    private boolean waiting = false;

    public Cotxe(int numCotxe) {
        this.id = numCotxe;

    }

    public boolean isWaiting() {
        return waiting;
    }

    public int darPlazaLibre(boolean[] arrayBool){
        for(int i = 0; i < arrayBool.length; i++){
            if(!arrayBool[i]){//si la plaza esta libre
                plazaOcupada = i;
                arrayBool[plazaOcupada] = true;
                return i;
            }
        }
        return 999;
    }

    public void desocuparPlaza(boolean[] arrayBool){
        arrayBool[plazaOcupada] = false;
    }


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

            if(Parking.hayPlazasDisponiblesCoches(Parking.getArrayBool())){
                if(waiting == true) {
                    waiting = false;
                }

                try {
                    this.darPlazaLibre(Parking.getArrayBool());//ocupar plaza
                    Parking.sem.acquire();
                    System.out.println("Plaza::" + plazaOcupada + ":: OCUPADA por: coche" + id +
                            " Plazas libres >> " + Parking.sem.availablePermits());


                    Thread.sleep(1000);//tiempo de coche aparcado

                    if(Parking.parkingLleno()){
                        if(Parking.getContadorLleno() == Parking.getNumPlazas()){//condicion para que pueda entrar otro coche
                            notify();
                        }
                        Parking.setContadorLleno();
                    }
                    Parking.sem.release();
                    this.desocuparPlaza(Parking.getArrayBool());//desocupar plaza
                    System.out.println("Plaza::" + plazaOcupada + ":: LIBRE" +
                            " Plazas disponibles >> " + Parking.sem.availablePermits());


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    System.out.println("...PARKING LLENO coche" + id + " en cola. Plazas libres: "+
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
