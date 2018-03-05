import java.util.Random;

class CoinFlip{

    private static long numOfHeads = 0;


    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        long numOfCoinTosses = 0;
        int numOfThreads = 0;
        boolean finishedFlag = false;
        

        if(args.length==2){
            numOfThreads = Integer.parseInt(args[0]);
            numOfCoinTosses = Long.parseLong(args[1]);
        }
        else{
            printUsage();
            finishedFlag = true;
        }


        if(!finishedFlag) {
            if (tossCoins(numOfCoinTosses, numOfThreads)) {
                printResults(numOfHeads,numOfCoinTosses);
                printElapsedTime(startTime);
            }
        }

    }

    private static boolean tossCoins(long numOfCoinTosses, int numOfThreads){
        FlipRunner[] flipRunners = new FlipRunner[numOfThreads];
        Thread[] threads = new Thread[numOfThreads];
        Random random = new Random();
        boolean success = true;
        long coinTossesPerThread = numOfCoinTosses/numOfThreads;
        for(int i = 0; i<numOfThreads;i++){
            flipRunners[i]= new FlipRunner(coinTossesPerThread,i,random);
            threads[i] = new Thread(flipRunners[i]);
            threads[i].start();
        }

        for(int i = 0; i < numOfThreads; i++) {
            try {
                threads[i].join();
                numOfHeads += flipRunners[i].getNumOfHeads();
            }
            catch(InterruptedException e) {
                success = false;
                break;
            }
        }
        return success;
    }

    private static void printUsage(){
        System.out.println("Usage: CoinFlip #threads #iterations");
    }

    private static void printResults(long numOfHeads, long numOfCoinTosses){
        System.out.println(numOfHeads+" heads in "+numOfCoinTosses+" coin tosses");
    }

    private static void printElapsedTime(long startTime){
        long endTime = System.currentTimeMillis();
        System.out.println("Elapsed time: "+ (endTime-startTime));
    }
}



class FlipRunner implements Runnable{
    private int threadID;

    public int getThreadID() {
        return threadID;
    }

    public void setThreadID(int threadID) {
        this.threadID = threadID;
    }

    public long getTosses() {
        return tosses;
    }

    public void setTosses(long tosses) {
        this.tosses = tosses;
    }

    public long getNumOfHeads() {
        return numOfHeads;
    }

    public void setNumOfHeads(long numOfHeads) {
        this.numOfHeads = numOfHeads;
    }

    private long tosses;
    private long numOfHeads;
    private Random random;

    public FlipRunner(long tosses, int id, Random random){
        this.threadID = id;
        this.tosses=tosses;
        this.numOfHeads = 0;
        this.random = random;
    }

    @Override
    public void run(){
        for(long i=0; i< tosses;i++){
            numOfHeads+=random.nextInt(2);
        }
    }
}