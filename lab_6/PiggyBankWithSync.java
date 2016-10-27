// PiggyBankWithSync.java: Demonstrate avoiding resource conflict
public class PiggyBankWithSync
{
  private PiggyBank bank = new PiggyBank();
  private Thread[] thread = new Thread[100];

  public static void main(String[] args)
  {
    PiggyBankWithSync test = new PiggyBankWithSync();
    System.out.println("What is balance ? " +
      test.bank.getBalance());
  }

  public PiggyBankWithSync()
  {
    ThreadGroup g1 = new ThreadGroup("group");
    boolean done = false;

    for (int i=0; i<100; i++)
    {
      thread[i] = new Thread(g1, new AddAPennyThread(), "t");
      thread[i].start();
    }

    while(!done)
      if (g1.activeCount() == 0)
        done = true;
  }

  // Synchronize: add a penny one at a time
  private static synchronized void addAPenny(PiggyBank bank)
  {
    int newBalance = bank.getBalance() + 1;

    try
    {
      Thread.sleep(5);
    }
    catch (InterruptedException ex)
    {
      System.out.println(ex);
    }

    bank.setBalance(newBalance);
  }

  // A thread for adding a penny to the piggy bank
  class AddAPennyThread extends Thread
  {
    public void run()
    {
      addAPenny(bank);
    }
  }
}


