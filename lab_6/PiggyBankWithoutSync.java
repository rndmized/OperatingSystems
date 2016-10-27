
// PiggyBankWithoutSync.java: Demonstrate resource conflict
public class PiggyBankWithoutSync
{
  private PiggyBank bank = new PiggyBank();
  private Thread[] thread = new Thread[100];

  public static void main(String[] args)
  {
    PiggyBankWithoutSync test = new PiggyBankWithoutSync();
    System.out.println("What is balance ? " +
      test.bank.getBalance());
  }

  public PiggyBankWithoutSync()
  {
    ThreadGroup g = new ThreadGroup("group");
    boolean done = false;

    // Create and launch 100 threads
    for (int i=0; i<100; i++)
    {
      thread[i] = new Thread(g, new AddAPennyThread(), "t");
      thread[i].start();
    }

    // Check if all the threads are finished
    while(!done)
      if (g.activeCount() == 0)
        done = true;
  }

  // A thread for adding a penny to the piggy bank
  class AddAPennyThread extends Thread
  {
    public void run()
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
  }
}

// A class for piggy bank
class PiggyBank
{
  private int balance = 0;

  public int getBalance()
  {
    return balance;
  }

  public void setBalance(int balance)
  {
    this.balance = balance;
  }
}



